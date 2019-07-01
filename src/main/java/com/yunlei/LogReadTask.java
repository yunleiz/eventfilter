package com.yunlei;

import java.util.concurrent.Callable;
import java.util.List;

public class LogReadTask implements Callable<List<LogEntry>> {
    private LogEntriesReader reader;

    public LogReadTask(LogEntriesReader reader) {
        this.reader = reader;
    }

    @Override
    public List<LogEntry> call(){
        List<LogEntry> logs = null;
        try {
            logs = reader.logEntires();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
}
