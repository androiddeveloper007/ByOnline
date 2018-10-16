package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.dialog.AffirmDialog.AffirmDialogListenner;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorCommentInfo;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.dialog.ShareDialog;
import com.bowen.tcm.common.event.AttentionChanStatusEvent;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.inquiry.contract.DoctorDetailContract;
import com.bowen.tcm.inquiry.presenter.DoctorDetailPresenter;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;
import com.bowen.tcm.medicalrecord.activity.AddMedicalInfoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * Describe:名医详情
 * Created by AwenZeng on 2018/7/3.
 */
public class FamousDoctorDetailActivity extends BaseActivity implements DoctorDetailContract.View {
    @BindView(R.id.mBackImg)
    ImageView mBackImg;
    @BindView(R.id.mDoctorNameTopTv)
    TextView mDoctorNameTopTv;
    @BindView(R.id.mShareImg)
    ImageView mShareImg;
    @BindView(R.id.mPraiseRateTv)
    TextView mPraiseRateTv;
    @BindView(R.id.mComplainCountTv)
    TextView mComplainCountTv;
    @BindView(R.id.mReplyRateTv)
    TextView mReplyRateTv;
    @BindView(R.id.mServicePeopleCountTv)
    TextView mServicePeopleCountTv;
    @BindView(R.id.mDoctorHeadPortraitImg)
    CircleImageView mDoctorHeadPortraitImg;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDesTv)
    TextView mDoctorDesTv;
    @BindView(R.id.mDoctorClinicTv)
    TextView mDoctorClinicTv;
    @BindView(R.id.mAttentionBtn)
    TextView mAttentionBtn;

    private DoctorDetailPresenter mPresenter;
    private String mDoctorId;
    private boolean isFollow;//是否被关注
    private DoctorDetail mDoctorDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_famous_doctor_detail);
        ButterKnife.bind(this);

        mDoctorId = RouterActivityUtil.getString(this);
        mPresenter = new DoctorDetailPresenter(this, this);
        mPresenter.getDoctorDetail(mDoctorId);
    }

    private void updateUI() {
        if (EmptyUtils.isNotEmpty(mDoctorDetail)) {
            isFollow = mDoctorDetail.isFollow();
            Doctor doctor = mDoctorDetail.getEmrDoctor();
            ImageLoaderUtil.getInstance().show(this, doctor.getHeadImgUrl(), mDoctorHeadPortraitImg, R.drawable.man);
            mDoctorNameTv.setText(doctor.getName());
            mDoctorNameTopTv.setText(doctor.getName());
            mDoctorDesTv.setText(doctor.getHospitalDepartments() + " " + doctor.getPositionStr());
            mDoctorClinicTv.setText(doctor.getHospital());
            if(doctor.getFeedbackRate()==0){
                mPraiseRateTv.setText("暂无评价");
            }else{
                mPraiseRateTv.setText((int)(doctor.getFeedbackRate()*100) +"%");
            }

            if(doctor.getReversionRate()==0){
                mReplyRateTv.setText("暂无咨询");
            }else{
                mReplyRateTv.setText((int)(doctor.getReversionRate()*100) +"%");
            }
            if(doctor.getComplaintsNumber()==0){
                mComplainCountTv.setText("0");
            }else{
                mComplainCountTv.setText(doctor.getComplaintsNumber()+"");
            }
            if(doctor.getServiceNumber()==0){
                mServicePeopleCountTv.setText("暂无记录");
            }else{
                mServicePeopleCountTv.setText(doctor.getServiceNumber()+"");
            }
            if (mDoctorDetail.isFollow()) {
                mAttentionBtn.setText("已关注");
                mAttentionBtn.setSelected(true);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main_gray_01));
            } else {
                mAttentionBtn.setText("+关注");
                mAttentionBtn.setSelected(false);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main));
            }
        }
    }

    @Override
    protected boolean enableUpSlidingClose() {
        return true;
    }

    @Override
    public void onChangeAttentionStatus(boolean isSuccess) {
        if(isSuccess){
            isFollow = !isFollow;
            if (isFollow) {
                mAttentionBtn.setText("已关注");
                mAttentionBtn.setSelected(true);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main_gray_01));
                ToastUtil.getInstance().toast("关注成功");
            } else {
                mAttentionBtn.setText("+关注");
                mAttentionBtn.setSelected(false);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main));
                ToastUtil.getInstance().toast("已取消关注");
            }
        }
        EventBus.getDefault().post(new AttentionChanStatusEvent(isFollow));
    }

    @Override
    public void onGetDoctorDetailSuccess(DoctorDetail doctorDetail) {
        mDoctorDetail = doctorDetail;
        updateUI();
    }

    @OnClick({R.id.mBackImg, R.id.mShareImg, R.id.mAttentionBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackImg:
                onBackPressed();
                break;
            case R.id.mShareImg:
                Doctor doctor = mDoctorDetail.getEmrDoctor();
                ShareData shareData = new ShareData();
                shareData.setShareType(Platform.SHARE_WEBPAGE);
                shareData.setLinkUrl(doctor.getShareUrl());
                shareData.setImgUrl(doctor.getHeadImgUrl());
                shareData.setTitile(doctor.getName() + doctor.getHospitalDepartments() + doctor.getPositionStr());
                shareData.setContent("擅长治疗：" + doctor.getDiseases());
                ShareDialog.getBuilder().shareData(shareData).build(this).show();
                break;
            case R.id.mAttentionBtn:
                if (isLogin()) {
                    if(isFollow){
                        showTipsAttentionDialog();
                    }else{
                        mPresenter.changeAttentionStatus(mDoctorDetail.getEmrDoctor().getDoctorId(), !isFollow);
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckImmediateConsultSuccess(Doctor doctor) {

    }

    private void showTipsAttentionDialog(){
        AffirmDialog affirmDialog = new AffirmDialog(FamousDoctorDetailActivity.this,"确定要取消关注吗？");
        affirmDialog.setAffirmDialogListenner(new AffirmDialogListenner() {
            @Override
            public void onCancle() {

            }

            @Override
            public void onOK() {
                mPresenter.changeAttentionStatus(mDoctorDetail.getEmrDoctor().getDoctorId(), !isFollow);
            }
        });
        affirmDialog.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        mPresenter.getDoctorDetail(mDoctorId);
    }

}
