package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;
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

    private void convertToAwt(Element element, String tagName, double x, double y) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                toAwt(childElement, x, y);
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

    private List<Double> getListAttribute(Element element, String attribute) {
        List<Double> list = new ArrayList<Double>();
        if (element.hasAttribute(attribute)) {
            String value = element.getAttribute(attribute);
            for (String subValue : value.split(" ")) {
                list.add(Double.parseDouble(subValue));
            }
        } else {
            list.add(0.0);
        }
        return list;
    }

    private void toAwt(Element element, double x, double y) {
        List<Double> xList = getListAttribute(element, "x");
        List<Double> yList = getListAttribute(element, "y");
        Iterator<Double> xIt = xList.listIterator();
        while (xIt.hasNext()) {
            Double dx = xIt.next();
            Iterator<Double> yIt = yList.listIterator();
            while (yIt.hasNext()) {
                Double dy = yIt.next();
                toAwt(element, x, y, dx, dy);
            }
        }
    }

    private void toAwt(Element element, double x, double y, double dx, double dy) {
        double xdx = x - dx;
        double ydy = y + dy;
        double height = getAttribute(element, "h");
        double width = getAttribute(element, "b");
        double radius = getAttribute(element, "r");
        switch (element.getTagName()) {
            case "del":
                System.out.println("del name=\"" + element.getAttribute("name"));
                concretePart = new ConcretePart(element.getAttribute("name"));
                partList.addPart(concretePart);
                concretePart.setOrigo(dx, dy);
                convertToAwt(element, "emne", x, y);
                break;
            case "emne":
                concretePart.addRect(xdx, ydy, width, height, radius);
                convertToAwt(element, xdx, ydy);
                break;
            case "komp":
                convertToAwt(element, xdx, ydy);
                break;
            case "rekt":
                concretePart.subtractRect(xdx, ydy, width, height, radius);
                break;
            case "sirk":
                concretePart.subtractCircle(xdx, ydy, radius);
                break;
            default:
                convertById(element.getTagName(), xdx, ydy);
        }
    }

}
