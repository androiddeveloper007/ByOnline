package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog.BaseDialogListener;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.bean.SubmitAppointmentInfo;
import com.bowen.tcm.common.bean.SubmitOrderInfo;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.dialog.ChooseMemberDialog;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.model.PayModel;
import com.bowen.tcm.mine.activity.AddFamilyMemberActivity;
import com.bowen.tcm.mine.model.FamilyMemberModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:提交预约
 * Created by AwenZeng on 2018/7/12.
 */
public class SubmitReservationActivity extends BaseActivity {

    @BindView(R.id.mChooseMemberTipsTv)
    TextView mChooseMemberTipsTv;
    @BindView(R.id.mArrowRightImg)
    ImageView mArrowRightImg;
    @BindView(R.id.mMemberHeadPortraitImg)
    CircleImageView mMemberHeadPortraitImg;
    @BindView(R.id.mMemberNameTv)
    TextView mMemberNameTv;
    @BindView(R.id.mMemberAgeTv)
    TextView mMemberAgeTv;
    @BindView(R.id.mMemberInfoLayout)
    LinearLayout mMemberInfoLayout;
    @BindView(R.id.mChooseMemberLayout)
    RelativeLayout mChooseMemberLayout;
    @BindView(R.id.mAddFamilyMemberTv)
    TextView mAddFamilyMemberTv;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mClinicAddressTv)
    TextView mClinicAddressTv;
    @BindView(R.id.mReservationFeeTv)
    TextView mReservationFeeTv;
    @BindView(R.id.mTreateDateTv)
    TextView mTreateDateTv;
    @BindView(R.id.mTreatePeriodTv)
    TextView mTreatePeriodTv;
    @BindView(R.id.mSubminReservationBtn)
    TextView mSubminReservationBtn;


    private FamilyMemberModel mFamilyMemberModel;
    private FamilyMember mChooseFamilyMember;
    private SubmitOrderInfo mSubmitOrderInfo;
    private SubmitAppointmentInfo mSubmitAppointmentInfo;
    private PayModel mPayModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_submit_reservation);
        ButterKnife.bind(this);
        setTitle("提交预约");
        mPayModel = new PayModel(this);
        mSubmitAppointmentInfo = (SubmitAppointmentInfo) RouterActivityUtil.getSerializable(this);
        mFamilyMemberModel = new FamilyMemberModel(this);
        mFamilyMemberModel.getFamilyUserRelationShip();
        if(EmptyUtils.isNotEmpty(mSubmitAppointmentInfo)){
            mDoctorNameTv.setText(mSubmitAppointmentInfo.getDoctorName()+"  "+mSubmitAppointmentInfo.getDoctorPosRank());
            mClinicNameTv.setText(mSubmitAppointmentInfo.getClinicName());
            mClinicAddressTv.setText(mSubmitAppointmentInfo.getClinicAddress());
            mReservationFeeTv.setText(mSubmitAppointmentInfo.getProductPrice() +"元");
            mTreateDateTv.setText(DateUtil.date2String(mSubmitAppointmentInfo.getAppointmentDate(),DateUtil.DEFAULT_FORMAT_DATE));
            mTreatePeriodTv.setText(mSubmitAppointmentInfo.getTimePeriod());
        }
    }


    @OnClick({R.id.mChooseMemberLayout, R.id.mAddFamilyMemberTv, R.id.mSubminReservationBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChooseMemberLayout:
                ChooseMemberDialog chooseMemberDialog = new ChooseMemberDialog(this);
                chooseMemberDialog.show();
                chooseMemberDialog.setBaseDialogListener(new BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mChooseFamilyMember = (FamilyMember) obj[0];
                        mMemberInfoLayout.setVisibility(View.VISIBLE);
                        ImageLoaderUtil.getInstance().show(SubmitReservationActivity.this, mChooseFamilyMember.getHeadSculptureUrl(),
                                mMemberHeadPortraitImg, R.drawable.man);
                        mMemberNameTv.setText(mChooseFamilyMember.getFamilyNickname());
                        if(EmptyUtils.isNotEmpty(mChooseFamilyMember.getAge())){
                            mMemberAgeTv.setText(mChooseFamilyMember.getAge() + "岁");
                        }else{
                            mMemberAgeTv.setText("未知");
                        }
                    }
                });
                break;
            case R.id.mAddFamilyMemberTv:
                RouterActivityUtil.startActivity(this, AddFamilyMemberActivity.class);
                break;
            case R.id.mSubminReservationBtn:
                if (checkContent()) {
                    addSubmitOrderInfo();
                    createOrder();
                }
                break;
        }
    }

    private boolean checkContent() {
        if (EmptyUtils.isEmpty(mChooseFamilyMember)) {
            ToastUtil.getInstance().showToastDialog("请选选择就诊人");
            return false;
        }
        return true;
    }

    private void addSubmitOrderInfo(){
        mSubmitOrderInfo = new SubmitOrderInfo();
        if(EmptyUtils.isNotEmpty(mSubmitAppointmentInfo)&&EmptyUtils.isNotEmpty(mChooseFamilyMember)){
            mSubmitOrderInfo.setDoctorId(mSubmitAppointmentInfo.getDoctorId());
            mSubmitOrderInfo.setPrice(mSubmitAppointmentInfo.getProductPrice());
            mSubmitOrderInfo.setOrderType(Constants.TYPE_PRODUCT_APPIONTMENT+"");
            mSubmitOrderInfo.setFamilyId(mChooseFamilyMember.getFamilyId());
            mSubmitOrderInfo.setAppointmentId(mSubmitAppointmentInfo.getAppointmentId());
        }
    }


    private void createOrder(){
        mPayModel.createPayOrder(mSubmitOrderInfo, new HttpTaskCallBack<PayOrderInfo>() {
            @Override
            public void onSuccess(HttpResult<PayOrderInfo> result) {
                if(result.getData().isPay()){
                    PayOrderInfo payOrderInfo = result.getData();
                    payOrderInfo.setPaySuccess(true);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo);
                    RouterActivityUtil.startActivity(SubmitReservationActivity.this, PayResultActivity.class,bundle,true);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG,result.getData());
                    RouterActivityUtil.startActivity(SubmitReservationActivity.this, PayDetaitActivity.class,bundle,true);
                }
            }

            @Override
            public void onFail(HttpResult<PayOrderInfo> result) {
                ToastUtil.getInstance().toast(result.getMsg());
            }
        });
    }
}
