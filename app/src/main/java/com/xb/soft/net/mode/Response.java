package com.xb.soft.net.mode;

public class Response {

    /**
     * code : 0-1
     * error : 0
     * message : 请求操作成功！
     */

    private String code;
    private int error;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
