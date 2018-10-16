package com.bowen.tcm.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.model.Constants;

import java.util.Calendar;

/**
 * Created by AwenZeng on 2018/5/29.
 */
public class AlarmManagerUtil {
    public static final String ALARM_ACTION = "com.bowen.tcm.alarm";

    public static final String ALARM_BUNDLE = "alarmBundle";
    public static final String ALARM_INTERVAL = "intervalMillis";
    public static final String ALARM_INFO = "alarmInfo";//闹钟信息
    public static final String ALARM_ID = "id";
    public static final String ALARM_RING_WAY = "soundOrVibrator";
    public static final String ALARM_RING_SOUND_URI = "soundUri";

    public static void setAlarmTime(Context context, Alarm alarm, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        String[] times = DateUtil.date2String(alarm.getRemindTime(), DateUtil.DEFAULT_FORMAT_HOUR).toString().split(":");
        int hour, minute;
        hour = Integer.parseInt(times[0]);
        minute = Integer.parseInt(times[1]);

        //当天日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 10);

        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        long interval = intent.getLongExtra(ALARM_INTERVAL, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, calculateResetTime(Integer.parseInt(alarm.getDrugCycle()), calendar.getTimeInMillis()), interval, sender);
        }
    }


    /**
     * 设置重复闹钟
     * @param alarmType 闹钟类型
     * @param dayTime
     * @return
     */
    private static long calculateResetTime(int alarmType, long dayTime) {
        long time = 0;
        switch (alarmType) {
            case Constants.TYPE_REMIND_REPEAT_ONETIME://一次
                time = dayTime;
                break;
            case Constants.TYPE_REMIND_REPEAT_EVERYDAY://每天
                time = dayTime + 24 * 3600 * 1000;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL1DAY://间隔1天
                time = dayTime + 24 * 3600 * 1000 * 2;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL2DAY://间隔2天
                time = dayTime + 24 * 3600 * 1000 * 3;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL3DAY://间隔3天
                time = dayTime + 24 * 3600 * 1000 * 4;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL4DAY://间隔4天
                time = dayTime + 24 * 3600 * 1000 * 5;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL5DAY://间隔5天
                time = dayTime + 24 * 3600 * 1000 * 6;
                break;
            case Constants.TYPE_REMIND_REPEAT_EVERYWEEK://每周
                time = dayTime + 24 * 3600 * 1000 * 7;
                break;
            case Constants.TYPE_REMIND_REPEAT_EVERYMONTH://每月
                time = dayTime + 24 * 3600 * 1000 * 30;
                break;
        }
        return time;
    }


    /**
     * 取消闹钟
     *
     * @param context
     * @param action
     * @param id
     */
    public static void cancelAlarm(Context context, String action, int id) {
        Intent intent = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * 设置闹钟
     *
     * @param context
     * @param startDate       闹钟开始日期
     * @param alarmType       闹钟重复类型
     * @param hour            小时
     * @param minute          分
     * @param id              闹钟ID
     * @param alarm           闹钟提示语
     * @param soundOrVibrator 闹钟提醒方式：震动或铃声
     * @param soundUri        铃声Uri地址
     */
    public static void setAlarm(Context context, String startDate, int alarmType, int hour, int minute, String id, Alarm alarm,
                                int soundOrVibrator, String soundUri) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //当天日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 10);

        //开始日期
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtil.getDateFormat(startDate));
        startCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 10);

        int intervalDays = DateUtil.getIntervalDays(startCalendar.getTime(), calendar.getTime());
        long intervalMillis = 0;
        switch (alarmType) {
            case Constants.TYPE_REMIND_REPEAT_ONETIME://一次
                intervalMillis = 0;
                break;
            case Constants.TYPE_REMIND_REPEAT_EVERYDAY://每天
                intervalMillis = 24 * 3600 * 1000;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL1DAY://间隔1天
                intervalMillis = 24 * 3600 * 1000 * 2;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL2DAY://间隔2天
                intervalMillis = 24 * 3600 * 1000 * 3;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL3DAY://间隔3天
                intervalMillis = 24 * 3600 * 1000 * 4;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL4DAY://间隔4天
                intervalMillis = 24 * 3600 * 1000 * 5;
                break;
            case Constants.TYPE_REMIND_REPEAT_INTERVAL5DAY://间隔5天
                intervalMillis = 24 * 3600 * 1000 * 6;
                break;
            case Constants.TYPE_REMIND_REPEAT_EVERYWEEK://每周
                intervalMillis = 24 * 3600 * 1000 * 7;
                break;
            case Constants.TYPE_REMIND_REPEAT_EVERYMONTH://每月
                intervalMillis = 24 * 3600 * 1000 * 30;
                break;
        }
        Bundle bundle = new Bundle();
        Intent intent = new Intent(ALARM_ACTION);
        bundle.putSerializable(ALARM_INFO, alarm);
        intent.putExtra(ALARM_BUNDLE, bundle);
        intent.putExtra(ALARM_INTERVAL, intervalMillis);
        intent.putExtra(ALARM_ID, id);
        intent.putExtra(ALARM_RING_WAY, soundOrVibrator);
        intent.putExtra(ALARM_RING_SOUND_URI, soundUri);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, calculateStartTime(alarmType, intervalDays, startCalendar.getTimeInMillis(),
                    calendar.getTimeInMillis()), intervalMillis, sender);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calculateStartTime(alarmType, intervalDays, startCalendar.getTimeInMillis(),
                    calendar.getTimeInMillis()), intervalMillis, sender);
        }
    }

    /**
     * 计算闹钟开始时间
     *
     * @param alarmType    闹钟类型
     * @param intervalDays 起始日期到今天日期的间隔天数
     * @param startTime    开始时间
     * @param dayTime      今天时间
     * @return 规则：
     * <p>
     * 1.设置闹钟起始时间大于等于今天时间，闹钟开始时间为起始时间
     * 2.设置闹钟起始时间小于今天时间：
     * (1).计算起始日期到今天日期的间隔天数，再根据闹钟类型，算出闹钟设置时间
     * (2).算出的闹钟设置时间再与系统当前时间做比较，如果大于等于，直接作为闹钟的起始时间，如果小于，再按照闹钟类型，算出闹钟设置时间
     * 3.设置闹钟
     */
    private static long calculateStartTime(int alarmType, int intervalDays, long startTime, long dayTime) {
        long time = 0;
        long currentTime = System.currentTimeMillis();
        if (startTime >= currentTime) {//开始时间大于今天时间，以开始时间为准
            time = startTime;
        } else {//开始日期小于今天日期，计算对应闹钟类型的开始时间
            switch (alarmType) {
                case Constants.TYPE_REMIND_REPEAT_ONETIME://一次
                    time = dayTime;
                    break;
                case Constants.TYPE_REMIND_REPEAT_EVERYDAY://每天
                    time = dayTime + 24 * 3600 * 1000;
                    break;
                case Constants.TYPE_REMIND_REPEAT_INTERVAL1DAY://间隔1天
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 2);
                    break;
                case Constants.TYPE_REMIND_REPEAT_INTERVAL2DAY://间隔2天
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 3);
                    break;
                case Constants.TYPE_REMIND_REPEAT_INTERVAL3DAY://间隔3天
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 4);
                    break;
                case Constants.TYPE_REMIND_REPEAT_INTERVAL4DAY://间隔4天
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 5);
                    break;
                case Constants.TYPE_REMIND_REPEAT_INTERVAL5DAY://间隔5天
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 6);
                    break;
                case Constants.TYPE_REMIND_REPEAT_EVERYWEEK://每周
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 7);
                    break;
                case Constants.TYPE_REMIND_REPEAT_EVERYMONTH://每月
                    time = dayTime + 24 * 3600 * 1000 * (intervalDays % 30);
                    break;
            }
            //算出的时间，小于今天现在的时间，根据闹钟类型加上对应的时间间隔
            if (time < currentTime) {
                switch (alarmType) {
                    case Constants.TYPE_REMIND_REPEAT_ONETIME://一次
                        time = dayTime;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_EVERYDAY://每天
                        time = dayTime + 24 * 3600 * 1000;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_INTERVAL1DAY://间隔1天
                        time = dayTime + 24 * 3600 * 1000 * 2;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_INTERVAL2DAY://间隔2天
                        time = dayTime + 24 * 3600 * 1000 * 3;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_INTERVAL3DAY://间隔3天
                        time = dayTime + 24 * 3600 * 1000 * 4;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_INTERVAL4DAY://间隔4天
                        time = dayTime + 24 * 3600 * 1000 * 5;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_INTERVAL5DAY://间隔5天
                        time = dayTime + 24 * 3600 * 1000 * 6;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_EVERYWEEK://每周
                        time = dayTime + 24 * 3600 * 1000 * 7;
                        break;
                    case Constants.TYPE_REMIND_REPEAT_EVERYMONTH://每月
                        time = dayTime + 24 * 3600 * 1000 * 30;
                        break;
                }
            }
        }
        return time;
    }
}
