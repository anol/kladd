package com.aeolsen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Main {

    static void printChildren(Node node, int level) {
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

    public static void main(String[] args) {
        try {
            File inputFile = new File("data/input.xml");
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getChildNodes();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                printChildren(node, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
