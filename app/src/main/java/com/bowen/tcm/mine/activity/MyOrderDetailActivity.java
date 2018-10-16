package com.bowen.tcm.mine.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.TraOrder;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.event.CancelOrDeleteOrderSuccessEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.activity.ChatActivity;
import com.bowen.tcm.inquiry.activity.DoctorDetailActivity;
import com.bowen.tcm.inquiry.activity.PayDetaitActivity;
import com.bowen.tcm.mine.contract.MyOrderDetailContract;
import com.bowen.tcm.mine.presenter.MyOrderDetailPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:我的订单详情
 * Created by zhuzhipeng on 2018/6/29.
 */

public class MyOrderDetailActivity extends BaseActivity implements MyOrderDetailContract.View {
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
    @BindView(R.id.orderNo)
    TextView orderNo;
    @BindView(R.id.payAmount)
    TextView payAmount;
    @BindView(R.id.payWay)
    TextView payWay;
    @BindView(R.id.orderTime)
    TextView orderTime;
    @BindView(R.id.immediateConsultTv)
    TextView immediateConsultTv;
    @BindView(R.id.cancelOrder)
    TextView cancelOrder;
    @BindView(R.id.continuePay)
    TextView continuePay;
    @BindView(R.id.deleteOrder)
    TextView deleteOrder;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.continueConsult)
    TextView continueConsult;
    private Activity mActivity;
    private String orderId;
    private MyOrderDetailPresenter mPresenter;
    private TraOrder mTraOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_my_order_detail);
        ButterKnife.bind(this);
        mActivity = this;
        mPresenter = new MyOrderDetailPresenter(this, this);
        orderId = RouterActivityUtil.getString(this);
        mPresenter.getTraOrderById(orderId);
    }

    private void updateUI() {
        if (EmptyUtils.isEmpty(mTraOrder))
            return;
        int orderStatus = mTraOrder.getOrderStatus();
        int orderType = mTraOrder.getOrderType();
        switch (orderStatus) { //1：待支付，2已支付，3：已取消，4：已结束，5：已删除
            case Constants.PAY_ORDER_STATUS_WAIT:
                myOrderDetailStatus.setText("待支付");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_wait);
                continuePay.setVisibility(View.VISIBLE);
                cancelOrder.setVisibility(View.VISIBLE);
                break;
            case Constants.PAY_ORDER_STATUS_FINISH:
                myOrderDetailStatus.setText("已支付");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_succ);
                immediateConsultTv.setVisibility(View.VISIBLE);
                break;
            case Constants.PAY_ORDER_STATUS_CANCEL:
                myOrderDetailStatus.setText("已取消");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_failed);
                deleteOrder.setVisibility(View.VISIBLE);
                break;
            case Constants.PAY_ORDER_STATUS_OVER:
                myOrderDetailStatus.setText("已结束");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_end);
                comment.setVisibility(View.VISIBLE);
                continueConsult.setVisibility(View.VISIBLE);
                break;
            case Constants.PAY_ORDER_STATUS_COMMENT:
                myOrderDetailStatus.setText("已评价");
                myOrderDetailStatusIcon.setImageResource(R.drawable.pay_end);
                comment.setVisibility(View.GONE);
                continueConsult.setVisibility(View.VISIBLE);
                break;
        }
        switch (orderType) {//01：图文问诊，02：送心意，03：门诊预约
            case Constants.TYPE_PRODUCT_CONSULT:
                mOrderTypeTv.setText("图文问诊");
                setTextViewDrawableLeft(mOrderTypeTv, R.drawable.picture_word_commend);
                break;
            case Constants.TYPE_PRODUCT_SEND_GIFT:
                mOrderTypeTv.setText("送心意");
                setTextViewDrawableLeft(mOrderTypeTv, R.drawable.picture_word_commend);
                break;
            case Constants.TYPE_PRODUCT_APPIONTMENT:
                mOrderTypeTv.setText("门诊预约");
                setTextViewDrawableLeft(mOrderTypeTv, R.drawable.appointment);
                break;
        }
        orderFee.setText(Constants.RMB + mTraOrder.getTotalAmount());
        ImageLoaderUtil.getInstance().show(this, mTraOrder.getFileLink(), doctorImg, R.drawable.img_bg, true);
        doctorName.setText(mTraOrder.getName());
        orderDepartment.setText(mTraOrder.getHospitalDepartments());
        doctorLevel.setText(mTraOrder.getPositionStr());
        hospitalName.setText(mTraOrder.getHospital());
        orderNo.setText(mTraOrder.getOrderId());
        payAmount.setText(Constants.RMB + mTraOrder.getRealAmount());
        switch (mTraOrder.getPayChannel()) {
            case "WECHAT":
                findViewById(R.id.payChannelLayout).setVisibility(View.VISIBLE);
                payWay.setText("微信支付");
                setTextViewDrawableLeft(payWay, R.drawable.weixin_pay);
                break;
            case "ALIPAY":
                findViewById(R.id.payChannelLayout).setVisibility(View.VISIBLE);
                payWay.setText("支付宝支付");
                setTextViewDrawableLeft(payWay, R.drawable.ali_pay);
                break;
            default:
                findViewById(R.id.payChannelLayout).setVisibility(View.GONE);
                break;
        }
        orderTime.setText(DateUtil.date2String(mTraOrder.getPayTime(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND));
    }

    @Override
    public void onLoadSuccess(TraOrder traOrder) {
        mTraOrder = traOrder;
        updateUI();
    }

    @OnClick({R.id.immediateConsultTv, R.id.cancelOrder, R.id.continuePay, R.id.deleteOrder,
            R.id.comment, R.id.continueConsult, R.id.backText})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.immediateConsultTv:
                continueConsult(bundle);
                break;
            case R.id.cancelOrder:
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
            case R.id.continuePay:
                PayOrderInfo payOrderInfo = new PayOrderInfo();
                payOrderInfo.setOrderType(mTraOrder.getOrderType());
                payOrderInfo.setAmount(mTraOrder.getRealAmount());
                payOrderInfo.setOrderId(mTraOrder.getOrderId());
                payOrderInfo.setPayChannel(mTraOrder.getPayChannel());
                Doctor doctorInfo = new Doctor();
                doctorInfo.setDoctorId(mTraOrder.getDoctorId());
                doctorInfo.setName(mTraOrder.getName());
                doctorInfo.setHospitalDepartments(mTraOrder.getHospitalDepartments());
                doctorInfo.setPositionStr(mTraOrder.getPositionStr());
                doctorInfo.setHospital(mTraOrder.getHospital());
                doctorInfo.setFileLink(mTraOrder.getFileLink());
                payOrderInfo.setEmrDoctor(doctorInfo);
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, payOrderInfo);
                RouterActivityUtil.startActivity(mActivity, PayDetaitActivity.class, bundle);
                break;
            case R.id.deleteOrder:
                AffirmDialog affirmDialog1 = new AffirmDialog(this, "确认删除订单？");
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
            case R.id.comment://评价
                bundle.putString(RouterActivityUtil.FROM_TAG, mTraOrder.getOrderId());
                RouterActivityUtil.startActivity(MyOrderDetailActivity.this, ServiceEvaluateActivity.class, bundle);
                break;
            case R.id.continueConsult:
                bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG, mTraOrder.getDoctorId());
                RouterActivityUtil.startActivity(mActivity, DoctorDetailActivity.class, bundle);
                break;
            case R.id.backText:
                onBackPressed();
                break;
        }
    }

    private void continueConsult(Bundle bundle) {
        PayOrderInfo payOrderInfo1 = new PayOrderInfo();
        Doctor doctor = new Doctor();
        doctor.setHeadImgUrl(mTraOrder.getFileLink());
        doctor.setDoctorId(mTraOrder.getDoctorId());
        doctor.setName(mTraOrder.getName());
        payOrderInfo1.setEmrDoctor(doctor);
        payOrderInfo1.setOrderId(mTraOrder.getOrderId());
        bundle.putSerializable(RouterActivityUtil.FROM_TAG, payOrderInfo1);
        RouterActivityUtil.startActivity(mActivity, ChatActivity.class, bundle);
    }

    @Override
    public void onLoadFail(TraOrder list) {

    }

    private void setTextViewDrawableLeft(TextView tv, int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 这一步必须要做,否则不会显示.
        tv.setCompoundDrawables(drawable, null, null, null);
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