package com.xb.soft.net;

import java.net.MalformedURLException;
import java.net.URL;

public class TestURLPingTask implements Runnable {
    private String url;
    private TestUrlCallBack callBack;
    public TestURLPingTask(String url, TestUrlCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        URL uRl = null;
        try {
            uRl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String domain = uRl.getHost();
        String protocol = uRl.getProtocol();
        String ServerStatusUrl = protocol+"://"+domain;
        NetClient.getNetClient().callPingNet(ServerStatusUrl, new MyCallBack() {
            @Override
            public void onFailure(int code) {
                callBack.onFailure(url);
            }

            @Override
            public void onResponse(String json) {
                callBack.onSuccess(url);
            }
        });
    }
}
