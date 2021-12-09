import elf.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            //throw new IllegalArgumentException("Illegal arguments. Must be: <input file path> <output file path>");
        }

        byte[] data = readBytes(args[0]);

        SectionHeaderInfo[] sHeaders = getSectionHeaders(data);

        SectionHeaderInfo textSection = Arrays.stream(sHeaders)
                .filter(x -> x.sh_type == 0x1) // .text type (program data)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("File does not contain .text section"));

        SymbolEntry[] symbolEntries = parseSymbolTable(data, sHeaders);

        for (int i = 0; i < symbolEntries.length; i++) {
            SymbolEntry entry = symbolEntries[i];
            System.out.printf("[%4d] %s\n", i, entry);
        }
    }

    private static SectionHeaderInfo[] getSectionHeaders(byte[] data) {
        ElfHeaderReader ehReader = new ElfHeaderReader();
        ehReader.testBlocks();
        ElfHeaderInfo eHeader = ehReader.readBlocks(data, 0);
        System.out.println("ELF header read successfully");

        ProgramHeaderReader phReader = new ProgramHeaderReader();
        phReader.testBlocks();
        ProgramHeaderInfo[] pHeaders = new ProgramHeaderInfo[(int) eHeader.e_phnum];
        for (int i = 0; i < eHeader.e_phnum; i++) {
            int off = (int) (eHeader.e_phoff + eHeader.e_phentsize * i);
            pHeaders[i] = phReader.readBlocks(data, off);
        }
        System.out.println("Read " + pHeaders.length + " program headers");

        SectionHeaderReader shReader = new SectionHeaderReader();
        phReader.testBlocks();
        SectionHeaderInfo[] sHeaders = new SectionHeaderInfo[(int) eHeader.e_shnum];
        for (int i = 0; i < eHeader.e_shnum; i++) {
            int off = (int) (eHeader.e_shoff + eHeader.e_shentsize * i);
            sHeaders[i] = shReader.readBlocks(data, off);
        }
        System.out.println("Read " + sHeaders.length + " section headers");
        return sHeaders;
    }

    private static SymbolEntry[] parseSymbolTable(byte[] data, SectionHeaderInfo[] sectionHeaders) {
        SectionHeaderInfo symtabSection = Arrays.stream(sectionHeaders)
                .filter(x -> x.sh_type == 0x2) // .symtab type (symbol table)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("File does not contain .symtab section"));

        SectionHeaderInfo strtabSection = Arrays.stream(sectionHeaders)
                .filter(x -> x.sh_type == 0x3) // .symtab type (symbol table)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("File does not contain .strtab section"));

        SymbolEntryReader seReader = new SymbolEntryReader();
        StringTableReader stReader = new StringTableReader(data, (int) strtabSection.sh_offset,
                (int) strtabSection.sh_size);

        assert symtabSection.sh_size % symtabSection.sh_entsize == 0 :
                "Size of symbol entry must be divisor of .symtab section size";
        SymbolEntry[] symbolEntries = new SymbolEntry[(int) (symtabSection.sh_size / symtabSection.sh_entsize)];
        for (int i = 0; i < symbolEntries.length; i++) {
            symbolEntries[i] = seReader.readBlocks(data, (int) (symtabSection.sh_offset + i * symtabSection.sh_entsize));
            symbolEntries[i].setName(stReader.readString(symbolEntries[i].st_name));
        }

        System.out.println("Read " + symbolEntries.length + " entities of symbol table");

        return symbolEntries;
    }

    private static byte[] readBytes(String path) throws IOException {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found." + System.lineSeparator() + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Failed to read file:" + System.lineSeparator() + e.getMessage());
        }
    }
}
