package riscv;

public class Instruction {
    private final InstructionType type;
    private final int func;
    private final int opcode;
    private final String name;

    public Instruction(InstructionType type, int func, int opcode, String name) {
        this.type = type;
        this.func = func;
        this.opcode = opcode;
        this.name = name;
    }

    // constructor without func parameter, for U and J types
    public Instruction(InstructionType type, int opcode, String name) {
        assert type == InstructionType.U || type == InstructionType.J;
        this.type = type;
        this.func = -1;
        this.opcode = opcode;
        this.name = name;
    }

    private static int getInstructionLength(int word16) {
        if ((word16 & 0b11) != 0b11) {
            return 16;
        }
        if ((word16 & 0b11100) != 0b11100) {
            return 32;
        }
        if (((word16 >> 5) & 0b1) == 0) {
            return 48;
        }
        if (((word16 >> 6) & 0b1) == 0) {
            return 64;
        }
        throw new UnsupportedOperationException("Unimplemented instruction length");
    }
}
