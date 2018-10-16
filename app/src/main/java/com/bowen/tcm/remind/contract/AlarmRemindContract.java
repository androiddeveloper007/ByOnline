package com.bowen.tcm.remind.contract;

import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmInfo;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface AlarmRemindContract {

    interface View {
        void onGetAlarmRemindListSuccess(AlarmInfo info);
        void onDeleteAlarmSuccess(int alarmId);
        void onUpdateAlarmStatusSuccess(Alarm alarm);
    }

    interface Presenter{
        void getAlarmRemindList(int pageNo, int pageSize);
        void deleteAlarm(Alarm alarm);
    }
}
