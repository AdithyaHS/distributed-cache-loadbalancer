package com.distributedsystems.loadbalancer.dao;

public class ClientReadRequest {

    private String key;
    private String lamportClock;

    public ClientReadRequest(String key, String lamportClock) {
        this.key = key;
        this.lamportClock = lamportClock;
    }

    public ClientReadRequest() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLamportClock() {
        return lamportClock;
    }

    public void setLamportClock(String lamportClock) {
        this.lamportClock = lamportClock;
    }
}
