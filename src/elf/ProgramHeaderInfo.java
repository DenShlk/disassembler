package elf;

public class ProgramHeaderInfo extends AbstractInfo {
    public long p_type;
    public long p_offset;
    public long p_vaddr;
    public long p_paddr;
    public long p_filesz;
    public long p_memsz;
    public long p_flags;
    public long p_align;
}
