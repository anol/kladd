package anol;

import org.w3c.dom.Element;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import static java.awt.geom.PathIterator.*;

public class ToPs {

    public Area inputArea;

    public ToPs(Area inputArea) {
        this.inputArea = inputArea;
    }

    static double mm(double points) {
        return points * 72.0 / 24.5;
    }

    static String immi(double points) {
        Double mmpoints = mm(points);
        Integer immipoints = mmpoints.intValue();
        return immipoints.toString();
    }

    String getBoundingBox() {
        Rectangle2D bounds = inputArea.getBounds2D();
        String boundingBox = immi(-bounds.getMinX()) + " " +
                immi(bounds.getMinY()) + " " +
                immi(-bounds.getMaxX()) + " " +
                immi(bounds.getMaxY());
        return boundingBox;
    }

    String getCreationDate() {
        String today = (new Date()).toString();
        return today;
    }

    public String getHeader(String title) {
        String header = "%!PS-Adobe-2.0\n" +
                "%%Creator: kladd\n" +
                "%%CreationDate: " + getCreationDate() + "\n" +
                "%%Title: " + title + "\n" +
                "%%Pages: 1\n" +
                "%%PageOrder: Ascend\n" +
                "%%BoundingBox: " + getBoundingBox() + "\n" +
                "%%DocumentPaperSizes: a0\n" +
                "%%Orientation: Landscape\n" +
                "%%EndComments\n";
        return header;
    }

    public String getTrailer() {
        String trailer = "%%EOF\n";
        return trailer;
    }

    public String convert() {
        String outputString = "%%Page: 1 1\n" +
                "%%BeginPageSetup\n" +
                "/pagelevel save def\n" +
                "90 rotate 3000 -2000 translate\n" +
                "%%EndPageSetup\n";
        this.inputArea = inputArea;
        String points = "";
        Element polygon = null;
        for (PathIterator pi = inputArea.getPathIterator(null); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    outputString += "newpath\n";
                    outputString += mm(-coords[0]) + " " + mm(coords[1]) + " moveto\n";
                    break;
                case SEG_LINETO: // 1 point
                    outputString += mm(-coords[0]) + " " + mm(coords[1]) + " lineto\n";
                    break;
                case SEG_QUADTO: // 2 point
                    outputString += mm(-coords[0]) + " " + mm(coords[1]) + " lineto\n";
                    outputString += mm(-coords[2]) + " " + mm(coords[3]) + " lineto\n";
                    break;
                case SEG_CUBICTO: // 3 points
                    outputString += mm(-coords[0]) + " " + mm(coords[1]) + " ";
                    outputString += mm(-coords[2]) + " " + mm(coords[3]) + " ";
                    outputString += mm(-coords[4]) + " " + mm(coords[5]) + " curveto\n";
                    break;
                case SEG_CLOSE: // 0 points
                    outputString += "closepath\n";
                    outputString += "stroke\n";
                    break;
                default:
                    System.out.print("?");
            }
        }
        outputString += "pagelevel restore\n" +
                "showpage\n";
        return outputString;
    }
}
