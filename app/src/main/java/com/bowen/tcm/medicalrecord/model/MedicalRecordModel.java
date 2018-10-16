package com.bowen.tcm.medicalrecord.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.AddCaseInfo;
import com.bowen.tcm.common.bean.network.Category;
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.common.bean.network.VisiablePerson;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/3.
 */

public class MedicalRecordModel extends BaseModel {

    public MedicalRecordModel(Context mContext) {
        super(mContext);
    }

    /**
     * 获取家庭成员的病历记录
     * @param pageNo
     * @param pageSize
     * @param familyId
     * @param mListener
     */
    public void getFamilyMembersMedicalRecord(int pageNo, int pageSize,String familyId, final HttpTaskCallBack<HomeMedicalRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("familyId", familyId);
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_MEDICAL_RECORD_list, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<HomeMedicalRecord> result = new HttpResult<HomeMedicalRecord>();
                result.copy(httpResult);
                HomeMedicalRecord data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), HomeMedicalRecord.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<HomeMedicalRecord> result = new HttpResult<HomeMedicalRecord>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取病程记录
     * @param pageNo
     * @param pageSize
     * @param courseId 病程id
     * @param mListener
     */
    public void getMedicalAllCourse(int pageNo, int pageSize,String courseId, final HttpTaskCallBack<HomeMedicalRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("courseId", courseId);
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_MEDICAL_PROGRESS_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<HomeMedicalRecord> result = new HttpResult<HomeMedicalRecord>();
                result.copy(httpResult);
                HomeMedicalRecord data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), HomeMedicalRecord.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<HomeMedicalRecord> result = new HttpResult<HomeMedicalRecord>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void addMedicalRecordDetail(AddCaseInfo addCaseInfo, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("userPhone", UserInfo.getInstance().getUserMobile());
        networkRequest.setParam("courseId", addCaseInfo.getCourseId());
        networkRequest.setParam("familyId", addCaseInfo.getFamilyId());
        networkRequest.setParam("caseName", addCaseInfo.getCaseName());
        networkRequest.setParam("caseDetails", addCaseInfo.getCaseDetails());
        networkRequest.setParam("illTime", addCaseInfo.getIllTime());
        networkRequest.setParam("doctorTime", addCaseInfo.getDiagnoseTime());
        networkRequest.setParam("doctorStage", addCaseInfo.getDiagnoseStage());
        networkRequest.setParam("doctorResult", addCaseInfo.getDiagnoseResult());
        networkRequest.setParam("clinicName", addCaseInfo.getDiagnoseClinic());
        networkRequest.setParam("doctorName", addCaseInfo.getDiagnoseDoctorName());
        networkRequest.setParam("seerPhone", addCaseInfo.getSeePhone());
        networkRequest.requestSRV(HttpContants.CMD_ADD_MEDICAL_RECORD_INFO, "数据上传中，请稍后...", new HttpTaskCallBack<Object>() {
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

    public void updateMedicalRecordDetail(AddCaseInfo addCaseInfo, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("caseId", addCaseInfo.getCaseId());
        networkRequest.setParam("caseName", addCaseInfo.getCaseName());
        networkRequest.setParam("caseDetails", addCaseInfo.getCaseDetails());
        networkRequest.setParam("illTime", addCaseInfo.getIllTime());
        networkRequest.setParam("doctorTime", addCaseInfo.getDiagnoseTime());
        networkRequest.setParam("doctorStage", addCaseInfo.getDiagnoseStage());
        networkRequest.setParam("doctorResult", addCaseInfo.getDiagnoseResult());
        networkRequest.setParam("clinicName", addCaseInfo.getDiagnoseClinic());
        networkRequest.setParam("doctorName", addCaseInfo.getDiagnoseDoctorName());
        networkRequest.setParam("seerPhone", addCaseInfo.getSeePhone());

        networkRequest.requestSRV(HttpContants.CMD_UPDATE_MEDICAL_RECORD_INFO, "数据上传中，请稍后...", new HttpTaskCallBack<Object>() {
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


    public void deleteMedicalRecord(String caseId,final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("caseId", caseId);
        networkRequest.requestSRV(HttpContants.CMD_DELETE_MEDICAL_RECORD_INFO, "", new HttpTaskCallBack<Object>() {
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
    public void getDiseaseNameCategory() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_GET_DISEASE_NAME, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<Category>>() {
                    }.getType();
                    List<Category> list = GsonUtil.getGson().fromJson(jsonObject.getString("voList"), typelist);
                    if (EmptyUtils.isNotEmpty(list)) {
                        AppConfigInfo.getInstance().setDiseaseNameList(list);
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

    public void getDiagnoseStage() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_GET_DIAGNOSE_STAGE, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<Category>>() {
                    }.getType();
                    List<Category> list = GsonUtil.getGson().fromJson(jsonObject.getString("voList"), typelist);
                    if (EmptyUtils.isNotEmpty(list)) {
                        AppConfigInfo.getInstance().setDiagnoseStageList(list);
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

    public void getVisiablePerson() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_GET_VISIABLE_PERSON, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<VisiablePerson>>() {
                    }.getType();
                    List<VisiablePerson> list = GsonUtil.getGson().fromJson(jsonObject.getString("phoneList"), typelist);
                    if (EmptyUtils.isNotEmpty(list)) {
                        AppConfigInfo.getInstance().setVisiablePersonList(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult){
            }
        });
    }

}
