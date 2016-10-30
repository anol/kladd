package anol;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MiscTest {

    @Test
    public void mainTest() throws Throwable {
        Processor processos = new Processor("data/input.xml", "data/output.svg", "a4");
    }

    @Test
    public void dump2SvgTest() throws Throwable {
        Converter dump2Svg = new Converter(loadDummyData());
    }

    private Document loadDummyData() throws Throwable {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element element = doc.createElement("rect");
        element.setAttribute("n", "pincode");
        element.setAttribute("h", "150");
        element.setAttribute("b", "1000");
        element.setAttribute("r", "20");
        return doc;
    }

}
