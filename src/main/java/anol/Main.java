package anol;

public class Main {

    public static void main(String[] args) throws Throwable {
        if (1 < args.length) {
            String inputFileName = args[0];
            String outputFileName = args[1];
            String language = "en";
            boolean colors = true;
            boolean annotations = true;
            for(int nArg = 2; nArg < args.length; nArg++) {
                switch (args[nArg]) {
                    case "norsk":
                        language = "no";
                        break;
                    case "nocolor":
                        colors = false;
                        break;
                    case "noanno":
                        colors = false;
                        annotations = false;
                        break;
                    default:
                        System.out.println("Unknown option: \"" + args[2] + "\"");
                        break;
                }
            }
            System.out.println("kladd input=\"" + inputFileName + " output=\"" + outputFileName + "\"");
            new Processor(inputFileName, outputFileName, annotations, colors, language);
        } else {
            System.out.println("Usage: kladd <input file name> <output file name> [nocolor|noanno|norsk]");
        }
    }

}
