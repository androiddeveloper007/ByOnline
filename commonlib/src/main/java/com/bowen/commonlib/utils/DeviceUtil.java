package com.bowen.commonlib.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bowen.commonlib.CommonLibApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * 获取手机信息的工具类
 * Created by AwenZeng on 2016/12/2.
 */
public class DeviceUtil {

    private static DeviceUtil mDeviceUtil;
    private TelephonyManager tm = (TelephonyManager) CommonLibApplication.getCommonLibContext().getSystemService(Context.TELEPHONY_SERVICE);

    private DeviceUtil() {
        tm = (TelephonyManager) CommonLibApplication.getCommonLibContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static DeviceUtil getInstance() {
        if (mDeviceUtil == null) {
            synchronized (DeviceUtil.class) {
                if (mDeviceUtil == null) {
                    mDeviceUtil = new DeviceUtil();
                }
            }
        }
        return mDeviceUtil;
    }

/*****************************************手机属性************************************************/

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public String getPhone() {
        return tm.getLine1Number();
    }

    /**
     * SDK版本号
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getSdkApi() {
        return Build.VERSION.SDK;
    }

    /**
     * Firmware/OS 版本号
     *
     * @return
     */
    public static String getVersionRelease() {
        return Build.VERSION.RELEASE;
    }


/*****************************************手机唯一设备号************************************************/

    /**
     * Serial Number号(非手机设备)
     *
     * @return
     */
    public static String getSerialNo() {
        return android.os.Build.SERIAL;
    }

    /**
     * imei
     *
     * @return
     */
    public String getImei() {
        try {
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取ANDROID_ID
     */
    public static String getAndroidId(Context context) {
        String aid = android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        return aid;
    }

    /**
     * 获取Mac地址
     */
    public static String getMacAddress() {
        WifiManager wifi = (WifiManager) CommonLibApplication.getCommonLibApplication().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * 获取AndroidID
     */
    private String getAndroidId() {
        String androidId = android.provider.Settings.System.getString(CommonLibApplication.getCommonLibContext().getContentResolver(),
                android.provider.Settings.System.ANDROID_ID);
        if (androidId == null)
            return "";
        return androidId;
    }


    /**
     * 获取手机唯一识别码
     */
    public String getDeviceId() {
        String deviceId = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            deviceId = getImei();
        }

        if(EmptyUtils.isEmpty(deviceId)){
            deviceId = getAndroidId();
        }

        if(EmptyUtils.isEmpty(deviceId)){
            deviceId = getSerialNo();
        }

        if(deviceId.equals("unknown")||EmptyUtils.isEmpty(deviceId)){
            deviceId = getMacAddress();
        }

        if (EmptyUtils.isEmpty(deviceId)||deviceId.equals("unknown")|| deviceId.equals("000000000000000")) {
            deviceId = getPhoneUUID();
        }
//        LogUtil.androidLog("手机设备唯一码："+" imei "+getImei() + " SeriNo "+getSerialNo() + " macAddress "+getMacAddress() + " AndroidId "+getAndroidId()+" UUID "+ getPhoneUUID());
        return deviceId;
    }

    /**
     * 获取设备UUID并保存到缓存中
     * @return
     */
    public String getPhoneUUID(){
        String uuid = DataCacheUtil.getInstance().getString(DataCacheUtil.DEVICE_UUID,"");
        if(EmptyUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            DataCacheUtil.getInstance().putString(DataCacheUtil.DEVICE_UUID,uuid);
        }
        return uuid;
    }

    /**
     *
     * @return
     */
    public String getUUID() {
        String uuid = "";
        if (uuid == null) {
            File installation = new File(FileUtil.getAppFileSaveRootDri(), "INSTALLATION");
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                uuid = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return uuid;
    }


    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }


}