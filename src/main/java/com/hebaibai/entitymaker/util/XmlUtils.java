package com.hebaibai.entitymaker.util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 读取xml
 * @author hjx
 */
public class XmlUtils {

    /**
     * 读取 Document
     *
     * @param xmlPath
     * @return
     */
    public static Document getConfigDocument(String xmlPath) {
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath);
            Scanner scanner = new Scanner(resourceAsStream);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append("\n");
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilde = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilde.parse(new ByteArrayInputStream(stringBuilder.toString().getBytes()));
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
