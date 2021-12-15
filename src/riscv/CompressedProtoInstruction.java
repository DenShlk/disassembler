package riscv;

/**
 * Extension of ProtoInstruction class to support compressed instructions.
 * Due to less structured encoding of compressed instructions, separating them by formats would not let decode
 * instructions explicitly. Thus, any decoding function would turn into if for each instruction.
 *
 * CompressedProtoInstruction can have special lambdas to match and decode binary instructions (lambdas are effectively
 * equal to body of 'if' from previous paragraph). If instruction can be matched only by opcode and func, it can omit
 * Matcher lambda. It is strongly recommended to test build for explicit decoding of all instructions.
 */
public class CompressedProtoInstruction extends ProtoInstruction {

    private static final Matcher DEFAULT_MATCHER = (int i) -> true;

    private final int func2_11_10;
    private final int func2_6_5;
    private final int func1;
    private final Matcher matcher;
    private final ImmediateDecoder decoder;
    private final RegistersEncodingInfo regs;

    public CompressedProtoInstruction(String name, int opcode, int func3, int func1, int func2_11_10, int func2_6_5,
                                      RegistersEncodingInfo regs, Matcher matcher, ImmediateDecoder decoder) {
        super(null, name, opcode, func3, UNDEFINED_FUNC);
        this.func1 = func1;
        this.func2_11_10 = func2_11_10;
        this.func2_6_5 = func2_6_5;
        this.regs = regs;
        this.decoder = decoder;
        this.matcher = matcher;
    }

    public CompressedProtoInstruction(String name, int opcode, int func3,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder, Matcher matcher) {
        this(name, opcode, func3, UNDEFINED_FUNC, UNDEFINED_FUNC, UNDEFINED_FUNC, regs, matcher, decoder);
    }

    public CompressedProtoInstruction(String name, int opcode, int func3, int func1,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder, Matcher matcher) {
        this(name, opcode, func3, func1, UNDEFINED_FUNC, UNDEFINED_FUNC, regs, matcher, decoder);
    }

    public CompressedProtoInstruction(String name, int opcode, int func3, int func1,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder) {
        this(name, opcode, func3, func1, UNDEFINED_FUNC, UNDEFINED_FUNC, regs, DEFAULT_MATCHER, decoder);
    }

    public CompressedProtoInstruction(String name, int opcode, int func3,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder) {
        this(name, opcode, func3, UNDEFINED_FUNC, UNDEFINED_FUNC, UNDEFINED_FUNC, regs, DEFAULT_MATCHER, decoder);
    }

    public CompressedProtoInstruction(String name, int opcode, int func3, int func1, int func2_11_10,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder, Matcher matcher) {
        this(name, opcode, func3, func1, func2_11_10, UNDEFINED_FUNC, regs, matcher, decoder);
    }

    public CompressedProtoInstruction(String name, int opcode, int func3, int func1, int func2_11_10,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder) {
        this(name, opcode, func3, func1, func2_11_10, UNDEFINED_FUNC, regs, DEFAULT_MATCHER, decoder);
    }

    public CompressedProtoInstruction(String name, int opcode, int func3, int func1, int func2_11_10, int func2_6_5,
                                      RegistersEncodingInfo regs, ImmediateDecoder decoder) {
        this(name, opcode, func3, func1, func2_11_10, func2_6_5, regs, DEFAULT_MATCHER, decoder);
    }

    public RegisterEncoding getRdEncoding() {
        return regs.rd;
    }

    public RegisterEncoding getRs1Encoding() {
        return regs.rs1;
    }

    public RegisterEncoding getRs2Encoding() {
        return regs.rs2;
    }

    @Override
    public InstructionType getType() {
        throw new UnsupportedOperationException("Compressed instruction has only format, not type.");
    }

    @Override
    public int getFunc7() {
        return UNDEFINED_FUNC;
    }

    public long extractImmediate(long bits) {
        return decoder.decode((int) bits);
    }

    public boolean matches(long bits) {
        return (getFunc3() == (bits >> 13)) &&
                (func1 == UNDEFINED_FUNC || func1 == ((bits >> 12) & 1)) &&
                (func2_11_10 == UNDEFINED_FUNC || func2_11_10 == ((bits >> 10) & 0b11)) &&
                (func2_6_5 == UNDEFINED_FUNC || func2_6_5 == ((bits >> 5) & 0b11)) &&
                        matcher.matches((int) bits);
    }

    @Override
    public InstructionSize getSize() {
        return InstructionSize.COMPRESSED_16;
    }

    // assumes that opcodes and funcs of instructions are equal
    @FunctionalInterface
    interface Matcher {
        boolean matches(int instr);
    }

    @FunctionalInterface
    interface ImmediateDecoder {
        long decode(int instr);
    }

    public static class RegistersEncodingInfo {
        public final RegisterEncoding rd;
        public final RegisterEncoding rs1;
        public final RegisterEncoding rs2;

        public RegistersEncodingInfo(RegisterEncoding rd, RegisterEncoding rs1, RegisterEncoding rs2) {
            this.rd = rd;
            this.rs1 = rs1;
            this.rs2 = rs2;
        }
    }

    public enum RegisterEncoding {
        EXCLUDED,
        FULL_AT_RS1,
        SHORT_AT_RS1,
        FULL_AT_RS2,
        SHORT_AT_RS2,
    }
}
