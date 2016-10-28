package anol;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class Processor {

    public Processor(String inputFileName, String outputFileName) throws Throwable {
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document inputDoc = dBuilder.parse(inputFile);
        inputDoc.getDocumentElement().normalize();
        Converter converter = new Converter(inputDoc);
        String outputString;
        if (outputFile.getName().endsWith(".svg")) {
            Document outputDoc = converter.convertToSvg();
            outputString = serialize(outputDoc);
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else if (outputFile.getName().endsWith(".ps")) {
            outputString = converter.convertToPs(outputFile.getName());
            new FileOutputStream(outputFile).write(outputString.getBytes());
        } else {
            String temp = "data/temp.ps";
            File tempFile = new File(temp);
            outputString = converter.convertToPs(outputFile.getName());
            new FileOutputStream(tempFile).write(outputString.getBytes());
            (new ToPdf()).convert(temp,outputFile);
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
