package elf;

public class ProgramHeaderReader extends AbstractElfReader<ProgramHeaderInfo> {

    private static final DataBlock[] blocks = new DataBlock[]{
            new DataBlock("p_type", 4),
            new DataBlock("p_offset", 4),
            new DataBlock("p_vaddr", 4),
            new DataBlock("p_paddr", 4),
            new DataBlock("p_filesz", 4),
            new DataBlock("p_memsz", 4),
            new DataBlock("p_flags", 4),
            new DataBlock("p_align", 4),
    };

    @Override
    protected DataBlock[] getBlocks() {
        return blocks;
    }

    @Override
    protected ProgramHeaderInfo getInfoClassInstance() {
        return new ProgramHeaderInfo();
    }

    @Override
    protected Class<ProgramHeaderInfo> getInfoClass() {
        return ProgramHeaderInfo.class;
    }
}
