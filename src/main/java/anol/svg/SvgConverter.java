package anol.svg;

import anol.awg.ToAwt;
import anol.converter.ConcretePart;
import anol.converter.ConcretePartList;
import anol.converter.Converter;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.Iterator;

import static java.awt.geom.PathIterator.*;
import static java.awt.geom.PathIterator.SEG_CLOSE;

public class SvgConverter extends Converter {

    String outputDirectoryName;
    File outputDirectory;

    public SvgConverter(Document kladdDoc, String outputDirectoryName, boolean annotations, boolean colors, String language) throws Throwable {
        super(kladdDoc, annotations, colors, language);
        this.outputDirectoryName = outputDirectoryName;
        this.outputDirectory = new File(outputDirectoryName);
        if (!this.outputDirectory.exists()) {
            this.outputDirectory.mkdir();
        }
    }

    public void convert() throws Throwable {
        new ToAwt(kladdDoc, partList, language);
        convertToSvg(partList);
    }

    private void convertToSvg(ConcretePartList parts) throws ParserConfigurationException, IOException {
        Iterator<ConcretePart> iterator = parts.getIterator();
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            CreatePart(part, doc);
            String outputString = serialize(doc);
            String outputFileName = this.outputDirectoryName + "/" + part.getName() + ".svg";
            File outputFile = new File(outputFileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            new FileOutputStream(outputFile).write(outputString.getBytes());
        }
    }

    private void CreatePart(ConcretePart part, Document doc) {
        Rectangle2D bounds = part.getBounds();
        Element rootElement = doc.createElement("svg");
        rootElement.setAttribute("height", Double.toString(bounds.getHeight()));
        rootElement.setAttribute("width", Double.toString(bounds.getWidth()));
        doc.appendChild(rootElement);
        String points = "";
        Element polygon = null;
        for (PathIterator pi = part.getPathIterator(); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    polygon = doc.createElement("polygon");
                    rootElement.appendChild(polygon);
                    points = coords[0] + "," + coords[1];
                    break;
                case SEG_LINETO: // 1 point
                    points += " " + coords[0] + "," + coords[1];
                    break;
                case SEG_QUADTO: // 2 point
                    points += " " + coords[0] + "," + coords[1];
                    points += " " + coords[2] + "," + coords[3];
                    break;
                case SEG_CUBICTO: // 3 points
                    points += " " + coords[0] + "," + coords[1];
                    points += " " + coords[2] + "," + coords[3];
                    points += " " + coords[4] + "," + coords[5];
                    break;
                case SEG_CLOSE: // 0 points
                    polygon.setAttribute("points", points);
                    polygon.setAttribute("style", "fill:none;stroke:black;stroke-width:1;fill-rule:evenodd;");
                    break;
                default:
                    System.out.print("?");
            }
        }
    }

    private String serialize(Document document) throws IOException {
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
