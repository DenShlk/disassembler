
with open('table_text.txt') as fin:
    lines = fin.readlines()
    lines = [l[:-1] for l in lines]


def find_type(s: str) -> str:
    if '[11:0]' in s or '1110011' in s:  # ecall, ebreak, csr**
        return 'I'
    if 'imm' not in s:
        return 'R'
    if '[11:5]' in s:
        return 'S'
    if '[12|10:5]' in s:
        return 'B'
    if '[31:12]' in s:
        return 'U'
    if '[20|10:1' in s:
        return 'J'


def find_compressed_type(s, func, name):
    if len(func) == 4:
        return 'CR'
    if '[11|4|9:8|' in s:
        return 'CJ'
    if 'imm[8|4:3]' in s:
        return 'CB'
    if 'rs10/rd0' in s: # renders as rs1/rd
        return 'CI'
    if 'uimm[5:3]' in s:
        if 'rd' in s:
            return 'CL'
        return 'CS'
    if 'nzuimm[5:4' in s:
        return 'CIW'


def is_rv32IMC(line):
    if 'C.F' in line:
        return False
    if ("RV64" in line or "RV128" in line) and "RV32" not in line:
        return False

    return True

# format of table is too randomized too make fully automated decoding rational
# this function skips some instructions in sake of simplifying and minimising mistakes
def parse_compressed_instr(line):
    if not is_rv32IMC(line):
        return None

    #print(line, end=' ')
    tokens = line.split()
    name = [t for t in tokens if t.startswith('C.')][0]
    op_code = tokens[tokens.index(name) - 1]
    func = tokens[0]
    if tokens[1] in '01':
        func += tokens[1] # func4
    itype = find_compressed_type(line, func, name)

    return itype, name, 16, op_code, func, None


def parse_instr(line):
    size = 16 if 'C.' in line else 32
    if size == 16:
        return parse_compressed_instr(line)

    tokens = line.split()
    name = tokens[-1]
    tokens = tokens[:-1]
    itype = find_type(line)
    func3 = ''
    func7 = ''
    op_code = tokens[-1]
    if itype == 'R':
        func7 = tokens[0]
        func3 = tokens[3]
    elif itype == 'I':
        func3 = tokens[2]
    elif itype == 'S' or itype == 'B':
        func3 = tokens[3]

    return itype, name, 32, op_code, func3, func7


def build_constructor_call(itype, name, size, op_code, func3='', func7='') -> str:
    if size == 32:
        call = 'new ProtoInstruction('
        call += f'InstructionType.{itype}, "{name}", 0b{op_code}'
        if func3:
            call += f', 0b{func3}'
            if func7:
                call += f', 0b{func7}'
        call += '),'
        return call
    else:
        call = 'new CompressedProtoInstruction('
        call += f'CompressedFormat.{itype}, "{name}", 0b{op_code}, 0b{func3}),'
        return call


# parsing table
items = ['']
for line in lines:
    if not line or line.startswith('//'):
        items.append(line)
        continue

    args = parse_instr(line)
    if args:
        items.append(build_constructor_call(*args))
    else:
        # not supported
        items.append('// skipped: ' + line)


# read java file
with open('..\\src\\riscv\\ProtoInstructionList.java') as fout:
    text = ''.join(fout.readlines())

# find start and end of array declaration
START_TAG = '<list start>'
END_TAG = '<list end>'

start = text.index('\n', text.index(START_TAG)) + 1
end = text.rindex('\n', start, text.index(END_TAG))
spaces_count = 12
# replace content of file with generated list of constructor calls
text = text[:start] + ('\n' + ' ' * spaces_count).join(items) + text[end:]
with open('..\\src\\riscv\\ProtoInstructionList.java', 'w') as fout:
    fout.write(text)
