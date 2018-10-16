package com.bowen.commonlib.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bowen.commonlib.CommonLibApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AwenZeng on 2017/3/30.
 */

public class ApplicationUtils {
    /**
     * 获取应用的版本号
     * @return
     */
    public static String getVersionName(){
        PackageInfo packInfo = getPackageInfo();
        if(packInfo!=null){
            return packInfo.versionName;
        }
        return "";
    }

    /**
     * 获取应用的版本Code
     * @return
     */
    public static int getVersionCode(){
        PackageInfo packInfo = getPackageInfo();
        if(packInfo!=null){
            return packInfo.versionCode;
        }
        return 0;
    }

    /**
     * 获取应用的包名
     * @return
     */
    public static String getVersionPakageName(){
        PackageInfo packInfo = getPackageInfo();
        if(packInfo!=null){
            return packInfo.packageName;
        }
        return "";
    }

    /**
     * 得到当前系统SDK版本
     */
    private int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前程序的版本号
     */
    public  static PackageInfo getPackageInfo() {
        try{
            PackageManager packageManager = CommonLibApplication.getCommonLibApplication().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(CommonLibApplication.getCommonLibApplication().getPackageName(), 0);
            return packInfo;
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前程序的版本号
     */
    public  static String getPackageName() {
        return getPackageInfo().packageName;
    }

    /**
     * 获取MetaDataValue(Manifest配置数据)
     * @param name
     * @param def
     * @return
     */
    public static String getMetaDataValue(String name, String def) {
        String value = getMetaDataValue(name);
        return (value == null) ? def : value;
    }

    private static String getMetaDataValue(String name) {
        Object value = null;
        PackageManager packageManager = CommonLibApplication.getCommonLibApplication().getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(CommonLibApplication.getCommonLibApplication().getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (Exception e) {
        }
        return value.toString();
    }

    /**
     * 判断应用是否存在的方法
     */
    public static boolean isHavaApp(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 应用是否进入后台
     * @param context
     * @return
     */
    public static boolean isAppGoToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打开键盘，焦点定位到指定的view上
     * @param v 要打开键盘的View
     */
    public static void openKeyboard(View v){
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager)CommonLibApplication.getCommonLibApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 关闭键盘
     * @param v 键盘焦点所在的View
     */
    public static void closeKeyboard(View v){
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager)CommonLibApplication.getCommonLibApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
    }

    /***
     * 判断软键盘是否打开
     * @return
     */
    public static boolean isKeyboardOpen() {
        InputMethodManager imm = (InputMethodManager)CommonLibApplication.getCommonLibApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();//若返回true，则表示输入法打开
    }

    /**
     * 如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
     * @return
     */
    public static boolean isScreenOn(){
        PowerManager pm = (PowerManager) CommonLibApplication.getCommonLibApplication().getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }
}
