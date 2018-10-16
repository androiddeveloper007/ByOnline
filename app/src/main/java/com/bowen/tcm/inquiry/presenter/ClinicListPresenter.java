package com.bowen.tcm.inquiry.presenter;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.inquiry.contract.ClinicListContract;
import com.bowen.tcm.inquiry.model.ClinicModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class ClinicListPresenter extends BasePresenter implements ClinicListContract.Presenter{

    private ClinicModel mClinicModel;
    private ClinicListContract.View mView;

    public ClinicListPresenter(Context mContext, ClinicListContract.View view) {
        super(mContext);
        mClinicModel = new ClinicModel(mContext);
        mView = view;
    }

    @Override
    public void getAllClinicListOrMap(int pageNo, int pageSize,FindClinicParam param) {
        mClinicModel.getAllClinicListOrMap(param, pageNo, pageSize, new HttpTaskCallBack<CliniclInfo>() {
            @Override
            public void onSuccess(HttpResult<CliniclInfo> result) {
               mView.onGetAllClinicListOrMapSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<CliniclInfo> result) {
              mView.onGetAllClinicListOrMapFailed();
              ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }
}
