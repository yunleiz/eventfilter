package com.yunlei;

import java.time.ZonedDateTime;

public class LogEntry {
    private String clientAddr;
    private String clientGuid;
    private ZonedDateTime requestTime;
    private String serviceGuid;
    private int requestRetires;
    private int packetsRequest;
    private int packetServiced;
    private int maxHoleSize;

    public LogEntry(String clientAddr,
                    String clientGuid,
                    ZonedDateTime requestTime,
                    String serviceGuid,
                    int requestRetires,
                    int packetsRequest,
                    int packetServiced,
                    int maxHoleSize) {

        this.clientAddr = clientAddr;
        this.clientGuid = clientGuid;
        this.requestTime = requestTime;
        this.serviceGuid = serviceGuid;
        this.requestRetires = requestRetires;
        this.packetsRequest = packetsRequest;
        this.packetServiced = packetServiced;
        this.maxHoleSize = maxHoleSize;
    }

    public int packetServiced() {
        return this.packetServiced;
    }

    public ZonedDateTime requestTime() {
        return this.requestTime;
    }

    public String serviceGuid() {
        return this.serviceGuid;
    }

    public String clientGuid() {
        return this.clientGuid;
    }

    public String clientAddr() {
        return clientAddr;
    }

    public int requestRetires() {
        return this.requestRetires;
    }

    public int packetsRequest() {
        return this.packetsRequest;
    }

    public int maxHoleSize() {
        return this.maxHoleSize;
    }
}
