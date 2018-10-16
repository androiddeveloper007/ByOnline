package com.bowen.tcm.mine.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.Category;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class FamilyMemberModel extends BaseModel {

    public FamilyMemberModel(Context mContext) {
        super(mContext);
    }

    public void getFamilyMembers(final HttpTaskCallBack<ArrayList<FamilyMember>> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_GET_ALL_FAMILY_MEMBERS, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<FamilyMember>> result = new HttpResult<ArrayList<FamilyMember>>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<FamilyMember>>() {
                    }.getType();
                    ArrayList<FamilyMember> list = GsonUtil.getGson().fromJson(jsonObject.getString("userFamilyList"), typelist);
                    if (EmptyUtils.isNotEmpty(list)) {
                        result.setData(list);
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
                HttpResult<ArrayList<FamilyMember>> result = new HttpResult<ArrayList<FamilyMember>>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void deleteFamilyMember(String familyId, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("familyId", familyId);
        networkRequest.requestSRV(HttpContants.CMD_DELETE_USER_FAMILY_INFO, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<Object>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }

            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<Object>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void getFamilyUserRelationShip() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_FAMILY_USER_RELATIONSHIP, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<Category>>() {
                    }.getType();
                    List<Category> list = GsonUtil.getGson().fromJson(jsonObject.getString("voList"), typelist);
                    if (EmptyUtils.isNotEmpty(list)) {
                        AppConfigInfo.getInstance().setFamilyUserRelationList(list);
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

    public void updateFamilyMember(String familyId, String familyType, String familyNickname, String familyPhone, String sex, String birthday, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("familyId", familyId);
        if (EmptyUtils.isNotEmpty(familyType)){
            networkRequest.setParam("familyType", familyType);
        }
        if (EmptyUtils.isNotEmpty(familyNickname)){
            networkRequest.setParam("familyNickname", familyNickname);
        }
        if (EmptyUtils.isNotEmpty(familyPhone)){
            networkRequest.setParam("familyPhone", familyPhone);
        }
        if (EmptyUtils.isNotEmpty(sex)){
            networkRequest.setParam("sex", sex);
        }
        if (EmptyUtils.isNotEmpty(birthday)){
            networkRequest.setParam("birthday", DateUtil.dateToLong(birthday));
        }

        networkRequest.requestSRV(HttpContants.CMD_UPDATE_USER_FAMILY_INFO, "", new HttpTaskCallBack<List<PhotoFile>>() {
            @Override
            public void onSuccess(HttpResult<List<PhotoFile>> httpResult) {
                HttpResult<List<PhotoFile>> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<List<PhotoFile>> httpResult) {
                HttpResult<Object> result = new HttpResult<Object>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}
