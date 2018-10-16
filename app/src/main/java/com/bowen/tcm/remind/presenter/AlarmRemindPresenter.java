package com.bowen.tcm.remind.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmInfo;
import com.bowen.tcm.remind.contract.AlarmRemindContract;
import com.bowen.tcm.common.util.AlarmManagerUtil;
import com.bowen.tcm.remind.model.AlarmRemindModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class AlarmRemindPresenter extends BasePresenter implements AlarmRemindContract.Presenter {

    private AlarmRemindModel mAlarmRemindModel;
    private AlarmRemindContract.View mView;

    public AlarmRemindPresenter(Context mContext, AlarmRemindContract.View view) {
        super(mContext);
        mView = view;
        mAlarmRemindModel = new AlarmRemindModel(mContext);
    }

    public void setAlarm(Alarm alarm){
        mAlarmRemindModel.setAlarm(alarm);
    }

    public void cancelAlarm(int alarmId){
        AlarmManagerUtil.cancelAlarm(mContext,AlarmManagerUtil.ALARM_ACTION,alarmId);
    }

    public void changeAlarmStatus(String remindId, boolean isOpen){
        mAlarmRemindModel.updateAlarmStatus(remindId, isOpen, new HttpTaskCallBack<Alarm>() {
            @Override
            public void onSuccess(HttpResult<Alarm> result) {
                mView.onUpdateAlarmStatusSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<Alarm> result) {

            }
        });
    }

    @Override
    public void getAlarmRemindList(int pageNo, int pageSize) {
        mAlarmRemindModel.getAlarmList(pageNo, pageSize, new HttpTaskCallBack<AlarmInfo>() {

            @Override
            public void onSuccess(HttpResult<AlarmInfo> result) {
               mView.onGetAlarmRemindListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<AlarmInfo> result) {

            }
        });
    }

    @Override
    public void deleteAlarm(final Alarm alarm) {
        mAlarmRemindModel.deleteAlarm(alarm.getRemindId(), new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onDeleteAlarmSuccess(alarm.getId());
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }
}
