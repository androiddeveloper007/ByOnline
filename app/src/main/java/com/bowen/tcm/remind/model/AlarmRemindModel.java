package com.bowen.tcm.remind.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmId;
import com.bowen.tcm.common.bean.network.AlarmInfo;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.AlarmManagerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Describe: 闹钟信息管理类
 * Created by AwenZeng on 2018/4/2.
 */

public class AlarmRemindModel extends BaseModel {

    public AlarmRemindModel(Context mContext) {
        super(mContext);
    }

    /**
     * 转换数据
     *
     * @param addAlarmInfo
     * @return
     */
    public Alarm copyToAlarm(AddAlarmInfo addAlarmInfo) {
        Alarm alarm = new Alarm();
        if (EmptyUtils.isNotEmpty(addAlarmInfo)) {
            alarm.setRemindTime(addAlarmInfo.getRemindTime());
            alarm.setRemindDate(addAlarmInfo.getRemindDate());
            alarm.setDrugCycle(addAlarmInfo.getRepeatPeriod());
            alarm.setRemindFamilyType(addAlarmInfo.getRemindPersonType());
            alarm.setRemindUserId(addAlarmInfo.getRemindPersonId());
            alarm.setRingtone(addAlarmInfo.getRemindRingUri());
            alarm.setIsShockBool(addAlarmInfo.isShake());
            alarm.setEmrMedicineList(addAlarmInfo.getDrugList());
        }
        return alarm;
    }


    public void setAlarm(Alarm alarm) {
        String[] times = DateUtil.date2String(alarm.getRemindTime(), DateUtil.DEFAULT_FORMAT_HOUR).toString().split(":");
        int soundOrVibrator;
        boolean isHaveRing = EmptyUtils.isNotEmpty(alarm.getRingtone());
        if (isHaveRing && alarm.isIsShockBool()) {
            soundOrVibrator = Constants.TYPE_ALARM_SOUNDVIBRATOR;
        } else if (isHaveRing && !alarm.isIsShockBool()) {
            soundOrVibrator = Constants.TYPE_ALARM_SOUND;
        } else if (!isHaveRing && alarm.isIsShockBool()) {
            soundOrVibrator = Constants.TYPE_ALARM_VIBRATOR;
        } else {
            soundOrVibrator = Constants.TYPE_ALARM_NOTHING;
        }
        AlarmManagerUtil.setAlarm(mContext, DateUtil.date2String(alarm.getRemindDate(), DateUtil.DEFAULT_FORMAT_DATE),
                Integer.parseInt(alarm.getDrugCycle()), Integer.parseInt(times[0]), Integer.parseInt(times[1]),
                alarm.getRemindId(), alarm, soundOrVibrator, alarm.getRingtone());
    }

    /**
     * 保存闹钟信息
     *
     * @param alarmInfo
     * @param mListener
     */
    public void saveAlarmRemindInfo(AddAlarmInfo alarmInfo, final HttpTaskCallBack<AlarmId> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("remindTime", alarmInfo.getRemindTime());
        networkRequest.setParam("remindDate", alarmInfo.getRemindDate());
        networkRequest.setParam("drugCycle", alarmInfo.getRepeatPeriod());
        networkRequest.setParam("ringtone", alarmInfo.getRemindRingUri());
        networkRequest.setParam("isShock", alarmInfo.isShake() ? 1 : 0);//震动 1：开 0:关
        networkRequest.setParam("remindUserId", alarmInfo.getRemindPersonId());
        networkRequest.setParam("remindFamilyType", alarmInfo.getRemindPersonType());
        networkRequest.setParam("status", alarmInfo.isStartAlarm() ? 1 : 0);//开启 1：开启 0：关闭
        try {
            networkRequest.setParam("emrMedicineList", URLEncoder.encode(GsonUtil.toJson(alarmInfo.getDrugList()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        networkRequest.requestSRV(HttpContants.CMD_SAVE_ALARM_REMIND, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<AlarmId> result = new HttpResult<AlarmId>();
                result.copy(httpResult);
                AlarmId alarmId = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), AlarmId.class);
                if (EmptyUtils.isNotEmpty(alarmId)) {
                    result.setData(alarmId);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<AlarmId> result = new HttpResult<AlarmId>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 更新闹钟信息
     *
     * @param remindId
     * @param alarmInfo
     * @param mListener
     */
    public void updateAlarmRemindInfo(String remindId, AddAlarmInfo alarmInfo, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("remindId", remindId);
        networkRequest.setParam("remindTime", alarmInfo.getRemindTime());
        networkRequest.setParam("remindDate", alarmInfo.getRemindDate());
        networkRequest.setParam("drugCycle", alarmInfo.getRepeatPeriod());
        networkRequest.setParam("ringtone", alarmInfo.getRemindRingUri());
        networkRequest.setParam("isShock", alarmInfo.isShake() ? 1 : 0);//震动 1：开 0:关
        networkRequest.setParam("remindUserId", alarmInfo.getRemindPersonId());
        networkRequest.setParam("remindFamilyType", alarmInfo.getRemindPersonType());
        networkRequest.setParam("status", alarmInfo.isStartAlarm() ? 1 : 0);//开启 1：开启 0：关闭
        try {
            networkRequest.setParam("emrMedicineList", URLEncoder.encode(GsonUtil.toJson(alarmInfo.getDrugList()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        networkRequest.requestSRV(HttpContants.CMD_UPDATE_ALARM_REMIND, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onSuccess(httpResult);
                }

            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onFail(httpResult);
                }
            }
        });
    }

    /**
     * 更新闹钟状态，是否开启
     *
     * @param remindId
     * @param isStartAlarm
     * @param mListener
     */
    public void updateAlarmStatus(String remindId, final boolean isStartAlarm, final HttpTaskCallBack<Alarm> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("remindId", remindId);
        networkRequest.setParam("status", isStartAlarm ? 1 : 0);//开启 1：开启 0：关闭
        networkRequest.requestSRV(HttpContants.CMD_UPDATE_ALARM_STATUS, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Alarm> result = new HttpResult<Alarm>();
                result.copy(httpResult);
                try {
                    if(EmptyUtils.isEmpty(httpResult.getData())){
                         return;
                    }
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Alarm alarm = GsonUtil.fromJson(jsonObject.optString("remindMedicine"), Alarm.class);
                    if (alarm != null) {
                        result.setData(alarm);
                    }
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Alarm> result = new HttpResult<Alarm>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 删除闹钟
     *
     * @param remindId
     * @param mListener
     */
    public void deleteAlarm(String remindId, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("remindId", remindId);
        networkRequest.requestSRV(HttpContants.CMD_DELETE_ALARM_REMIND, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onSuccess(httpResult);
                }

            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onFail(httpResult);
                }
            }
        });
    }

    /**
     * 获取闹钟列表
     *
     * @param pageNo
     * @param pageSize
     * @param mListener
     */
    public void getAlarmList(int pageNo, int pageSize, final HttpTaskCallBack<AlarmInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_ALARM_REMIND_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<AlarmInfo> result = new HttpResult<AlarmInfo>();
                result.copy(httpResult);
                AlarmInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), AlarmInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<AlarmInfo> result = new HttpResult<AlarmInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}
