package elf;

import java.lang.reflect.Field;

// Reader of ELF files. Suited only for 32-bit encoding, little endian.
// If upgrading to 64 bit, note that sign arithmetics will lead to bugs with 8 byte values in longs.
public abstract class AbstractElfReader<T extends AbstractInfo> {
    protected abstract DataBlock[] getBlocks();

    protected abstract T getInfoClassInstance();

    protected abstract Class<T> getInfoClass();

    public T readBlocks(byte[] data, int offset) {
        T info = getInfoClassInstance();

        int pos = offset;
        for (DataBlock block : getBlocks()) {
            long value = readWithAssert(data, pos, block.len, block.minValue, block.maxValue, block.errorMessage);
            if (!block.name.isEmpty()) {
                try {
                    Field field = getInfoClass().getField(block.name);
                    field.setLong(info, value);
                } catch (NoSuchFieldException e) {
                    System.err.println("Field " + block.name + " not found. It was skipped." +
                            System.lineSeparator() + e.getMessage());
                } catch (IllegalAccessException e) {
                    System.err.println("Field " + block.name + " is not accessible. It was skipped." +
                            System.lineSeparator() + e.getMessage());
                }
            }
            pos += block.len;
        }

        return info;
    }

    /** Reads bytes from range and checks if their value lies in given range, if negative, throws exception
     * @param minValue inclusive
     * @param maxValue inclusive
     */
    private static long readWithAssert(byte[] data, int offset, int len, long minValue, long maxValue, String message) {
        if (offset + len > data.length) {
            throw new IllegalArgumentException("Part of block is out of bounds of file");
        }
        long value = 0;
        for (int i = 0; i < len; i++) {
            value = value | (Byte.toUnsignedLong(data[offset + i]) << (i * 8));
        }
        if (minValue <= value && value <= maxValue) {
            return value;
        }
        throw new IllegalStateException(message);
    }

    protected static class DataBlock {
        // exact name of field in elf.ElfHeaderInfo class
        String name;

        int len;
        long minValue;
        long maxValue;
        String errorMessage;

        public DataBlock(String name, int len, long exactValue, String errorMessage) {
            this(name, len, exactValue, exactValue, errorMessage);
        }

        // any value
        public DataBlock(String name, int len, String errorMessage) {
            this(name, len, 0x0, (1L << (len * 8 + 1)) - 1, errorMessage);
        }

        public DataBlock(String name, int len, long minValue, long maxValue, String errorMessage) {
            this.name = name;
            this.len = len;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.errorMessage = errorMessage;
        }
    }

    public void testBlocks() {
        boolean ok = true;

        AbstractInfo info = getInfoClassInstance();

        for (DataBlock block : getBlocks()) {
            if (!block.name.isEmpty()) {
                try {
                    Field field = getInfoClass().getField(block.name);
                    field.setLong(info, 0L);
                } catch (NoSuchFieldException e) {
                    System.err.println("Field " + block.name + " not found. It was skipped." +
                            System.lineSeparator() + e.getMessage());
                    ok = false;
                } catch (IllegalAccessException e) {
                    System.err.println("Field " + block.name + " is not accessible. It was skipped." +
                            System.lineSeparator() + e.getMessage());
                    ok = false;
                }
            }
        }

        if (!ok) {
            System.err.println("Errors in blocks configuration found!");
        }
    }
}
