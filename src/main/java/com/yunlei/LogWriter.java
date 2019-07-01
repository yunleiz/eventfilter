package com.yunlei;

import java.util.List;

public interface LogWriter {
    LogWriter setDest(String dest) throws Exception;
    void write(List<LogEntry> logEntries) throws Exception;
}
