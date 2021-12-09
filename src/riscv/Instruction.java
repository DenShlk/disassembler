package riscv;

public class Instruction {
    public static final long UNDEFINED_VALUE = Long.MAX_VALUE;

    private final ProtoInstruction proto;
    private final long r1;
    private final long r2;
    private final long rd;
    private final long imm;

    public Instruction(ProtoInstruction proto, long r1, long r2, long rd, long imm) {
        this.proto = proto;
        this.r1 = r1;
        this.r2 = r2;
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return String.format("%s %d, %d, %d, %d", proto.getName(), rd, r1, r2, imm);
    }

    public long getR1() {
        return r1;
    }

    public long getR2() {
        return r2;
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

    public String getName() {
        return proto.getName();
    }
}
