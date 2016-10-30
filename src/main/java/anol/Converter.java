package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.Area;
import java.io.File;

public class Converter {

    private Area mainArea;
    private Document kladdDoc;

    public Converter(Document kladdDoc) throws Throwable {
        mainArea = new Area();
        this.kladdDoc = kladdDoc;
        traverseAllElements();
    }

    public Document convertToSvg() throws Throwable {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document svgDoc = dBuilder.newDocument();
        Area mainArea = new Area();
        new ToAwt(kladdDoc, mainArea);
        new ToSvg(mainArea, svgDoc);
        return svgDoc;
    }

    public String convertToPs(String title, String pageSize) throws Throwable {
        Area mainArea = new Area();
        new ToAwt(kladdDoc, mainArea);
        ToPs toPs = new ToPs(mainArea);
        String body = toPs.convert();
        String header = toPs.getDocumentHeader(title, pageSize) + toPs.getPageHeader(title, pageSize);
        return (header + body + toPs.getTrailer());
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

    public void convertToPdf(String temp, File outputFile, String pageSize) throws Throwable {
        (new ToPdf()).convert(temp, outputFile, pageSize);
    }
}
