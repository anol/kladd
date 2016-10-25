package com.aeolsen;

public class Main {

    public static void main(String[] args) throws Throwable {
        if (1 < args.length) {
            String inputFileName = args[0];
            String outputFileName = args[1];
            System.out.println("kladd input=\"" + inputFileName + " output=\"" + outputFileName + "\"");
            new Processor(inputFileName, outputFileName);
        } else {
            System.out.println("Usage: kladd <input file name> <output file name>");
        }
    }

}
