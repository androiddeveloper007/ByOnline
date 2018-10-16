package com.bowen.tcm.inquiry.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.MoneyUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.activity.ChatActivity;
import com.bowen.tcm.inquiry.activity.HospitalDetailActivity;
import com.bowen.tcm.inquiry.activity.ImgTextCosultDetailActivity;
import com.bowen.tcm.inquiry.model.DoctorModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/2.
 */
public class FindDoctorAdapter extends BaseQuickAdapter<Doctor> {

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mFamousDoctorTv)
    TextView mFamousDoctorTv;
    @BindView(R.id.mDocotorAvatarLayout)
    RelativeLayout mDocotorAvatarLayout;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDepartmentTv)
    TextView mDoctorDepartmentTv;
    @BindView(R.id.mDoctorRankTv)
    TextView mDoctorRankTv;
    @BindView(R.id.mDoctorClinicTv)
    TextView mDoctorClinicTv;
    @BindView(R.id.mRecommendImg)
    ImageView mRecommendImg;
    @BindView(R.id.mDoctorDesTv)
    TextView mDoctorDesTv;
    @BindView(R.id.mPraiseRateProgressBar)
    ProgressBar mPraiseRateProgressBar;
    @BindView(R.id.mPraiseRateTv)
    TextView mPraiseRateTv;
    @BindView(R.id.mReplyRateProgressBar)
    ProgressBar mReplyRateProgressBar;
    @BindView(R.id.mReplyRateTv)
    TextView mReplyRateTv;
    @BindView(R.id.mProductPriceTv)
    TextView mProductPriceTv;
    @BindView(R.id.mServerPeopleCountTv)
    TextView mServerPeopleCountTv;
    @BindView(R.id.mConsultBtn)
    TextView mConsultBtn;

    private DoctorModel mDoctorModel;



    public FindDoctorAdapter(Context cxt) {
        super(cxt);
        mDoctorModel = new DoctorModel(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_find_doctor;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Doctor item, int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext.get(), item.getHeadImgUrl(), mHeadPortraitImg, R.drawable.man);
        if (item.getRecommended() == 1) {//为1:为名医
            mFamousDoctorTv.setVisibility(View.VISIBLE);
            mRecommendImg.setVisibility(View.VISIBLE);
        } else {
            mFamousDoctorTv.setVisibility(View.GONE);
            mRecommendImg.setVisibility(View.GONE);
        }
        mDoctorNameTv.setText(item.getName());
        mDoctorDepartmentTv.setText(item.getHospitalDepartments());
        mDoctorRankTv.setText(item.getPositionStr());
        mDoctorClinicTv.setText(item.getHospital());
        mDoctorDesTv.setText("擅长治疗：" + item.getDiseases());
        if (item.getFeedbackRate() == 0) {
            mPraiseRateProgressBar.setProgress(100);
            mPraiseRateTv.setText("暂无评价");
        } else {
            int praiseRate = (int) (item.getFeedbackRate() * 100);
            mPraiseRateProgressBar.setProgress(praiseRate);
            mPraiseRateTv.setText(praiseRate + "%");
        }
        if (item.getReversionRate()==0) {
            mReplyRateProgressBar.setProgress(100);
            mReplyRateTv.setText("暂无回复");
        } else {
            int replyRate = (int) (item.getReversionRate() * 100);
            mReplyRateProgressBar.setProgress(replyRate);
            mReplyRateTv.setText(replyRate + "%");
        }
        if (EmptyUtils.isEmpty(item.getServiceNumber())) {
            mServerPeopleCountTv.setText(SpannableStringUtils.getBuilder("服务人次 ")
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray))
                    .append("暂无服务")
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_red))
                    .create());
        } else {
            mServerPeopleCountTv.setText(SpannableStringUtils.getBuilder("服务人次 ")
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray))
                    .append(item.getServiceNumber() + "")
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_red))
                    .create());
        }
        if(item.isShowConsult()){
            mProductPriceTv.setText(Constants.RMB + MoneyUtil.getInstance().formatFloatMoney(item.getConsultFee()));
            mConsultBtn.setBackgroundResource(R.drawable.button_guide_enter);
        }else{
            mProductPriceTv.setText("暂不服务");
            mConsultBtn.setBackgroundResource(R.drawable.button_main_rectangle_gray);
        }

        mConsultBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.isShowConsult()){
                   return;
                }
                BaseActivity baseActivity = (BaseActivity) mContext.get();
                if (baseActivity.isLogin()){
                    checkImmediateConsult(item.getDoctorId());
                }
            }
        });
    }



    private void checkImmediateConsult(String doctorId){
        mDoctorModel.checkImmediateConsult(doctorId, new HttpTaskCallBack<Doctor>() {
            @Override
            public void onSuccess(HttpResult<Doctor> result) {
                Doctor doctor = result.getData();
                if(!doctor.isAsk()){
                    if(doctor.isShowConsult()){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(RouterActivityUtil.FROM_TAG, doctor);
                        RouterActivityUtil.startActivity((BaseActivity) mContext.get(), ImgTextCosultDetailActivity.class,bundle);
                    }else{
                        ToastUtil.getInstance().showToastDialog("该医生暂不提供图文咨询服务");
                    }
                }else{
                    Bundle bundle = new Bundle();
                    PayOrderInfo payOrderInfo = new PayOrderInfo();
                    payOrderInfo.setEmrDoctor(doctor);
                    payOrderInfo.setOrderId(doctor.getOrderId());
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo);
                    RouterActivityUtil.startActivity((BaseActivity) mContext.get(), ChatActivity.class,bundle);
                }
            }

            @Override
            public void onFail(HttpResult<Doctor> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }

}
