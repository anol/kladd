package com.aeolsen;

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
        DocToSvg dumper = new DocToSvg(inputDoc);
        Document outputDoc = dumper.convert();
        String outputString = serialize(outputDoc);
        new FileOutputStream(outputFile).write(outputString.getBytes());
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
