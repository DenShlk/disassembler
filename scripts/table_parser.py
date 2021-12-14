
with open('table_text.txt') as fin:
    lines = fin.readlines()
    lines = [l[:-1] for l in lines]


def find_type(s):
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


def build_constructor_call(itype, name, size, op_code, func3='', func7=''):
    call = 'new ProtoInstruction('
    call += f'InstructionType.{itype}, "{name}", ProtoInstruction.SizeType.{size}, 0b{op_code}'
    if func3:
        call += f', 0b{func3}'
        if func7:
            call += f', 0b{func7}'
    call += '),'
    return call


# parsing table
items = ['']
for line in lines:
    if not line:
        continue
    if line.startswith('//'):
        items.append(line)
        continue
    size = "NORMAL_32"
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
    items.append(build_constructor_call(itype, name, size, op_code, func3, func7))


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
