package com.candao.www.utils;

/**
 * Created by donglin on 2015/3/11.
 */
public class ReturnObj {

    private Constant code;
    private String msg;
    private Object data;

    public Constant getCode() {
        return code;
    }

    public void setCode(Constant code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ReturnObj() {
    }


    public ReturnObj(Constant code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ReturnObj createError(String  msg){
        return new ReturnObj(Constant.ERROR,msg,null);
    }
    public static ReturnObj createError(Object data){
        return new ReturnObj(Constant.ERROR,"失败",data);
    }
    public static ReturnObj createError(){
        return new ReturnObj(Constant.ERROR,"失败",null);
    }
    public static ReturnObj createSuccess(Object data){
       return new ReturnObj(Constant.SUCCESS,"成功",data);
    }

    public static ReturnObj createSuccess(){
       return new ReturnObj(Constant.SUCCESS,"成功",null);
    }


    enum Constant{
        SUCCESS,ERROR
    }
}
