package com.bowen.tcm.inquiry.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.model.PermissionsModel.PermissionListener;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.dialog.adapter.ClinicGallerAdapter;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.inquiry.model.ClinicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/26.
 */
public class ClinicMapDetailActivity extends BaseActivity {
    @BindView(R.id.mMapView)
    MapView mMapView;
    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mClinicAddressTv)
    TextView mClinicAddressTv;
    @BindView(R.id.mDialLayout)
    RelativeLayout mDialLayout;
    @BindView(R.id.mMoreGalleryTv)
    TextView mMoreGalleryTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private String mClinicId;
    private Clinic mClinic;
    private ClinicGallerAdapter mAdapter;
    private ClinicModel mClinicModel;
    private List<String> mPhotoList;
    private PermissionsModel mPermissionsModel;
    private AMap mAMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_clinic_map_detail);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setTitle("");
        mClinicId = RouterActivityUtil.getString(this);
        mClinicModel = new ClinicModel(this);
        mAMap = mMapView.getMap();
        mPermissionsModel = new PermissionsModel(this);
        mAdapter = new ClinicGallerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(EmptyUtils.isNotEmpty(mClinic)){
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(ClinicMapDetailActivity.this,
                            null,position+1,mClinicModel.transfromPhotos(mClinic.getImgUrlList()));
                    showBigPicDialog.show();
                }
            }
        });
        getClinicDetail();
    }

    private void updateUI(){
        if(EmptyUtils.isNotEmpty(mClinic)){
            setTitle(mClinic.getHospitalName());
            mClinicNameTv.setText(mClinic.getHospitalName());
            mClinicAddressTv.setText(mClinic.getAddressStr());
            if(EmptyUtils.isNotEmpty(mClinic.getPhone())){
                mDialLayout.setVisibility(View.VISIBLE);
            }else{
                mDialLayout.setVisibility(View.GONE);
            }
            ArrayList<String> gallers = mClinic.getImgUrlList();
            if(EmptyUtils.isNotEmpty(gallers)){
                mMoreGalleryTv.setText(String.format("更多（%s）",gallers.size()));
                if(gallers.size()>4){
                    mPhotoList = gallers.subList(0,4);
                }else{
                    mPhotoList = gallers;
                }
                mAdapter.setNewData(mPhotoList);
            }else{
                mPhotoList = new ArrayList<>();
                mAdapter.setNewData(mPhotoList);
                View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
                mAdapter.addFooterView(emptyView);
            }
            LocationEvent event = AppConfigInfo.getInstance().getLocationEvent();
            mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.drawable.my_location_icon)))
                    .draggable(true));
            mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mClinic.getLatitudeValue(), mClinic.getLongitudeValue()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.drawable.location_red_middle_icon)))
                    .draggable(true));

            LatLng latLng = new LatLng(mClinic.getLatitudeValue(),mClinic.getLongitudeValue());
            mAMap. moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }

    }


    @OnClick({R.id.mDialLayout, R.id.mMoreGalleryTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDialLayout:
                AffirmDialog affirmDialog = new AffirmDialog(this, "", "拨打电话 " + mClinic.getPhone(), "取消", "确定");
                affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                    @Override
                    public void onCancle() {
                    }

                    @Override
                    public void onOK() {
                        mPermissionsModel.checkCallPhonePermission(new PermissionsModel.PermissionListener() {
                            @Override
                            public void onPermission(boolean isPermission) {
                                if (isPermission) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + mClinic.getPhone());
                                    intent.setData(data);
                                    ClinicMapDetailActivity.this.startActivity(intent);
                                }
                            }
                        });
                    }
                });
                affirmDialog.show();
                break;
            case R.id.mMoreGalleryTv:
                if(EmptyUtils.isNotEmpty(mClinic)){
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(this,null,1,
                            mClinicModel.transfromPhotos(mClinic.getImgUrlList()));
                    showBigPicDialog.show();
                }
                break;
        }
    }

    private void getClinicDetail(){
        mClinicModel.getMapClinicDetail(mClinicId, new HttpTaskCallBack<Clinic>() {
            @Override
            public void onSuccess(HttpResult<Clinic> result) {
                mClinic = result.getData();
                updateUI();
            }

            @Override
            public void onFail(HttpResult<Clinic> result) {

            }
        });
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
