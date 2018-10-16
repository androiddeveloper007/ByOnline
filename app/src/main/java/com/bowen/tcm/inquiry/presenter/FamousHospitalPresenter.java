package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.inquiry.contract.FamousHospitalContract;
import com.bowen.tcm.inquiry.model.FamousHospitalModel;

/**
 * Describe:根据省市区搜索医馆分页(用户端)
 * Created by zhuzhipeng on 2018/7/9.
 */
public class FamousHospitalPresenter extends BasePresenter {
    private FamousHospitalModel famousHospitalModel;
    private FamousHospitalContract.View mView;

    public FamousHospitalPresenter(Context mContext, FamousHospitalContract.View view) {
        super(mContext);
        famousHospitalModel = new FamousHospitalModel(mContext);
        mView = view;
    }

    public void getAllHospitalPage(int pageNo, int pageSize,String provinceCode, String cityCode,String areaCode, double longitude, double latitude) {
        famousHospitalModel.getAllHospitalPage(pageNo, pageSize, provinceCode,cityCode,areaCode,longitude,latitude, new HttpTaskCallBack<CliniclInfo>() {
            @Override
            public void onSuccess(HttpResult<CliniclInfo> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<CliniclInfo> result) {
               mView.onLoadFail(result.getData());
            }
        });
    }
}
