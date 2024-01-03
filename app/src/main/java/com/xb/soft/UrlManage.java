package com.xb.soft;

public class UrlManage {
    private static Object mLock = new Object();
    private static UrlManage mInstance;
    private String url;

    public String getUrl() {
        return url;
    }

    private UrlManage(){
    }

    public static UrlManage  getInstance(){
        if(mInstance == null){
            synchronized (mLock){
                mInstance = new UrlManage();
            }

        }
        return mInstance;
    }
    public void setUrl(String url){
        this.url = url;
    }
}
