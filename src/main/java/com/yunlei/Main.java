package com.yunlei;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final int THREADS_COUNT = 3;

    static public void main(String... args) throws Exception {
        String[] filenames;
        LogWriterFactory writerFactory = new LogWriterFactory();
        LogWriter logWriter = writerFactory.getLogWriter();

        if (args.length >= 2) { // Use user input, if user input args are valid input, consist of output file and input files
            logWriter.setDest(args[0]);
            filenames = Arrays.copyOfRange(args, 1, args.length);
        } else {                // Use default input for demo.
            filenames = new String[] {
                    System.getProperty("user.dir") + "/data/reports.csv",
                    System.getProperty("user.dir") + "/data/reports.xml",
                    System.getProperty("user.dir") + "/data/reports.json"
            };

            logWriter.setDest("out.csv");
        }

        LogProcesser processer = new LogProcesser(filenames);

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        try {
            processer.load(executor)
                    .filter(e -> e.packetServiced() > 0)
                    .sort((e1, e2) -> e1.requestTime().isBefore(e2.requestTime()) ? -1 : 1)
                    .countServiceUsage();

            Future future = executor.submit(()-> processer.write(logWriter));

            Map<String, Long> serviceUsage = processer.serviceUsage();

            //Print out service usage summary.
            System.out.println("service guid, count");
            serviceUsage.forEach((k,v) -> System.out.println(String.format("%s, %s", k, v.toString())));

            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            executor.shutdown();
        }
        System.out.println("Finished ...");
    }
}
