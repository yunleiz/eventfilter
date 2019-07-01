package com.yunlei;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Constructor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LogReaderFactory {
    private Map<String, Class> type2reader;

    public LogReaderFactory() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        this.type2reader = new HashMap<>();
        String configFile = System.getProperty("user.dir")+ "/config/config.xml";

        File xmlFile = new File(configFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        Element config = (Element) doc.getElementsByTagName("logReadFactory").item(0);
        NodeList formatReaders = config.getElementsByTagName("reader");

        // Build format-> ReaderClass map.
        for (int i = 0; i < formatReaders.getLength(); ++i) {
            Element reader = (Element) formatReaders.item(i);
            String format = reader.getElementsByTagName("format").item(0).getTextContent();
            String className = reader.getElementsByTagName("class").item(0).getTextContent();
            type2reader.put(format,Class.forName(className));
        }
    }

    public LogEntriesReader getReaderByFileName(String filePath)  {
        String ext = filePath.substring(filePath.lastIndexOf(".")+1);
        LogEntriesReader reader = null;
        if (this.type2reader.containsKey(ext)) {
            try {
                Constructor<?> constructor = this.type2reader.get(ext).getConstructor(String.class);
                reader = (LogEntriesReader) constructor.newInstance(filePath);
            } catch (NoSuchMethodException |
                    InstantiationException |
                    IllegalAccessException |
                    InvocationTargetException e) {
                System.out.println(String.format("Cannot construct %s reader, due to: %s", ext, e.toString()));
                e.printStackTrace();
            }
        }
        return reader;
    }
}
