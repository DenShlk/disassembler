package riscv;

import java.util.*;

public class InstructionDecoder {
    // opcode -> instruction
    private final Map<Integer, List<ProtoInstruction>> opcode2Instruction = new HashMap<>();
    // only for opcodes relevant to instructions of only one type
    private final Map<Integer, InstructionType> opcode2Type = new HashMap<>();

    private static final long ECALL = 0b000000000000_00000_000_00000_1110011;
    private static final long EBREAK = 0b000000000001_00000_000_00000_1110011;
    private final ProtoInstruction ecallProto;
    private final ProtoInstruction ebreakProto;

    public InstructionDecoder() {
        List<Integer> ambiguousType = new ArrayList<>();
        for (ProtoInstruction proto : ProtoInstructionList.PROTOS) {
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

        // ugly instructions with difference only in immediacies
        ecallProto = Arrays.stream(ProtoInstructionList.PROTOS)
                .filter(x -> x.getName().equals("ECALL"))
                .findFirst()
                .orElseThrow(AssertionError::new);
        ebreakProto = Arrays.stream(ProtoInstructionList.PROTOS)
                .filter(x -> x.getName().equals("EBREAK"))
                .findFirst()
                .orElseThrow(AssertionError::new);
    }

    public Instruction decode(long bits) {
        ProtoInstruction proto = searchProto(bits);
        if (proto == null) {
            return null;
        }
        switch (proto.getType()) {
            case R:
                return new Instruction(proto,
                        extractRegister(Register.Rd, bits),
                        extractRegister(Register.R1, bits),
                        extractRegister(Register.R2, bits),
                        Instruction.UNDEFINED_VALUE
                );
            case I:
                return new Instruction(proto,
                        extractRegister(Register.Rd, bits),
                        extractRegister(Register.R1, bits),
                        Instruction.UNDEFINED_VALUE,
                        extractImmediate(proto.getType(), bits)
                );
            case S:
            case B:
                return new Instruction(proto,
                        Instruction.UNDEFINED_VALUE,
                        extractRegister(Register.R1, bits),
                        extractRegister(Register.R2, bits),
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

    public long extractImmediate(InstructionType type, long bits) {
        long imm = 0;
        switch (type) {
            case R:
                throw new UnsupportedOperationException("R-type has no immediate");
            case I:
                imm = bits >> 20;
                break;
            case S:
                imm = ((bits >> 25) << 5) | ((bits >> 7) & 0x1f);
                break;
            case B:
                imm = (((bits >> 31) & 1) << 12);
                imm |= (((bits >> 7) & 1) << 11);
                imm |= (((bits >> 25) & 0x3f) << 5);
                imm |= (((bits >> 8) & 0xf) << 1);
                break;
            case U:
                imm = bits & 0xff_ff_f0_00;
                break;
            case J:
                imm = ((bits >> 31) & 1) << 20; // 20
                imm |= ((bits >> 21) & 0x3ff) << 1; // 10:1
                imm |= ((bits >> 20) & 1) << 11; // 11
                imm |= ((bits >> 12) & 0xff) << 12; // 19:12
                break;
        }
        return imm;
    }

    public long extractRegister(Register reg, long bits) {
        switch (reg) {
            case Rd:
                return (bits >> 7) & 0x1f;
            case R1:
                return (bits >> 15) & 0x1f;
            case R2:
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
        for (ProtoInstruction proto : opcode2Instruction.get(opcode)) {
            if (proto.getFunc3() == func3 && proto.getFunc7() == extractFunc7(opcode, proto.getType())) {
                assert result == null;
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

    public static int getInstructionLength(int word16) {
        if ((word16 & 0b11) != 0b11) {
            return 16;
        }
        if ((word16 & 0b11100) != 0b11100) {
            return 32;
        }
        if ((word16 & (1 << 5)) == 0) {
            return 48;
        }
        if ((word16 & (1 << 6)) == 0) {
            return 64;
        }
        throw new UnsupportedOperationException("Unimplemented instruction length");
    }

    public enum Register {
        Rd,
        R1,
        R2,
    }
}
