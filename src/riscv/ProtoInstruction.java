package riscv;

public class ProtoInstruction {
    public static final int UNDEFINED_FUNC = -1;

    private final InstructionType type;
    private final int func3;
    private final int func7;
    private final int opcode;
    private final String name;

    public ProtoInstruction(InstructionType type, int func3, int func7, int opcode, String name) {
        this.type = type;
        this.func3 = func3;
        this.func7 = func7;
        this.opcode = opcode;
        this.name = name;
    }

    // constructor without func7 parameter, for all types except R
    public ProtoInstruction(InstructionType type, int func3, int opcode, String name) {
        this(type, func3, UNDEFINED_FUNC, opcode, name);
        assert type != InstructionType.R;
    }

    // constructor without func parameters, for U and J types
    public ProtoInstruction(InstructionType type, int opcode, String name) {
        this(type, UNDEFINED_FUNC, UNDEFINED_FUNC, opcode, name);
        assert type == InstructionType.U || type == InstructionType.J;
    }

    public InstructionType getType() {
        return type;
    }

    public int getFunc3() {
        return func3;
    }

    public int getFunc7() {
        return func7;
    }

    public int getOpcode() {
        return opcode;
    }

    public String getName() {
        return name;
    }
}
