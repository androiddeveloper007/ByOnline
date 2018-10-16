package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.dialog.AffirmDialog.AffirmDialogListenner;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.commonlib.widget.CollapsedTextView;
import com.bowen.commonlib.widget.CustomScrollView;
import com.bowen.commonlib.widget.CustomScrollView.OnScrollListener;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorCommentInfo;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.dialog.ShareDialog;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.event.AttentionChanStatusEvent;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.contract.DoctorDetailContract;
import com.bowen.tcm.inquiry.presenter.DoctorDetailPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * Describe:医生详情
 * Created by AwenZeng on 2018/7/3.
 */
public class DoctorDetailActivity extends BaseActivity implements DoctorDetailContract.View, OnScrollListener {
    @BindView(R.id.mBackImg)
    ImageView mBackImg;
    @BindView(R.id.mShareImg)
    ImageView mShareImg;
    @BindView(R.id.mDoctorHeadPortraitImg)
    CircleImageView mDoctorHeadPortraitImg;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDesTv)
    TextView mDoctorDesTv;
    @BindView(R.id.mAttentionBtn)
    TextView mAttentionBtn;
    @BindView(R.id.mPraiseRateTv)
    TextView mPraiseRateTv;
    @BindView(R.id.mReplyRateTv)
    TextView mReplyRateTv;
    @BindView(R.id.mComplainCountTv)
    TextView mComplainCountTv;
    @BindView(R.id.mServerPeopleCountTv)
    TextView mServerPeopleCountTv;
    @BindView(R.id.mImgTextConsultImg)
    ImageView mImgTextConsultImg;
    @BindView(R.id.mImgTextConsultNameTv)
    TextView mImgTextConsultNameTv;
    @BindView(R.id.mImgTextConsultLayout)
    LinearLayout mImgTextConsultLayout;
    @BindView(R.id.mImgTextConsultPriceTv)
    TextView mImgTextConsultPriceTv;
    @BindView(R.id.mGoConsultBtn)
    TextView mGoConsultBtn;
    @BindView(R.id.mInquiryReservationImg)
    ImageView mInquiryReservationImg;
    @BindView(R.id.mPayProductNameTv)
    TextView mPayProductNameTv;
    @BindView(R.id.mInquiryReservationLayout)
    LinearLayout mInquiryReservationLayout;
    @BindView(R.id.mInquiryReservationPriceTv)
    TextView mInquiryReservationPriceTv;
    @BindView(R.id.mGoReservationBtn)
    TextView mGoReservationBtn;
    @BindView(R.id.mDoctorGoodSkillTv)
    TextView mDoctorGoodSkillTv;
    @BindView(R.id.mDoctorHistoryTv)
    CollapsedTextView mDoctorHistoryTv;
    @BindView(R.id.mMoreCommentTv)
    TextView mMoreCommentTv;
    @BindView(R.id.mCommentUserHeadPortraitImg)
    CircleImageView mCommentUserHeadPortraitImg;
    @BindView(R.id.mCommentUserNameTv)
    TextView mCommentUserNameTv;
    @BindView(R.id.mUserCommentTimeTv)
    TextView mUserCommentTimeTv;
    @BindView(R.id.mCommentProductNameTv)
    TextView mCommentProductNameTv;
    @BindView(R.id.mCommentProductPriceTv)
    TextView mCommentProductPriceTv;
    @BindView(R.id.mCommentRatingBar)
    RatingBar mCommentRatingBar;
    @BindView(R.id.mUserCommentContentTv)
    CollapsedTextView mUserCommentContentTv;
    @BindView(R.id.mUserCommentLayout)
    RelativeLayout mUserCommentLayout;
    @BindView(R.id.mMoreGiftTv)
    TextView mMoreGiftTv;
    @BindView(R.id.mUserHeadPortrait01Img)
    CircleImageView mUserHeadPortrait01Img;
    @BindView(R.id.mGiveGiftUserName01Tv)
    TextView mGiveGiftUserName01Tv;
    @BindView(R.id.mGiveGiftDes01Tv)
    TextView mGiveGiftDes01Tv;
    @BindView(R.id.mUserHeadPortrait02Img)
    CircleImageView mUserHeadPortrait02Img;
    @BindView(R.id.mGiveGiftUserName02Tv)
    TextView mGiveGiftUserName02Tv;
    @BindView(R.id.mGiveGiftDes02Tv)
    TextView mGiveGiftDes02Tv;
    @BindView(R.id.mUserHeadPortrait03Img)
    CircleImageView mUserHeadPortrait03Img;
    @BindView(R.id.mGiveGiftUserName03Tv)
    TextView mGiveGiftUserName03Tv;
    @BindView(R.id.mGiveGiftDes03Tv)
    TextView mGiveGiftDes03Tv;
    @BindView(R.id.mGiveGiftBtn)
    TextView mGiveGiftBtn;
    @BindView(R.id.mGiveGiftLayout)
    RelativeLayout mGiveGiftLayout;
    @BindView(R.id.mDoctorTalkMoreTv)
    TextView mDoctorTalkMoreTv;
    @BindView(R.id.mDoctorTalk01Img)
    CircleImageView mDoctorTalk01Img;
    @BindView(R.id.mDoctorTalkTitle01Tv)
    TextView mDoctorTalkTitle01Tv;
    @BindView(R.id.mDoctorTalkTime01Tv)
    TextView mDoctorTalkTime01Tv;
    @BindView(R.id.mDoctorTalk02Img)
    CircleImageView mDoctorTalk02Img;
    @BindView(R.id.mDoctorTalkTitle02Tv)
    TextView mDoctorTalkTitle02Tv;
    @BindView(R.id.mDoctorTalkTime02Tv)
    TextView mDoctorTalkTime02Tv;
    @BindView(R.id.mDoctorTalk03Img)
    CircleImageView mDoctorTalk03Img;
    @BindView(R.id.mDoctorTalkTitle03Tv)
    TextView mDoctorTalkTitle03Tv;
    @BindView(R.id.mDoctorTalkTime03Tv)
    TextView mDoctorTalkTime03Tv;
    @BindView(R.id.mDoctorTalkLayout)
    RelativeLayout mDoctorTalkLayout;
    @BindView(R.id.mProductImgTextLayout)
    RelativeLayout mProductImgTextLayout;
    @BindView(R.id.mProductInquiryReservationLayout)
    RelativeLayout mProductInquiryReservationLayout;
    @BindView(R.id.mTitleBackImg)
    ImageView mTitleBackImg;
    @BindView(R.id.mTitleHeadPortraitImg)
    CircleImageView mTitleHeadPortraitImg;
    @BindView(R.id.mTitleNameTv)
    TextView mTitleNameTv;
    @BindView(R.id.mTitleShareImg)
    ImageView mTitleShareImg;
    @BindView(R.id.mTitleLayout)
    RelativeLayout mTitleLayout;
    @BindView(R.id.mCustomScrollView)
    CustomScrollView mCustomScrollView;
    @BindView(R.id.mTopBgImg)
    ImageView mTopBgImg;
    @BindView(R.id.mCommentContentLayout)
    LinearLayout mCommentContentLayout;
    @BindView(R.id.viewStubDoctorConsultEmpty)
    ViewStub viewStubDoctorConsultEmpty;


    private DoctorDetailPresenter mPresenter;
    private DoctorDetail mDoctorDetail;
    private String mDoctorId;
    private boolean isFollow;//是否被关注
    private int mRecommend = 0;
    private int scrollPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_doctor_detail);
        ButterKnife.bind(this);

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            mDoctorId = bundle.getString(RouterActivityUtil.FROM_TAG);
            mRecommend = bundle.getInt(RouterActivityUtil.FROM_TAG1);
        }
        mPresenter = new DoctorDetailPresenter(this, this);
        mPresenter.getDoctorDetail(mDoctorId);
        mCustomScrollView.setScrolListener(this);
        goToFamousDoctor();
    }

    private void updateUI() {
        if (EmptyUtils.isNotEmpty(mDoctorDetail)) {
            isFollow = mDoctorDetail.isFollow();
            Doctor doctor = mDoctorDetail.getEmrDoctor();
            ImageLoaderUtil.getInstance().show(this, doctor.getHeadImgUrl(), mDoctorHeadPortraitImg, R.drawable.man);
            mDoctorNameTv.setText(doctor.getName());
            String doctorClinic = doctor.getHospital() + "";

            //topLayout
            ImageLoaderUtil.getInstance().show(this, doctor.getHeadImgUrl(), mTitleHeadPortraitImg, R.drawable.man);
            mTitleNameTv.setText(doctor.getName());

            mDoctorDesTv.setText(doctorClinic + " " + doctor.getHospitalDepartments() + " " + doctor.getPositionStr());

            if (doctor.getFeedbackRate() == 0) {
                mPraiseRateTv.setText("暂无");
            } else {
                mPraiseRateTv.setText((int) (doctor.getFeedbackRate() * 100) + "%");
            }

            if (doctor.getReversionRate() == 0) {
                mReplyRateTv.setText("暂无");
            } else {
                mReplyRateTv.setText((int) (doctor.getReversionRate() * 100) + "%");
            }
            if (doctor.getComplaintsNumber() == 0) {
                mComplainCountTv.setText("0");
            } else {
                mComplainCountTv.setText(doctor.getComplaintsNumber() + "");
            }

            if (doctor.getServiceNumber() == 0) {
                mServerPeopleCountTv.setText("暂无");
            } else {
                mServerPeopleCountTv.setText(doctor.getServiceNumber() + "");
            }
            if (mDoctorDetail.isFollow()) {
                mAttentionBtn.setText("已关注");
                mAttentionBtn.setSelected(true);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main_gray_01));
            } else {
                mAttentionBtn.setText("+关注");
                mAttentionBtn.setSelected(false);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_white));
            }

            if (mDoctorDetail.isShowConsult()) {
                mImgTextConsultPriceTv.setText(doctor.getConsultFee() + "元");
            } else {
                mImgTextConsultPriceTv.setText("暂不服务");
                mGoConsultBtn.setBackgroundResource(R.drawable.button_main_rectangle_gray);
            }

            if (mDoctorDetail.isShowAppointment()) {
                mInquiryReservationPriceTv.setText(doctor.getRegisterFee() + "元");
            } else {
                mInquiryReservationPriceTv.setText("暂不服务");
                mGoReservationBtn.setBackgroundResource(R.drawable.button_main_rectangle_gray);
            }

            mDoctorGoodSkillTv.setText("擅长治疗：" + doctor.getDiseases());
            if (EmptyUtils.isNotEmpty(doctor.getIntroduction())) {
                mDoctorHistoryTv.setText(doctor.getIntroduction());
            }

            //用户评论
            DoctorCommentInfo doctorCommentInfo = mDoctorDetail.getEmrComment();
            if (EmptyUtils.isNotEmpty(doctorCommentInfo)) {
                mUserCommentLayout.setVisibility(View.VISIBLE);
                mCommentContentLayout.setVisibility(View.VISIBLE);
                mMoreCommentTv.setText(String.format("更多（%s）", mDoctorDetail.getCommentCount()));
                ImageLoaderUtil.getInstance().show(this, doctorCommentInfo.getUserImg(), mCommentUserHeadPortraitImg, R.drawable.man);
                mCommentUserNameTv.setText(doctorCommentInfo.getUserName());
                mUserCommentTimeTv.setText(DateUtil.date2String(doctorCommentInfo.getUserEvaluateTime(), DateUtil.DEFAULT_FORMAT_DATETIME));
                mCommentProductNameTv.setText(SpannableStringUtils.getBuilder("问诊方式：")
                        .setForegroundColor(getResources().getColor(R.color.color_main_gray_01))
                        .append(doctorCommentInfo.getAppointmentTypeStr())
                        .setForegroundColor(getResources().getColor(R.color.color_main_black))
                        .create());

                mCommentProductPriceTv.setText(SpannableStringUtils.getBuilder("问诊费用：")
                        .setForegroundColor(getResources().getColor(R.color.color_main_gray_01))
                        .append(doctorCommentInfo.getAmount() + "元")
                        .setForegroundColor(getResources().getColor(R.color.color_main_black))
                        .create());
                mCommentRatingBar.setRating(doctorCommentInfo.getScore());
                if (EmptyUtils.isNotEmpty(doctorCommentInfo.getUserEvaluate())) {
                    mUserCommentContentTv.setText(doctorCommentInfo.getUserEvaluate());
                }
            } else {
                mUserCommentLayout.setVisibility(View.VISIBLE);
                mMoreCommentTv.setText(String.format("更多（%s）", mDoctorDetail.getCommentCount()));
                mCommentContentLayout.setVisibility(View.GONE);
                viewStubDoctorConsultEmpty.setVisibility(View.VISIBLE);
            }

        }
    }


    /**
     * 跳转到名医界面
     */
    private void goToFamousDoctor() {
        if (mRecommend == Constants.TYPE_DOCTOR_FAMOUS) {
            Bundle bundle = new Bundle();
            bundle.putString(RouterActivityUtil.FROM_TAG, mDoctorId);
            RouterActivityUtil.startActivity(DoctorDetailActivity.this, FamousDoctorDetailActivity.class, bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onScroll(scrollPos);
    }

    @Override
    public void onScroll(int scrollY) {
        scrollPos = scrollY;
        if (scrollY < ScreenSizeUtil.dp2px(5)) {
            mTitleLayout.getBackground().setAlpha(255);
            mTitleBackImg.getBackground().setAlpha(255);
            mTitleShareImg.getBackground().setAlpha(255);
            mTitleHeadPortraitImg.setAlpha(255);
            mTopBgImg.setVisibility(View.VISIBLE);
            mTitleLayout.setVisibility(View.INVISIBLE);
        } else if (scrollY >= ScreenSizeUtil.dp2px(5) && scrollY < ScreenSizeUtil.dp2px(60)) {
        } else {
            mTitleLayout.getBackground().setAlpha(255);
            mTitleBackImg.getBackground().setAlpha(255);
            mTitleHeadPortraitImg.setAlpha(255);
            mTitleShareImg.getBackground().setAlpha(255);
            mTopBgImg.setVisibility(View.INVISIBLE);
            mTitleLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChangeAttentionStatus(boolean isSuccess) {
        if (isSuccess) {
            isFollow = !isFollow;
            if (isFollow) {
                mAttentionBtn.setText("已关注");
                mAttentionBtn.setSelected(true);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main_gray_01));
                ToastUtil.getInstance().toast("关注成功");
            } else {
                mAttentionBtn.setText("+关注");
                mAttentionBtn.setSelected(false);
                mAttentionBtn.setTextColor(getResources().getColor(R.color.color_white));
                ToastUtil.getInstance().toast("已取消关注");
            }
        }
    }

    @Override
    public void onGetDoctorDetailSuccess(DoctorDetail doctorDetail) {
        mDoctorDetail = doctorDetail;
        updateUI();
    }

    @OnClick({R.id.mTitleBackImg, R.id.mBackImg, R.id.mAttentionBtn, R.id.mShareImg, R.id.mTitleShareImg, R.id.mGoConsultBtn, R.id.mGoReservationBtn, R.id.mMoreCommentTv, R.id.mMoreGiftTv, R.id.mGiveGiftBtn, R.id.mDoctorTalkMoreTv,R.id.mDoctorHeadPortraitImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mTitleBackImg:
            case R.id.mBackImg:
                onBackPressed();
                break;
            case R.id.mTitleShareImg:
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
            case R.id.mGoConsultBtn:
                if (isLogin() && mDoctorDetail.isShowConsult()) {
                    mPresenter.checkImmediateConsult(mDoctorDetail.getEmrDoctor().getDoctorId());
                }
                break;
            case R.id.mGoReservationBtn:
                if (isLogin() && mDoctorDetail.isShowAppointment()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG, mDoctorDetail.getEmrDoctor());//getEmrDoctor()' on a null object reference
                    RouterActivityUtil.startActivity(this, OutpatientAppointmentActivity.class, bundle);
                }
                break;
            case R.id.mAttentionBtn:
                if (isLogin()) {
                    if (isFollow) {
                        showTipsAttentionDialog();
                    } else {
                        mPresenter.changeAttentionStatus(mDoctorDetail.getEmrDoctor().getDoctorId(), !isFollow);
                    }
                }
                break;
            case R.id.mMoreCommentTv:
                if (isLogin()&&mDoctorDetail.getCommentCount()>0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(RouterActivityUtil.FROM_TAG, mDoctorDetail.getEmrDoctor().getDoctorId());
                    RouterActivityUtil.startActivity(this, UserDoctorCommentActivity.class, bundle);
                }
                break;
            case R.id.mMoreGiftTv:
                RouterActivityUtil.startActivity(this, UserGiftActivity.class);
                break;
            case R.id.mGiveGiftBtn:
                RouterActivityUtil.startActivity(this, GiveGiftActivity.class);
                break;
            case R.id.mDoctorHeadPortraitImg:
                if(EmptyUtils.isNotEmpty(mDoctorDetail)&&EmptyUtils.isNotEmpty(mDoctorDetail.getEmrDoctor())){
                    List<PhotoFile> list = new ArrayList<>();
                    PhotoFile photoFile = new PhotoFile();
                    photoFile.setFileLink(mDoctorDetail.getEmrDoctor().getHeadImgUrl());
                    list.add(photoFile);
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(this,null,1,list);
                    showBigPicDialog.show();
                }
                break;
            case R.id.mDoctorTalkMoreTv:
                break;
        }
    }

    private void showTipsAttentionDialog() {
        AffirmDialog affirmDialog = new AffirmDialog(DoctorDetailActivity.this, "确定要取消关注吗？");
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

    @Override
    public void onCheckImmediateConsultSuccess(Doctor doctor) {
        if (!doctor.isAsk()) {
            if (doctor.isShowConsult()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, doctor);
                RouterActivityUtil.startActivity(DoctorDetailActivity.this, ImgTextCosultDetailActivity.class, bundle);
            } else {
                ToastUtil.getInstance().showToastDialog("该医生暂不提供图文咨询服务");
            }
        } else {
            Bundle bundle = new Bundle();
            PayOrderInfo payOrderInfo = new PayOrderInfo();
            payOrderInfo.setEmrDoctor(doctor);
            payOrderInfo.setOrderId(doctor.getOrderId());
            bundle.putSerializable(RouterActivityUtil.FROM_TAG, payOrderInfo);
            RouterActivityUtil.startActivity(DoctorDetailActivity.this, ChatActivity.class, bundle);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        mPresenter.getDoctorDetail(mDoctorId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AttentionChanStatusEvent event) {
        isFollow = (boolean) event.getData();
        if (isFollow) {
            mAttentionBtn.setText("已关注");
            mAttentionBtn.setSelected(true);
            mAttentionBtn.setTextColor(getResources().getColor(R.color.color_main_gray_01));
        } else {
            mAttentionBtn.setText("+关注");
            mAttentionBtn.setSelected(false);
            mAttentionBtn.setTextColor(getResources().getColor(R.color.color_white));
        }
    }

}