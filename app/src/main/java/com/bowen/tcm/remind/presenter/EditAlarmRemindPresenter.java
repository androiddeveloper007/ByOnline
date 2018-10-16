package com.bowen.tcm.remind.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.mine.model.FamilyMemberModel;
import com.bowen.tcm.remind.contract.EditAlarmRemindContract;
import com.bowen.tcm.remind.model.AlarmRemindModel;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class EditAlarmRemindPresenter extends BasePresenter implements EditAlarmRemindContract.Presenter {

    private AlarmRemindModel mAlarmRemindModel;
    private FamilyMemberModel mFamilyMemberModel;
    private EditAlarmRemindContract.View mView;

    public EditAlarmRemindPresenter(Context mContext, EditAlarmRemindContract.View view) {
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
    public void updateAlarmRemindInfo(String remindId, AddAlarmInfo alarmInfo) {
        mAlarmRemindModel.updateAlarmRemindInfo(remindId, alarmInfo, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onUpdateAlarmSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

}
