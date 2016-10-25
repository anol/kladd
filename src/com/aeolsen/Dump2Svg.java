package com.aeolsen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.RoundRectangle2D;

import static java.awt.geom.PathIterator.*;

public class Dump2Svg {

    private Area mainArea;
    private Document doc;

    public Dump2Svg(Document doc) throws Throwable {
        mainArea = new Area();
        this.doc = doc;
        convertToAwt(doc);
        convertToSvg(mainArea);
    }

    private void convertToSvg(Area mainArea) {
        System.out.println("mainArea:\n" + mainArea.toString());
        for (PathIterator pi = mainArea.getPathIterator(null); !pi.isDone(); pi.next()) {
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

    private void convertToAwt(Document doc) {
        NodeList rectList = doc.getElementsByTagName("rekt");
        for (int k = 0; k < rectList.getLength(); k++) {
            toAwt((Element) rectList.item(k));
        }
    }

    private void toAwt(Element element) {
        switch (element.getTagName()) {
            case "rekt":
                int height = Integer.parseInt(element.getAttribute("h"));
                int width = Integer.parseInt(element.getAttribute("b"));
                int radius = Integer.parseInt(element.getAttribute("r"));
                Area rect = new Area(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
                mainArea.add(rect);
                break;
            default:
        }
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
