package com.xb.soft.net;

public interface MyCallBack {
    void onFailure(int code);
    void onResponse(String json);
}
