package riscv;

import util.Range;

import java.util.HashMap;
import java.util.Map;

public class RegisterNamingConverter {

    private static final Map<Range, NamingRange> RANGES = Map.of(
            new Range(0), new NamingRange("zero"),
            new Range(1), new NamingRange("ra"),
            new Range(2), new NamingRange("sp"),
            new Range(3), new NamingRange("gp"),
            new Range(4), new NamingRange("tp"),
            new Range(7, 5), new NamingRange("t", 2, 0),
            new Range(9, 8), new NamingRange("s", 1, 0), // what means s0/fp?
            new Range(17, 10), new NamingRange("a", 7, 0),
            new Range(27, 18), new NamingRange("s", 11, 2),
            new Range(31, 28), new NamingRange("t", 6, 3)
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

    static {
        RANGES.forEach((range, naming) -> {
            for (int i = 0; i < range.getLength(); i++) {
                register2name.put((long) (range.lower + i), naming.toString(i));
            }
        });
    }

    public static String toAbi(long reg) {
        if (!register2name.containsKey(reg)) {
            throw new IllegalArgumentException("Register " + reg + " is not recognised");
        }
        return register2name.get(reg);
    }

    public static String CSRtoAbi(long reg) {
        if (! CSR_REGISTER_2_NAME.containsKey((int) reg)) {
            throw new IllegalArgumentException("CSR register " + reg + " is not recognised");
        }
        return CSR_REGISTER_2_NAME.get((int) reg);
    }

    private static class NamingRange extends Range {
        public final String pref;
        private final boolean hasIndex;

        private NamingRange(String pref, int upper, int lower, boolean hasIndex) {
            super(upper, lower);
            this.pref = pref;
            this.hasIndex = hasIndex;
        }

        public NamingRange(String pref, int upper, int lower) {
            this(pref, upper, lower, true);
        }

        public NamingRange(String pref, int value) {
            this(pref, value, value, true);
        }

        public NamingRange(String pref) {
            this(pref, 0, 0, false);
        }

        public String toString(int i) {
            return pref + (hasIndex ? lower + i : "");
        }
    }
}
