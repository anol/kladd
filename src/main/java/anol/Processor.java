package anol;

import anol.pdf.PdfConverter;
import anol.step.StepConverter;
import anol.svg.SvgConverter;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;

public class Processor {

    public Processor(String inputFileName, String outputFileName, boolean annotations, boolean colors, String language) throws Throwable {
        File inputFile = new File(inputFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document inputDoc = dBuilder.parse(inputFile);
        inputDoc.getDocumentElement().normalize();
        if (outputFileName.endsWith(".svg")) {
            new SvgConverter(inputDoc, outputFileName, annotations, colors, language).convert();
        } else if (outputFileName.endsWith(".stp")) {
            File outputFile = new File(outputFileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            StepConverter converter = new StepConverter(inputDoc, annotations, colors, language);
            String outputString = converter.convertToStep(inputFile.getName(), outputFile.getName());
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else if (outputFileName.endsWith(".ps")) {
            File outputFile = new File(outputFileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            PdfConverter converter = new PdfConverter(inputDoc, annotations, colors, language);
            String outputString = converter.convertToPs(outputFile.getName());
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else if (outputFileName.endsWith(".pdf")) {
            File outputFile = new File(outputFileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            String temp = "data/temp.ps";
            File tempFile = new File(temp);
            PdfConverter converter = new PdfConverter(inputDoc, annotations, colors, language);
            String outputString = converter.convertToPs(outputFile.getName());
            new FileOutputStream(tempFile).write(outputString.getBytes());
            converter.convertToPdf(temp, outputFile);
        } else {
            throw new Exception("Unsupported output format!");
        }
    }
}
