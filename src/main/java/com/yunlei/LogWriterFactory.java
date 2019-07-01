package com.yunlei;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class LogWriterFactory {
    public LogWriter getLogWriter() throws ParserConfigurationException,
            IOException,
            SAXException,
            ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException
    {
        String configFile = System.getProperty("user.dir")+ "/config/config.xml";
        File xmlFile = new File(configFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        Element config = (Element) doc.getElementsByTagName("logWriterFactory").item(0);
        String className = config.getElementsByTagName("class").item(0).getTextContent();
        return (LogWriter) Class.forName(className).getConstructor().newInstance();
    }
}
