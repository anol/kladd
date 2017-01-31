package anol.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static anol.converter.TagNames.Tags.*;

public class Converter {

    protected Document kladdDoc;
    protected ConcretePartList parts;
    protected boolean colors;
    protected boolean annotations;
    protected String language;
    protected TagNames tag;
    protected String docTitle = "kladd";
    protected String pageSize = "a4";
    protected Element designElement;

    public Converter(Document kladdDoc, boolean annotations, boolean colors, String language) throws Throwable {
        parts = new ConcretePartList();
        this.kladdDoc = kladdDoc;
        this.annotations = annotations;
        this.colors = colors;
        this.language = language;
        this.tag = new TagNames(language);
        traverseAllElements();
        NodeList nodeList = kladdDoc.getElementsByTagName(this.tag.get(DESIGN));
        if (0 == nodeList.getLength()) {
            nodeList = kladdDoc.getElementsByTagName(this.tag.get(SHEET));
        }
        if (0 < nodeList.getLength()) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                designElement = (Element) node;
                pageSize = designElement.getAttribute(this.tag.get(PAGE_SIZE));
                docTitle = designElement.getAttribute(this.tag.get(NAME));
            }
        }
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
}
