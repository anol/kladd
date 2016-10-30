package anol;

public class Main {

    public static void main(String[] args) throws Throwable {
        if (1 < args.length) {
            String inputFileName = args[0];
            String outputFileName = args[1];
            String pageSize;
            if (2 < args.length) {
                pageSize = args[2];
            } else {
                pageSize = "a4";
            }
            System.out.println("kladd input=\"" + inputFileName + " output=\"" + outputFileName + "\"" + " page=\"" + pageSize + "\"");
            new Processor(inputFileName, outputFileName, pageSize);
        } else {
            System.out.println("Usage: kladd <input file name> <output file name> [page size]");
        }
    }

}
