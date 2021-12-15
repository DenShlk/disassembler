package riscv;

import riscv.CompressedProtoInstruction.RegisterEncoding;
import riscv.CompressedProtoInstruction.RegistersEncodingInfo;
import util.Range;

/**
 * List of compressed proto instructions. Generated with table_parser.py . It parses text of tables from risc-v spec.
 * https://riscv.org/wp-content/uploads/2017/05/riscv-spec-v2.2.pdf
 * (page 82-83, figs 12.4-12.6)
 */
public class CompressedProtoInstructionList {

    public static final CompressedProtoInstruction[] PROTOS = new CompressedProtoInstruction[]{
            // <list start>

            // C, illegal/reserved instructions are manually disabled

            // quadrant 0
            //000 0 0 00 Illegal instruction
            new CompressedProtoInstruction("C.ADDI4SPN", 0b00, 0b000, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS2,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> reorder(instr, 12, 5, 4, 9, 8, 7, 6, 2, 3)
            ),
            new CompressedProtoInstruction("C.LW", 0b00, 0b010, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS2,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> reorder(instr, 12, 5, 4, 3, -1, -1, -1, 2, 6)
            ),
            //100 — 0 Reserved
            new CompressedProtoInstruction("C.SW", 0b00, 0b110, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS2),
                    instr -> reorder(instr, 12, 5, 4, 3, -1, -1, -1, 2, 6)
            ),

            // quadrant 1
            new CompressedProtoInstruction("C.NOP", 0b01, 0b000, 0,
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractFullRs1(instr) == 0
            ),
            new CompressedProtoInstruction("C.ADDI", 0b01, 0b000, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractUimm5(instr),
                    instr -> extractFullRs1(instr) != 0
            ),
            new CompressedProtoInstruction("C.JAL", 0b01, 0b001, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractImm11(instr)
            ),
            new CompressedProtoInstruction("C.LI", 0b01, 0b010, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractUimm5(instr)
            ),
            new CompressedProtoInstruction("C.ADDI16SP", 0b01, 0b011, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> extendSign(reorder(instr, 12, 9, -1, -1, -1, -1, -1, 4, 6, 8, 7, 5), 9),
                    instr -> extractFullRs1(instr) == 2
            ),
            new CompressedProtoInstruction("C.LUI", 0b01, 0b011, ProtoInstruction.UNDEFINED_FUNC,
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> extendSign(reorder(instr, 12, 17, -1, -1, -1, -1, -1, 16, 15, 14, 13, 12), 17),
                    instr -> extractFullRs1(instr) != 2
            ),
            new CompressedProtoInstruction("C.SRLI", 0b01, 0b100, ProtoInstruction.UNDEFINED_FUNC, 0b00,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractUimm5(instr),
                    instr -> extractUimm5(instr) != 0
            ),
            new CompressedProtoInstruction("C.SRLI64", 0b01, 0b100, 0, 0b00,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractUimm5(instr) == 0
            ),
            new CompressedProtoInstruction("C.SRAI", 0b01, 0b100, ProtoInstruction.UNDEFINED_FUNC, 0b01,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractUimm5(instr),
                    instr -> extractUimm5(instr) != 0
            ),
            new CompressedProtoInstruction("C.SRAI64", 0b01, 0b100, 0, 0b01,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractUimm5(instr) == 0
            ),
            new CompressedProtoInstruction("C.ANDI", 0b01, 0b100, ProtoInstruction.UNDEFINED_FUNC, 0b10,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractImm5(instr)
                    ),
            new CompressedProtoInstruction("C.SUB", 0b01, 0b100, 0, 0b11, 0b00,
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS2),
                    instr -> Instruction.UNDEFINED_VALUE
            ),
            new CompressedProtoInstruction("C.XOR", 0b01, 0b100, 0, 0b11, 0b01, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS2),
                    instr -> Instruction.UNDEFINED_VALUE
                            ),
            new CompressedProtoInstruction("C.OR", 0b01, 0b100, 0, 0b11, 0b10, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS2),
                    instr -> Instruction.UNDEFINED_VALUE
                            ),
            new CompressedProtoInstruction("C.AND", 0b01, 0b100, 0, 0b11, 0b11, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.SHORT_AT_RS2),
                    instr -> Instruction.UNDEFINED_VALUE
                            ),
            //100 1 11 — 10 — 01 Reserved
            //100 1 11 — 11 — 01 Reserved
            new CompressedProtoInstruction("C.J", 0b01, 0b101, ProtoInstruction.UNDEFINED_FUNC, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractImm11(instr)
                            ),
            new CompressedProtoInstruction("C.BEQZ", 0b01, 0b110, ProtoInstruction.UNDEFINED_FUNC, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extendSign(reorder(instr, 12, 8, 4, 3, -1, -1, -1, 7, 6, 2, 1, 5), 8)
                            ),
            new CompressedProtoInstruction("C.BNEZ", 0b01, 0b111, ProtoInstruction.UNDEFINED_FUNC, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.SHORT_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extendSign(reorder(instr, 12, 8, 4, 3, -1, -1, -1, 7, 6, 2, 1, 5), 8)
                            ),
            
            // quadrant 2
            new CompressedProtoInstruction("C.SLLI", 0b10, 0b000, ProtoInstruction.UNDEFINED_FUNC, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> extractUimm5(instr),
                    instr -> extractUimm5(instr) != 0
                            ),
            new CompressedProtoInstruction("C.SLLI64", 0b10, 0b000, 0, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractUimm5(instr) == 0
                            ),
            new CompressedProtoInstruction("C.LWSP", 0b10, 0b010, ProtoInstruction.UNDEFINED_FUNC, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> reorder(instr, 12, 5, -1, -1, -1, -1, -1, 4, 3, 2, 7, 6)
                            ),
            new CompressedProtoInstruction("C.JR", 0b10, 0b100, 0, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractUimm5(instr) == 0
                            ),
            new CompressedProtoInstruction("C.MV", 0b10, 0b100, 0, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.FULL_AT_RS2),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractFullRs2(instr) != 0
                            ),
            new CompressedProtoInstruction("C.EBREAK", 0b10, 0b100, 1, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractFullRs1(instr) == 0 && extractFullRs2(instr) == 0
                            ),
            new CompressedProtoInstruction("C.JALR", 0b10, 0b100, 1, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.EXCLUDED),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractFullRs1(instr) != 0 && extractFullRs2(instr) == 0
                            ),
            new CompressedProtoInstruction("C.ADD", 0b10, 0b100, 1, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.FULL_AT_RS1,
                            RegisterEncoding.FULL_AT_RS2),
                    instr -> Instruction.UNDEFINED_VALUE,
                    instr -> extractFullRs2(instr) != 0
                            ),
            new CompressedProtoInstruction("C.SWSP", 0b10, 0b110, ProtoInstruction.UNDEFINED_FUNC, 
                    new RegistersEncodingInfo(
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.EXCLUDED,
                            RegisterEncoding.FULL_AT_RS2),
                    instr -> reorder(instr, 12, 5, 4, 3, 2, 7, 6)
                            ),
            // <list end>
    };

    private static int extractFullRs2(int instr) {
        return extract(new Range(6, 2), instr);
    }

    private static int extractFullRs1(int instr) {
        return extract(new Range(11, 7), instr);
    }

    private static long extractUimm5(int instr) {
        return reorder(instr, 12, 5, -1, -1, -1, -1, -1, 4, 3, 2, 1, 0);
    }

    private static long extractImm5(int instr) {
        return extendSign(extractUimm5(instr), 5);
    }

    private static long extractImm11(int instr) {
        return extendSign(reorder(instr, 12,
                11, 4, 9, 8, 10, 6, 7, 3, 2, 1, 5), 11);
    }

    private static long extendSign(long bits, int upper) {
        if ((bits & (1L << upper)) != 0) {
            return bits | (-1L << upper);
        }
        return bits;
    }

    private static int extract(Range range, int instr) {
        return (instr >> range.lower) & ((1 << range.getLength()) - 1);
    }

    /**
     * Reorders bits in given order, starting from upper. Skips bit i if to[i]=-1.
     */
    private static int reorder(int bits, int upper, int... to) {
        int value = 0;
        for (int i = 0; i < to.length; i++) {
            if (to[i] == -1) {
                continue;
            }
            value |= ((bits >> (upper - i)) & 1) << to[i];
        }
        return value;
    }
}