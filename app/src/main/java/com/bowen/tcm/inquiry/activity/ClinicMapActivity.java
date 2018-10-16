package com.bowen.tcm.inquiry.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bowen.commonlib.base.BasePopWindow.BasePopWindowListener;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.common.dialog.ChooseAddressPopWindow;
import com.bowen.tcm.common.dialog.ClinicMapDetailDialog;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.util.LocationUtil;
import com.bowen.tcm.inquiry.contract.ClinicMapContract;
import com.bowen.tcm.inquiry.model.ClinicModel;
import com.bowen.tcm.inquiry.presenter.ClinicMapPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class ClinicMapActivity extends BaseActivity implements OnMarkerClickListener,ClinicMapContract.View {
    @BindView(R.id.mCurrentPositionTv)
    TextView mCurrentPositionTv;
    @BindView(R.id.mChangeCityTv)
    TextView mChangeCityTv;
    @BindView(R.id.mMapView)
    MapView mMapView;

    private ClinicModel mClinicModel;
    private ClinicMapPresenter mPresenter;
    private ChooseAddressPopWindow chooseAddressPopWindow;
    private PermissionsModel mPermissionsModel;
    private String mAreaCode;
    private AMap mAMap;
    private ArrayList<Clinic> mClinicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_clinic_map);
        ButterKnife.bind(this);
        setTitle("中医馆");
        mMapView.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        mPresenter = new ClinicMapPresenter(this,this);
        mClinicModel = new ClinicModel(this);
        mAMap = mMapView.getMap();
        mAMap.setOnMarkerClickListener(this);
        LocationEvent event = AppConfigInfo.getInstance().getLocationEvent();
        addMapMarker(event.getLongitude(),event.getLatitude(),R.drawable.my_location_icon);
        mCurrentPositionTv.setText("当前位置："+event.getProvince()+event.getCity());
        mPresenter.getAllCliniMapData(event.getLongitude(),event.getLatitude(),"");
        jumpSpecialLocation(event.getLongitude(),event.getLatitude());
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        mPresenter.jumpPointAnim(mAMap,marker,marker.getPosition().longitude,marker.getPosition().latitude);
        if(EmptyUtils.isNotEmpty(mClinicList)){
            Clinic clinic = mClinicModel.getChooseClinic(mClinicList,marker);
            if(EmptyUtils.isNotEmpty(clinic)){
                mPresenter.getClinicMapDetail(clinic.getHospitalId());
            }
        }
        return false;
    }




    @OnClick(R.id.mChangeCityTv)
    public void onViewClicked(View view) {
        mAMap.clear();
        if (EmptyUtils.isNotEmpty(chooseAddressPopWindow)) {
            chooseAddressPopWindow.disMissWindow();
            chooseAddressPopWindow = null;
        }
        chooseAddressPopWindow = new ChooseAddressPopWindow(this);
        chooseAddressPopWindow.show(mChangeCityTv);
        chooseAddressPopWindow.setBaseDialogListener(new BasePopWindowListener() {
            @Override
            public void onDataCallBack(Object... obj) {
                mAreaCode = (String) obj[2];
                mCurrentPositionTv.setText("当前位置：" + chooseAddressPopWindow.getSelectedPositionStr());
                mPresenter.getAllCliniMapData(0,0,mAreaCode);
            }
        });
    }


    /**
     * 地图上添加Marker
     * @param longitude
     * @param latitude
     */
    private void addMapMarker(double longitude,double latitude,int resId){
        mAMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), resId)))
                .draggable(true));
    }

    /**
     * 跳转到具体的位置
     * @param longitude
     * @param latitude
     */
    private void jumpSpecialLocation(double longitude,double latitude){
        LatLng latLng = new LatLng(latitude,longitude);
        mAMap. moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));
    }


    @Override
    public void jumpAnimOver(double longitude, double latitude) {
        addMapMarker(longitude,latitude,R.drawable.location_red_middle_icon);
    }

    @Override
    public void onGetAllCliniMapDataSuccess(CliniclInfo info) {
        mClinicList = info.getMapHospitalList();
        if (EmptyUtils.isEmpty(mClinicList)) {
            return;
        }
        for (Clinic clinic : mClinicList) {
            addMapMarker(clinic.getLongitudeValue(),clinic.getLatitudeValue(),R.drawable.location_red_middle_icon);
        }

        if(EmptyUtils.isNotEmpty(mAreaCode)){
            Clinic clinic = mClinicList.get(mClinicList.size()/2);
            jumpSpecialLocation(clinic.getLongitudeValue(),clinic.getLatitudeValue());
        }
    }

    @Override
    public void onGetCliniMapDetailSuccess(Clinic clinic) {
        ClinicMapDetailDialog clinicMapDetailDialog = new ClinicMapDetailDialog(this);
        clinicMapDetailDialog.setClinic(clinic);
        clinicMapDetailDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}
