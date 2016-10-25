package com.aeolsen;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.geom.*;

import static java.awt.geom.PathIterator.*;

public class DocToSvg {

    private Area mainArea;
    private Document doc;

    public DocToSvg(Document doc) throws Throwable {
        mainArea = new Area();
        this.doc = doc;
        traverseAllElements();
    }

    public Document convert() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document svgDoc = dBuilder.newDocument();
        convertToAwt("del", 0.0, 0.0);
        convertToSvg(svgDoc, mainArea);
        return svgDoc;
    }

    private void traverseAllElements() {
        NodeList nodeList = doc.getElementsByTagName("*");
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.hasAttribute("id")) {
                    element.setIdAttribute("id", true);
                }
            }
        }
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
                x = x - xOffset - width / 2;
                y = y + yOffset - height / 2;
                System.out.println("rekt x=\"" + x + "\" y=" + y + "\" h=" + height + "\" b=" + width + "\" r=" + radius);
                rekt = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
                mainArea.subtract(rekt);
                break;
            case "sirk":
                x = x - xOffset - radius / 2;
                y = y + yOffset - radius / 2;
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

    private void convertToSvg(Document doc, Area area) {
        Rectangle2D bounds = area.getBounds2D();
        Element rootElement = doc.createElement("svg");
        rootElement.setAttribute("height", Double.toString(bounds.getHeight()));
        rootElement.setAttribute("width", Double.toString(bounds.getWidth()));
        doc.appendChild(rootElement);
        String points = "";
        Element polygon = null;
        for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {
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
}
