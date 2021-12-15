package riscv;

import elf.SymbolEntry;

import java.util.*;

// TODO: 14.12.2021 tests
public class LabelMarker {

    private final static Set<String> controlInstructions = Set.of(
            "BEQ",
            "BNE",
            "BGE",
            "BLT",
            "BLTU",
            "BGEU",
            "JAL",
            "JALR",
            // C
            "C.BEQZ",
            "C.BNEZ",
            "C.J",
            "C.JAL"
    );

    public static void mark(List<Instruction> instructions, SymbolEntry[] labels) {
        Map<Long, String> address2label = new HashMap<>();
        for (SymbolEntry label : labels) {
            address2label.put(label.st_value, label.getName());
        }

        int missingLabels = 0;
        for (Instruction instr : instructions) {
            if (controlInstructions.contains(instr.getName())) {
                //labeled.add((int) (instr.getAddress() + extractOffset(instr)));
                long pointsTo = instr.getAddress() + extractOffset(instr);
                if (address2label.containsKey(pointsTo)) {
                    instr.setOutLabel(address2label.get(pointsTo));
                } else {
                    instr.setOutLabel(generateMissingLabel(missingLabels++));
                    address2label.put(pointsTo, instr.getOutLabel());
                }
            }
        }

        for (Instruction instr : instructions) {
            String label = address2label.getOrDefault(instr.getAddress(), null);
            if (label != null) {
                instr.setLabel(label);
            }
        }
    }

    private static String generateMissingLabel(long address) {
        return String.format("LOC_%05x", address);
    }

    private static int extractOffset(Instruction instr) {
        if (instr.getImm() != Instruction.UNDEFINED_VALUE) {
            return (int) instr.getImm();
        }
        throw new IllegalStateException("Wrong instruction, offset assumed to be in immediate, but it is undefined.");
    }
}
