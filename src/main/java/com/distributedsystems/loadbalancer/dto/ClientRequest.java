package com.distributedsystems.loadbalancer.dto;


public class ClientRequest {
    private String key;
    private String value;
    private String lamportClock;

    public ClientRequest(String key, String value, String lamportClock) {
        this.key = key;
        this.value = value;
        this.lamportClock = lamportClock;
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
