package com.xb.soft.net;

import com.xb.soft.net.mode.Response;
import com.google.gson.Gson;
import com.xb.soft.utils.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class TestURLTask implements Runnable {
    private static final String TAG = "TestURLTask";
    private String url;
    private TestUrlCallBack callBack;
    public TestURLTask(String url,TestUrlCallBack callBack) {
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
        String ServerStatusUrl = protocol+"://"+domain +"/ServerStatus";
        Log.i(TAG,"ServerStatusUrl="+ServerStatusUrl);
        NetClient.getNetClient().callGetNet(ServerStatusUrl, new MyCallBack() {
            @Override
            public void onFailure(int code) {
                callBack.onFailure(url);
            }

            @Override
            public void onResponse(String json) {
                Gson gson = new Gson();
                Response response = gson.fromJson(json,Response.class);
                if(response.getError()==0){
                    callBack.onSuccess(url);
                }else{
                    callBack.onFailure(url);
                }
            }
        });
    }
}
