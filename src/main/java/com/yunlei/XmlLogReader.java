package com.yunlei;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlLogReader implements LogEntriesReader {
    private String filePath;
    private List<LogEntry> entries;
    private DateTimeFormatter dateTimeFormatter;

    public XmlLogReader(String filePath) {
        this.filePath = filePath;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    }

    public void load() throws ParserConfigurationException, IOException, SAXException {
        this.entries = new ArrayList<>();
        File xmlFile = new File(this.filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("report");
        for (int i=0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            ZonedDateTime requestTime = ZonedDateTime.parse(element.getElementsByTagName("request-time").item(0).getTextContent(),
                    this.dateTimeFormatter);
            LogEntry e = new LogEntry(
                    element.getElementsByTagName("client-address").item(0).getTextContent(),
                    element.getElementsByTagName("client-guid").item(0).getTextContent(),
                    requestTime,
                    element.getElementsByTagName("service-guid").item(0).getTextContent(),
                    Integer.parseInt(element.getElementsByTagName("retries-request").item(0).getTextContent()),
                    Integer.parseInt(element.getElementsByTagName("packets-requested").item(0).getTextContent()),
                    Integer.parseInt(element.getElementsByTagName("packets-serviced").item(0).getTextContent()),
                    Integer.parseInt(element.getElementsByTagName("max-hole-size").item(0).getTextContent())
                    );
            entries.add(e);
        }
    }

    @Override
    public List<LogEntry> logEntires() throws Exception {
        if (entries == null) {
            this.load();
        }
        return entries;
    }
}
