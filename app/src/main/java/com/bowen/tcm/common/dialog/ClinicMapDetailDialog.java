package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.model.PermissionsModel.PermissionListener;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.dialog.adapter.ClinicGallerAdapter;
import com.bowen.tcm.inquiry.model.ClinicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 单选选择弹框
 */
public class ClinicMapDetailDialog extends BaseDialog {

    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mCloseImg)
    ImageView mCloseImg;
    @BindView(R.id.mClinicAddressTv)
    TextView mClinicAddressTv;
    @BindView(R.id.mDialLayout)
    RelativeLayout mDialLayout;
    @BindView(R.id.mMoreGalleryTv)
    TextView mMoreGalleryTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private Clinic mClinic;
    private ClinicGallerAdapter mAdapter;
    private ClinicModel mClinicModel;
    private List<String> mPhotoList;
    private PermissionsModel mPermissionsModel;

    public ClinicMapDetailDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    public ClinicMapDetailDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setClinic(Clinic mClinic) {
        this.mClinic = mClinic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clinic_map_detail);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        setCanceledOnTouchOutside(true);
        init();
    }

    private void init(){
        mClinicModel = new ClinicModel(mContext);
        mPermissionsModel = new PermissionsModel(mContext);
        mAdapter = new ClinicGallerAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(EmptyUtils.isNotEmpty(mClinic)){
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(mContext,
                            null,position+1,mClinicModel.transfromPhotos(mClinic.getImgUrlList()));
                    showBigPicDialog.show();
                }
            }
        });
        updateUI();
    }

    private void updateUI(){
        if(EmptyUtils.isNotEmpty(mClinic)){
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
        }

    }


    @OnClick({R.id.mCloseImg, R.id.mDialLayout,R.id.mMoreGalleryTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCloseImg:
                dismiss();
                break;
            case R.id.mMoreGalleryTv:
                if(EmptyUtils.isNotEmpty(mClinic)){
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(mContext,null,1,
                            mClinicModel.transfromPhotos(mClinic.getImgUrlList()));
                    showBigPicDialog.show();
                }
                break;
            case R.id.mDialLayout:
                mPermissionsModel.checkCallPhonePermission(new PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + mClinic.getPhone());
                            intent.setData(data);
                            mContext.startActivity(intent);
                        }
                    }
                });

                break;
        }
    }
}