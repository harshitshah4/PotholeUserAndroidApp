package com.example.potholeuserandroidapp.Models;

import com.google.gson.annotations.SerializedName;

public class Signed {

    @SerializedName("url")
    String url;
    @SerializedName("fields")
    Field fields;
    @SerializedName("id")
    String id;

    public Signed(String url, Field fields, String id) {
        this.url = url;
        this.fields = fields;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Field getFields() {
        return fields;
    }

    public void setFields(Field fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
