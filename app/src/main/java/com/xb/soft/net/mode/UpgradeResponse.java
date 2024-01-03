package com.xb.soft.net.mode;

public class UpgradeResponse {
    /**
     * content : 检测到版本更新：新增国内线路，请更新后再次运行！
     * version : 1.0.1
     * url : https://www.788xn.com/static/download/ios.html?v=20190110001
     * forceUpdate : false
     */

    private String content;
    private String version;
    private String url;
    private boolean forceUpdate;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
