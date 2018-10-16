package com.bowen.commonlib.http;

import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.DeviceUtil;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by AwenZeng on 2016/12/02.
 */
public class HttpRequestParam extends HashMap<String, String> {

    public HttpRequestParam() {
        put("deviceId", DeviceUtil.getInstance().getDeviceId());
        put("appType","android");
        put("requestTime", DateUtil.getDateFormat(new Date(System.currentTimeMillis()),DateUtil.DATE_YEAR_MONTH_DAY_MIN_SEC_FORMAT));
    }

    public String put(String key, Object value) {
        if (value == null){
            value = "";
        }
        if (value instanceof String ||
                value instanceof Integer
                || value instanceof Float
                || value instanceof Long
                || value instanceof Double
                || value instanceof Boolean)
            return put(key, String.valueOf(value));

        return null;
    }

    @Override
    public String put(String key, String value) {
        return super.put(key, value);
    }

}
