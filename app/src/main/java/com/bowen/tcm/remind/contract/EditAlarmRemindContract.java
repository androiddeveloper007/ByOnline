package com.bowen.tcm.remind.contract;

import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.FamilyMember;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface EditAlarmRemindContract {

    interface View {
        void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list);
        void onUpdateAlarmSuccess();
    }

    interface Presenter{
        void getFamilyMembers();
        void updateAlarmRemindInfo(String remindId, AddAlarmInfo alarmInfo);
        Alarm copyToAlarm(AddAlarmInfo alarmInfo);
    }
}
