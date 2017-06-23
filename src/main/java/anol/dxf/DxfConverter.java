package anol.dxf;

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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.geom.PathIterator.*;

public class DxfConverter extends Converter {

    String outputDirectoryName;
    File outputDirectory;

    public DxfConverter(Document kladdDoc, String outputDirectoryName, boolean annotations, boolean colors, String language) throws Throwable {
        super(kladdDoc, annotations, colors, language);
        this.outputDirectoryName = outputDirectoryName;
        this.outputDirectory = new File(outputDirectoryName);
        if (!this.outputDirectory.exists()) {
            this.outputDirectory.mkdir();
        }
    }

    public void convert() throws Throwable {
        new ToAwt(kladdDoc, parts, language);
        convertToDxf(parts);
    }

    private void convertToDxf(ConcretePartList parts) throws ParserConfigurationException, IOException {
        for (ConcretePart part : parts) {
            System.out.println("\n\nPart: " + part.getName());
            String filename = this.outputDirectoryName + "/" + part.getName() + ".dxf";
            File outputFile = new File(filename);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            writeDxf(part, filename);
        }
    }

    public void writeDxf(ConcretePart part, String filename) {
        // Using nio.file
        Path path = Paths.get(filename);
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            ToDxf toDxf = new ToDxf(writer);
            toDxf.prolog();
            writeShape(toDxf, part);
            toDxf.epilog();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    private void writeShape(ToDxf toDxf, ConcretePart part) throws IOException {
        Element polygon = null;
        for (PathIterator pi = part.getPathIterator(); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    toDxf.open("P",0);
                    toDxf.moveTo(coords[0], coords[1]);
                    break;
                case SEG_LINETO: // 1 point
                    toDxf.lineTo(coords[0], coords[1]);
                    break;
                case SEG_QUADTO: // 2 point
                    toDxf.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case SEG_CUBICTO: // 3 points
                    toDxf.cubicTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case SEG_CLOSE: // 0 points
                    toDxf.close();
                    break;
                default:
                    System.out.print("?");
            }
        }
    }
}

