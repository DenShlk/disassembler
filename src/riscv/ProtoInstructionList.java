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
            new ProtoInstruction(InstructionType.U, 0b0110111, "LUI"),
            new ProtoInstruction(InstructionType.U, 0b0010111, "AUIPC"),
            new ProtoInstruction(InstructionType.J, 0b1101111, "JAL"),
            new ProtoInstruction(InstructionType.I, 0b000, 0b1100111, "JALR"),
            new ProtoInstruction(InstructionType.B, 0b000, 0b1100011, "BEQ"),
            new ProtoInstruction(InstructionType.B, 0b001, 0b1100011, "BNE"),
            new ProtoInstruction(InstructionType.B, 0b100, 0b1100011, "BLT"),
            new ProtoInstruction(InstructionType.B, 0b101, 0b1100011, "BGE"),
            new ProtoInstruction(InstructionType.B, 0b110, 0b1100011, "BLTU"),
            new ProtoInstruction(InstructionType.B, 0b111, 0b1100011, "BGEU"),
            new ProtoInstruction(InstructionType.I, 0b000, 0b0000011, "LB"),
            new ProtoInstruction(InstructionType.I, 0b001, 0b0000011, "LH"),
            new ProtoInstruction(InstructionType.I, 0b010, 0b0000011, "LW"),
            new ProtoInstruction(InstructionType.I, 0b100, 0b0000011, "LBU"),
            new ProtoInstruction(InstructionType.I, 0b101, 0b0000011, "LHU"),
            new ProtoInstruction(InstructionType.S, 0b000, 0b0100011, "SB"),
            new ProtoInstruction(InstructionType.S, 0b001, 0b0100011, "SH"),
            new ProtoInstruction(InstructionType.S, 0b010, 0b0100011, "SW"),
            new ProtoInstruction(InstructionType.I, 0b000, 0b0010011, "ADDI"),
            new ProtoInstruction(InstructionType.I, 0b010, 0b0010011, "SLTI"),
            new ProtoInstruction(InstructionType.I, 0b011, 0b0010011, "SLTIU"),
            new ProtoInstruction(InstructionType.I, 0b100, 0b0010011, "XORI"),
            new ProtoInstruction(InstructionType.I, 0b110, 0b0010011, "ORI"),
            new ProtoInstruction(InstructionType.I, 0b111, 0b0010011, "ANDI"),
            new ProtoInstruction(InstructionType.R, 0b001, 0b0000000, 0b0010011, "SLLI"),
            new ProtoInstruction(InstructionType.R, 0b101, 0b0000000, 0b0010011, "SRLI"),
            new ProtoInstruction(InstructionType.R, 0b101, 0b0100000, 0b0010011, "SRAI"),
            new ProtoInstruction(InstructionType.R, 0b000, 0b0000000, 0b0110011, "ADD"),
            new ProtoInstruction(InstructionType.R, 0b000, 0b0100000, 0b0110011, "SUB"),
            new ProtoInstruction(InstructionType.R, 0b001, 0b0000000, 0b0110011, "SLL"),
            new ProtoInstruction(InstructionType.R, 0b010, 0b0000000, 0b0110011, "SLT"),
            new ProtoInstruction(InstructionType.R, 0b011, 0b0000000, 0b0110011, "SLTU"),
            new ProtoInstruction(InstructionType.R, 0b100, 0b0000000, 0b0110011, "XOR"),
            new ProtoInstruction(InstructionType.R, 0b101, 0b0000000, 0b0110011, "SRL"),
            new ProtoInstruction(InstructionType.R, 0b101, 0b0100000, 0b0110011, "SRA"),
            new ProtoInstruction(InstructionType.R, 0b110, 0b0000000, 0b0110011, "OR"),
            new ProtoInstruction(InstructionType.R, 0b111, 0b0000000, 0b0110011, "AND"),
            // 0000 pred succ 00000 000 00000 0001111 FENCE
            // 0000 0000 0000 00000 001 00000 0001111 FENCE.I
            new ProtoInstruction(InstructionType.I, 0b000, 0b1110011, "ECALL"),
            new ProtoInstruction(InstructionType.I, 0b000, 0b1110011, "EBREAK"),
            new ProtoInstruction(InstructionType.I, 0b001, 0b1110011, "CSRRW"),
            new ProtoInstruction(InstructionType.I, 0b010, 0b1110011, "CSRRS"),
            new ProtoInstruction(InstructionType.I, 0b011, 0b1110011, "CSRRC"),
            new ProtoInstruction(InstructionType.I, 0b101, 0b1110011, "CSRRWI"),
            new ProtoInstruction(InstructionType.I, 0b110, 0b1110011, "CSRRSI"),
            new ProtoInstruction(InstructionType.I, 0b111, 0b1110011, "CSRRCI"),
            // M
            new ProtoInstruction(InstructionType.R, 0b000, 0b0000001, 0b0110011, "MUL"),
            new ProtoInstruction(InstructionType.R, 0b001, 0b0000001, 0b0110011, "MULH"),
            new ProtoInstruction(InstructionType.R, 0b010, 0b0000001, 0b0110011, "MULHSU"),
            new ProtoInstruction(InstructionType.R, 0b011, 0b0000001, 0b0110011, "MULHU"),
            new ProtoInstruction(InstructionType.R, 0b100, 0b0000001, 0b0110011, "DIV"),
            new ProtoInstruction(InstructionType.R, 0b101, 0b0000001, 0b0110011, "DIVU"),
            new ProtoInstruction(InstructionType.R, 0b110, 0b0000001, 0b0110011, "REM"),
            new ProtoInstruction(InstructionType.R, 0b111, 0b0000001, 0b0110011, "REM"),
            // <list end>
    };
}