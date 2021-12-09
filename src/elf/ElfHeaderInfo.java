package elf;

public class ElfHeaderInfo extends AbstractInfo {
    public long EI_CLASS;
    public long EI_DATA;
    public long EI_VERSION;
    public long EI_OSABI;
    public long EI_ABIVERSION;
    public long e_type;
    public long e_machine;
    public long e_version;
    public long e_entry;
    public long e_phoff;
    public long e_shoff;
    public long e_flags;
    public long e_ehsize;
    public long e_phentsize;
    public long e_phnum;
    public long e_shentsize;
    public long e_shnum;
    public long e_shstrndx;
}
