package com.bink.netty.entity;

/**
 * Created by chenbinghao on 16/10/12.下午9:50
 */
public class NettyResponse {

    private String resBody;

    private Integer statusCode;

    private String message;

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
