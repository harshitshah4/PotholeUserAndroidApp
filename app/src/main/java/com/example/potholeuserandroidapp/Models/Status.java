package com.example.potholeuserandroidapp.Models;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("sid")
    String sid;
    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("timestamp")
    long timestamp;

    public Status(String sid, String status, String message, long timestamp) {
        this.sid = sid;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
