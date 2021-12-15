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
        int i = 0;
        for (Instruction instr : instructions) {
            if (address2label.containsKey((int) instr.getAddress())) {
                instr.setLabel(address2label.get((int) instr.getAddress()));
            } else if (labeled.contains((int) instr.getAddress())) {
                instr.setLabel(generateMissingLabel(i++));
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
