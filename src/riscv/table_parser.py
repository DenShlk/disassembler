with open('table_text.txt') as fin:
    lines = fin.readlines()
    lines = [l[:-1] for l in lines]


def find_type(s):
    if 'imm' not in s:
        return 'R'
    if '[11:0]' in s:
        return 'I'
    if '[11:5]' in s:
        return 'S'
    if '[12|10:5]' in s:
        return 'B'
    if '[31:12]' in s:
        return 'U'
    if '[20|10:1' in s:
        return 'J'


for line in lines:
    if not line:
        continue
    if line.startswith('#'):
        print('//' + line[1:])
        continue

    vals = line.split()
    name = vals[-1]
    vals = vals[:-1]
    t = find_type(line)
    func = ''
    op = vals[-1]
    if t == 'R':
        func = vals[0] + vals[3]
    elif t == 'I':
        func = vals[2]
    elif t == 'S' or t == 'B':
        func = vals[3]
    if func:
        print(f'new Instruction(InstructionType.{t}, 0b{func}, 0b{op}, "{name}"),')
    else:
        print(f'new Instruction(InstructionType.{t}, 0b{op}, "{name}"),')
