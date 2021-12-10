package elf;

import riscv.Instruction;
import riscv.InstructionDecoder;

import java.util.ArrayList;
import java.util.List;

public class TextSectionReader {
    
    public static List<Instruction> read(byte[] data, SymbolEntry[] labels, int offset, int size, int startAddress) {
        List<Instruction> result = new ArrayList<>();

        int labelInd = 0;
        int missingLabels = 0;

        InstructionDecoder decoder = new InstructionDecoder();
        for (int i = 0; i * 4 < size; i++) {
            long value = readBytes(data, offset + i * 4, 4);
            // TODO: 10.12.2021 remove when .C is supported
            assert InstructionDecoder.getInstructionLength((int) value) == 32;
            Instruction instr = decoder.decode(value);
            instr.setAddress(startAddress + i * 4L);

            while (labelInd < labels.length && labels[labelInd].st_value < instr.getAddress()) {
                labelInd++;
            }
            if (labelInd < labels.length && labels[labelInd].st_value == instr.getAddress()) {
                String label = labels[labelInd].getName();
                if (label.isEmpty()) {
                    label = generateMissingLabel(missingLabels++);
                }
                instr.setLabel(label);
            }

            result.add(instr);
        }

        return result;
    }

    private static String generateMissingLabel(int i) {
        return String.format("LOC_%05x", i);
    }

    private static long readBytes(byte[] data, int offset, int len) {
        assert offset + len <= data.length : "Incorrect bounds";
        long value = 0;
        for (int i = 0; i < len; i++) {
            value = value | (Byte.toUnsignedLong(data[offset + i]) << (i * 8));
        }
        return value;
    }
}
