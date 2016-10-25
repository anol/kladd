package com.aeolsen;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MiscTest {

    @Test
    public void dump2SvgTest() throws Throwable {
        Dump2Svg dump2Svg = new Dump2Svg(load());
    }

    private Document load() throws Throwable {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element element = doc.createElement("rect");
        element.setAttribute("name", "pincode");
        element.setAttribute("height", "150");
        element.setAttribute("width", "1000");
        element.setAttribute("radius", "20");
        return doc;
    }

}
