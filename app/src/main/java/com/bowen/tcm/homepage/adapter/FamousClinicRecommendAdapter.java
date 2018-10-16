package com.bowen.tcm.homepage.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.inquiry.activity.ImgTextCosultDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/26.
 */
public class FamousClinicRecommendAdapter extends BaseQuickAdapter<Clinic> {


    @BindView(R.id.mFamousClinicImg)
    CircleImageView mFamousClinicImg;
    @BindView(R.id.mFamousClinicNameTv)
    TextView mFamousClinicNameTv;
    @BindView(R.id.mFamousClinicAddressTv)
    TextView mFamousClinicAddressTv;

    public FamousClinicRecommendAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_famous_clinic_recommend;
    }

    @Override
    protected void convert(BaseViewHolder helper, Clinic item, int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext.get(),item.getHospitalImgUrl(),mFamousClinicImg,R.drawable.img_bg);
        mFamousClinicNameTv.setText(item.getHospitalName());
        mFamousClinicAddressTv.setText(item.getAddressStr());
    }
}
