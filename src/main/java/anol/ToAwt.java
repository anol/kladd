package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class ToAwt {

    // The AWT coordinate system has origo in the top-left corner

    private Area mainArea;
    private MajorPoints pointList;
    private Document doc;

    public ToAwt(Document sourceDocument, Area mainArea, MajorPoints pointList) throws Throwable {
        this.mainArea = mainArea;
        this.pointList = pointList;
        this.doc = sourceDocument;
        convertToAwt("del", 0.0, 0.0);
    }

    private void savePoint(double x, double y) {
        Point2D.Double point = new Point2D.Double(x, y);
        pointList.add(point);
    }

    private void convertToAwt(String tagName, double x, double y) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                toAwt(element, x, y);
            }
        }
    }

    private void convertById(String id, double x, double y) {
        Element element = doc.getElementById(id);
        if (null == element) {
            System.out.println("Fant ikke \"" + id + "\" elementet");
        } else {
            toAwt(element, x, y);
        }
    }

    private void convertToAwt(Element element, double x, double y) {
        NodeList nodeList = element.getChildNodes();
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                toAwt(childElement, x, y);
            }
        }
    }

    private double getAttribute(Element element, String attribute) {
        if (element.hasAttribute(attribute)) {
            return Double.parseDouble(element.getAttribute(attribute));
        } else {
            return 0.0;
        }
    }

    private void toAwt(Element element, double x, double y) {
        double height = getAttribute(element, "h");
        double width = getAttribute(element, "b");
        double radius = getAttribute(element, "r");
        double xOffset = getAttribute(element, "x");
        double yOffset = getAttribute(element, "y");
        Area sirk = null;
        Area rekt = null;
        switch (element.getTagName()) {
            case "del":
                convertToAwt("emne", x, y);
                break;
            case "emne":
                System.out.println("emne x=\"" + x + "\" y=" + y + "\" h=" + height + "\" b=" + width);
                mainArea.add(new Area(new Rectangle2D.Double(x, y, width, height)));
                x = x  + width / 2;
                y = y  + height / 2;
                convertToAwt(element, x, y);
                break;
            case "komp":
                x = x - xOffset;
                y = y + yOffset;
                System.out.println("komp x=\"" + x + "\" y=" + y);
                convertToAwt(element, x, y);
                break;
            case "rekt":
                savePoint(x, y);
                x = x - xOffset - width / 2;
                y = y + yOffset - height / 2;
                System.out.println("rekt x=\"" + x + "\" y=" + y + "\" h=" + height + "\" b=" + width + "\" r=" + radius);
                rekt = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
                mainArea.subtract(rekt);
                break;
            case "sirk":
                savePoint(x, y);
                x = x - radius;
                y = y - radius;
                width = height = radius * 2;
                System.out.println("sirk x=\"" + x + "\" y=" + y + "\" r=" + radius);
                sirk = new Area(new Ellipse2D.Double(x, y, width, height));
                mainArea.subtract(sirk);
                break;
            default:
                x = x - xOffset;
                y = y + yOffset;
                convertById(element.getTagName(), x, y);
        }
    }
}
