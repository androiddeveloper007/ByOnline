package com.bowen.tcm.medicalrecord.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.util.ChooseTypeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */

public class MedicalAllDetailAdapter extends BaseQuickAdapter<MedicalRecord> {

    @BindView(R.id.redTagImg)
    ImageView redTagImg;
    @BindView(R.id.mDiagnoseDesAndTimeTv)
    TextView mDiagnoseDesAndTimeTv;
    @BindView(R.id.mMedicalDesTv)
    TextView mMedicalDesTv;
    @BindView(R.id.mDiagnoseHospitalTv)
    TextView mDiagnoseHospitalTv;
    @BindView(R.id.mDiagnoseDoctorTv)
    TextView mDiagnoseDoctorTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mTopLineImg)
    ImageView mTopLineImg;
    @BindView(R.id.mMainLineImg)
    ImageView mMainLineImg;
    @BindView(R.id.mShadowLayout)
    LinearLayout mShadowLayout;

    public MedicalAllDetailAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_medical_all_detail;
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicalRecord item, int position) {
        ButterKnife.bind(this,helper.convertView);
        if(position == 1){
            mTopLineImg.setVisibility(View.INVISIBLE);
        }else{
            mTopLineImg.setVisibility(View.VISIBLE);
        }
        if(EmptyUtils.isNotEmpty(item.getDoctorTime())&&EmptyUtils.isNotEmpty(item.getDoctorStage())){
            mDiagnoseDesAndTimeTv.setText(ChooseTypeUtil.getDiagnoseStage(item.getDoctorStage())
                    +" "+ DateUtil.date2String(item.getIllTime(),DateUtil.DEFAULT_FORMAT_DATE));
        }else{
            mDiagnoseDesAndTimeTv.setText("");
        }

        mMedicalDesTv.setText(item.getCaseDetails());
        mDiagnoseHospitalTv.setText(item.getClinicName());
        mDiagnoseDoctorTv.setText(item.getDoctorName());

        GridLayoutManager layoutManager = new GridLayoutManager(mContext.get(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        MedicalRecordPhotoAdapter mAdapter = new MedicalRecordPhotoAdapter(mContext.get());
        mAdapter.setShowDelete(false);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setNewData(ChooseTypeUtil.getPhotoList(item.getComFileInfoList()));
        if(getData().size() != 1){//只有一条不添加红线
            ViewGroup.LayoutParams para;
            para = mMainLineImg.getLayoutParams();
            int size = mAdapter.getData().size();
            int temp;
            if(size%3 == 0){
                temp = mAdapter.getData().size()/3 *ScreenSizeUtil.dp2px(95);
            }else{
                temp = (mAdapter.getData().size()/3 + 1)*ScreenSizeUtil.dp2px(95);
            }
            para.height = ScreenSizeUtil.dp2px(165) + temp + ScreenSizeUtil.dp2px(112);
        }


    }
}
