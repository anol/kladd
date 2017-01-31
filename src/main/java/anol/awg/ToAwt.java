package anol.awg;

import anol.converter.ConcretePart;
import anol.converter.ConcretePartList;
import anol.converter.TagNames;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static anol.converter.TagNames.Tags.*;
import static java.lang.Math.floor;

public class ToAwt {

    private ConcretePartList partList;
    private ConcretePart concretePart;
    private Document doc;
    private double weight;
    private double thickness;
    private TagNames tag;
    private int sheet;

    public ToAwt(Document sourceDocument, ConcretePartList partList, String language) throws Exception {
        this.tag = new TagNames(language);
        this.partList = partList;
        this.doc = sourceDocument;
        this.weight = 0;
        this.thickness = 3.0;
        this.sheet = 0;
        document2design();
    }

    private void document2design() throws Exception {
        NodeList nodeList = doc.getElementsByTagName(tag.getTagName(DESIGN));
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element design = (Element) node;
                design2sheet(design);
            }
        }
    }

    private void design2sheet(Element design) throws Exception {
        String designName = design.getAttribute(tag.getTagName(NAME));
        NodeList nodeList = design.getElementsByTagName(tag.getTagName(SHEET));
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element sheet = (Element) node;
                String sheetName = sheet.getAttribute(tag.getTagName(NAME));
                this.thickness = getAttribute(sheet, tag.getTagName(THICK) );
                System.out.println("--- " + designName + " --- " + sheetName + " ---");
                sheet2part(sheet);
                System.out.println("m=" + floor(10 * this.weight) / 10 + "kg");
                this.weight = 0.0;
                this.sheet++;
            }
        }
    }

    private void sheet2part(Element sheet) throws Exception {
        NodeList nodeList = sheet.getElementsByTagName(tag.getTagName(PART));
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element part = (Element) node;
                element2elements(part, 0.0, 0.0);
            }
        }
    }

    private void element2elements(Element element, double x, double y) throws Exception {
        int nx0 = ((int) getAttribute(element, tag.getTagName(REPEAT_X)));
        if (0 == nx0) nx0 = 1;
        double dx = getAttribute(element, tag.getTagName(DELTA_X));
        int ny0 = ((int) getAttribute(element, tag.getTagName(REPEAT_Y)));
        if (0 == ny0) ny0 = 1;
        double dy = getAttribute(element, tag.getTagName(DELTA_Y));
        List<Double> xList = getListAttribute(element, "x");
        List<Double> yList = getListAttribute(element, "y");
        for (Double x1 : xList) {
            for (Double y1 : yList) {
                for (int nx = nx0; 0 < nx; nx--) {
                    for (int ny = ny0; 0 < ny; ny--) {
                        element2awt(element, x, y, x1, y1);
                        y1 += dy;
                    }
                    x1 += dx;
                }
            }
        }
    }

    private void element2awt(Element element, double x, double y, double dx, double dy) throws Exception {
        double xdx = x - dx;
        double ydy = y + dy;
        double height = getAttribute(element, tag.getTagName(HEIGHT));
        double width = getAttribute(element, tag.getTagName(WIDTH));
        double radius = getAttribute(element, tag.getTagName(RADIUS));
        double rotate = getAttribute(element, tag.getTagName(ROTATE));
        String tagName = element.getTagName();
        switch (tag.getTagValue(tagName)) {
            case PART:
                addPart(element, x, y, dx, dy);
                break;
            case SOLID:
                concretePart.addRect(xdx, ydy, width, height, radius, rotate);
                element2children(element, xdx, ydy);
                break;
            case SHAPE:
                element2children(element, xdx, ydy);
                break;
            case RECTANGLE:
                concretePart.subtractRect(xdx, ydy, width, height, radius, rotate);
                break;
            case CIRCLE:
                concretePart.subtractCircle(xdx, ydy, radius);
                break;
            case USER_DEFINED:
                userDefined2element(element.getTagName(), xdx, ydy);
                break;
            default:
                System.out.println("Kjenner ikke \"" + tagName + "\" elementer.");
                break;
        }
    }

    private void addPart(Element element, double x, double y, double dx, double dy) throws Exception {
        String name = element.getAttribute(tag.getTagName(NAME));
        String funk = element.getAttribute(tag.getTagName(FUNCTION));
        concretePart = new ConcretePart(name, sheet, funk);
        partList.add(concretePart);
        concretePart.setOrigo(dx, dy);
        solid2awt(element, x, y);
        this.weight += concretePart.getWeight(this.thickness);
    }

    private void solid2awt(Element element, double x, double y) throws Exception {
        NodeList nodeList = element.getElementsByTagName(tag.getTagName(SOLID));
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                element2elements(childElement, x, y);
            }
        }
    }

    private void userDefined2element(String id, double x, double y) throws Exception {
        Element element = doc.getElementById(id);
        if (null == element) {
            System.out.println("Fant ikke \"" + id + "\" elementet");
        } else {
            element2elements(element, x, y);
        }
    }

    private void element2children(Element element, double x, double y) throws Exception {
        NodeList nodeList = element.getChildNodes();
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                element2elements(childElement, x, y);
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
        List<Double> list = new ArrayList<>();
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

}
