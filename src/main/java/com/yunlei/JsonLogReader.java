package com.yunlei;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public class JsonLogReader implements LogEntriesReader {
    private String filePath;
    private List<LogEntry> entries;

    public JsonLogReader (String filePath) {
        this.filePath = filePath;
    }

    private LogEntry parseJsonObj(JSONObject obj) {
        ZonedDateTime requestTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli((long) obj.get("request-time")),
                ZoneId.of("UTC"));

        return new LogEntry(
                (String) obj.get("client-address"),
                (String) obj.get("client-guid"),
                requestTime,
                (String) obj.get("service-guid"),
                ((Long) obj.get("retries-request")).intValue(),
                ((Long) obj.get("packets-requested")).intValue(),
                ((Long) obj.get("packets-serviced")).intValue(),
                ((Long) obj.get("max-hole-size")).intValue());
    }

    private void load() throws IOException, ParseException {
        this.entries = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(this.filePath);
        JSONArray jsonArr = (JSONArray) jsonParser.parse(reader);
        jsonArr.forEach(e -> entries.add(parseJsonObj((JSONObject) e)));
    }

    @Override
    public List<LogEntry> logEntires() throws Exception {
        if (entries == null) {
            this.load();
        }
        return entries;
    }
}
