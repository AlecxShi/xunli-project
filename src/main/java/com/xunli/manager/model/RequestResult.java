package com.xunli.manager.model;


import com.xunli.manager.enumeration.ReturnCode;

/**
 * 查询返回封装类
 * Created by wanglei on 2015/10/29.
 */
public class RequestResult {

    private String code;

    private String msg;

    private String detail;

    private Object data;

    public RequestResult(){}

    public static RequestResult create(ReturnCode returnCode, Object obj){
        return new RequestResult(returnCode, obj);
    }

    public static RequestResult create(ReturnCode returnCode){
        return new RequestResult(returnCode);
    }

    public RequestResult(ReturnCode returnCode, Object obj){
        setReturnCode(returnCode);
        this.data = obj;
    }

    public RequestResult(ReturnCode returnCode){
        setReturnCode(returnCode);
    }

    public void setReturnCode(ReturnCode returnCode) {
        this.code = returnCode.getCode();
        this.msg = returnCode.getMsg();
        this.detail = returnCode.getDetail();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}


