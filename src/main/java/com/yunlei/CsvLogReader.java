package com.yunlei;

import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class CsvLogReader implements LogEntriesReader {
    private String filePath;
    private List<LogEntry> entries;
    private DateTimeFormatter dateTimeFormatter;

    public CsvLogReader(String filePath) {
        this.filePath = filePath;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    }

    private void load() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        this.entries = new ArrayList<>();
        String[] nextRecord;

        while ((nextRecord = reader.readNext()) != null) {
            try {
                ZonedDateTime requestTime = ZonedDateTime.parse(nextRecord[2], this.dateTimeFormatter);
                entries.add(new LogEntry(
                        nextRecord[0],
                        nextRecord[1],
                        requestTime,
                        nextRecord[3],
                        Integer.parseInt(nextRecord[4]),
                        Integer.parseInt(nextRecord[5]),
                        Integer.parseInt(nextRecord[6]),
                        Integer.parseInt(nextRecord[7]))
                );
            } catch (Exception e) {
                System.out.println(String.format("Cannot add record %s, due to: %s", nextRecord[0], e.getLocalizedMessage()));
            }
        }

    }

    @Override
    public List<LogEntry> logEntires() throws IOException{
        if (entries == null) {
            this.load();
        }
        return this.entries;
    }
}
