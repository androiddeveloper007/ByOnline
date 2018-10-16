package com.bowen.tcm.mine.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.main.model.DataUploadModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/3.
 */

public class AddFamilyMemberModel extends BaseModel {

    private DataUploadModel mPhotoUploadModel;

    public AddFamilyMemberModel(Context mContext) {
        super(mContext);
        mPhotoUploadModel = new DataUploadModel(mContext);
    }

    public void saveFamilyMember(String mRelationShipStr, String mNickNameStr, String sex, String birthday, ArrayList<String> pics,final HttpTaskCallBack<List<FamilyMember>> mListener) {
        mPhotoUploadModel.uploadFamilyMemberData(mRelationShipStr, mNickNameStr,
                Constants.TYPE_UPLOAD_PHOTO_FAMILY_MEMBER, sex, birthday, pics, new HttpTaskCallBack() {
                    @Override
                    public void onSuccess(HttpResult result) {
                        if(mListener!=null){
                            mListener.onSuccess(result);
                        }
                    }

                    @Override
                    public void onFail(HttpResult result) {
                        if(mListener!=null){
                            mListener.onFail(result);
                        }
                    }
                });
    }

    /**
     * 添加家庭成员，并上传头像
     */
    public void saveFamilyMember(String familyType, String familyNickname, String sex, String birthday, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("familyNickname", familyNickname);
        networkRequest.setParam("familyType", familyType);
        networkRequest.setParam("sex", sex);
        networkRequest.setParam("birthday", DateUtil.dateToLong(birthday));
        networkRequest.requestSRV(HttpContants.CMD_SAVE_USER_FAMILY_INFO, "数据上传中，请稍后...", new HttpTaskCallBack<Object>() {
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

    public void updateFamilyMember(String familyId, String familyType, String familyNickname, String familyPhone,String sex, String birthday, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("familyId", familyId);
        networkRequest.setParam("sex", sex);
        networkRequest.setParam("birthday", birthday);
        if(EmptyUtils.isNotEmpty(familyType)){
            networkRequest.setParam("familyType",familyType);
        }
        if(EmptyUtils.isNotEmpty(familyNickname)){
            networkRequest.setParam("familyNickname",familyNickname);
        }
        if(EmptyUtils.isNotEmpty(familyPhone)){
            networkRequest.setParam("familyPhone", familyPhone);
        }
        networkRequest.requestSRV(HttpContants.CMD_UPDATE_USER_FAMILY_INFO, "", new HttpTaskCallBack<Object>() {
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

}
