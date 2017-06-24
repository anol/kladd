package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class DxfSection {

    protected String emptyString = " ";
    private BufferedWriter writer;

    public DxfSection(BufferedWriter writer) {
        this.writer = writer;
    }

    public abstract void printSection() throws IOException;

    protected void printIntVariable(int code, int number) throws IOException {
        this.writer.write(code + "\n" + number + "\n");
    }

    protected void printDoubleVariable(int code, double number) throws IOException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.0", symbols);
        format.setMaximumFractionDigits(6);
        this.writer.write(code + "\n" + format.format(number) + "\n");
    }

    protected void printStringVariable(int id, String name) throws IOException {
        this.writer.write(id + "\n" + name + "\n");
    }

    protected void printVariable(int id, String name) throws IOException {
        printStringVariable(id, name);
    }

    protected void printVariable(int id, String name, int id1, String value1) throws IOException {
        printStringVariable(id, name);
        printStringVariable(id1, value1);
    }

    protected void printVariable(int id, String name, int id1, double value1) throws IOException {
        printStringVariable(id, name);
        printDoubleVariable(id1, value1);
    }

    protected void printVariable(int id, String name, int id1, double value1, int id2, double value2) throws IOException {
        printStringVariable(id, name);
        printDoubleVariable(id1, value1);
        printDoubleVariable(id2, value2);
    }

    protected void printVariable(int id1, double value1) throws IOException {
        printDoubleVariable(id1, value1);
    }

    protected void printVariable(int id1, double value1, int id2, double value2) throws IOException {
        printDoubleVariable(id1, value1);
        printDoubleVariable(id2, value2);
    }

    protected void printVariable(int id1, double value1, int id2, double value2, int id3, double value3) throws IOException {
        printDoubleVariable(id1, value1);
        printDoubleVariable(id2, value2);
        printDoubleVariable(id3, value3);
    }

    protected void printVariable(int id, String name, int id1, double value1, int id2, double value2, int id3, double value3) throws IOException {
        printStringVariable(id, name);
        printDoubleVariable(id1, value1);
        printDoubleVariable(id2, value2);
        printDoubleVariable(id3, value3);
    }

}
