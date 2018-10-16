package com.bowen.tcm.inquiry.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.AppiontmentPeriodInfo;
import com.bowen.tcm.common.bean.network.AppointmentInfo;
import com.bowen.tcm.common.bean.network.OutpatientAppointmentRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:搜名医
 * Created by zzp on 2018/7/10.
 */

public class OutpatientAppointmentModel extends BaseModel {

    public OutpatientAppointmentModel(Context mContext) {
        super(mContext);
    }

    /**
     * 获取医生的预约信息
     * @param doctorId
     * @param weekStatus 1：上一周，2当前周，3下一周
     * @param mListener
     */
    public void getDoctorAppointmentInfo(String doctorId,String weekStatus,final HttpTaskCallBack<ArrayList<AppointmentInfo>> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.setParam("weekStatus", weekStatus);
        networkRequest.requestSRV(HttpContants.CMD_GET_DOCTOR_APPOINTMENT_INFO, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<AppointmentInfo>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<ArrayList<AppointmentInfo>>() {
                    }.getType();
                    ArrayList<AppointmentInfo> appointmentList = GsonUtil.getGson().fromJson(jsonObject.getString("appointmentList"), typelist);
                    if (EmptyUtils.isNotEmpty(appointmentList)) {
                        result.setData(appointmentList);
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
                HttpResult<ArrayList<AppointmentInfo>> result = new HttpResult<ArrayList<AppointmentInfo>>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 获取预约医生的时间段
     * @param doctorId
     * @param date
     * @param type 1:上午 2：下午 3：晚上
     * @param mListener
     */
    public void getAppointmentPeriod(String doctorId,long date,int type,final HttpTaskCallBack<AppiontmentPeriodInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.setParam("appointmentDate", date);
        networkRequest.setParam("type", type);
        networkRequest.requestSRV(HttpContants.CMD_GET_DOCTOR_APPIONTMENT_PERIOD, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<AppiontmentPeriodInfo> result = new HttpResult<AppiontmentPeriodInfo>();
                result.copy(httpResult);
                AppiontmentPeriodInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), AppiontmentPeriodInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<AppiontmentPeriodInfo> result = new HttpResult<AppiontmentPeriodInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

}