package com.bowen.tcm.inquiry.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

/**
 * Describe:根据省市区搜索医馆分页(用户端)
 * Created by zhuzhipeng on 2018/7/9.
 */
public class FamousHospitalModel extends BaseModel {

    public FamousHospitalModel(Context mContext) {
        super(mContext);
    }

    public void getAllHospitalPage(int pageNo, int pageSize,String provinceCode, String cityCode,
                                   String areaCode, double longitude, double latitude, final HttpTaskCallBack<CliniclInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        if(!TextUtils.isEmpty(provinceCode)) networkRequest.setParam("provinceCode", provinceCode);
        if(!TextUtils.isEmpty(cityCode)) networkRequest.setParam("cityCode", cityCode);
        if(!TextUtils.isEmpty(areaCode)) networkRequest.setParam("areaCode", areaCode);
        if(EmptyUtils.isNotEmpty(longitude)) networkRequest.setParam("longitude", longitude);
        if(EmptyUtils.isNotEmpty(latitude)) networkRequest.setParam("latitude", latitude);
        networkRequest.requestSRV(HttpContants.CMD_GET_ALL_HOSPITAL_PAGE, new HttpTaskCallBack<CliniclInfo>() {
            @Override
            public void onSuccess(HttpResult<CliniclInfo> httpResult) {
                HttpResult<CliniclInfo> result = new HttpResult<>();
                result.copy(httpResult);
                CliniclInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), CliniclInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<CliniclInfo> httpResult) {
                HttpResult<CliniclInfo> result = new HttpResult<CliniclInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}
