package elf;

import riscv.Instruction;
import riscv.InstructionDecoder;
import riscv.InstructionSize;
import riscv.LabelMarker;

import java.util.ArrayList;
import java.util.List;

public class TextSectionReader {
    
    public static List<Instruction> read(byte[] data, SymbolEntry[] labels, int offset, int size, int startAddress) {
        List<Instruction> result = new ArrayList<>();

        InstructionDecoder decoder = new InstructionDecoder();
        for (int i = 0; i * 2 < size; i++) {
            long value = readBytes(data, offset + i * 2, 2);
            InstructionSize instrSize = InstructionDecoder.getInstructionSize((int) value);
            if (instrSize == InstructionSize.NORMAL_32) {
                value = readBytes(data, offset + i * 2, 4);
            }

            Instruction instr = decoder.decode(value, instrSize);
            instr.setAddress(startAddress + i * 2L);

            result.add(instr);

            if (instrSize == InstructionSize.NORMAL_32) {
                i++;
            }
        }

        LabelMarker.mark(result, labels);

        return result;
    }

    private static long readBytes(byte[] data, int offset, int len) {
        if (offset + len > data.length) {
            throw new IllegalArgumentException("Incorrect bounds");
        }
        long value = 0;
        for (int i = 0; i < len; i++) {
            value = value | (Byte.toUnsignedLong(data[offset + i]) << (i * 8));
        }
        return value;
    }
}
