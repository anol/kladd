package anol;

import anol.pdf.PdfConverter;
import anol.step.StepConverter;
import anol.svg.SvgConverter;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class Processor {

    public Processor(String inputFileName, String outputFileName, boolean annotations, boolean colors, String language) throws Throwable {
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document inputDoc = dBuilder.parse(inputFile);
        inputDoc.getDocumentElement().normalize();
        if (outputFile.getName().endsWith(".svg")) {
            SvgConverter converter = new SvgConverter(inputDoc, annotations, colors, language);
            Document outputDoc = converter.convertToSvg();
            String outputString = serialize(outputDoc);
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else if (outputFile.getName().endsWith(".stp")) {
            StepConverter converter = new StepConverter(inputDoc, annotations, colors, language);
            String outputString = converter.convertToStep(inputFile.getName(), outputFile.getName());
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else if (outputFile.getName().endsWith(".ps")) {
            PdfConverter converter = new PdfConverter(inputDoc, annotations, colors, language);
            String outputString = converter.convertToPs(outputFile.getName());
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else {
            String temp = "data/temp.ps";
            File tempFile = new File(temp);
            PdfConverter converter = new PdfConverter(inputDoc, annotations, colors, language);
            String outputString = converter.convertToPs(outputFile.getName());
            new FileOutputStream(tempFile).write(outputString.getBytes());
            converter.convertToPdf(temp, outputFile);
        }
    }

    public String serialize(Document document) throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(data);
        OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
        of.setIndent(1);
        of.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(ps, of);
        serializer.asDOMSerializer();
        serializer.serialize(document);
        return data.toString();
    }
}
