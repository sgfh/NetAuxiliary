package com.emoke.core.auxiliary.pojo;

import com.emoke.core.auxiliary.interfaces.HttpListener;
import com.emoke.core.auxiliary.resp.HttpResp;

public class Test extends HttpResp implements HttpListener {
    private String msg;

    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Test{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", isSuccess=" + isSuccess() +
                ", statusCode=" + statusCode +
                ", httpErrorMsg='" + httpErrorMsg + '\'' +
                '}';
    }

    @Override
    public boolean isSuccess() {
        if(isSuccessful()&&"0".equals(code)){
            return true;
        }
        return false;
    }
}
