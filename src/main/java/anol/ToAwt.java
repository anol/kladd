package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.geom.*;
import java.util.List;

public class ToAwt {

    private ConcretePartList partList;
    private ConcretePart concretePart;
    private Document doc;

    public ToAwt(Document sourceDocument, ConcretePartList partList) {
        this.partList = partList;
        this.doc = sourceDocument;
        convertToAwt("del", 0.0, 0.0);
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
        switch (element.getTagName()) {
            case "del":
                concretePart = new ConcretePart();
                partList.addPart(concretePart);
                concretePart.setOrigo(xOffset, yOffset);
                convertToAwt("emne", x, y);
                break;
            case "emne":
                System.out.println("emne x=\"" + x + "\" y=" + y + "\" h=" + height + "\" b=" + width);
                concretePart.addRect(x, y, width, height, radius, radius);
                x = x + width / 2;
                y = y + height / 2;
                convertToAwt(element, x, y);
                break;
            case "komp":
                x = x - xOffset;
                y = y + yOffset;
                System.out.println("komp x=\"" + x + "\" y=" + y);
                convertToAwt(element, x, y);
                break;
            case "rekt":
                concretePart.addMjorPoint(x, y);
                x = x - xOffset - width / 2;
                y = y + yOffset - height / 2;
                System.out.println("rekt x=\"" + x + "\" y=" + y + "\" h=" + height + "\" b=" + width + "\" r=" + radius);
                concretePart.subtractRect(x, y, width, height, radius, radius);
                break;
            case "sirk":
                concretePart.addMjorPoint(x, y);
                x = x - radius;
                y = y - radius;
                width = height = radius * 2;
                System.out.println("sirk x=\"" + x + "\" y=" + y + "\" r=" + radius);
                concretePart.subtractCircle(x, y, width, height);
                break;
            default:
                x = x - xOffset;
                y = y + yOffset;
                convertById(element.getTagName(), x, y);
        }
    }
}
