package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.EnvUtils.EnvEnum;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.MoneyUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.PayInfo;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.event.PayResultEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.pay.PayManager;
import com.bowen.tcm.inquiry.model.PayModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bowen.tcm.common.model.Constants.TYPE_PAYMENT_ALIPAY;
import static com.bowen.tcm.common.model.Constants.TYPE_PAYMENT_NO;
import static com.bowen.tcm.common.model.Constants.TYPE_PAYMENT_WECHAPAY;
import static com.bowen.tcm.common.model.Constants.TYPE_PRODUCT_APPIONTMENT;
import static com.bowen.tcm.common.model.Constants.TYPE_PRODUCT_CONSULT;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/3.
 */
public class PayDetaitActivity extends BaseActivity {
    @BindView(R.id.mPayProductImg)
    ImageView mPayProductImg;
    @BindView(R.id.mPayProductNameTv)
    TextView mPayProductNameTv;
    @BindView(R.id.mPayProductPriceTv)
    TextView mPayProductPriceTv;
    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDepartmentTv)
    TextView mDoctorDepartmentTv;
    @BindView(R.id.mDoctorRankTv)
    TextView mDoctorRankTv;
    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mWechatPayImg)
    ImageView mWechatPayImg;
    @BindView(R.id.mWechatPayCheckBox)
    CheckBox mWechatPayCheckBox;
    @BindView(R.id.mAliPayImg)
    ImageView mAliPayImg;
    @BindView(R.id.mAliPayCheckBox)
    CheckBox mAliPayCheckBox;
    @BindView(R.id.mTotalPayTitleTv)
    TextView mTotalPayTitleTv;
    @BindView(R.id.mTotalPayTv)
    TextView mTotalPayTv;
    @BindView(R.id.mWechatPayLayout)
    RelativeLayout mWechatPayLayout;
    @BindView(R.id.mAlipayLayout)
    RelativeLayout mAlipayLayout;
    @BindView(R.id.mImmediatePayTv)
    TextView mImmediatePayTv;

    private PayOrderInfo mPayOrderInfo;
    private HashMap<String, String> mParamMap;
    private PayModel mPayModel;

    private int mPayType = Constants.TYPE_PAYMENT_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_pay_detail);
        ButterKnife.bind(this);
        setTitle("支付");
        mPayOrderInfo = (PayOrderInfo) RouterActivityUtil.getSerializable(this);
        mPayModel = new PayModel(this);
        mParamMap = new HashMap<>();
        mAliPayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mWechatPayCheckBox.setChecked(false);
                    mPayType = TYPE_PAYMENT_ALIPAY;
                }
            }
        });

        mWechatPayCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAliPayCheckBox.setChecked(false);
                    mPayType = TYPE_PAYMENT_WECHAPAY;
                }
            }
        });
        updateUI();
    }

    private void updateUI() {
        if (EmptyUtils.isNotEmpty(mPayOrderInfo)){
            Doctor doctor = mPayOrderInfo.getEmrDoctor();
            mPayProductPriceTv.setText(Constants.RMB + MoneyUtil.getInstance().formatFloatMoney(mPayOrderInfo.getAmount()));
            switch (mPayOrderInfo.getOrderType()){
                case TYPE_PRODUCT_CONSULT:
                    mPayProductImg.setBackgroundResource(R.drawable.inquiry_img_text_consult);
                    mPayProductNameTv.setText("图文咨询");
                    break;
                case TYPE_PRODUCT_APPIONTMENT:
                    mPayProductImg.setBackgroundResource(R.drawable.inquiry_reservation);
                    mPayProductNameTv.setText("门诊预约");
                    break;
            }
            if (EmptyUtils.isNotEmpty(mPayOrderInfo.getPayChannel())) {
                switch (mPayOrderInfo.getPayChannel()) {
                    case "WECHAT":
                        mWechatPayLayout.setVisibility(View.VISIBLE);
                        mAlipayLayout.setVisibility(View.GONE);
                        mWechatPayCheckBox.setChecked(true);
                        break;
                    case "ALIPAY":
                        mWechatPayLayout.setVisibility(View.GONE);
                        mAlipayLayout.setVisibility(View.VISIBLE);
                        mAliPayCheckBox.setChecked(true);
                        break;
                }
            }
            ImageLoaderUtil.getInstance().show(this, doctor.getFileLink(), mHeadPortraitImg, R.drawable.man);
            mDoctorNameTv.setText(doctor.getName());
            mDoctorDepartmentTv.setText(doctor.getHospitalDepartments());
            mDoctorRankTv.setText(doctor.getPositionStr());
            mClinicNameTv.setText(doctor.getHospital());
            mTotalPayTv.setText(Constants.RMB + MoneyUtil.getInstance().formatFloatMoney(mPayOrderInfo.getAmount()));
        }
    }

    @OnClick(R.id.mImmediatePayTv)
    public void onViewClicked() {
        if (mPayType != TYPE_PAYMENT_NO) {
            getPayInfo();
        } else {
            ToastUtil.getInstance().showToastDialog("请选择支付方式");
        }
    }

    private void getPayInfo() {
        if (EmptyUtils.isEmpty(mPayOrderInfo))
            return;
        mPayModel.getPayOrderInfo(mPayType, mPayOrderInfo, new HttpTaskCallBack<PayInfo>() {
            @Override
            public void onSuccess(HttpResult<PayInfo> result) {
                PayManager payManager = new PayManager(PayDetaitActivity.this, mPayType, result.getData().getPayResult());
                payManager.pay();
            }

            @Override
            public void onFail(HttpResult<PayInfo> result) {
                ToastUtil.getInstance().toast(result.getMsg());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PayResultEvent event) {
        mPayOrderInfo.setPaySuccess(event.isPaySuccess());
        if (event.isPaySuccess()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RouterActivityUtil.FROM_TAG, mPayOrderInfo);
            RouterActivityUtil.startActivity(PayDetaitActivity.this, PayResultActivity.class, bundle, true);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RouterActivityUtil.FROM_TAG, mPayOrderInfo);
            RouterActivityUtil.startActivity(PayDetaitActivity.this, PayResultActivity.class, bundle);
        }

    }

}
