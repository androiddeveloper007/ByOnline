package com.bowen.commonlib.http;


import android.net.Uri;

import java.util.Map;

/**
 * Http请求基类
 * Created by AwenZeng on 2016/12/6.
 */

public abstract class BaseHttp {
    public final String TAG = "OkHttp";
    public HttpRequestParam reqParam = new HttpRequestParam();

    public void setParam(String key, Object value) {
        if (reqParam == null)
            reqParam = new HttpRequestParam();
        reqParam.put(key, value);
    }

    public HttpRequestParam getParam(){
        return reqParam;
    }

    public void setMapParam(Map<String, String> map) {
        if (reqParam == null)
            reqParam = new HttpRequestParam();
        reqParam.putAll(map);
    }


    public String appendParameter(String url, Map<String, String> params) {
        url += "?";
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build().toString();
    }

    public abstract void requestSRV(String url, HttpTaskCallBack mListener);

    public abstract void requestSRV(Method method, String url,String loadingStr,HttpTaskCallBack mListener);

    public abstract void requestNewSRV(String url,HttpTaskCallBack mListener);

    public enum Method {
        GET,
        POST
    }
}

