package elf;

public class StringTableReader {

    private final byte[] data;
    private final int offset;
    private final int size;

    public StringTableReader(byte[] data, int offset, int size) {
        this.data = data;
        this.offset = offset;
        this.size = size;
    }

    public String readString(long index) {
        StringBuilder sb = new StringBuilder();
        for (int i = (int) index; i < size && data[offset + i] != 0; i++) {
            sb.append((char) data[offset + i]);
        }
        return sb.toString();
    }
}
