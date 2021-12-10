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
                        instr.getImm()
                );
            case U:
            case J:
                return String.format("%s, %s",
                        RegisterNamingConverter.toAbi(instr.getRd()),
                        instr.getImm()
                );
            default:
                throw new UnsupportedOperationException("Unknown type of instruction");
        }
    }
}
