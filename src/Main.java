import io.OutputAccumulator;
import elf.*;
import riscv.Instruction;
import riscv.InstructionPrinter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    private static final OutputAccumulator out = new OutputAccumulator();

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            throw new IllegalArgumentException("Illegal arguments. Must be: <input file path> <output file path>");
        }

        byte[] data = readBytes(args[0]);
        System.out.printf("Read %d bytes from input file\n", data.length);

        SectionHeaderInfo[] sHeaders = decodeSectionHeaders(data);
        SymbolEntry[] symbolEntries = decodeSymbolTable(data, sHeaders);
        List<Instruction> instructions = decodeInstructions(data, sHeaders, symbolEntries);

        printText(instructions);
        printSymtab(symbolEntries);

        dumpFile(args[1]);
        System.out.println("Wrote output file");
    }

    private static void printText(List<Instruction> instructions) {
        out.println(".text");
        for (Instruction instruction : instructions) {
            out.println(InstructionPrinter.print(instruction));
        }
    }

    private static List<Instruction> decodeInstructions(byte[] data, SectionHeaderInfo[] sHeaders,
                                                        SymbolEntry[] symbolEntries) {
        int textSectionIndex = IntStream.range(0, sHeaders.length)
                .filter(i -> sHeaders[i].sh_type == 0x1) // .text type (program data)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("File does not contain .text section"));
        SectionHeaderInfo textSection = sHeaders[textSectionIndex];

        SymbolEntry[] textLabels = Arrays.stream(symbolEntries)
                //.filter(e -> e.st_shndx == textSectionIndex)
                .toArray(SymbolEntry[]::new);

        List<Instruction> instructions = TextSectionReader.read(data, textLabels,
                (int) textSection.sh_offset, (int) textSection.sh_size, (int) textSection.sh_addr);
        return instructions;
    }

    private static void printSymtab(SymbolEntry[] symbolEntries) {
        out.println();
        out.println(".symtab");
        out.printf("%s %-15s %7s %-8s %-8s %-8s %6s %s\n",
                "Symbol", "Value", "Size", "Type", "Bind", "Vis", "Index", "Name");
        for (int i = 0; i < symbolEntries.length; i++) {
            SymbolEntry entry = symbolEntries[i];
            out.println(entry);
        }
    }

    private static SectionHeaderInfo[] decodeSectionHeaders(byte[] data) {
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

    private static SymbolEntry[] decodeSymbolTable(byte[] data, SectionHeaderInfo[] sectionHeaders) {
        SectionHeaderInfo symtabSection = Arrays.stream(sectionHeaders)
                .filter(x -> x.sh_type == 0x2) // .symtab type (symbol table)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("File does not contain .symtab section"));

        SectionHeaderInfo strtabSection = Arrays.stream(sectionHeaders)
                .filter(x -> x.sh_type == 0x3) // .strtab type (string table)
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

    private static void dumpFile(String path) throws IOException {
        try {
            Files.writeString(Path.of(path), out.dump());
        } catch (IOException e) {
            throw new IOException("Failed writing result to file:" + System.lineSeparator() + e.getMessage());
        }
    }
}
