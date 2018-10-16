package com.bowen.tcm.common.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.commonlib.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * 地图地位工具类
 * Created by AwenZeng on 2016/12/16.
 */

public class LocationUtil {
    private final String TAG = "LocationUtil";
    private static LocationUtil instance;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption;

    public static LocationUtil getInstance() {
        if (instance == null) {
            synchronized (LocationUtil.class) {
                if (instance == null) {
                    instance = new LocationUtil();
                }
            }
        }
        return instance;
    }

    public LocationUtil() {
        locationClient = new AMapLocationClient(CommonLibApplication.getCommonLibContext());
        locationOption = new AMapLocationClientOption();
        //设置定位参数
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        locationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(2000);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.setLocationOption(locationOption);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        locationClient.startLocation();
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                LocationEvent event = new LocationEvent();
                event.setProvince(loc.getProvince());
                event.setAddress(loc.getAddress());
                event.setCity(loc.getCity());
                event.setArea(loc.getDistrict());
                event.setCityCode(loc.getCityCode());
                event.setAreaCode(loc.getAdCode());
                event.setLongitude(loc.getLongitude());
                event.setLatitude(loc.getLatitude());
                EventBus.getDefault().post(event);
                LogUtil.v(TAG,loc.getAddress());
            }
        }
    };

}
