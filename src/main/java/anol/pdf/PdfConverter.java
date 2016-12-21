package anol.pdf;

import anol.converter.ConcretePart;
import anol.converter.Converter;
import anol.awg.ToAwt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Iterator;

import static anol.converter.TagNames.Tags.NAME;

public class PdfConverter extends Converter {

    public PdfConverter(Document kladdDoc, boolean annotations, boolean colors, String language) throws Throwable {
        super(kladdDoc, annotations, colors, language);
    }

    public String convertToPs(String title) throws Throwable {
        new ToAwt(kladdDoc, partList, language);
        String boundingBox = partList.getBoundingBox();
        ToPs toPs = new ToPs(colors);
        int pageNumber = 0;
        NodeList nodeList = kladdDoc.getElementsByTagName("sheet");
        String postScript = toPs.getDocumentHeader(title, pageSize, boundingBox, nodeList.getLength());
        for (int k = 0; k < nodeList.getLength(); k++) {
            Node node = nodeList.item(k);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element sheet = (Element) node;
                postScript += toPs.getPageHeader(designElement, sheet, pageNumber);
                Iterator<ConcretePart> iterator = partList.getIterator();
                while (iterator.hasNext()) {
                    ConcretePart part = iterator.next();
                    if (part.isOnSheet(pageNumber)) {
                        postScript += toPs.convertArea(part);
                        if (annotations) {
                            postScript += toPs.convertPoints(part);
                            postScript += toPs.printHelpLines(part);
                        }
                    }
                }
                if (annotations) {
                    String sheet1st = sheet.getAttribute(this.tag.get(NAME));
                    String sheet2nd = sheet.getAttribute(this.tag.get(NAME) + 2);
                    postScript += toPs.printSheetAnnotations(docTitle, sheet1st, sheet2nd, pageNumber);
                }
                postScript += toPs.getPageTrailer();
                pageNumber++;
            }
        }
        postScript += toPs.getDocumentTrailer();
        return postScript;
    }

    public void convertToPdf(String temp, File outputFile) throws Throwable {
        (new ToPdf()).convert(temp, outputFile, pageSize);
    }
}
