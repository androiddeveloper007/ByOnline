package com.bowen.tcm;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.multidex.MultiDex;

import com.awen.camera.CameraApplication;
import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.tcm.common.database.util.DataBaseManager;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.main.activity.SplashActivity;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;


/**
 * Describe:
 * Created by AwenZeng on 2018/3/16.
 */

public class BowenApplication extends Application {

    private RefWatcher mRefWatcher;
    public static final String TAG = "BowenApplication";
    private static BowenApplication application;
    public static final boolean DEBUG = BuildConfig.DEBUG;
//    public static final boolean DEBUG = false;
    public static final Thread.UncaughtExceptionHandler sUncaughtExceptionHandler = Thread
        .getDefaultUncaughtExceptionHandler();

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        CommonLibApplication.init(this, DEBUG);
        CameraApplication.init(this,true);
        MobSDK.init(this);
        DataBaseManager.init(this);
        if (DEBUG) {
            MobclickAgent.setDebugMode(true);//友盟测试模式
            mRefWatcher =  LeakCanary.install(this);
        }
        createShortCut();
        //恢复异常捕获的功能，第三方的sdk可能截断捕获功能导致crash等报错日志不能打印 by zzp
        Thread.setDefaultUncaughtExceptionHandler(BowenApplication.sUncaughtExceptionHandler);
    }



    public static Context getBoWenAppContext() {
        return application.getApplicationContext();
    }

    public static BowenApplication getBoWenApplication() {
        return application;
    }

    /**
     * 创建快捷图标
     */
    private void createShortCut() {
        if (!DataCacheUtil.getInstance().getBoolean(DataCacheUtil.FIRST_START_APP, false)) {//第一次启动的时候创建
            Intent intent = new Intent();
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName component = new ComponentName(this, SplashActivity.class);
            i.setComponent(component);
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
            sendBroadcast(intent);
        }
    }
}
