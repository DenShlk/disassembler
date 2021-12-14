package riscv;

public class CompressedProtoInstruction extends ProtoInstruction {

    private final CompressedFormat format;
    private final int func4;

    public CompressedProtoInstruction(CompressedFormat format, String name, int opcode, int func3or4) {
        super(null, name, opcode, format == CompressedFormat.CR ? UNDEFINED_FUNC : func3or4, UNDEFINED_FUNC);
        this.format = format;
        this.func4 = format == CompressedFormat.CR ? func3or4 : UNDEFINED_FUNC;
    }

    @Override
    public InstructionType getType() {
        throw new UnsupportedOperationException("Compressed instruction has only format, not type.");
    }

    @Override
    public int getFunc7() {
        return UNDEFINED_FUNC;
    }

    @Override
    public InstructionSize getSize() {
        return InstructionSize.COMPRESSED_16;
    }

    public CompressedFormat getFormat() {
        return format;
    }

    public int getFunc4() {
        return func4;
    }
}
