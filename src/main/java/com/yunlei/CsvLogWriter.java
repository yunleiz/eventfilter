package com.yunlei;

import java.io.*;
import java.util.List;

public class CsvLogWriter implements LogWriter {
    private String filename;
    final private String headers = "client-address, client-guid, request-time, service-guid, retries-request, packets-requested, packets-serviced, max-hole-size";

    @Override
    public LogWriter setDest(String dest) throws IOException {
        this.filename = dest;
        File destFile = new File(this.filename);
        destFile.createNewFile();
        return this;
    }

    @Override
    public void write(List<LogEntry> logEntries) throws IOException {
        File destFile = new File(this.filename);
        FileOutputStream fos = new FileOutputStream(destFile);
        OutputStreamWriter swriter = new OutputStreamWriter(fos);
        BufferedWriter bwriter = new BufferedWriter(swriter);
        bwriter.write(this.headers);
        for (LogEntry e : logEntries) {
            bwriter.newLine();
            bwriter.write(String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                    e.clientAddr(),
                    e.clientGuid(),
                    e.requestTime().toString(),
                    e.serviceGuid(),
                    e.requestRetires(),
                    e.packetsRequest(),
                    e.packetServiced(),
                    e.maxHoleSize()));
        }
        bwriter.close();
        swriter.close();
        fos.close();
    }
}
