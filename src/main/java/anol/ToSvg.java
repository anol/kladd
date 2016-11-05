package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import static java.awt.geom.PathIterator.*;
import static java.awt.geom.PathIterator.SEG_CLOSE;

public class ToSvg {

    public ToSvg(ConcretePartList parts, Document destinationDocument) {
        Rectangle2D bounds = parts.getBounds();
        Element rootElement = destinationDocument.createElement("svg");
        rootElement.setAttribute("height", Double.toString(bounds.getHeight()));
        rootElement.setAttribute("width", Double.toString(bounds.getWidth()));
        destinationDocument.appendChild(rootElement);
        String points = "";
        Element polygon = null;
        Iterator<ConcretePart> iterator = parts.getIterator();
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            for (PathIterator pi = part.getPathIterator(); !pi.isDone(); pi.next()) {
                double[] coords = new double[6];
                int type = pi.currentSegment(coords);
                switch (type) {
                    case SEG_MOVETO: // 1 point
                        polygon = destinationDocument.createElement("polygon");
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
    }

}
