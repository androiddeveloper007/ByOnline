package com.bowen.commonlib.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类
 * Created by AwenZeng on 2016/12/24.
 */

public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static Gson getGson(){
        return gson;
    }
    /**
     * 转成json
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param jsonStr
     * @param cls
     * @return
     */
    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(jsonStr, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param jsonStr
     * @param cls
     * @return
     */
    public static <T> List<T> fromJsonList(String jsonStr, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(jsonStr, new TypeToken<List<T>>() {}.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param jsonStr
     * @return
     */
    public static <T> List<Map<String, T>> fromJsonMaps(String jsonStr) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(jsonStr,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param jsonStr
     * @return
     */
    public static <T> Map<String, T> fromJsonMap(String jsonStr) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(jsonStr, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 将Json对象转换成Map
     *
     * @param jsonString
     *            json对象
     * @return Map对象
     * @throws JSONException
     */
    public static Map toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;

    }
}
