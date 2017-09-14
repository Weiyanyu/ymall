package com.ymall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 服务端通用返回对象,主要用于响应前端
 * @param <T>
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//当错误的时候是不应该返回data，但是在创建对象实例的时候其实是有data这个字段的，默认为null
//设置这个注解就可以再data为null的时候就忽略他，这样就不会返回类似data : null 这样的json了
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;


    //以下构造器为私有的原因是，我们应该提供外部接口调用更好的控制对象的创建
    private ServerResponse(int status) {
        this.status = status;
    }

    //这个构造器和下面那个ServerResponse(int status, String msg) 会有冲突
    // （当T的类型为String的时候，程序会优先调用下面那个签名，这是泛型的匹配规则）
    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //JsonIgnore注解表示isSuccess这个接口不会作为json主体返回到浏览器中
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    //getter
    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    //获得对象实例接口
    //成功的实例
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String message) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String message, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), message, data);
    }


    //错误,注意登陆失败的时候是不会返回data的
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String message) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), message);
    }

    public static <T> ServerResponse<T> createByErrorMessage(int errorCode, String message) {
        return new ServerResponse<T>(errorCode, message);
    }
}
