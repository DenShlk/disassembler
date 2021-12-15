package riscv;

public class InstructionPrinter {

    private static final String UNKNOWN_INSTRUCTION = "unknown_instruction";

    public static String print(Instruction instr) {
        if (instr == null) {
            return UNKNOWN_INSTRUCTION;
        }
        return String.format("%08x %11s %s %s",
                instr.getAddress(),
                instr.getLabel() == null ? "" : instr.getLabel() + ":",
                instr.getName(),
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
                        instr.getZimm()); // TODO: 12.12.2021 called zimm, probably unsigned
            } else {
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.CSRtoAbi(instr.getImm()),
                        RegisterNamingConverter.toAbi(instr.getR1()));
            }
        }
        if (instr.getName().equals("LW")) {
            return String.format("%s, %s(%s)",
                    RegisterNamingConverter.toAbi(instr.getRd()),
                    instr.getImm(),
                    RegisterNamingConverter.toAbi(instr.getR1())
            );
        }
        if (instr.getName().equals("SW")) {
            return String.format("%s, %s(%s)",
                    RegisterNamingConverter.toAbi(instr.getR2()),
                    instr.getImm(),
                    RegisterNamingConverter.toAbi(instr.getR1())
            );
        }

        switch (instr.getType()) {
            case R:
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.toAbi(instr.getR1()),
                        RegisterNamingConverter.toAbi(instr.getR2())
                );
            case I:
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        RegisterNamingConverter.toAbi(instr.getR1()),
                        instr.getImm()
                );
            case S:
            case B:
                return String.format("%s, %s, %s",
                        RegisterNamingConverter.toAbi(instr.getR1()),
                        RegisterNamingConverter.toAbi(instr.getR2()),
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
                        RegisterNamingConverter.toAbi(instr.getR1())
                );
            case ("C.SW"):
                return String.format("%s, %s(%s)",
                        RegisterNamingConverter.toAbi(instr.getR2()),
                        instr.getImm(),
                        RegisterNamingConverter.toAbi(instr.getR1())
                );
            case("C.LWSP"):
                return String.format("%s, %s(sp)",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        instr.getImm()
                );
            case ("C.SWSP"):
                return String.format("%s, %s(sp)",
                        RegisterNamingConverter.toAbi(instr.getR2()),
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
        if (instr.getR1() != Instruction.UNDEFINED_VALUE) {
            if (!res.isEmpty()) {
                res += ", ";
            }
            res += RegisterNamingConverter.toAbi(instr.getR1());
        }
        if (instr.getR2() != Instruction.UNDEFINED_VALUE) {
            if (!res.isEmpty()) {
                res += ", ";
            }
            res += RegisterNamingConverter.toAbi(instr.getR2());
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
