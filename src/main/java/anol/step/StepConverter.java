package anol.step;

import anol.awg.ToAwt;
import anol.converter.ConcretePart;
import anol.converter.Converter;
import org.w3c.dom.Document;

public class StepConverter extends Converter {


    public StepConverter(Document kladdDoc, boolean annotations, boolean colors, String language) throws Throwable {
        super(kladdDoc, annotations, colors, language);
    }

    /*
     * Convert the document to a STEP "file"
     * @param inputFile The input file name
     * @param outputFile The output file name
     * @return The STEP "file" as a string
     */
    public String convertToStep(String inputFile, String outputFile) throws Throwable {
        new ToAwt(kladdDoc, parts, language);
        ToStep toStep = new ToStep();
        String step = toStep.getProlog();
        step += toStep.getHeaderSection(docTitle, inputFile, outputFile);
        int counter = 0;
        for (ConcretePart part : parts) {
            step += toStep.getDataSection(part, ++counter);
        }
        step += toStep.getEpilog();
        return step;
    }
}
