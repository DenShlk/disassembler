package riscv;

import elf.SymbolEntry;

import java.util.*;

public class LabelMarker {

    private final static Set<String> controlInstructions = Set.of(
            "BEQ",
            "BNE",
            "BGE",
            "BLT",
            "BLTU",
            "BGEU",
            "JAL",
            "JALR"
            //"AUIPS"
    );

    public static void mark(List<Instruction> instructions, SymbolEntry[] labels) {
        Set<Integer> labeled = new HashSet<>();

        for (Instruction instr : instructions) {
            if (controlInstructions.contains(instr.getName())) {
                labeled.add((int) (instr.getAddress() + extractOffset(instr)));
            }
        }

        Map<Integer, String> address2label = new HashMap<>();
        for (SymbolEntry label : labels) {
            address2label.put((int) label.st_value, label.getName());
        }

        int missingLabels = 0;
        for (Instruction instr : instructions) {
            if (labeled.contains((int) instr.getAddress())) {
                String label = address2label.get((int) instr.getAddress());
                if (label == null) {
                    label = generateMissingLabel(missingLabels++);
                }
                instr.setLabel(label);
            }
        }
    }

    private static String generateMissingLabel(int i) {
        return String.format("LOC_%05x", i);
    }

    private static int extractOffset(Instruction instr) {
        if (instr.getImm() != Instruction.UNDEFINED_VALUE) {
            return (int) instr.getImm();
        }
        throw new IllegalStateException("Wrong instruction, offset assumed to be in immediate, but it is undefined.");
    }
}
