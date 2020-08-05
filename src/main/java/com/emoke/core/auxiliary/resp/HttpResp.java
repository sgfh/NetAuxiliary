package com.emoke.core.auxiliary.resp;


import okhttp3.Headers;

/**
 * 基础响应，主要反馈请求成功或者失败
 */
public class HttpResp {
    public static final String IS_SUCCESS = "isSuccessful";
    public static final String STATUS_CODE = "statusCode";
    public static final String HTTP_ERROR_MSG = "httpErrorMsg";
    public static final String HEADERS = "headers";
    /**
     * 请求是否成功
     */
    public boolean isSuccessful;

    /**
     * 状态码
     */
    public int statusCode;

    /**
     * 错误异常原因
     */
    public String httpErrorMsg;

    /**
     * 获取头部信息，授权相关
     * */
    public Headers headers;

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccess(boolean success) {
        isSuccessful = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHttpErrorMsg() {
        return httpErrorMsg;
    }

    public void setHttpErrorMsg(String httpErrorMsg) {
        this.httpErrorMsg = httpErrorMsg;
    }
}
