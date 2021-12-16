package elf;

public class SymbolEntryReader extends AbstractElfReader<SymbolEntry> {

    private static final DataBlock[] blocks = new DataBlock[]{
            new DataBlock("st_name", 4),
            new DataBlock("st_value", 4),
            new DataBlock("st_size", 4),
            new DataBlock("st_info", 1),
            new DataBlock("st_other", 1),
            new DataBlock("st_shndx", 2),
    };

    @Override
    protected DataBlock[] getBlocks() {
        return blocks;
    }

    @Override
    protected SymbolEntry getInfoClassInstance() {
        return new SymbolEntry();
    }

    @Override
    protected Class<SymbolEntry> getInfoClass() {
        return SymbolEntry.class;
    }
}
