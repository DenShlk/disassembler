package elf;

import riscv.Instruction;
import riscv.InstructionManager;
import riscv.ProtoInstruction;

import java.util.ArrayList;
import java.util.List;

public class TextSectionReader {
    
    public static List<Instruction> read(byte[] data, int offset, int size) {
        List<Instruction> result = new ArrayList<>();

        InstructionManager manager = new InstructionManager();
        for (int i = 0; i * 4 < size; i++) {
            long value = readBytes(data, offset + i * 4, 4);
            Instruction instr = manager.disassemble(value);
            result.add(instr);
        }

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
