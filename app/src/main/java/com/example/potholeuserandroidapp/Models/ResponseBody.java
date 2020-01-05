package com.example.potholeuserandroidapp.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseBody {

    @SerializedName("msg")
    private String msg;

    public ResponseBody(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
