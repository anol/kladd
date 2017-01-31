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

    private final String inputFileName;
    private final String outputFileName;
    private final boolean annotations;
    private final boolean colors;
    private final String language;

    public Processor(String inputFileName, String outputFileName, boolean annotations, boolean colors, String language) throws Throwable {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.annotations = annotations;
        this.colors = colors;
        this.language = language;
        Document inputDoc = buildInputDocument();
        String fileExtension = outputFileName.substring(outputFileName.lastIndexOf('.'));
        switch (fileExtension) {
            case ".dxf":
                export2Dxf(inputDoc);
                break;
            case ".svg":
                export2Svg(inputDoc);
                break;
            case ".stp":
                export2Stp(inputDoc);
                break;
            case ".ps":
                export2Ps(inputDoc);
                break;
            case ".pdf":
                export2Pdf(inputDoc);
                break;
            default:
                throw new Exception("Unsupported output format: " + fileExtension);
        }
    }

    // Export to PDF
    private void export2Pdf(Document inputDoc) throws Throwable {
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
    }

    // Export to Postscript
    private void export2Ps(Document inputDoc) throws Throwable {
        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        PdfConverter converter = new PdfConverter(inputDoc, annotations, colors, language);
        String outputString = converter.convertToPs(outputFile.getName());
        new FileOutputStream(outputFile).write(outputString.getBytes());
    }

    // Export to STEP
    private void export2Stp(Document inputDoc) throws Throwable {
        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        StepConverter converter = new StepConverter(inputDoc, annotations, colors, language);
        String outputString = converter.convertToStep(inputFileName, outputFileName);
        new FileOutputStream(outputFile).write(outputString.getBytes());
    }

    // Export to SVG
    private void export2Svg(Document inputDoc) throws Throwable {
        new SvgConverter(inputDoc, outputFileName, annotations, colors, language).convert();
    }

    // Export to DXF
    private void export2Dxf(Document inputDoc) throws Throwable {
        new SvgConverter(inputDoc, outputFileName, annotations, colors, language).convert();
    }

    private Document buildInputDocument() throws Throwable {
        File inputFile = new File(inputFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document inputDoc = dBuilder.parse(inputFile);
        inputDoc.getDocumentElement().normalize();
        return inputDoc;
    }
}
