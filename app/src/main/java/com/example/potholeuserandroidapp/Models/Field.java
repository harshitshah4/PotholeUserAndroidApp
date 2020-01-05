package com.example.potholeuserandroidapp.Models;

import com.google.gson.annotations.SerializedName;

public class Field {
    @SerializedName("key")
    String key;
    @SerializedName("Content-Disposition")
    String ContentDisposition;
    @SerializedName("Content-Type")
    String ContentType;
    @SerializedName("bucket")
    String bucket;
    @SerializedName("X-Amz-Algorithm")
    String XAMZAlgorithm;
    @SerializedName("X-Amz-Credential")
    String XAMZCredentials;
    @SerializedName("X-Amz-Date")

    String XAMZDate;
    @SerializedName("Policy")
    String Policy;
    @SerializedName("X-Amz-Signature")
    String XAMZSignature;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContentDisposition() {
        return ContentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        ContentDisposition = contentDisposition;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getXAMZAlgorithm() {
        return XAMZAlgorithm;
    }

    public void setXAMZAlgorithm(String XAMZAlgorithm) {
        this.XAMZAlgorithm = XAMZAlgorithm;
    }

    public String getXAMZCredentials() {
        return XAMZCredentials;
    }

    public void setXAMZCredentials(String XAMZCredentials) {
        this.XAMZCredentials = XAMZCredentials;
    }

    public String getXAMZDate() {
        return XAMZDate;
    }

    public void setXAMZDate(String XAMZDate) {
        this.XAMZDate = XAMZDate;
    }

    public String getPolicy() {
        return Policy;
    }

    public void setPolicy(String policy) {
        Policy = policy;
    }

    public String getXAMZSignature() {
        return XAMZSignature;
    }

    public void setXAMZSignature(String XAMZSignature) {
        this.XAMZSignature = XAMZSignature;
    }
}
