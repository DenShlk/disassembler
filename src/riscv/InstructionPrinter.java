package riscv;

import java.util.Set;

public class InstructionPrinter {

    private static final boolean LOWER_CASE_NAMES = true;
    private static final Set<String> LOAD_INSTRUCTIONS = Set.of("LW", "LH", "LHU", "LB", "LBU");
    private static final Set<String> STORE_INSTRUCTIONS = Set.of("SW", "SH", "SB");
    private static final Set<String> BINARY_OP_INSTRUCTIONS = Set.of("SLLI", "SRLI", "SRAI");
    private static final Set<String> IGNORE_OPERANDS_INSTRUCTIONS = Set.of("ECALL", "EBREAK");
    private static final String UNKNOWN_INSTRUCTION = "unknown_instruction";

    public static String print(Instruction instr) {
        if (instr == null) {
            return UNKNOWN_INSTRUCTION;
        }
        return String.format("%08x %11s %s %s",
                instr.getAddress(),
                instr.getLabel() == null ? "" : instr.getLabel() + ":",
                LOWER_CASE_NAMES ? instr.getName().toLowerCase() : instr.getName(),
                printOperands(instr));
    }

    private static String printOperands(Instruction instr) {
        if (instr.isUnknownInstruction()) {
            return "";
        }
        if (instr.getSize() == InstructionSize.COMPRESSED_16) {
            return printCompressedOperands(instr);
        }
        if (instr.isCSR()) {
            if (instr.getZimm() != Instruction.UNDEFINED_VALUE) {
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.CSRtoAbi(instr.getImm()),
                        instr.getZimm());
            } else {
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.CSRtoAbi(instr.getImm()),
                        RegisterNamingConverter.toAbi(instr.getRs1()));
            }
        }
        if (LOAD_INSTRUCTIONS.contains(instr.getName())) {
            return String.format("%s, %s(%s)",
                    RegisterNamingConverter.toAbi(instr.getRd()),
                    instr.getImm(),
                    RegisterNamingConverter.toAbi(instr.getRs1())
            );
        }
        if (STORE_INSTRUCTIONS.contains(instr.getName())) {
            return String.format("%s, %s(%s)",
                    RegisterNamingConverter.toAbi(instr.getRs2()),
                    instr.getImm(),
                    RegisterNamingConverter.toAbi(instr.getRs1())
            );
        }
        if (BINARY_OP_INSTRUCTIONS.contains(instr.getName())) {
            int shamt = (int) instr.getRs2();
            return String.format("%s, %s, %s",
                    RegisterNamingConverter.toAbi(instr.getRd()),
                    RegisterNamingConverter.toAbi(instr.getRs1()),
                    shamt
            );
        }
        if (IGNORE_OPERANDS_INSTRUCTIONS.contains(instr.getName())) {
            return "";
        }

        switch (instr.getType()) {
            case R:
                assert !instr.hasOutLabel();
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.toAbi(instr.getRs1()),
                        RegisterNamingConverter.toAbi(instr.getRs2())
                );
            case I:
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.toAbi(instr.getRs1()),
                        instr.hasOutLabel() ? instr.getOutLabel() : instr.getImm()
                );
            case S:
            case B:
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRs1()),
                        RegisterNamingConverter.toAbi(instr.getRs2()),
                        instr.hasOutLabel() ? instr.getOutLabel() : instr.getImm()
                );
            case U:
            case J:
                return String.format("%s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        instr.hasOutLabel() ? instr.getOutLabel() : instr.getImm()
                );
            default:
                throw new UnsupportedOperationException("Unknown type of instruction");
        }
    }

    private static String printCompressedOperands(Instruction instr) {
        switch (instr.getName()) {
            case ("C.LW"):
                return String.format("%s, %s(%s)",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        instr.getImm(),
                        RegisterNamingConverter.toAbi(instr.getRs1())
                );
            case ("C.SW"):
                return String.format("%s, %s(%s)",
                        RegisterNamingConverter.toAbi(instr.getRs2()),
                        instr.getImm(),
                        RegisterNamingConverter.toAbi(instr.getRs1())
                );
            case("C.LWSP"):
                return String.format("%s, %s(sp)",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        instr.getImm()
                );
            case ("C.SWSP"):
                return String.format("%s, %s(sp)",
                        RegisterNamingConverter.toAbi(instr.getRs2()),
                        instr.getImm()
                );
            case ("C.ADDI4SPN"):
                return String.format("%s, sp, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        instr.getImm()
                );
            case ("C.ADDI16SP"):
                return String.format("sp, %s",
                        instr.getImm()
                );
        }

        String res = "";
        if (instr.getRd() != Instruction.UNDEFINED_VALUE) {
            res += RegisterNamingConverter.toAbi(instr.getRd());
        }
        if (instr.getRs1() != Instruction.UNDEFINED_VALUE) {
            if (!res.isEmpty()) {
                res += ", ";
            }
            res += RegisterNamingConverter.toAbi(instr.getRs1());
        }
        if (instr.getRs2() != Instruction.UNDEFINED_VALUE) {
            if (!res.isEmpty()) {
                res += ", ";
            }
            res += RegisterNamingConverter.toAbi(instr.getRs2());
        }
        if (instr.hasOutLabel()) {
            if (!res.isEmpty()) {
                res += ", ";
            }
            res += instr.getOutLabel();
        } else if (instr.getImm() != Instruction.UNDEFINED_VALUE) {
            if (!res.isEmpty()) {
                res += ", ";
            }
            res += instr.getImm();
        }
        return res;
    }
}
