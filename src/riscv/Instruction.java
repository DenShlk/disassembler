package riscv;

public class Instruction {
    public static final long UNDEFINED_VALUE = Long.MAX_VALUE;
    private static final int UNKNOWN_INSTRUCTION_OPCODE = -1;

    private final ProtoInstruction proto;
    private final long rs1;
    private final long rs2;
    private final long rd;
    private final long imm;
    private long address;
    private String label;

    private String outLabel;

    public Instruction(ProtoInstruction proto, long rd, long rs1, long rs2, long imm) {
        this.proto = proto;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rd = rd;
        this.imm = imm;
    }

    public boolean isUnknownInstruction() {
        return proto.getOpcode() == UNKNOWN_INSTRUCTION_OPCODE;
    }

    public static Instruction unknownInstruction() {
        return new Instruction(
                new ProtoInstruction(null, "unknown_instruction", UNKNOWN_INSTRUCTION_OPCODE),
                UNDEFINED_VALUE,
                UNDEFINED_VALUE,
                UNDEFINED_VALUE,
                UNDEFINED_VALUE);
    }

    @Override
    public String toString() {
        return String.format("%s %d, %d, %d, %d", proto.getName(), rd, rs1, rs2, imm);
    }

    public long getRs1() {
        return rs1;
    }

    public long getRs2() {
        return rs2;
    }

    public long getRd() {
        return rd;
    }

    public long getImm() {
        return imm;
    }

    public InstructionType getType() {
        return proto.getType();
    }

    public InstructionSize getSize() {
        return proto.getSize();
    }

    public String getName() {
        return proto.getName();
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCSR() {
        return getName().startsWith("CSR");
    }

    public long getZimm() {
        if (!isCSR() || !getName().contains("I")) {
            return UNDEFINED_VALUE;
        }
        return rs1;
    }

    public String getOutLabel() {
        return outLabel;
    }

    public boolean hasOutLabel() {
        return outLabel != null;
    }

    public void setOutLabel(String outLabel) {
        this.outLabel = outLabel;
    }
}
