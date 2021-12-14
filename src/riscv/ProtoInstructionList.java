package riscv;

/**
 * List of instructions. Generated with table_parser.py . It parses text of table from risc-v spec.
 * https://riscv.org/wp-content/uploads/2017/05/riscv-spec-v2.2.pdf
 * (page 104)
 */
public class ProtoInstructionList {
    public static final ProtoInstruction[] PROTOS = new ProtoInstruction[]{
            // <list start>

            // I
            new ProtoInstruction(InstructionType.U, "LUI", ProtoInstruction.SizeType.NORMAL_32, 0b0110111),
            new ProtoInstruction(InstructionType.U, "AUIPC", ProtoInstruction.SizeType.NORMAL_32, 0b0010111),
            new ProtoInstruction(InstructionType.J, "JAL", ProtoInstruction.SizeType.NORMAL_32, 0b1101111),
            new ProtoInstruction(InstructionType.I, "JALR", ProtoInstruction.SizeType.NORMAL_32, 0b1100111, 0b000),
            new ProtoInstruction(InstructionType.B, "BEQ", ProtoInstruction.SizeType.NORMAL_32, 0b1100011, 0b000),
            new ProtoInstruction(InstructionType.B, "BNE", ProtoInstruction.SizeType.NORMAL_32, 0b1100011, 0b001),
            new ProtoInstruction(InstructionType.B, "BLT", ProtoInstruction.SizeType.NORMAL_32, 0b1100011, 0b100),
            new ProtoInstruction(InstructionType.B, "BGE", ProtoInstruction.SizeType.NORMAL_32, 0b1100011, 0b101),
            new ProtoInstruction(InstructionType.B, "BLTU", ProtoInstruction.SizeType.NORMAL_32, 0b1100011, 0b110),
            new ProtoInstruction(InstructionType.B, "BGEU", ProtoInstruction.SizeType.NORMAL_32, 0b1100011, 0b111),
            new ProtoInstruction(InstructionType.I, "LB", ProtoInstruction.SizeType.NORMAL_32, 0b0000011, 0b000),
            new ProtoInstruction(InstructionType.I, "LH", ProtoInstruction.SizeType.NORMAL_32, 0b0000011, 0b001),
            new ProtoInstruction(InstructionType.I, "LW", ProtoInstruction.SizeType.NORMAL_32, 0b0000011, 0b010),
            new ProtoInstruction(InstructionType.I, "LBU", ProtoInstruction.SizeType.NORMAL_32, 0b0000011, 0b100),
            new ProtoInstruction(InstructionType.I, "LHU", ProtoInstruction.SizeType.NORMAL_32, 0b0000011, 0b101),
            new ProtoInstruction(InstructionType.S, "SB", ProtoInstruction.SizeType.NORMAL_32, 0b0100011, 0b000),
            new ProtoInstruction(InstructionType.S, "SH", ProtoInstruction.SizeType.NORMAL_32, 0b0100011, 0b001),
            new ProtoInstruction(InstructionType.S, "SW", ProtoInstruction.SizeType.NORMAL_32, 0b0100011, 0b010),
            new ProtoInstruction(InstructionType.I, "ADDI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b000),
            new ProtoInstruction(InstructionType.I, "SLTI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b010),
            new ProtoInstruction(InstructionType.I, "SLTIU", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b011),
            new ProtoInstruction(InstructionType.I, "XORI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b100),
            new ProtoInstruction(InstructionType.I, "ORI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b110),
            new ProtoInstruction(InstructionType.I, "ANDI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b111),
            new ProtoInstruction(InstructionType.R, "SLLI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b001, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRLI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b101, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRAI", ProtoInstruction.SizeType.NORMAL_32, 0b0010011, 0b101, 0b0100000),
            new ProtoInstruction(InstructionType.R, "ADD", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b000, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SUB", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b000, 0b0100000),
            new ProtoInstruction(InstructionType.R, "SLL", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b001, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SLT", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b010, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SLTU", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b011, 0b0000000),
            new ProtoInstruction(InstructionType.R, "XOR", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b100, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRL", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b101, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRA", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b101, 0b0100000),
            new ProtoInstruction(InstructionType.R, "OR", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b110, 0b0000000),
            new ProtoInstruction(InstructionType.R, "AND", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b111, 0b0000000),
            // 0000 pred succ 00000 000 00000 0001111 FENCE
            // 0000 0000 0000 00000 001 00000 0001111 FENCE.I
            new ProtoInstruction(InstructionType.I, "ECALL", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b000),
            new ProtoInstruction(InstructionType.I, "EBREAK", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b000),
            new ProtoInstruction(InstructionType.I, "CSRRW", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b001),
            new ProtoInstruction(InstructionType.I, "CSRRS", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b010),
            new ProtoInstruction(InstructionType.I, "CSRRC", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b011),
            new ProtoInstruction(InstructionType.I, "CSRRWI", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b101),
            new ProtoInstruction(InstructionType.I, "CSRRSI", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b110),
            new ProtoInstruction(InstructionType.I, "CSRRCI", ProtoInstruction.SizeType.NORMAL_32, 0b1110011, 0b111),
            // M
            new ProtoInstruction(InstructionType.R, "MUL", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b000, 0b0000001),
            new ProtoInstruction(InstructionType.R, "MULH", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b001, 0b0000001),
            new ProtoInstruction(InstructionType.R, "MULHSU", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b010, 0b0000001),
            new ProtoInstruction(InstructionType.R, "MULHU", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b011, 0b0000001),
            new ProtoInstruction(InstructionType.R, "DIV", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b100, 0b0000001),
            new ProtoInstruction(InstructionType.R, "DIVU", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b101, 0b0000001),
            new ProtoInstruction(InstructionType.R, "REM", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b110, 0b0000001),
            new ProtoInstruction(InstructionType.R, "REM", ProtoInstruction.SizeType.NORMAL_32, 0b0110011, 0b111, 0b0000001),
            // <list end>
    };
}