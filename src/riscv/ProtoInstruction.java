package riscv;

public class ProtoInstruction {
    public static final int UNDEFINED_FUNC = -1;

    private final InstructionType type;
    private final int func3;
    private final int func7;
    private final int opcode;
    private final String name;

    public ProtoInstruction(InstructionType type, String name, int opcode, int func3, int func7) {
        this.type = type;
        this.func3 = func3;
        this.func7 = func7;
        this.opcode = opcode;
        this.name = name;
    }

    // constructor without func7 parameter, for all types except R
    public ProtoInstruction(InstructionType type, String name, int opcode, int func3) {
        this(type, name, opcode, func3, UNDEFINED_FUNC);
        assert type != InstructionType.R;
    }

    // constructor without func parameters, for U and J types
    public ProtoInstruction(InstructionType type, String name, int opcode) {
        this(type, name, opcode, UNDEFINED_FUNC);
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
