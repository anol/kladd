package anol.step;

import anol.converter.ConcretePart;
import anol.converter.Converter;
import anol.awg.ToAwt;
import org.w3c.dom.Document;

import java.util.Iterator;

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
        new ToAwt(kladdDoc, partList, language);
        ToStep toStep = new ToStep();
        String step = toStep.getProlog();
        step += toStep.getHeaderSection(docTitle, inputFile, outputFile);
        Iterator<ConcretePart> iterator = partList.getIterator();
        int counter = 0;
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            step += toStep.getDataSection(part, ++counter);
        }
        step += toStep.getEpilog();
        return step;
    }
}
