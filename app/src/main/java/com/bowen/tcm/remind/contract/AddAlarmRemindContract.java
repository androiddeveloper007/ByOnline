package com.bowen.tcm.remind.contract;

import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmId;
import com.bowen.tcm.common.bean.network.FamilyMember;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface AddAlarmRemindContract {

    interface View {
        void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list);
        void onSaveAlarmSuccess(AlarmId alarmId);
    }

    interface Presenter{
        void getFamilyMembers();
        void saveAlarmRemindInfo(AddAlarmInfo alarmInfo);
         Alarm copyToAlarm(AddAlarmInfo alarmInfo);
    }
}
