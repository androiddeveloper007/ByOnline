package com.bowen.commonlib;

import android.app.Application;
import android.content.Context;

import com.bowen.commonlib.http.HttpContants;
import com.bowen.commonlib.utils.AutoScreenUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by AwenZeng on 2017/3/30.
 */

public class CommonLibApplication {

    private static Application application;

    public static boolean DEBUG = true;

    public static void init(Application app,boolean debug){
        application = app;
        DEBUG = debug;
        Logger.init(getCommonLibContext().getString(R.string.app_name));
        AutoScreenUtils.AdjustDensity(app);
    }

    public static Context getCommonLibContext(){
        return application.getApplicationContext();
    }

    public static Application getCommonLibApplication(){
        return application;
    }

    public static  void setHttpUrl(String httpUrl){
        HttpContants.REQUEST_URL = httpUrl;
    }
}
