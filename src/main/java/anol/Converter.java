package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Iterator;

public class Converter {

    private Document kladdDoc;
    private String pageSize = "a4";
    private ConcretePartList partList;
    private boolean colors;
    private boolean annotations;

    public Converter(Document kladdDoc, boolean annotations, boolean colors) throws Throwable {
        partList = new ConcretePartList();
        this.kladdDoc = kladdDoc;
        this.annotations = annotations;
        this.colors = colors;
        traverseAllElements();
        NodeList nodeList = kladdDoc.getElementsByTagName("ark");
        if (0 < nodeList.getLength()) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                pageSize = element.getAttribute("type");
            }
        }
    }

    public Document convertToSvg() throws Throwable {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document svgDoc = dBuilder.newDocument();
        new ToAwt(kladdDoc, partList);
        new ToSvg( partList, svgDoc);
        return svgDoc;
    }

    public String convertToPs(String title) throws Throwable {
        new ToAwt(kladdDoc, partList);
        String boundingBox = partList.getBoundingBox();
        ToPs toPs = new ToPs(colors);
        String postScript = toPs.getDocumentHeader(title, pageSize, boundingBox);
        postScript += toPs.getPageHeader(title, pageSize);
        Iterator<ConcretePart> iterator = partList.getIterator();
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            postScript += toPs.convertArea(part);
            if(annotations) {
                postScript += toPs.convertPoints(part);
            }
        }
        postScript += toPs.getPageTrailer();
        postScript += toPs.getDocumentTrailer();
        return postScript;
    }

    private void traverseAllElements() {
        NodeList nodeList = kladdDoc.getElementsByTagName("*");
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

    public void convertToPdf(String temp, File outputFile) throws Throwable {
        (new ToPdf()).convert(temp, outputFile, pageSize);
    }
}
