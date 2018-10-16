package com.bowen.tcm.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.OutpatientDetailRecord;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.event.CancelOrDeleteOrderSuccessEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.activity.DoctorDetailActivity;
import com.bowen.tcm.inquiry.activity.PayDetaitActivity;
import com.bowen.tcm.mine.contract.OutpatientDetailContract;
import com.bowen.tcm.mine.presenter.OutpatientDetailPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:门诊预约详情
 * Created by zhuzhipeng on 2018/6/29.
 */

public class OutpatientAppointmentDetailActivity extends BaseActivity implements OutpatientDetailContract.View {
    @BindView(R.id.myOrderDetailStatus)
    TextView myOrderDetailStatus;
    @BindView(R.id.myOrderDetailStatusIcon)
    ImageView myOrderDetailStatusIcon;
    @BindView(R.id.mOrderTypeTv)
    TextView mOrderTypeTv;
    @BindView(R.id.orderFee)
    TextView orderFee;
    @BindView(R.id.doctorImg)
    CircleImageView doctorImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.orderDepartment)
    TextView orderDepartment;
    @BindView(R.id.doctorLevel)
    TextView doctorLevel;
    @BindView(R.id.hospitalName)
    TextView hospitalName;
    @BindView(R.id.clinicName)
    TextView clinicName;
    @BindView(R.id.clinicAddress)
    TextView clinicAddress;
    @BindView(R.id.visitTime)
    TextView visitTime;
    @BindView(R.id.payWay)
    TextView payWay;
    @BindView(R.id.appointTime)
    TextView appointTime;
    @BindView(R.id.appointAgainTv)
    TextView appointAgainTv;
    @BindView(R.id.cancelAppoint)
    TextView cancelAppoint;
    @BindView(R.id.continuePay)
    TextView continuePay;
    @BindView(R.id.deleteAppointment)
    TextView deleteAppointment;
    @BindView(R.id.payChannelLayout)
    RelativeLayout payChannelLayout;
    private Activity mActivity;
    private String orderId;
    private OutpatientDetailPresenter mPresenter;
    private OutpatientDetailRecord mTraOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_outpatient_appointment_detail);
        ButterKnife.bind(this);
        mActivity = this;
        mPresenter = new OutpatientDetailPresenter(this, this);
        orderId = RouterActivityUtil.getString(this);
        mPresenter.getTraOrderById(orderId);
    }

    private void updateUI(){
        if(EmptyUtils.isEmpty(mTraOrder))
            return;
        int appointmentStatus = mTraOrder.getOrderList().getOrderStatus();
        int orderType = mTraOrder.getOrderList().getOrderType();
        switch (appointmentStatus) { //1：待支付 ，2：待就诊 ，3：已取消 ，4：已就诊 ，5：已过期
            case Constants.APPOINTMENT_PAY_WAIT:
                myOrderDetailStatus.setText("待支付");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_wait);
                continuePay.setVisibility(View.VISIBLE);
                cancelAppoint.setVisibility(View.VISIBLE);
                payChannelLayout.setVisibility(View.GONE);
                break;
            case Constants.APPOINTMENT_WAIT_SERVICE:
                myOrderDetailStatus.setText("待就诊");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_succ);
                findViewById(R.id.inflateView).setVisibility(View.GONE);
                break;
            case Constants.APPOINTMENT_CANCEL:
                myOrderDetailStatus.setText("已取消");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_failed);
                deleteAppointment.setVisibility(View.VISIBLE);
                payChannelLayout.setVisibility(View.GONE);
                break;
            case Constants.APPOINTMENT_HAD_SERVICE:
                myOrderDetailStatus.setText("已就诊");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_end);
                appointAgainTv.setVisibility(View.VISIBLE);
                break;
            case Constants.APPOINTMENT_END:
                myOrderDetailStatus.setText("已过期");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_end);
                appointAgainTv.setVisibility(View.VISIBLE);
                break;
        }
        switch (orderType) {//01：图文问诊，02：送心意，03：门诊预约
            case Constants.TYPE_PRODUCT_CONSULT:
                mOrderTypeTv.setText("图文问诊");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mActivity,mOrderTypeTv, R.drawable.picture_word_commend);
                break;
            case Constants.TYPE_PRODUCT_SEND_GIFT:
                mOrderTypeTv.setText("送心意");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mActivity,mOrderTypeTv, R.drawable.send_present);
                break;
            case Constants.TYPE_PRODUCT_APPIONTMENT:
                mOrderTypeTv.setText("门诊预约");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mActivity,mOrderTypeTv, R.drawable.appointment);
                break;
        }
        orderFee.setText(Constants.RMB + mTraOrder.getOrderList().getTotalAmount());
        ImageLoaderUtil.getInstance().show(this,mTraOrder.getOrderList().getFileLink(),doctorImg,R.drawable.img_bg,true);
        doctorName.setText(mTraOrder.getOrderList().getName());
        orderDepartment.setText(mTraOrder.getOrderList().getHospitalDepartments());
        doctorLevel.setText(mTraOrder.getOrderList().getPositionStr());
        hospitalName.setText(mTraOrder.getOrderList().getHostipal());
        clinicName.setText(mTraOrder.getClinic());
        clinicAddress.setText(mTraOrder.getClinicAddress());
        visitTime.setText(mTraOrder.getVisitTimeStr());
        appointTime.setText(DateUtil.date2String(mTraOrder.getOrderList().getPayTime(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND));
        switch (mTraOrder.getOrderList().getPayChannel()) {
            case "WECHAT":
                findViewById(R.id.payChannelLayout).setVisibility(View.VISIBLE);
                payWay.setText("微信支付");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mActivity,payWay, R.drawable.weixin_pay);
                break;
            case "ALIPAY":
                findViewById(R.id.payChannelLayout).setVisibility(View.VISIBLE);
                payWay.setText("支付宝支付");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mActivity,payWay, R.drawable.ali_pay);
                break;
            default:
                findViewById(R.id.payChannelLayout).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onLoadSuccess(OutpatientDetailRecord record) {
        mTraOrder = record;
        updateUI();
    }

    @OnClick({R.id.appointAgainTv, R.id.cancelAppoint, R.id.continuePay, R.id.deleteAppointment,
            R.id.backText})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.appointAgainTv://再次预约
                bundle.putString(RouterActivityUtil.FROM_TAG, mTraOrder.getOrderList().getDoctorId());
                RouterActivityUtil.startActivity(mActivity, DoctorDetailActivity.class,bundle);
                break;
            case R.id.cancelAppoint://取消预约
                AffirmDialog affirmDialog = new AffirmDialog(this, "确认取消订单？");
                affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                    @Override
                    public void onCancle() {
                    }
                    @Override
                    public void onOK() {
                        mPresenter.cancelTraOrder(orderId, "3");
                    }
                });
                affirmDialog.show();
                break;
            case R.id.continuePay://继续支付
                PayOrderInfo payOrderInfo = new PayOrderInfo();
                payOrderInfo.setOrderType(mTraOrder.getOrderList().getOrderType());
                payOrderInfo.setAmount(mTraOrder.getOrderList().getRealAmount());
                payOrderInfo.setOrderId(mTraOrder.getOrderList().getOrderId());
                payOrderInfo.setPayChannel(mTraOrder.getOrderList().getPayChannel());
                Doctor doctorInfo = new Doctor();
                doctorInfo.setDoctorId(mTraOrder.getOrderList().getDoctorId());
                doctorInfo.setName(mTraOrder.getOrderList().getName());
                doctorInfo.setHospitalDepartments(mTraOrder.getOrderList().getHospitalDepartments());
                doctorInfo.setPositionStr(mTraOrder.getOrderList().getPositionStr());
                doctorInfo.setHospital(mTraOrder.getClinic());
                doctorInfo.setFileLink(mTraOrder.getOrderList().getFileLink());
                payOrderInfo.setEmrDoctor(doctorInfo);
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, payOrderInfo);
                RouterActivityUtil.startActivity(mActivity, PayDetaitActivity.class, bundle);
                break;
            case R.id.deleteAppointment://删除
                AffirmDialog affirmDialog1 = new AffirmDialog(this, "确认删除预约？");
                affirmDialog1.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                    @Override
                    public void onCancle() {
                    }
                    @Override
                    public void onOK() {
                        mPresenter.cancelTraOrder(orderId, "5");
                    }
                });
                affirmDialog1.show();
                break;
            case R.id.backText:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onLoadFail(OutpatientDetailRecord list) {

    }

    @Override
    public void cancelOrderSucc() {
        EventBus.getDefault().post(new CancelOrDeleteOrderSuccessEvent());
        finish();
    }

    @Override
    public void cancelOrderFail() {

    }
}