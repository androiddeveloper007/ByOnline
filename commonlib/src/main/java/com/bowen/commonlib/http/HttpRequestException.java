package com.bowen.commonlib.http;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by AwenZeng on 2016/12/29.
 */

public class HttpRequestException {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    public static final int ERRO_SYSTEM = 1000;
    public static final int ERRO_JSON = 1001;
    public static final int ERRO_NETWORK_CONNECT = 1002;

    public static HttpResult handleException(Throwable e){
        HttpResult result = new HttpResult();;
        if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    result.setCode(ERRO_SYSTEM);
                    result.setMsg("抱歉，服务器出现异常");
                    break;
            }
            return result;
        }else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            result.setCode(ERRO_JSON);
            result.setMsg("抱歉，解析错误");
            return result;
        }else if(e instanceof ConnectException){
            result.setCode(ERRO_NETWORK_CONNECT);
            result.setMsg("抱歉，网络连接失败");
            return result;
        }else {
            result.setCode(ERRO_SYSTEM);
            result.setMsg("抱歉，服务器出现异常");
            return result;
        }
    }
}
