package com.bowen.tcm.login.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.callback.BanCopyPasteCallback;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.login.contract.VerifiPhoneContract;
import com.bowen.tcm.login.model.RegistModel;
import com.bowen.tcm.login.presenter.VerifiPhonePresenter;
import com.bowen.tcm.main.activity.BrowserActivity;
import com.bowen.tcm.main.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/10.
 */

public class VerifiPhoneActivity extends BaseActivity implements VerifiPhoneContract.View {
    @BindView(R.id.mBackLayout)
    LinearLayout mBackLayout;
    @BindView(R.id.mAuthCodeEdit)
    EditText mAuthCodeEdit;
    @BindView(R.id.mGetAuthCodeBtn)
    TextView mGetAuthCodeBtn;
    @BindView(R.id.mPasswordEdit)
    EditText mPasswordEdit;
    @BindView(R.id.mFinishVerifiTv)
    TextView mFinishVerifiTv;
    @BindView(R.id.mChooseAgreeCheckBox)
    CheckBox mChooseAgreeCheckBox;
    @BindView(R.id.mAgentProtocolTv)
    TextView mAgentProtocolTv;
    private VerifiPhonePresenter mPresenter;
    private String mPhoneNum;
    private String mPassword;
    private String mAuthCode;
    private boolean isChooseProtocol = false;
    private final int DEFULT_TIME = 60;
    private int time = DEFULT_TIME;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            if (time > 0) {
                mGetAuthCodeBtn.setText("重新获取" + time + "秒");
                mHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mGetAuthCodeBtn.setEnabled(true);
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.button_main_02);
                mGetAuthCodeBtn.setTextColor(getResources().getColor(R.color.color_main));
                mGetAuthCodeBtn.setText("重新获取");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_verifi_phone);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            mPhoneNum = bundle.getString(RouterActivityUtil.FROM_TAG);
        }
        mAgentProtocolTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mPresenter = new VerifiPhonePresenter(this, this);
        mPasswordEdit.setCustomSelectionActionModeCallback(new BanCopyPasteCallback());
        mPasswordEdit.setLongClickable(false);
        mPasswordEdit.setTextIsSelectable(false);
        mChooseAgreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChooseProtocol = isChecked;
            }
        });
    }

    private void getInputData() {
        mPassword = mPasswordEdit.getText().toString();
        mAuthCode = mAuthCodeEdit.getText().toString();
    }

    @OnClick({R.id.mBackLayout, R.id.mGetAuthCodeBtn, R.id.mFinishVerifiTv,R.id.mAgentProtocolTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackLayout:
                onBackPressed();
                break;
            case R.id.mGetAuthCodeBtn:
                mPresenter.getAuthCode(mPhoneNum, RegistModel.AUTHCODE_MESSAGE, Constants.AUTHCODE_LOGIN_PASSWORD_REGIST);
                changeGetAuthCodeBtnStatus(true);
                break;
            case R.id.mFinishVerifiTv:
                getInputData();
                if (mPresenter.checkContent(mPassword, mAuthCode,isChooseProtocol)) {
                    mPresenter.regist(mPhoneNum, mPassword, mAuthCode);
                }
                break;
            case R.id.mAgentProtocolTv:
                Bundle bundle = new Bundle();
                bundle.putString(BrowserActivity.URL, AppConfigInfo.getInstance().getLicenseUrl());
                RouterActivityUtil.startActivity(VerifiPhoneActivity.this, BrowserActivity.class, bundle);
                break;
        }
    }

    private void changeGetAuthCodeBtnStatus(boolean isGetAuthCode) {
        if (isGetAuthCode) {
            mGetAuthCodeBtn.setBackgroundResource(R.drawable.button_main_04);
            mGetAuthCodeBtn.setTextColor(getResources().getColor(R.color.color_main_gray));
            time = DEFULT_TIME;
            mGetAuthCodeBtn.setText("重新获取" + time + "秒");
            mGetAuthCodeBtn.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            mHandler.removeCallbacksAndMessages(null);
            mGetAuthCodeBtn.setEnabled(true);
            mGetAuthCodeBtn.setBackgroundResource(R.drawable.button_main_02);
            mGetAuthCodeBtn.setTextColor(getResources().getColor(R.color.color_main));
            mGetAuthCodeBtn.setText("获取验证码");
        }
    }

    @Override
    public void onVerifiSuccess() {
        RouterActivityUtil.startActivity(this, MainActivity.class, true);
        EventBus.getDefault().post(new LoginEvent());
    }

    @Override
    public void onGetAuthCodeFailed() {
        changeGetAuthCodeBtnStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
