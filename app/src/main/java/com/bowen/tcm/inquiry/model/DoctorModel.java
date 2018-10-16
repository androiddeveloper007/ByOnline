package com.bowen.tcm.inquiry.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.Appointment;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.AppiontmentPeriodInfo;
import com.bowen.tcm.common.bean.network.AppointmentInfo;
import com.bowen.tcm.common.bean.network.BannerInfo;
import com.bowen.tcm.common.bean.network.Department;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.bean.network.DoctorInfo;
import com.bowen.tcm.common.bean.network.DoctorRank;
import com.bowen.tcm.common.bean.network.FindDoctorInfo;
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class DoctorModel extends BaseModel {

    public DoctorModel(Context mContext) {
        super(mContext);
    }
    /**
     * 获取寻医首页数据
     * @param mListener
     */
    public void getHomeFindDoctorData(final HttpTaskCallBack<FindDoctorInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_GET_HOME_FIND_DOCTOR, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<FindDoctorInfo> result = new HttpResult<FindDoctorInfo>();
                result.copy(httpResult);
                FindDoctorInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), FindDoctorInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<FindDoctorInfo> result = new HttpResult<FindDoctorInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }



    public void findDoctorList(int pageNo, int pageSize, SearchField searchField,final HttpTaskCallBack<DoctorInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        if(EmptyUtils.isNotEmpty(searchField)){
            networkRequest.setParam("proviceCode", searchField.getProviceCode());
            networkRequest.setParam("cityCode", searchField.getCityCode());
            networkRequest.setParam("areaCode", searchField.getAreaCode());
            networkRequest.setParam("departmentsId", searchField.getDepartmentsId());
            networkRequest.setParam("diseaseId", searchField.getDiseaseId());
            networkRequest.setParam("position", searchField.getPosition());
            networkRequest.setParam("searchInfo", searchField.getSearchInfo());
            networkRequest.setParam("sortType", searchField.getSortType());//如果按照好评率搜索,传入1，回复率传2,价格高低传入3搜索
        }
        networkRequest.requestSRV(HttpContants.CMD_FIND_DOCTOR_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<DoctorInfo> result = new HttpResult<DoctorInfo>();
                result.copy(httpResult);
                DoctorInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), DoctorInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<DoctorInfo> result = new HttpResult<DoctorInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void findDoctorListByPreId(int pageNo, int pageSize, String preId, final HttpTaskCallBack<DoctorInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.setParam("prescriptionId", preId);
        networkRequest.requestSRV(HttpContants.CMD_FIND_DOCTOR_LIST_BY_PRE_ID, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<DoctorInfo> result = new HttpResult<DoctorInfo>();
                result.copy(httpResult);
                DoctorInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), DoctorInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<DoctorInfo> result = new HttpResult<DoctorInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取Doctor详情
     * @param doctorId
     * @param mListener
     */
    public void getDoctorDetail(String doctorId,final HttpTaskCallBack<DoctorDetail> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.requestSRV(HttpContants.CMD_GET_DOCTOR_DETAIL, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<DoctorDetail> result = new HttpResult<DoctorDetail>();
                result.copy(httpResult);
                DoctorDetail data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), DoctorDetail.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<DoctorDetail> result = new HttpResult<DoctorDetail>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取Doctor详情
     * @param doctorId
     * @param mListener
     */
    public void checkImmediateConsult(String doctorId,final HttpTaskCallBack<Doctor> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.requestSRV(HttpContants.CMD_CHECK_IMMEDIATE_CONSULT, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Doctor> result = new HttpResult<Doctor>();
                result.copy(httpResult);
                Doctor data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Doctor.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Doctor> result = new HttpResult<Doctor>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 改变关注状态
     * @param doctorId
     * @param attentionStatus
     * @param mListener
     */
    public void changeAttentionStatus(String doctorId,boolean attentionStatus,final HttpTaskCallBack<Boolean> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.setParam("isFen", attentionStatus);
        networkRequest.requestSRV(HttpContants.CMD_CHANGE_ATTENTION_STATUS, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Boolean> result = new HttpResult<Boolean>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    boolean isSuccess = jsonObject.optBoolean("isSuccess",false);
                    result.setData(isSuccess);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Boolean> result = new HttpResult<Boolean>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取科室列表
     */
    public void getDepartmentsList() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.requestSRV(HttpContants.CMD_GET_DEPARTMENT_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<Department>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<ArrayList<Department>>() {
                    }.getType();
                    ArrayList<Department> departments = GsonUtil.getGson().fromJson(jsonObject.getString("depList"), typelist);
                    if (EmptyUtils.isNotEmpty(departments)) {
                        Department department = new Department();
                        department.setDepartmentsName("全部");
                        department.setDepartmentsId("");
                        departments.add(0,department);
                        AppConfigInfo.getInstance().setDepartmentList(departments);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {

            }
        });
    }

    /**
     * 获取医生职称
     */
    public void getDoctorRankList() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.requestSRV(HttpContants.CMD_GET_DOCTOR_RANK_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<DoctorRank>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<ArrayList<DoctorRank>>() {
                    }.getType();
                    ArrayList<DoctorRank> doctorRanks = GsonUtil.getGson().fromJson(jsonObject.getString("voList"), typelist);
                    if (EmptyUtils.isNotEmpty(doctorRanks)) {
                        DoctorRank doctorRank = new DoctorRank();
                        doctorRank.setPosition("全部");
                        doctorRank.setCode("");
                        doctorRanks.add(0,doctorRank);
                        AppConfigInfo.getInstance().setDoctorRankList(doctorRanks);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {

            }
        });
    }
}
