package com.bowen.tcm.medicalrecord.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.MedicalRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/20.
 */

public class MedicalRecordAdapter extends BaseQuickAdapter<MedicalRecord> {

    @BindView(R.id.mDateTv)
    TextView mDateTv;
    @BindView(R.id.mDiseaseNameTv)
    TextView mDiseaseNameTv;
    @BindView(R.id.mDiseaseDesTv)
    TextView mDiseaseDesTv;
    @BindView(R.id.mDiagnoseClinicTv)
    TextView mDiagnoseClinicTv;
    @BindView(R.id.mDiagnoseClinicLayout)
    LinearLayout mDiagnoseClinicLayout;
    @BindView(R.id.mPhoto01Img)
    CircleImageView mPhoto01Img;
    @BindView(R.id.mPhoto02Img)
    CircleImageView mPhoto02Img;
    @BindView(R.id.mPhoto03Img)
    CircleImageView mPhoto03Img;
    @BindView(R.id.mPhotoDetailLayout)
    LinearLayout mPhotoDetailLayout;

    public MedicalRecordAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_medical_record;
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicalRecord item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mDateTv.setText(DateUtil.date2String(item.getIllTime(),DateUtil.DEFAULT_FORMAT_DATE));
        mDiseaseNameTv.setText(item.getCaseName());

        if(EmptyUtils.isNotEmpty(item.getCaseDetails())){
            mDiseaseDesTv.setText(item.getCaseDetails());
            mDiseaseDesTv.setVisibility(View.VISIBLE);
        }else{
            mDiseaseDesTv.setVisibility(View.GONE);
        }
        if(EmptyUtils.isNotEmpty(item.getClinicName())){
            mDiagnoseClinicTv.setText(item.getClinicName());
            mDiagnoseClinicLayout.setVisibility(View.VISIBLE);
        }else{
            mDiagnoseClinicLayout.setVisibility(View.GONE);
        }
        if(EmptyUtils.isNotEmpty(item.getComFileInfoList())){
            int size = item.getComFileInfoList().size();
            ImageLoaderUtil.getInstance().show(mContext.get(),item.getComFileInfoList().get(0).getFileLink(),mPhoto01Img,R.drawable.img_bg);
            if(size>1){
                mPhoto02Img.setVisibility(View.VISIBLE);
                ImageLoaderUtil.getInstance().show(mContext.get(),item.getComFileInfoList().get(1).getFileLink(),mPhoto02Img,R.drawable.img_bg);
            }else{
                mPhoto02Img.setVisibility(View.GONE);
            }
            if(size>2) {
                mPhoto03Img.setVisibility(View.VISIBLE);
                ImageLoaderUtil.getInstance().show(mContext.get(),item.getComFileInfoList().get(2).getFileLink(),mPhoto03Img,R.drawable.img_bg);
            }else{
                mPhoto03Img.setVisibility(View.GONE);
            }
            mPhotoDetailLayout.setVisibility(View.VISIBLE);
        }else{
            mPhotoDetailLayout.setVisibility(View.GONE);
        }
    }
}
