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

    static double mm2pt(double mm) {
        return mm * 72.0 / 24.5;
    }

    static String mm2pti(double mm) {
        Double mmpoints = mm2pt(mm);
        Integer immipoints = mmpoints.intValue();
        return immipoints.toString();
    }

    String getBoundingBox() {
        Rectangle2D bounds = inputArea.getBounds2D();
        String boundingBox = mm2pti(-bounds.getMinX()) + " " +
                mm2pti(bounds.getMinY()) + " " +
                mm2pti(-bounds.getMaxX()) + " " +
                mm2pti(bounds.getMaxY());
        return boundingBox;
    }

    String getCreationDate() {
        String today = (new Date()).toString();
        return today;
    }

    public String getDocumentHeader(String title, String pageSize) {
        String header = "%!PS-Adobe-2.0\n" +
                "%%Creator: kladd\n" +
                "%%CreationDate: " + getCreationDate() + "\n" +
                "%%Title: " + title + "\n" +
                "%%Pages: 1\n" +
                "%%PageOrder: Ascend\n" +
                "%%BoundingBox: " + getBoundingBox() + "\n" +
                "%%DocumentPaperSizes: " + pageSize + "\n" +
                "%%Orientation: Landscape\n" +
                "%%EndComments\n";
        return header;
    }

    public String getPageHeader(String title, String pageSize) {
        String header = "%%Page: 1 1\n" +
                "%%BeginPageSetup\n" +
                "/pagelevel save def\n" +
                "90 rotate\n";
        switch (pageSize) {
            case "a4":
                header += "3000 -500 translate\n";
                break;
            case "a3":
                header += "3000 -750 translate\n";
                break;
            case "a2":
                header += "3000 -1000 translate\n";
                break;
            case "a1":
                header += "3000 -1500 translate\n";
                break;
            case "a0":
                header += "3000 -2000 translate\n";
                break;
            default:
                break;
        }
        header += "%%EndPageSetup\n";
        return header;
    }

    public String getTrailer() {
        String trailer = "%%EOF\n";
        return trailer;
    }

    public String convert() {
        String outputString = "";
        this.inputArea = inputArea;
        String points = "";
        Element polygon = null;
        for (PathIterator pi = inputArea.getPathIterator(null); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    outputString += "newpath\n";
                    outputString += mm2pt(-coords[0]) + " " + mm2pt(coords[1]) + " moveto\n";
                    break;
                case SEG_LINETO: // 1 point
                    outputString += mm2pt(-coords[0]) + " " + mm2pt(coords[1]) + " lineto\n";
                    break;
                case SEG_QUADTO: // 2 point
                    outputString += mm2pt(-coords[0]) + " " + mm2pt(coords[1]) + " lineto\n";
                    outputString += mm2pt(-coords[2]) + " " + mm2pt(coords[3]) + " lineto\n";
                    break;
                case SEG_CUBICTO: // 3 points
                    outputString += mm2pt(-coords[0]) + " " + mm2pt(coords[1]) + " ";
                    outputString += mm2pt(-coords[2]) + " " + mm2pt(coords[3]) + " ";
                    outputString += mm2pt(-coords[4]) + " " + mm2pt(coords[5]) + " curveto\n";
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
