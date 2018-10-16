package com.bowen.tcm.homepage.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Doctor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/26.
 */
public class FamousDoctorRecommendAdapter extends BaseQuickAdapter<Doctor> {

    @BindView(R.id.mDoctorHeadPortraitImg)
    CircleImageView mDoctorHeadPortraitImg;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDepartmentTv)
    TextView mDoctorDepartmentTv;
    @BindView(R.id.mDoctorRankTv)
    TextView mDoctorRankTv;
    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mDoctorDesTv)
    TextView mDoctorDesTv;

    public FamousDoctorRecommendAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_famous_doctor_recommend;
    }

    @Override
    protected void convert(BaseViewHolder helper, Doctor item, int position) {
        ButterKnife.bind(this,helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext.get(),item.getHeadImgUrl(),mDoctorHeadPortraitImg,R.drawable.man);
        mDoctorNameTv.setText(item.getName());
        mDoctorDepartmentTv.setText(item.getHospitalDepartments());
        mDoctorRankTv.setText(item.getPositionStr());
        mClinicNameTv.setText(item.getHospital());
        mDoctorDesTv.setText("擅长治疗："+item.getDiseases());
    }
}
