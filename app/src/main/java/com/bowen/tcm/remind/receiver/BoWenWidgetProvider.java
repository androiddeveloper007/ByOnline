package com.bowen.tcm.remind.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.main.activity.MainActivity;
import com.bowen.tcm.remind.activity.AlarmRemindActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AwenZeng on 2018/5/17.
 * <p>
 * 通过AppWidget实现进程保护
 */
public class BoWenWidgetProvider extends AppWidgetProvider {
    private static Timer myTimer;
    private static int index = 0;

    //定义我们要发送的事件
    private final String broadCastString = "com.bowen.tcm.appWidgetUpdate";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        DataCacheUtil.getInstance().putBoolean(DataCacheUtil.IS_ADD_REMIND_APPWIDGET,false);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //在插件被创建的时候这里会被调用， 所以我们在这里开启一个timer 每秒执行一次
        MyTask mMyTask = new MyTask(context);
        myTimer = new Timer();
        myTimer.schedule(mMyTask, 1000, 5000);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bowen_widget);
        Intent skipIntent = new Intent();
        if(LoginStatusUtil.getInstance().isLogin()){
            skipIntent.setClass(context,AlarmRemindActivity.class);
        }else{
            skipIntent.setClass(context,MainActivity.class);
        }
        PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.mWidgetIconImg, pi);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        DataCacheUtil.getInstance().putBoolean(DataCacheUtil.IS_ADD_REMIND_APPWIDGET,true);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //当判断到是该事件发过来时， 我们就获取插件的界面， 然后将index自加后传入到textview中
        if (intent.getAction().equals(broadCastString)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bowen_widget);
            Intent skipIntent = new Intent();
            if(LoginStatusUtil.getInstance().isLogin()){
                skipIntent.setClass(context,AlarmRemindActivity.class);
            }else{
                skipIntent.setClass(context,MainActivity.class);
            }
            PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.mWidgetIconImg, pi);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, BoWenWidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, remoteViews);

//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bowen_widget);
////            remoteViews.setTextViewText(R.id.mWidgetDesTv, Integer.toString(index));
//            Intent jumpIntent = new Intent(context, AlarmRemindActivity.class);
//            // 用Intent实例化一个PendingIntent
//            PendingIntent Pprevintent=PendingIntent.getService(context, 0, jumpIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            // 给RemoteView上的Button设置按钮事件
//            remoteViews.setOnClickPendingIntent(R.id.mWidgetIconImg, Pprevintent);
//            //将该界面显示到插件中
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            ComponentName componentName = new ComponentName(context, BoWenWidgetProvider.class);
//            appWidgetManager.updateAppWidget(componentName, remoteViews);
//
//            DataCacheUtil.getInstance().putBoolean(DataCacheUtil.IS_ADD_REMIND_APPWIDGET,true);

            Log.i("AlarmReceiver", "刷新界面：" + index);
        }
        super.onReceive(context, intent);
    }


    class MyTask extends TimerTask {

        private Context mcontext = null;
        private Intent intent = null;

        public MyTask(Context context) {

            //新建一个要发送的Intent
            mcontext = context;
            intent = new Intent();
            intent.setAction(broadCastString);
        }

        @Override
        public void run() {
            //发送广播(由onReceive来接收)
            mcontext.sendBroadcast(intent);
        }

    }
}
