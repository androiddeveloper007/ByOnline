package com.bowen.commonlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.bowen.commonlib.CommonLibApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * 网络工具类
 * Created by AwenZeng on 2016/12/2.
 */
public class NetworkUtil {

    public static final int NETTYPE_NOINTERNET = 0; //无网络
    public static final int NETTYPE_WIFI = 1;       //WIFI网络
    public static final int NETTYPE_MOBILE = 2;     //移动网络
    public static final int NETTYPE_CMWAP = 0x02;   //中国移动一种形式
    public static final int NETTYPE_CMNET = 0x03;   //中国移动一种形式

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public synchronized static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) CommonLibApplication.getCommonLibContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断移动网络,是否连接
     * @return
     */
    private static boolean isMoblileNetAvailable() {
        ConnectivityManager connectMgr = (ConnectivityManager) CommonLibApplication.getCommonLibContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobNetInfo.isAvailable();
    }

    /**
     * 判断WIFI,是否连接
     * @return
     */
    private static boolean isWifiNetAvailable() {
        ConnectivityManager connectMgr = (ConnectivityManager)CommonLibApplication.getCommonLibContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetInfo.isAvailable() && wifiNetInfo.isConnectedOrConnecting();
    }


    /**
     * @param context
     * @return
     * @Description: TODO(获取当前网络类型)
     */
    public static int getNetworkType(Context context) {
        int netType = NETTYPE_NOINTERNET;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.getDefault()).contains("net")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
            netType = NETTYPE_MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
    /**
     * 移动网络开关
     */
    public static void toggleMobileData(Context context, boolean enabled) {

        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobNetInfo == null || !mobNetInfo.isAvailable() || mobNetInfo.isConnectedOrConnecting()) {
            return;    //设备不可用  或 已连接，跳过
        }
        // 可用，未连接，则连接
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
