package com.bowen.tcm.remind.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.remind.activity.ClockAlarmActivity;
import com.bowen.tcm.common.util.AlarmManagerUtil;

/**
 * Created by AwenZeng on 2018/05/29
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(AlarmManagerUtil.ALARM_BUNDLE);
        Alarm alarm = (Alarm) bundle.getSerializable(AlarmManagerUtil.ALARM_INFO);
        String soundUri = intent.getStringExtra(AlarmManagerUtil.ALARM_RING_SOUND_URI);
        long intervalMillis = intent.getLongExtra(AlarmManagerUtil.ALARM_INTERVAL, 0);
        if (intervalMillis != 0) {
            AlarmManagerUtil.setAlarmTime(context,alarm, intent);
        }
        int flag = intent.getIntExtra(AlarmManagerUtil.ALARM_RING_WAY, 0);
        Bundle putBundle = new Bundle();
        Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
        putBundle.putSerializable(AlarmManagerUtil.ALARM_INFO, alarm);
        clockIntent.putExtra(AlarmManagerUtil.ALARM_BUNDLE, putBundle);
        clockIntent.putExtra(AlarmManagerUtil.ALARM_RING_WAY, flag);
        clockIntent.putExtra(AlarmManagerUtil.ALARM_RING_SOUND_URI,soundUri);
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(clockIntent);
    }


}
