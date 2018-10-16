package com.bowen.commonlib.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created by AwenZeng on 2016/12/26.
 */

public class NetworkRequestUtil {

    private static NetworkRequestUtil instance;
    private Map<String,SoftReference<Subscription>> requestMap;

    public static NetworkRequestUtil getInstance(){
        if(instance == null){
            synchronized (NetworkRequestUtil.class){
                if(instance == null){
                    instance = new NetworkRequestUtil();
                }
            }
        }
        return instance;
    }

    public NetworkRequestUtil() {
       requestMap = new HashMap<>();
    }

    public void put(String key, Subscription subscription){
        SoftReference<Subscription> weakReference = new SoftReference<Subscription>(subscription);
        requestMap.put(key,weakReference);
    }
    /**
     * 直接删除
     * @param key
     */
    public void remove(String key){
        if(EmptyUtils.isNotEmpty(requestMap.get(key))){
            Subscription subscription = requestMap.get(key).get();
            if(subscription!=null&&subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
            requestMap.remove(key);
        }
    }

    /**
     * 查询之后关闭
     * @param key
     */
    public void closeRequest(String key){
        if(requestMap!=null&&requestMap.size()>0){
            for (HashMap.Entry<String, SoftReference<Subscription>> map : requestMap.entrySet()) {
                if(map.getKey().equals(key)&&!map.getValue().get().isUnsubscribed()){
                    map.getValue().get().unsubscribe();
                    requestMap.remove(key);
                }
            }
        }
    }

    /**
     * 关闭所有请求
     */
    public void closeAllRequest(){
        if(requestMap!=null&&requestMap.size()>0){
            for (HashMap.Entry<String, SoftReference<Subscription>> map : requestMap.entrySet()) {
                if(!map.getValue().get().isUnsubscribed()){
                    map.getValue().get().unsubscribe();
                }
            }
        }
        requestMap.clear();
    }
}
