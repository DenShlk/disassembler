package io;

public class OutputAccumulator {

    private final StringBuilder output = new StringBuilder();

    public void print(Object s) {
        output.append(s.toString());
    }

    public void println(Object s) {
        output.append(s.toString()).append(System.lineSeparator());
    }

    public void println() {
        println("");
    }

    public void printf(String format, Object... args) {
        output.append(String.format(format, args));
    }

    public String dump() {
        return output.toString();
    }
}
