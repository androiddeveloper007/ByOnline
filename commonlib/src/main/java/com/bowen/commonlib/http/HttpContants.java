package com.bowen.commonlib.http;


import com.bowen.commonlib.CommonLibApplication;

/**
 * PHP请求常量
 * Created by AwenZeng on 2016/7/8.
 */
public class HttpContants{

    /**
     * 服务器地址
     */
    public static String REQUEST_URL = CommonLibApplication.DEBUG?"http://192.168.0.241:8028/":"https://www.boyizaixian.com/";

    //请求状态
    public static final int HTTP_OK = 0000;//成功
    public static final int HTTP_FAIL = 9999;//失败
    public static final int HTTP_LOGIN_OVERDUE = 9911;//登录过期
    public static final int HTTP_LOGIN_INVALID = 9912;//登录失效
}
