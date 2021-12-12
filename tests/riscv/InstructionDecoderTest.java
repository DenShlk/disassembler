package riscv;

import org.junit.jupiter.api.Test;

import javax.naming.InsufficientResourcesException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InstructionDecoderTest {
    // TODO: 13.12.2021

    @Test
    void decode() {
    }

    @Test
    void extractImmediate() {
    }

    @Test
    void extractRegister() {
    }

    @Test
    void registerToString() {
    }

    @Test
    void searchProto() {
    }

    @Test
    void extractFunc3() {
        Random random = new Random(0);

        for (int i = 0; i < (1<<3); i++) {
            for (int j = 0; j < 10; j++) {
                for (InstructionType type : InstructionType.values()) {
                    int actual = InstructionDecoder.extractFunc3(((long) i << 12) +
                                    random.nextInt(1 << 12) + ((long) random.nextInt() << 15), type);
                    if (type == InstructionType.U || type == InstructionType.J) {
                        assertEquals(ProtoInstruction.UNDEFINED_FUNC, actual);
                    } else {
                        assertEquals(i, actual);
                    }
                }
            }
        }
    }

    @Test
    void extractFunc7() {
        Random random = new Random(0);

        for (int i = 0; i < (1<<7); i++) {
            assertEquals(i, InstructionDecoder.extractFunc7(((long) i << 25) + random.nextInt(1 << 25),
                    InstructionType.R));
        }

        for (InstructionType type : InstructionType.values()) {
            if (type != InstructionType.R) {
                assertEquals(ProtoInstruction.UNDEFINED_FUNC, InstructionDecoder.extractFunc7(random.nextInt(), type));
            }
        }
    }

    @Test
    void getInstructionLength() {
        Random random = new Random(0);

        for (int i = 0; i < 0b11; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(16,
                        InstructionDecoder.getInstructionLength((random.nextInt() << 2) + i));
            }
        }

        for (int i = 0; i < 0b111; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(32,
                        InstructionDecoder.getInstructionLength((random.nextInt() << 5) + (i << 2) + 0b11));
            }
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(48,
                    InstructionDecoder.getInstructionLength((random.nextInt() << 6) + 0b011111));
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(64,
                    InstructionDecoder.getInstructionLength((random.nextInt() << 7) + 0b0111111));
        }


        assertThrows(UnsupportedOperationException.class, () -> InstructionDecoder.getInstructionLength(-1));
    }
}