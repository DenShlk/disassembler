package riscv;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InstructionDecoderTest {
    // TODO: 13.12.2021
    private InstructionDecoder decoder = new InstructionDecoder();

    @Test
    void decode() {
    }

    private long nextIntMasked(Random random, long mask) {
        return random.nextInt() & mask;
    }

    private int extractBits(int instr, Mapping... maps) {
        String bits = Integer.toBinaryString(instr);
        bits = "0".repeat(32 - bits.length()) + bits;
        bits =  new StringBuilder(bits).reverse().toString();
        StringBuilder res = new StringBuilder("0".repeat(32));
        for (Mapping map : maps) {
            if (map.from.lower == map.from.upper) {
                for (int i = 0; i <= map.to.upper - map.to.lower; i++) {
                    res.setCharAt(map.to.lower + i, bits.charAt(map.from.lower));
                }
            } else {
                assert map.to.upper - map.to.lower == map.from.upper - map.from.lower;
                for (int i = 0; i <= map.to.upper - map.to.lower; i++) {
                    res.setCharAt(map.to.lower + i, bits.charAt(map.from.lower + i));
                }
            }
        }
        return Integer.parseUnsignedInt(res.reverse().toString(), 2);
    }

    @Test
    void extractBitsTest() {
        Random random = new Random(0);

        for (int i = 0; i < 100; i++) {
            int four = random.nextInt(1 << 4);
            int shift = random.nextInt(4);
            assertEquals(four, extractBits(four << shift,
                    new Mapping(new Range(3 + shift, shift), new Range(3, 0))
            ));

            int single = random.nextInt(2);
            assertEquals(-single, extractBits(single,
                    new Mapping(new Range(0), new Range(31, 0))
            ));
        }
    }

    @Test
    void extractImmediate() {
        Random random = new Random(0);

        for (int i = 0; i < 100; i++) {
            assertThrows(UnsupportedOperationException.class,
                    () -> decoder.extractImmediate(InstructionType.R, random.nextInt()));

            int instr = random.nextInt();
            assertEquals(extractBits(instr,
                    new Mapping(new Range(31), new Range(31, 11)),
                    new Mapping(new Range(30, 20), new Range(10, 0))
            ), decoder.extractImmediate(InstructionType.I, instr));

            assertEquals(extractBits(instr,
                    new Mapping(new Range(31), new Range(31, 11)),
                    new Mapping(new Range(30, 25), new Range(10, 5)),
                    new Mapping(new Range(11, 8), new Range(4, 1)),
                    new Mapping(new Range(7), new Range(0))
            ), decoder.extractImmediate(InstructionType.S, instr));

            assertEquals(extractBits(instr,
                    new Mapping(new Range(31), new Range(31, 12)),
                    new Mapping(new Range(7), new Range(11)),
                    new Mapping(new Range(30, 25), new Range(10, 5)),
                    new Mapping(new Range(11, 8), new Range(4, 1))
            ), decoder.extractImmediate(InstructionType.B, instr));


            // not convenient way of testing, but statements are true
            long imm11_0 = random.nextInt(1 << 12);
            assertEquals(imm11_0, decoder.extractImmediate(InstructionType.I, random.nextInt(1 << 20) + (imm11_0 << 20)));

            long imm_4_0 = random.nextInt(1 << 5);
            long imm_11_5 = random.nextInt(1 << 7);
            assertEquals((imm_11_5 << 5) + imm_4_0, decoder.extractImmediate(InstructionType.S,
                    (imm_11_5 << 25) + (imm_4_0 << 7) +
                            nextIntMasked(random, 0b0000000_11111_11111_111_00000_1111111)));

            long imm_12 = random.nextInt(2);
            long imm_10_5 = random.nextInt(1 << 6);
            long imm_4_1 = random.nextInt(1 << 4);
            long imm_11 = random.nextInt(2);
            assertEquals((imm_12 << 12) + (imm_10_5 << 5) + (imm_4_1 << 1) + (imm_11 << 11),
                    decoder.extractImmediate(InstructionType.B,
                    (imm_12 << 31) + (imm_10_5 << 25) + (imm_4_1 << 8) + (imm_11 << 7) +
                    nextIntMasked(random, 0b0000000_11111_11111_111_00000_1111111)));

            long imm_31_12 = nextIntMasked(random, ~0b11111_1111111);
            // TODO: 13.12.2021 all types
        }
    }

    @Test
    void extractRegister() {
        Random random = new Random(0);

        for (int i = 0; i < 100; i++) {
            for (int r = 0; r < 32; r++) {
                long rd = (random.nextInt() << 12) + random.nextInt(1 << 7) + (r << 7);
                assertEquals(r, decoder.extractRegister(InstructionDecoder.Register.Rd, rd));
                long r1 = (random.nextInt() << 20) + random.nextInt(1 << 15) + (r << 15);
                assertEquals(r, decoder.extractRegister(InstructionDecoder.Register.R1, r1));
                long r2 = (random.nextInt() << 25) + random.nextInt(1 << 20) + (r << 20);
                assertEquals(r, decoder.extractRegister(InstructionDecoder.Register.R2, r2));
            }
        }
    }

    @Test
    void searchProto() {
    }

    @Test
    void extractFunc3() {
        Random random = new Random(0);

        for (int i = 0; i < (1 << 3); i++) {
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

        for (int i = 0; i < (1 << 7); i++) {
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

    private static class Range {
        int lower;
        int upper;

        public Range(int value) {
            this.lower = value;
            this.upper = value;
        }

        public Range(int upper, int lower) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private static class Mapping {
        Range from;
        Range to;

        public Mapping(Range from, Range to) {
            this.from = from;
            this.to = to;
        }
    }
}