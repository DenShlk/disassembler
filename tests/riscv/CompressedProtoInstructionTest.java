package riscv;

import org.junit.jupiter.api.Test;
import riscv.CompressedFormat;
import riscv.CompressedProtoInstruction;
import riscv.InstructionSize;
import riscv.ProtoInstruction;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CompressedProtoInstructionTest {

    CompressedProtoInstruction generateRandomInstr(Random random, CompressedFormat format) {
        return new CompressedProtoInstruction(
                format,
                "",
                random.nextInt(1 << 2),
                format == CompressedFormat.CR ? random.nextInt(1 << 4) : random.nextInt(1 << 3)
        );
    }

    @Test
    void getSize() {
        Random random = new Random(0);

        for (CompressedFormat format : CompressedFormat.values()) {
            for (int i = 0; i < 100; i++) {
                assertEquals(InstructionSize.COMPRESSED_16, generateRandomInstr(random, format).getSize());
            }
        }
    }

    @Test
    void getType() {
        Random random = new Random(0);

        for (CompressedFormat format : CompressedFormat.values()) {
            for (int i = 0; i < 100; i++) {
                assertThrows(UnsupportedOperationException.class, () -> generateRandomInstr(random, format).getType());
            }
        }
    }

    @Test
    void getFunc3And4() {

        Random random = new Random(0);

        for (CompressedFormat format : CompressedFormat.values()) {
            for (int i = 0; i < 100; i++) {
                if (format == CompressedFormat.CR) {
                    assertEquals(ProtoInstruction.UNDEFINED_FUNC, generateRandomInstr(random, format).getFunc3());
                } else {
                    assertEquals(ProtoInstruction.UNDEFINED_FUNC, generateRandomInstr(random, format).getFunc4());
                }
            }
        }
    }

    @Test
    void getFunc7() {
        Random random = new Random(0);

        for (CompressedFormat format : CompressedFormat.values()) {
            for (int i = 0; i < 100; i++) {
                assertEquals(ProtoInstruction.UNDEFINED_FUNC, generateRandomInstr(random, format).getFunc7());
            }
        }
    }

    @Test
    void getOpcode() {
    }

    @Test
    void getName() {
    }

    @Test
    void getFormat() {
        Random random = new Random(0);

        for (CompressedFormat format : CompressedFormat.values()) {
            for (int i = 0; i < 100; i++) {
                assertEquals(format, generateRandomInstr(random, format).getFormat());
            }
        }
    }
}