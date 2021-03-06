package com.distributedsystems.loadbalancer.dao;


public class ClientWriteRequest {
    private String key;
    private String value;
    private String lamportClock;
    private String clientTimeStamp;

    public ClientWriteRequest(String key, String value, String lamportClock, String clientTimeStamp) {
        this.key = key;
        this.value = value;
        this.lamportClock = lamportClock;
        this.clientTimeStamp = clientTimeStamp;
    }

    public ClientWriteRequest() {
    }

    public String getClientTimeStamp() {
        return clientTimeStamp;
    }

    public void setClientTimeStamp(String clientTimeStamp) {
        this.clientTimeStamp = clientTimeStamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLamportClock() {
        return lamportClock;
    }

    public void setLamportClock(String lamportClock) {
        this.lamportClock = lamportClock;
    }
}
