package com.example.potholeuserandroidapp.Models;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("text")
    String text;
    @SerializedName("image")
    String image;
    @SerializedName("location")
    Location location;
    @SerializedName("status")
    Status status;
    @SerializedName("message")
    String message;
    @SerializedName("timestamp")
    long timestamp;

    public Post(String text, String image, Location location) {
        this.text = text;
        this.image = image;
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
