package util;

/**
 * Represents range with inclusive bounds.
 * For convenient use with bit indices in binary numbers, constructor has upper bound as first argument,
 * corresponding to bit numbering from right to left.
 */
public class Range {
    public final int lower;
    public final int upper;

    public Range(int value) {
        this.lower = value;
        this.upper = value;
    }

    public Range(int upper, int lower) {
        this.lower = lower;
        this.upper = upper;
    }

    public int getLength() {
        return upper - lower + 1;
    }
}
