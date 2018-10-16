package com.bowen.tcm.remind.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmId;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.mine.model.FamilyMemberModel;
import com.bowen.tcm.remind.contract.AddAlarmRemindContract;
import com.bowen.tcm.remind.model.AlarmRemindModel;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class AddAlarmRemindPresenter extends BasePresenter implements AddAlarmRemindContract.Presenter {

    private AlarmRemindModel mAlarmRemindModel;
    private FamilyMemberModel mFamilyMemberModel;
    private AddAlarmRemindContract.View mView;

    public AddAlarmRemindPresenter(Context mContext, AddAlarmRemindContract.View view) {
        super(mContext);
        mView = view;
        mAlarmRemindModel = new AlarmRemindModel(mContext);
        mFamilyMemberModel = new FamilyMemberModel(mContext);
    }

    @Override
    public Alarm copyToAlarm(AddAlarmInfo alarmInfo) {
        return mAlarmRemindModel.copyToAlarm(alarmInfo);
    }

    public void setAlarm(Alarm alarm){
        mAlarmRemindModel.setAlarm(alarm);
    }


    @Override
    public void getFamilyMembers() {
        mFamilyMemberModel.getFamilyMembers(new HttpTaskCallBack<ArrayList<FamilyMember>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<FamilyMember>> result) {
                mView.onGetFamilyMembersSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ArrayList<FamilyMember>> result) {

            }
        });
    }

    @Override
    public void saveAlarmRemindInfo(AddAlarmInfo alarmInfo) {
        mAlarmRemindModel.saveAlarmRemindInfo(alarmInfo, new HttpTaskCallBack<AlarmId>() {
            @Override
            public void onSuccess(HttpResult<AlarmId> result) {
                mView.onSaveAlarmSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<AlarmId> result) {

            }
        });
    }

}
