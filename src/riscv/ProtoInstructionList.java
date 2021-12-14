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
            new ProtoInstruction(InstructionType.R, "REMU", 0b0110011, 0b111, 0b0000001),
            
            // C, illegal/reserved instructions are manually disabled
            
            // quadrant 0
            //000 0 0 00 Illegal instruction
            new CompressedProtoInstruction(CompressedFormat.CIW, "C.ADDI4SPN", 0b00, 0b000),
            // skipped: 001 uimm[5:3] rs10 uimm[7:6] rd0 00 C.FLD (RV32/64)
            // skipped: 001 uimm[5:4|8] rs10 uimm[7:6] rd0 00 C.LQ (RV128)
            new CompressedProtoInstruction(CompressedFormat.CL, "C.LW", 0b00, 0b010),
            // skipped: 011 uimm[5:3] rs10 uimm[2|6] rd0 00 C.FLW (RV32)
            // skipped: 011 uimm[5:3] rs10 uimm[7:6] rd0 00 C.LD (RV64/128)
            //100 — 0 Reserved
            // skipped: 101 uimm[5:3] rs10 uimm[7:6] rs20 00 C.FSD (RV32/64)
            // skipped: 101 uimm[5:4|8] rs10 uimm[7:6] rs20 00 C.SQ (RV128)
            new CompressedProtoInstruction(CompressedFormat.CS, "C.SW", 0b00, 0b110),
            // skipped: 111 uimm[5:3] rs10 uimm[2|6] rs20 00 C.FSW (RV32)
            // skipped: 111 uimm[5:3] rs10 uimm[7:6] rs20 00 C.SD (RV64/128)
            
            // quadrant 1
            //new CompressedProtoInstruction(CompressedFormat.CR, "C.NOP", 0b01, 0b0000),
            new CompressedProtoInstruction(CompressedFormat.CI, "C.ADDI", 0b01, 0b000),
            new CompressedProtoInstruction(CompressedFormat.CJ, "C.JAL", 0b01, 0b001),
            // skipped: 001 imm[5] rs1/rd6=0 imm[4:0] 01 C.ADDIW (RV64/128; RES, rd=0)
            new CompressedProtoInstruction(CompressedFormat.CI, "C.LI", 0b01, 0b010),
            new CompressedProtoInstruction(CompressedFormat.CI, "C.ADDI16SP", 0b01, 0b011),
            new CompressedProtoInstruction(CompressedFormat.CI, "C.LUI", 0b01, 0b011),
            new CompressedProtoInstruction(CompressedFormat.CI, "C.SRLI", 0b01, 0b100),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.SRLI64", 0b01, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CI, "C.SRAI", 0b01, 0b100),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.SRAI64", 0b01, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CI, "C.ANDI", 0b01, 0b100),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.SUB", 0b01, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.XOR", 0b01, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.OR", 0b01, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.AND", 0b01, 0b1000),
            //100 1 11 — 10 — 01 Reserved
            //100 1 11 — 11 — 01 Reserved
            new CompressedProtoInstruction(CompressedFormat.CJ, "C.J", 0b01, 0b101),
            new CompressedProtoInstruction(CompressedFormat.CB, "C.BEQZ", 0b01, 0b110),
            new CompressedProtoInstruction(CompressedFormat.CB, "C.BNEZ", 0b01, 0b111),
            
            // quadrant 2
            new CompressedProtoInstruction(CompressedFormat.CI, "C.SLLI", 0b10, 0b000),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.SLLI64", 0b10, 0b0000),
            // skipped: 001 uimm[5] rd uimm[4:3|8:6] 10 C.FLDSP (RV32/64)
            // skipped: 001 uimm[5] rd6=0 uimm[4|9:6] 10 C.LQSP (RV128; RES, rd=0)
            new CompressedProtoInstruction(CompressedFormat.CI, "C.LWSP", 0b10, 0b010),
            // skipped: 011 uimm[5] rd uimm[4:2|7:6] 10 C.FLWSP (RV32)
            // skipped: 011 uimm[5] rd6=0 uimm[4:3|8:6] 10 C.LDSP (RV64/128; RES, rd=0)
            new CompressedProtoInstruction(CompressedFormat.CR, "C.JR", 0b10, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.MV", 0b10, 0b1000),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.EBREAK", 0b10, 0b1001),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.JALR", 0b10, 0b1001),
            new CompressedProtoInstruction(CompressedFormat.CR, "C.ADD", 0b10, 0b1001),
            // skipped: 101 uimm[5:3|8:6] rs2 10 C.FSDSP (RV32/64)
            // skipped: 101 uimm[5:4|9:6] rs2 10 C.SQSP (RV128)
            new CompressedProtoInstruction(CompressedFormat.CSS, "C.SWSP", 0b10, 0b110),
            // skipped: 111 uimm[5:2|7:6] rs2 10 C.FSWSP (RV32)
            // skipped: 111 uimm[5:3|8:6] rs2 10 C.SDSP (RV64/128
            // <list end>
    };
}