package riscv;

import java.util.HashMap;
import java.util.Map;

public class RegisterNamingConverter {

    private static final Map<Range, NamingRange> RANGES = Map.of(
            new Range(0), new NamingRange("zero"),
            new Range(1), new NamingRange("ra"),
            new Range(2), new NamingRange("sp"),
            new Range(3), new NamingRange("gp"),
            new Range(4), new NamingRange("tp"),
            new Range(5, 7), new NamingRange("t", 0, 2),
            new Range(8, 9), new NamingRange("s", 0, 1), // what means s0/fp?
            new Range(10, 17), new NamingRange("a", 0, 7),
            new Range(18, 27), new NamingRange("s", 2, 11),
            new Range(28, 31), new NamingRange("t", 3, 6)
    );

    private static final Map<Range, NamingRange> COMPRESSED_RANGES = Map.of(
            new Range(0, 7), new NamingRange("x", 8, 15)
    );

    private static final Map<Integer, String> CSR_REGISTER_2_NAME = Map.of(
            0x001, "fflags",
            0x002, "frm",
            0x003, "fcsr",
            0xC00, "cycle",
            0xC01, "time",
            0xC02, "instret",
            0xC80, "cycleh",
            0xC81, "timeh",
            0xC82, "instreth"
    );

    private static final Map<Long, String> register2name = new HashMap<>();
    private static final Map<Long, String> compressedRegister2name = new HashMap<>();

    static {
        unpackRanges(RANGES, register2name);
        unpackRanges(COMPRESSED_RANGES, compressedRegister2name);
    }

    private static void unpackRanges(Map<Range, NamingRange> ranges, Map<Long, String> fullMap) {
        ranges.forEach((range, naming) -> {
            for (int i = 0; i < range.getLength(); i++) {
                fullMap.put((long) (range.left + i), naming.toString(i));
            }
        });
    }


    public static String compressedToAbi(long reg) {
        assert compressedRegister2name.containsKey(reg) : reg;
        return compressedRegister2name.get(reg);
    }

    public static String toAbi(long reg) {
        assert register2name.containsKey(reg) : reg;
        return register2name.get(reg);
    }

    public static String CSRtoAbi(long reg) {
        assert CSR_REGISTER_2_NAME.containsKey((int) reg) : reg;
        return CSR_REGISTER_2_NAME.get((int) reg);
    }

    private static class Range {
        public final int left;
        public final int right;

        public Range(int value) {
            this(value, value);
        }

        public Range(int left, int right) {
            assert left <= right;
            this.left = left;
            this.right = right;
        }

        public int getLength() {
            return right - left + 1;
        }
    }

    private static class NamingRange extends Range {
        public final String pref;
        private final boolean hasIndex;

        private NamingRange(String pref, int left, int right, boolean hasIndex) {
            super(left, right);
            this.pref = pref;
            this.hasIndex = hasIndex;
        }

        public NamingRange(String pref, int left, int right) {
            this(pref, left, right, true);
        }

        public NamingRange(String pref, int value) {
            this(pref, value, value, true);
        }

        public NamingRange(String pref) {
            this(pref, 0, 0, false);
        }

        public String toString(int i) {
            return pref + (hasIndex ? left + i : "");
        }
    }
}
