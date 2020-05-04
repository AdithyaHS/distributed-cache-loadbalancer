package com.distributedsystems.loadbalancer.dao;

public class ReadResponse {
    String value;
    boolean success;

    public ReadResponse(String value, boolean success) {
        this.value = value;
        this.success = success;
    }

    public ReadResponse() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
