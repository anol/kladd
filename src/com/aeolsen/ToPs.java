package com.aeolsen;

import org.w3c.dom.Element;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import static java.awt.geom.PathIterator.*;

public class ToPs {

    public Area inputArea;

    public ToPs(Area inputArea) {
        this.inputArea = inputArea;
    }

    private static double mm(double points) {
        return points * 72.0 / 24.5;
    }

    public String convert(String title) {
        String outputString =
                "%!PS-Adobe-2.0\n" +
                        "%%Title: " + title + "\n" +
                        "%%EndComments\n";
        this.inputArea = inputArea;
        Rectangle2D bounds = inputArea.getBounds2D();
        String points = "";
        Element polygon = null;
        for (PathIterator pi = inputArea.getPathIterator(null); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    outputString += "newpath\n";
                    outputString += mm(coords[0]) + " " + mm(coords[1]) + " moveto\n";
                    break;
                case SEG_LINETO: // 1 point
                    outputString += mm(coords[0]) + " " + mm(coords[1]) + " lineto\n";
                    break;
                case SEG_QUADTO: // 2 point
                    outputString += mm(coords[0]) + " " + mm(coords[1]) + " lineto\n";
                    outputString += mm(coords[0]) + " " + mm(coords[1]) + " lineto\n";
                    break;
                case SEG_CUBICTO: // 3 points
                    outputString += mm(coords[0]) + " " + mm(coords[1]) + " lineto\n";
                    outputString += mm(coords[2]) + " " + mm(coords[3]) + " lineto\n";
                    outputString += mm(coords[4]) + " " + mm(coords[5]) + " lineto\n";
                    break;
                case SEG_CLOSE: // 0 points
                    outputString += "closepath\n";
                    outputString += "stroke\n";
                    break;
                default:
                    System.out.print("?");
            }
            System.out.print(".");
        }
        return outputString;
    }
}
