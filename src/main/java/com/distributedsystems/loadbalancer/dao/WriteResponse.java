package com.distributedsystems.loadbalancer.dao;

public class WriteResponse {
    boolean success;
    String timestamp;

    public WriteResponse(boolean success, String timestamp) {
        this.success = success;
        this.timestamp = timestamp;
    }
    public WriteResponse(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
