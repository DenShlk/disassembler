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
        for (int i = 0; i * 2 < size; i++) {
            long value = readBytes(data, offset + i * 2, 2);
            // TODO: 10.12.2021 remove when .C is supported
            // TODO: 12.12.2021 fix 2/4 
            //assert InstructionDecoder.getInstructionLength((int) value) == 32 : "Unsupported instruction length";
            if (InstructionDecoder.getInstructionLength((int) value) == 16) {
                continue;
            } else {
                value = readBytes(data, offset + i * 2, 4);
                i ++;
            }

            Instruction instr = decoder.decode(value);
            instr.setAddress(startAddress + i * 2L);

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
