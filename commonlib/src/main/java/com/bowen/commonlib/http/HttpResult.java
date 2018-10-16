package com.bowen.commonlib.http;


/**
 * Created by AwenZeng on 2016/12/22.
 */

public class HttpResult<T> {
    /**
     * code : 响应代码 0000:成功，9999:失败
     * msg : 消息提示
     * sign : 签名字符串
     */
    private int code;
    private String msg;
    private String sign;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * copy一下默认数据
     * @param httpResult
     */
    public void copy(HttpResult httpResult){
        code = httpResult.getCode();
        msg = httpResult.getMsg();
        sign = httpResult.getSign();
    }

}
