package com.bowen.tcm.inquiry.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.inquiry.contract.ClinicListContract;
import com.bowen.tcm.inquiry.contract.ClinicMapContract;
import com.bowen.tcm.inquiry.model.ClinicModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class ClinicMapPresenter extends BasePresenter implements ClinicMapContract.Presenter{

    private ClinicModel mClinicModel;
    private ClinicMapContract.View mView;

    public ClinicMapPresenter(Context mContext, ClinicMapContract.View view) {
        super(mContext);
        mClinicModel = new ClinicModel(mContext);
        mView = view;
    }

    @Override
    public void getAllCliniMapData(double longitude, double latitude,String areaCode) {

        mClinicModel.getClinicMapData(longitude, latitude,areaCode, new HttpTaskCallBack<CliniclInfo>() {
            @Override
            public void onSuccess(HttpResult<CliniclInfo> result) {
                mView.onGetAllCliniMapDataSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<CliniclInfo> result) {
             ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });

    }

    @Override
    public void getClinicMapDetail(String clinicId) {
        mClinicModel.getMapClinicDetail(clinicId, new HttpTaskCallBack<Clinic>() {
            @Override
            public void onSuccess(HttpResult<Clinic> result) {
                mView.onGetCliniMapDetailSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<Clinic> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPointAnim(AMap mAMap,final Marker marker, final double longitude, final double latitude) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mAMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatlng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }else{
                    marker.remove();
                    mView.jumpAnimOver(longitude,latitude);
                }
            }
        });
    }
}
