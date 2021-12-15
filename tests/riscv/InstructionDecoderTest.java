package riscv;

import org.junit.jupiter.api.Test;
import util.Range;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

// TODO: 15.12.2021 .C
class InstructionDecoderTest {
    private final InstructionDecoder decoder = new InstructionDecoder();

    @Test
    void decode() {
        // TODO: 13.12.2021
    }

    private String toFullBinaryString(int x) {
        String bits = Integer.toBinaryString(x);
        bits = "0".repeat(32 - bits.length()) + bits;
        return bits;
    }

    private String toFullBinaryString(long x) {
        String bits = Long.toBinaryString(x);
        bits = "0".repeat(64 - bits.length()) + bits;
        return bits;
    }

    private int extractBits(int instr, Mapping... maps) {
        String bits = new StringBuilder(toFullBinaryString(instr)).reverse().toString();
        StringBuilder res = new StringBuilder("0".repeat(32));
        for (Mapping map : maps) {
            if (map.from.lower == map.from.upper) {
                for (int i = 0; i < map.to.getLength(); i++) {
                    res.setCharAt(map.to.lower + i, bits.charAt(map.from.lower));
                }
            } else {
                assert map.to.getLength() == map.from.getLength();
                for (int i = 0; i < map.to.getLength(); i++) {
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

    /**
     * @link https://riscv.org/wp-content/uploads/2017/05/riscv-spec-v2.2.pdf
     * page 12, figure 2.4
     */
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

            assertEquals(extractBits(instr,
                    new Mapping(new Range(31), new Range(31)),
                    new Mapping(new Range(30, 12), new Range(30, 12))
            ), decoder.extractImmediate(InstructionType.U, instr));

            assertEquals(extractBits(instr,
                    new Mapping(new Range(31), new Range(31, 20)),
                    new Mapping(new Range(19, 12), new Range(19, 12)),
                    new Mapping(new Range(20), new Range(11)),
                    new Mapping(new Range(30, 25), new Range(10, 5)),
                    new Mapping(new Range(24, 21), new Range(4, 1))
            ), decoder.extractImmediate(InstructionType.J, instr));
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

    private long randomFillBits(Random random, long bits, Range... ranges) {
        for (Range range : ranges) {
            for (int i = range.lower; i <= range.upper; i++) {
                bits = bits ^ ((long) random.nextInt(2) << i);
            }
        }
        return bits;
    }

    @Test
    void randomFillBitsTest() {
        Random random = new Random(0);
        for (int i = 0; i < 100; i++) {
            long x = random.nextLong();
            String bits = toFullBinaryString(x);
            int from = random.nextInt(64);
            int len = random.nextInt(64 - from + 1);
            assertEquals(bits.substring(from, from + len),
                    toFullBinaryString(randomFillBits(random, x, new Range(from, from + len - 1)))
                            .substring(from, from + len));
        }
    }

    @Test
    void searchProto() {
        Random random = new Random(0);
        for (ProtoInstruction proto : ProtoInstructionList.PROTOS) {
            if (proto.getName().equals("ECALL") || proto.getName().equals("EBREAK")
                    || proto instanceof CompressedProtoInstruction) {
                continue;
            }

            for (int i = 0; i < 100; i++) {
                long instr = proto.getOpcode();
                if (proto.getFunc3() != ProtoInstruction.UNDEFINED_FUNC) {
                    instr |= proto.getFunc3() << 12;
                } else {
                    instr = randomFillBits(random, instr, new Range(14, 12));
                }
                if (proto.getFunc7() != ProtoInstruction.UNDEFINED_FUNC) {
                    instr |= proto.getFunc7() << 25;
                } else {
                    instr = randomFillBits(random, instr, new Range(31, 25));
                }

                instr = randomFillBits(random, instr,
                        new Range(24, 20),
                        new Range(19, 15),
                        new Range(11, 7));
                assertEquals(proto, decoder.searchProto(instr));
            }
        }

        long ecall = 0b1110011;
        long ebreak = ecall + (1 << 20);
        assertEquals("ECALL", decoder.searchProto(ecall).getName());
        assertEquals("EBREAK", decoder.searchProto(ebreak).getName());
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
                assertEquals(InstructionSize.COMPRESSED_16,
                        InstructionDecoder.getInstructionSize((random.nextInt() << 2) + i));
            }
        }

        for (int i = 0; i < 0b111; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(InstructionSize.NORMAL_32,
                        InstructionDecoder.getInstructionSize((random.nextInt() << 5) + (i << 2) + 0b11));
            }
        }

        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class, () ->
                    InstructionDecoder.getInstructionSize((random.nextInt() << 6) + 0b011111)); // 48
        }

        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class, () ->
                    InstructionDecoder.getInstructionSize((random.nextInt() << 7) + 0b0111111)); // 64
        }


        assertThrows(UnsupportedOperationException.class, () -> InstructionDecoder.getInstructionSize(-1));
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