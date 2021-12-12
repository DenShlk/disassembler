package io;

public class OutputAccumulator {

    private final StringBuilder output = new StringBuilder();

    public void print(String s) {
        output.append(s);
    }

    public void println(String s) {
        output.append(s).append(System.lineSeparator());
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
