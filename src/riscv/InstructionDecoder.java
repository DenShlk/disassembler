package riscv;

import java.util.*;

public class InstructionDecoder {
    // opcode -> instruction
    private final Map<Integer, List<ProtoInstruction>> opcode2Instruction = new HashMap<>();
    // opcode -> func3 -> instructions
    private final Map<Integer, Map<Integer, List<CompressedProtoInstruction>>> opcode2CompProtoInstruction = new HashMap<>();
    // only for opcodes relevant to instructions of only one type
    private final Map<Integer, InstructionType> opcode2Type = new HashMap<>();


    private static final long ECALL = 0b000000000000_00000_000_00000_1110011;
    private static final long EBREAK = 0b000000000001_00000_000_00000_1110011;
    private final ProtoInstruction ecallProto;
    private final ProtoInstruction ebreakProto;

    public InstructionDecoder() {
        // ugly instructions with difference only in immediacies
        ecallProto = Arrays.stream(ProtoInstructionList.PROTOS)
                .filter(x -> x.getName().equals("ECALL"))
                .findFirst()
                .orElseThrow(AssertionError::new);
        ebreakProto = Arrays.stream(ProtoInstructionList.PROTOS)
                .filter(x -> x.getName().equals("EBREAK"))
                .findFirst()
                .orElseThrow(AssertionError::new);

        List<Integer> ambiguousType = new ArrayList<>();
        for (ProtoInstruction proto : ProtoInstructionList.PROTOS) {
            if (proto == ecallProto || proto == ebreakProto || proto instanceof CompressedProtoInstruction) {
                continue;
            }

            int opcode = proto.getOpcode();
            opcode2Instruction.putIfAbsent(opcode, new ArrayList<>());
            opcode2Instruction.get(opcode).add(proto);

            if (opcode2Type.containsKey(opcode) && opcode2Type.get(opcode) != proto.getType()) {
                ambiguousType.add(opcode);
            } else {
                opcode2Type.putIfAbsent(opcode, proto.getType());
            }
        }
        for (Integer opcode : ambiguousType) {
            opcode2Type.remove(opcode);
        }

        for (CompressedProtoInstruction proto : CompressedProtoInstructionList.PROTOS) {
            Map<Integer, List<CompressedProtoInstruction>> func2protos = opcode2CompProtoInstruction
                    .getOrDefault(proto.getOpcode(), null);
            if (func2protos == null) {
                func2protos = new HashMap<>();
                opcode2CompProtoInstruction.put(proto.getOpcode(), func2protos);
            }
            List<CompressedProtoInstruction> protos = func2protos.getOrDefault(proto.getFunc3(), null);
            if (protos == null) {
                protos = new ArrayList<>();
                func2protos.put(proto.getFunc3(), protos);
            }
            protos.add(proto);
        }
    }

    public Instruction decode(long bits, InstructionSize size) {
        if (bits == 0 || ((int) bits) == -1 || bits == (-1 >> 16)) {
            return Instruction.illegalInstruction();
        }
        if (size == InstructionSize.COMPRESSED_16) {
            return decodeCompressed(bits);
        }

        ProtoInstruction proto = searchProto(bits);
        if (proto == null) {
            return Instruction.unknownInstruction();
        }
        switch (proto.getType()) {
            case R:
                return new Instruction(proto,
                        extractRegister(Register.Rd, bits),
                        extractRegister(Register.Rs1, bits),
                        extractRegister(Register.Rs2, bits),
                        Instruction.UNDEFINED_VALUE
                );
            case I:
                return new Instruction(proto,
                        extractRegister(Register.Rd, bits),
                        extractRegister(Register.Rs1, bits),
                        Instruction.UNDEFINED_VALUE,
                        extractImmediate(proto.getType(), bits)
                );
            case S:
            case B:
                return new Instruction(proto,
                        Instruction.UNDEFINED_VALUE,
                        extractRegister(Register.Rs1, bits),
                        extractRegister(Register.Rs2, bits),
                        extractImmediate(proto.getType(), bits)
                );
            case U:
            case J:
                return new Instruction(proto,
                        extractRegister(Register.Rd, bits),
                        Instruction.UNDEFINED_VALUE,
                        Instruction.UNDEFINED_VALUE,
                        extractImmediate(proto.getType(), bits)
                );
            default:
                throw new UnsupportedOperationException("Unknown type of instruction");
        }
    }

    private Instruction decodeCompressed(long bits) {
        int opcode = (int) (bits & 0b11);
        int func3 = (int) ((bits >> 13) & (0b111));

        if (!opcode2CompProtoInstruction.containsKey(opcode) ||
                !opcode2CompProtoInstruction.get(opcode).containsKey(func3)) {
            return Instruction.unknownInstruction();
        }

        CompressedProtoInstruction matchedProto = null;
        for (CompressedProtoInstruction proto : opcode2CompProtoInstruction.get(opcode).get(func3)) {
            if (proto.matches(bits)) {
                assert matchedProto == null : "Implicit proto instruction match: " + Long.toBinaryString(bits);
                matchedProto = proto;
            }
        }

        if (matchedProto == null) {
            return Instruction.unknownInstruction();
        }

        return new Instruction(matchedProto,
                extractRegFromCompressed(bits, matchedProto.getRdEncoding()),
                extractRegFromCompressed(bits, matchedProto.getRs1Encoding()),
                extractRegFromCompressed(bits, matchedProto.getRs2Encoding()),
                matchedProto.extractImmediate(bits)
        );
    }

    public long extractRegFromCompressed(long bits, CompressedProtoInstruction.RegisterEncoding encoding) {
        switch (encoding) {
            case EXCLUDED:
                return Instruction.UNDEFINED_VALUE;
            case FULL_AT_RS1:
                return (bits >> 7) & 0b11111;
            case SHORT_AT_RS1:
                return ((bits >> 7) & 0b111) + 8;
            case FULL_AT_RS2:
                return (bits >> 2) & 0b11111;
            case SHORT_AT_RS2:
                return ((bits >> 2) & 0b111) + 8;
        }
        throw new IllegalArgumentException("Unknown register encoding: " + encoding);
    }

    public long extractImmediate(InstructionType type, long bits) {
        long imm = 0;
        // some instructions use imm. less than 32 bits long, and all bits greater than length
        // are equal to the greatest bit of instruction
        int stretchFrom = 0;
        switch (type) {
            case R:
                throw new UnsupportedOperationException("R-type has no immediate");
            case I:
                imm = bits >> 20;
                stretchFrom = 11;
                break;
            case S:
                imm = ((bits >> 25) << 5) | ((bits >> 7) & 0x1f);
                stretchFrom = 11;
                break;
            case B:
                imm = (((bits >> 31) & 1) << 12);
                imm |= (((bits >> 7) & 1) << 11);
                imm |= (((bits >> 25) & 0x3f) << 5);
                imm |= (((bits >> 8) & 0xf) << 1);
                stretchFrom = 12;
                break;
            case U:
                imm = bits & 0xff_ff_f0_00;
                stretchFrom = 32;
                break;
            case J:
                imm = ((bits >> 31) & 1) << 20; // 20
                imm |= ((bits >> 21) & 0x3ff) << 1; // 10:1
                imm |= ((bits >> 20) & 1) << 11; // 11
                imm |= ((bits >> 12) & 0xff) << 12; // 19:12
                stretchFrom = 20;
                break;
        }
        if ((imm & (1L << (stretchFrom))) != 0) {
            // negative value
            imm |= (-1L) << stretchFrom;
        }
        return imm;
    }

    public long extractRegister(Register reg, long bits) {
        switch (reg) {
            case Rd:
                return (bits >> 7) & 0x1f;
            case Rs1:
                return (bits >> 15) & 0x1f;
            case Rs2:
                return (bits >> 20) & 0x1f;
            default:
                throw new UnsupportedOperationException("Unknown register");
        }
    }

    // returns null if instruction is undefined
    public ProtoInstruction searchProto(long bits) {
        if (bits == ECALL) {
            return ecallProto;
        }
        if (bits == EBREAK) {
            return ebreakProto;
        }

        int opcode = (int) (bits & 0x7f); // last 7 bits
        // if opcode2type does not contain this opcode, extract func3
        // (opcodes of instruction type R, U, J relate only to this type)
        int func3 = extractFunc3(bits, opcode2Type.getOrDefault(opcode, InstructionType.I));
        ProtoInstruction result = null;
        if (!opcode2Instruction.containsKey(opcode)) {
            return null;
        }

        for (ProtoInstruction proto : opcode2Instruction.get(opcode)) {
            if (proto.getFunc3() == func3 && proto.getFunc7() == extractFunc7(bits, proto.getType())) {
                assert result == null : "Implicit proto instruction match: " + Long.toBinaryString(bits);;
                result = proto;
            }
        }
        return result;
    }

    public static int extractFunc3(long bits, InstructionType type) {
        if (type == InstructionType.U || type == InstructionType.J) {
            return ProtoInstruction.UNDEFINED_FUNC;
        }
        return (int) ((bits >> 12) & 0b111); // func3
    }

    public static int extractFunc7(long bits, InstructionType type) {
        if (type == InstructionType.R) {
            return  (int) (bits >> 25); // func7
        }
        return ProtoInstruction.UNDEFINED_FUNC;
    }

    public static InstructionSize getInstructionSize(int word16) {
        if ((word16 & 0b11) != 0b11) {
            return InstructionSize.COMPRESSED_16;
        }
        if ((word16 & 0b11100) != 0b11100) {
            return InstructionSize.NORMAL_32;
        }
        throw new UnsupportedOperationException("Unimplemented instruction length");
    }

    public enum Register {
        Rd,
        Rs1,
        Rs2,
    }
}
