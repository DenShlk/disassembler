package elf;

import riscv.Instruction;
import riscv.InstructionDecoder;
import riscv.LabelMarker;

import java.util.ArrayList;
import java.util.List;

public class TextSectionReader {
    
    public static List<Instruction> read(byte[] data, SymbolEntry[] labels, int offset, int size, int startAddress) {
        List<Instruction> result = new ArrayList<>();

        int labelInd = 0;

        InstructionDecoder decoder = new InstructionDecoder();
        for (int i = 0; i * 4 < size; i++) {
            long value = readBytes(data, offset + i * 4, 4);
            // TODO: 10.12.2021 remove when .C is supported
            assert InstructionDecoder.getInstructionLength((int) value) == 32;
            Instruction instr = decoder.decode(value);
            instr.setAddress(startAddress + i * 4L);

            result.add(instr);
        }

        LabelMarker.mark(result, labels);

        return result;
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
