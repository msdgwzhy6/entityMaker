package com.hebaibai.entitymaker.util;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtilsTest {

    @Test
    public void getConfigDocument() {
        Document configXml = XmlUtils.getConfigDocument("config.xml");
        Element element = configXml.getDocumentElement();
        String jdbcUrl = element.getElementsByTagName("jdbc.url").item(0).getTextContent();
        System.out.println(jdbcUrl);
    }
}