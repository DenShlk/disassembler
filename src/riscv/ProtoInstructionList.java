package riscv;

import util.Range;

/**
 * List of instructions. Generated with table_parser.py . It parses text of table from risc-v spec.
 * https://riscv.org/wp-content/uploads/2017/05/riscv-spec-v2.2.pdf
 * (page 104)
 */
public class ProtoInstructionList {
    public static final ProtoInstruction[] PROTOS = new ProtoInstruction[]{
            // <list start>

            // I
            new ProtoInstruction(InstructionType.U, "LUI", 0b0110111),
            new ProtoInstruction(InstructionType.U, "AUIPC", 0b0010111),
            new ProtoInstruction(InstructionType.J, "JAL", 0b1101111),
            new ProtoInstruction(InstructionType.I, "JALR", 0b1100111, 0b000),
            new ProtoInstruction(InstructionType.B, "BEQ", 0b1100011, 0b000),
            new ProtoInstruction(InstructionType.B, "BNE", 0b1100011, 0b001),
            new ProtoInstruction(InstructionType.B, "BLT", 0b1100011, 0b100),
            new ProtoInstruction(InstructionType.B, "BGE", 0b1100011, 0b101),
            new ProtoInstruction(InstructionType.B, "BLTU", 0b1100011, 0b110),
            new ProtoInstruction(InstructionType.B, "BGEU", 0b1100011, 0b111),
            new ProtoInstruction(InstructionType.I, "LB", 0b0000011, 0b000),
            new ProtoInstruction(InstructionType.I, "LH", 0b0000011, 0b001),
            new ProtoInstruction(InstructionType.I, "LW", 0b0000011, 0b010),
            new ProtoInstruction(InstructionType.I, "LBU", 0b0000011, 0b100),
            new ProtoInstruction(InstructionType.I, "LHU", 0b0000011, 0b101),
            new ProtoInstruction(InstructionType.S, "SB", 0b0100011, 0b000),
            new ProtoInstruction(InstructionType.S, "SH", 0b0100011, 0b001),
            new ProtoInstruction(InstructionType.S, "SW", 0b0100011, 0b010),
            new ProtoInstruction(InstructionType.I, "ADDI", 0b0010011, 0b000),
            new ProtoInstruction(InstructionType.I, "SLTI", 0b0010011, 0b010),
            new ProtoInstruction(InstructionType.I, "SLTIU", 0b0010011, 0b011),
            new ProtoInstruction(InstructionType.I, "XORI", 0b0010011, 0b100),
            new ProtoInstruction(InstructionType.I, "ORI", 0b0010011, 0b110),
            new ProtoInstruction(InstructionType.I, "ANDI", 0b0010011, 0b111),
            new ProtoInstruction(InstructionType.R, "SLLI", 0b0010011, 0b001, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRLI", 0b0010011, 0b101, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRAI", 0b0010011, 0b101, 0b0100000),
            new ProtoInstruction(InstructionType.R, "ADD", 0b0110011, 0b000, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SUB", 0b0110011, 0b000, 0b0100000),
            new ProtoInstruction(InstructionType.R, "SLL", 0b0110011, 0b001, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SLT", 0b0110011, 0b010, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SLTU", 0b0110011, 0b011, 0b0000000),
            new ProtoInstruction(InstructionType.R, "XOR", 0b0110011, 0b100, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRL", 0b0110011, 0b101, 0b0000000),
            new ProtoInstruction(InstructionType.R, "SRA", 0b0110011, 0b101, 0b0100000),
            new ProtoInstruction(InstructionType.R, "OR", 0b0110011, 0b110, 0b0000000),
            new ProtoInstruction(InstructionType.R, "AND", 0b0110011, 0b111, 0b0000000),
            // 0000 pred succ 00000 000 00000 0001111 FENCE
            // 0000 0000 0000 00000 001 00000 0001111 FENCE.I
            new ProtoInstruction(InstructionType.I, "ECALL", 0b1110011, 0b000),
            new ProtoInstruction(InstructionType.I, "EBREAK", 0b1110011, 0b000),
            new ProtoInstruction(InstructionType.I, "CSRRW", 0b1110011, 0b001),
            new ProtoInstruction(InstructionType.I, "CSRRS", 0b1110011, 0b010),
            new ProtoInstruction(InstructionType.I, "CSRRC", 0b1110011, 0b011),
            new ProtoInstruction(InstructionType.I, "CSRRWI", 0b1110011, 0b101),
            new ProtoInstruction(InstructionType.I, "CSRRSI", 0b1110011, 0b110),
            new ProtoInstruction(InstructionType.I, "CSRRCI", 0b1110011, 0b111),
            
            // M
            new ProtoInstruction(InstructionType.R, "MUL", 0b0110011, 0b000, 0b0000001),
            new ProtoInstruction(InstructionType.R, "MULH", 0b0110011, 0b001, 0b0000001),
            new ProtoInstruction(InstructionType.R, "MULHSU", 0b0110011, 0b010, 0b0000001),
            new ProtoInstruction(InstructionType.R, "MULHU", 0b0110011, 0b011, 0b0000001),
            new ProtoInstruction(InstructionType.R, "DIV", 0b0110011, 0b100, 0b0000001),
            new ProtoInstruction(InstructionType.R, "DIVU", 0b0110011, 0b101, 0b0000001),
            new ProtoInstruction(InstructionType.R, "REM", 0b0110011, 0b110, 0b0000001),
            new ProtoInstruction(InstructionType.R, "REM", 0b0110011, 0b111, 0b0000001),
            // <list end>
    };
}