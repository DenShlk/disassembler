package elf;

public class SectionHeaderInfo extends AbstractInfo {
    public long sh_name;
    public long sh_type;
    public long sh_flags;
    public long sh_addr;
    public long sh_offset;
    public long sh_size;
    public long sh_link;
    public long sh_info;
    public long sh_addralign;
    public long sh_entsize;
}
