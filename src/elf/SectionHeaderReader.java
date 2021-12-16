package elf;

public class SectionHeaderReader extends AbstractElfReader<SectionHeaderInfo> {

    private static final DataBlock[] blocks = new DataBlock[]{
            new DataBlock("sh_name", 4),
            new DataBlock("sh_type", 4),
            new DataBlock("sh_flags", 4),
            new DataBlock("sh_addr", 4),
            new DataBlock("sh_offset", 4),
            new DataBlock("sh_size", 4),
            new DataBlock("sh_link", 4),
            new DataBlock("sh_info", 4),
            new DataBlock("sh_addralign", 4),
            new DataBlock("sh_entsize", 4),
    };

    @Override
    protected DataBlock[] getBlocks() {
        return blocks;
    }

    @Override
    protected SectionHeaderInfo getInfoClassInstance() {
        return new SectionHeaderInfo();
    }

    @Override
    protected Class<SectionHeaderInfo> getInfoClass() {
        return SectionHeaderInfo.class;
    }
}
