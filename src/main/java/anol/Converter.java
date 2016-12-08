package anol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Iterator;

import static anol.TagNames.Tags.NAME;
import static anol.TagNames.Tags.PAGE_SIZE;
import static anol.TagNames.Tags.SHEET;

public class Converter {

    private Document kladdDoc;
    private ConcretePartList partList;
    private boolean colors;
    private boolean annotations;
    private String language;
    private TagNames tag;
    private String docTitle = "kladd";
    private String pageSize = "a4";

    public Converter(Document kladdDoc, boolean annotations, boolean colors, String language) throws Throwable {
        partList = new ConcretePartList();
        this.kladdDoc = kladdDoc;
        this.annotations = annotations;
        this.colors = colors;
        this.language = language;
        this.tag = new TagNames(language);
        traverseAllElements();
        NodeList nodeList = kladdDoc.getElementsByTagName(this.tag.get(SHEET));
        if (0 < nodeList.getLength()) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                pageSize = element.getAttribute(this.tag.get(PAGE_SIZE));
                docTitle = element.getAttribute(this.tag.get(NAME));
            }
        }
    }

    public Document convertToSvg() throws Throwable {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document svgDoc = dBuilder.newDocument();
        new ToAwt(kladdDoc, partList, language);
        new ToSvg(partList, svgDoc);
        return svgDoc;
    }

    /*
     * Convert the document to a STEP "file"
     * @param inputFile The input file name
     * @param outputFile The output file name
     * @return The STEP "file" as a string
     */
    public String convertToStep(String inputFile, String outputFile) throws Throwable {
        new ToAwt(kladdDoc, partList, language);
        ToStep toStep = new ToStep();
        String step = toStep.getProlog();
        step += toStep.getHeaderSection(docTitle, inputFile, outputFile);
        Iterator<ConcretePart> iterator = partList.getIterator();
        int counter = 0;
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            step += toStep.getDataSection(part, ++counter);
        }
        step += toStep.getEpilog();
        return step;
    }

    public String convertToPs(String title) throws Throwable {
        new ToAwt(kladdDoc, partList, language);
        String boundingBox = partList.getBoundingBox();
        ToPs toPs = new ToPs(colors);
        String postScript = toPs.getDocumentHeader(title, pageSize, boundingBox);
        postScript += toPs.getPageHeader(title, pageSize);
        Iterator<ConcretePart> iterator = partList.getIterator();
        while (iterator.hasNext()) {
            ConcretePart part = iterator.next();
            postScript += toPs.convertArea(part);
            if (annotations) {
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
