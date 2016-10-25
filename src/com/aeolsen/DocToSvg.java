package com.aeolsen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.geom.*;

import static java.awt.geom.PathIterator.*;

public class DocToSvg {

    private Area mainArea;
    private Document doc;

    double x = 0.0;
    double y = 0.0;

    public DocToSvg(Document doc) throws Throwable {
        mainArea = new Area();
        this.doc = doc;
        convertToAwt(doc);
    }

    private void convertToAwt(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("rekt");
        for (int k = 0; k < nodeList.getLength(); k++) {
            toAwt((Element) nodeList.item(k));
        }
        nodeList = doc.getElementsByTagName("sirk");
        for (int k = 0; k < nodeList.getLength(); k++) {
            toAwt((Element) nodeList.item(k));
        }
    }

    private void toAwt(Element element) {
        int height = 0;
        int width = 0;
        int radius = 0;
        Area sirk = null;
        Area rekt = null;
        switch (element.getTagName()) {
            case "rekt":
                height = Integer.parseInt(element.getAttribute("h"));
                width = Integer.parseInt(element.getAttribute("b"));
                radius = Integer.parseInt(element.getAttribute("r"));
                rekt = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
                mainArea.add(rekt);
                x += width + 10;
                break;
            case "sirk":
                radius = Integer.parseInt(element.getAttribute("r"));
                width = height = radius * 2;
                sirk = new Area(new Ellipse2D.Double(x, y, width, height));
                mainArea.add(sirk);
                y += height + 10;
                break;
            default:
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

    private void printCoords(Area area) {
        System.out.println("area:\n" + area.toString());
        for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    System.out.print("\n< coord=\"" + coords[0] + "&" + coords[1]);
                    break;
                case SEG_LINETO: // 1 point
                    System.out.print(", " + coords[0] + "&" + coords[1]);
                    break;
                case SEG_QUADTO: // 2 point
                    System.out.print(", " + coords[0] + "&" + coords[1]);
                    System.out.print(", " + coords[2] + "&" + coords[3]);
                    break;
                case SEG_CUBICTO: // 3 points
                    System.out.print(", " + coords[0] + "&" + coords[1]);
                    System.out.print(", " + coords[2] + "&" + coords[3]);
                    System.out.print(", " + coords[4] + "&" + coords[5]);
                    break;
                case SEG_CLOSE: // 0 points
                    System.out.println("\"/>");
                    break;
                default:
                    System.out.print("?");
            }
        }
    }

    public Document dump() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document svgDoc = dBuilder.newDocument();
        convertToSvg(svgDoc, mainArea);
        return svgDoc;
    }

    public void print() {
        NodeList nList = doc.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            printChildren(node, 0);
        }
    }

    private void printChildren(Node node, int level) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            for (int col = 0; col < level; col++) {
                System.out.print("   ");
            }
            System.out.println(node.getNodeName());
            NodeList nList = node.getChildNodes();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node child = nList.item(temp);
                printChildren(child, level + 1);
            }
        }
    }
}
