package utils;

import java.io.*;

public class ConsoleAndFilePrinter extends PrintStream {
    private final PrintStream console;
    private final PrintStream fileOut;

    public ConsoleAndFilePrinter(String filePath) throws FileNotFoundException {
        super(System.out); // dummy, we override all relevant methods
        this.console = System.out;
        this.fileOut = new PrintStream(new FileOutputStream(filePath, false), true);
    }

    @Override
    public void println(String x) {
        console.println(x);
        fileOut.println(x);
    }

    @Override
    public void print(String x) {
        console.print(x);
        fileOut.print(x);
    }

    @Override
    public void println() {
        console.println();
        fileOut.println();
    }

    // Overload for all primitive types (important!)
    @Override
    public void println(Object x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(int x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(boolean x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(double x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(float x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(char x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(char[] x) {
        println(String.valueOf(x));
    }
    // ...and so on for other variants if needed

    @Override
    public void close() {
        super.close();
        fileOut.close();
    }
}