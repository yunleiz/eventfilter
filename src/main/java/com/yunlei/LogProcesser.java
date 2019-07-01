package com.yunlei;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.AbstractMap.SimpleEntry;

public class LogProcesser {
    private List<String> logFilenames;
    private List<LogEntry> logs;
    private LogReaderFactory logReaderFactory;
    private Map<String, Long> serviceUsage;

    public LogProcesser(String... filenames) throws Exception {
        this.logFilenames = Arrays.stream(filenames).collect(Collectors.toList());
        this.logs = new ArrayList<>();
        this.logReaderFactory = new LogReaderFactory();
    }

    public LogProcesser load(ExecutorService executor) throws InterruptedException, ExecutionException {
        List<Future<List<LogEntry>>> futuresList = new ArrayList<>();
        for (String s: logFilenames) {
            LogEntriesReader reader = this.logReaderFactory.getReaderByFileName(s);
            Callable<List<LogEntry>> task = new LogReadTask(reader);
            Future<List<LogEntry>> future = executor.submit(task);
            futuresList.add(future);
        }

        while (futuresList.size() > 0) {  // wait until all logs are loaded into mem.
            Iterator<Future<List<LogEntry>>> iter = futuresList.iterator();
            while (iter.hasNext()) {
                Future<List<LogEntry>> f = iter.next();
                if (f.isDone()) {
                    logs.addAll(f.get());
                    iter.remove();
                }
            }
        }

        return this;
    }

    public LogProcesser filter(Predicate<LogEntry> filter) {
        this.logs = this.logs.stream().filter(filter).collect(Collectors.toList());
        return this;
    }

    public LogProcesser sort(Comparator<LogEntry> sortComparator) {
        this.logs.sort(sortComparator);
        return this;
    }

    public LogProcesser countServiceUsage() {
        this.serviceUsage = this.logs.stream()
                .map(e -> new SimpleEntry<>(e.serviceGuid(), 1))
                .collect(Collectors.groupingBy(SimpleEntry::getKey, Collectors.counting()));

        return this;
    }

    public Map<String, Long> serviceUsage() {
        if (serviceUsage == null) {
            this.countServiceUsage();
        }
        return this.serviceUsage;
    }

    public LogProcesser write(LogWriter writer) throws Exception{
        writer.write(this.logs);
        return this;
    }

}
