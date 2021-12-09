package elf;

public class ElfHeaderReader extends AbstractElfReader<ElfHeaderInfo> {

    private static final DataBlock[] blocks = new DataBlock[]{
            new DataBlock("", 4, 0x46_4c_45_7f, "Not elf file (or corrupted)"),
            new DataBlock("EI_CLASS", 1, 0x1, "Only 32-bit encoding is supported"),
            new DataBlock("EI_DATA", 1, 0x1, "Only little endian encoding is supported"),
            new DataBlock("EI_VERSION", 1, 0x1, "Wrong EI_version"),
            new DataBlock("EI_OSABI", 1, 0x0, 0x12, "Unknown OS"),
            new DataBlock("EI_ABIVERSION", 1, ""),
            new DataBlock("", 7, ""),
            new DataBlock("e_type", 2, 0x02, "Wrong object file type (must be EXEC)"),
            new DataBlock("e_machine", 2, 0xF3, "Only RISC-V instruction set is supported"),
            new DataBlock("e_version", 4, 0x1, "Wrong E_version"),
            new DataBlock("e_entry", 4, ""),
            new DataBlock("e_phoff", 4, ""),
            new DataBlock("e_shoff", 4, ""),
            new DataBlock("e_flags", 4, ""),
            new DataBlock("e_ehsize", 2, 0x34,
                    "Header size must be 52, since only 32-bit format allowed"),
            new DataBlock("e_phentsize", 2, ""),
            new DataBlock("e_phnum", 2, ""),
            new DataBlock("e_shentsize", 2, ""),
            new DataBlock("e_shnum", 2, ""),
            new DataBlock("e_shstrndx", 2, ""),
    };


    @Override
    protected DataBlock[] getBlocks() {
        return blocks;
    }

    @Override
    protected ElfHeaderInfo getInfoClassInstance() {
        return new ElfHeaderInfo();
    }

    @Override
    protected Class<ElfHeaderInfo> getInfoClass() {
        return ElfHeaderInfo.class;
    }
}
