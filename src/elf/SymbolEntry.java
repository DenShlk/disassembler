package elf;

import java.util.Map;

public class SymbolEntry extends AbstractInfo {
    public long st_name;
    public long st_info;
    public long st_other;
    public long st_shndx;
    public long st_value;
    public long st_size;

    private String name = null;

    public String getName() {
        assert name != null : "Name is not initialized";
        return name;
    }

    public void setName(String name) {
        assert this.name == null : "Name has already been initialized";
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s",
                st_name, st_value, st_size, getType(), getBind(), getVisibility(), getIndexString(), name);
    }

    private static final Map<Integer, EntryVisibility> number2Visibility = Map.of(
            0, EntryVisibility.DEFAULT,
            1, EntryVisibility.INTERNAL,
            2, EntryVisibility.HIDDEN,
            3, EntryVisibility.PROTECTED);

    public EntryVisibility getVisibility() {
        return number2Visibility.get((int) (st_other & 0x3));
    }

    private static final Map<Integer, EntryType> number2Type = Map.of(
            0, EntryType.NOTYPE,
            1, EntryType.OBJECT,
            2, EntryType.FUNC,
            3, EntryType.SECTION,
            4, EntryType.FILE,
            13, EntryType.LOPROC,
            15, EntryType.HIPROC);

    public EntryType getType() {
        return number2Type.get((int) (st_info & 0xf));
    }

    private static final Map<Integer, EntryBind> number2Bind = Map.of(
            0, EntryBind.LOCAL,
            1, EntryBind.GLOBAL,
            2, EntryBind.WEAK,
            13, EntryBind.LOPROC,
            15, EntryBind.HIPROC);

    public EntryBind getBind() {
        return number2Bind.get((int) (st_info >> 4));
    }

    private static final Map<Integer, EntryIndex> number2Index = Map.of(
            0, EntryIndex.UNDEF,
            0xff00, EntryIndex.LOPROC,
            0xff1f, EntryIndex.HIPROC,
            0xfff1, EntryIndex.ABS,
            0xfff2, EntryIndex.COMMON);

    public String getIndexString() {
        if (number2Index.containsKey((int) st_shndx)) {
            return String.valueOf(number2Index.get((int) st_shndx));
        }
        // reserved value
        if (st_shndx > 0xff00) {
            return String.valueOf(EntryIndex.RESERVE);
        }
        return String.valueOf(st_shndx);
    }

    public enum EntryType {
        NOTYPE,
        OBJECT,
        FUNC,
        SECTION,
        FILE,
        LOPROC,
        HIPROC,
    }

    enum EntryBind {
        LOCAL,
        GLOBAL,
        WEAK,
        LOPROC,
        HIPROC,
    }

    enum EntryVisibility {
        DEFAULT,
        INTERNAL,
        HIDDEN,
        PROTECTED,
    }

    enum EntryIndex {
        UNDEF,
        LOPROC,
        HIPROC,
        ABS,
        COMMON,
        RESERVE,
    }
}
