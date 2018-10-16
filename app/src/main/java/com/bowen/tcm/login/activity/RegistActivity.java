package com.bowen.tcm.login.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.callback.BanCopyPasteCallback;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.util.CheckFieldUtil;
import com.bowen.tcm.login.contract.RegistContract;
import com.bowen.tcm.login.model.RegistModel;
import com.bowen.tcm.login.presenter.RegistPresenter;
import com.bowen.tcm.main.activity.BrowserActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity implements RegistContract.View {

    @BindView(R.id.mBackLayout)
    LinearLayout mBackLayout;
    @BindView(R.id.mPhoneNumEdit)
    EditText mPhoneNumEdit;
    @BindView(R.id.mAuthCodeEdit)
    EditText mAuthCodeEdit;
    @BindView(R.id.mGetAuthCodeBtn)
    TextView mGetAuthCodeBtn;
    @BindView(R.id.mPasswordEdit)
    EditText mPasswordEdit;
    @BindView(R.id.mRegistBtn)
    TextView mRegistBtn;
    @BindView(R.id.registChooseAgreeCheckBox)
    CheckBox registChooseAgreeCheckBox;
    @BindView(R.id.registAgentProtocolTv)
    TextView registAgentProtocolTv;

    private RegistPresenter mRegistPresenter;
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
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mRegistPresenter = new RegistPresenter(this, this);
        registAgentProtocolTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mPasswordEdit.setCustomSelectionActionModeCallback(new BanCopyPasteCallback());
        mPasswordEdit.setLongClickable(false);
        mPasswordEdit.setTextIsSelectable(false);
        changLoginBtnStatus();
        registChooseAgreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChooseProtocol = isChecked;
            }
        });
        mPhoneNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changLoginBtnStatus();
            }
        });

        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changLoginBtnStatus();
            }
        });

        mAuthCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changLoginBtnStatus();
            }
        });
    }

    private void getInputData() {
        mPhoneNum = mPhoneNumEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();
        mAuthCode = mAuthCodeEdit.getText().toString();
    }

    private void changLoginBtnStatus() {
        getInputData();
        if (EmptyUtils.isNotEmpty(mPhoneNum) && EmptyUtils.isNotEmpty(mPassword)&&EmptyUtils.isNotEmpty(mAuthCode)) {
            mRegistBtn.setEnabled(true);
            mRegistBtn.setBackgroundResource(R.drawable.button_main);
        } else {
            mRegistBtn.setEnabled(false);
            mRegistBtn.setBackgroundResource(R.drawable.button_main_no_click);
        }
    }

    @OnClick({R.id.mBackLayout, R.id.mGetAuthCodeBtn, R.id.mRegistBtn, R.id.registAgentProtocolTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackLayout:
                onBackPressed();
                break;
            case R.id.mGetAuthCodeBtn:
                getInputData();
                if (CheckFieldUtil.checkPhoneNum(mPhoneNum)) {
                    mRegistPresenter.checkAccount(mPhoneNum, Constants.TYPE_CHECK_ACCOUNT_PHONE);
                }
                break;
            case R.id.mRegistBtn:
                getInputData();
                if (mRegistPresenter.checkContent(mPhoneNum, mPassword, mAuthCode, isChooseProtocol)) {
                    mRegistPresenter.regist(mPhoneNum, mPassword, mAuthCode);
                }
                break;
            case R.id.registAgentProtocolTv:
                Bundle bundle = new Bundle();
                bundle.putString(BrowserActivity.URL, AppConfigInfo.getInstance().getLicenseUrl());
                RouterActivityUtil.startActivity(RegistActivity.this, BrowserActivity.class, bundle);
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
    public void onCheckAccountSuccess(boolean isRegist) {
        if (!isRegist) {
            mRegistPresenter.getAuthCode(mPhoneNum, RegistModel.AUTHCODE_MESSAGE, Constants.AUTHCODE_LOGIN_PASSWORD_REGIST);
            changeGetAuthCodeBtnStatus(true);
        } else {
            showLoginDialog();
        }
    }

    private void showLoginDialog() {
        AffirmDialog dialog = new AffirmDialog(this, "温馨提示", mPhoneNum + "手机号已被注册，请直接登录", "取消", "去登陆");
        dialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
            @Override
            public void onCancle() {

            }

            @Override
            public void onOK() {
                RouterActivityUtil.startActivity(RegistActivity.this, LoginActivity.class, true);
            }
        });
        dialog.show();
    }

    @Override
    public void onRegistSuccess() {
        DataCacheUtil.getInstance().putString(DataCacheUtil.LOGIN_ACCOUNT, mPhoneNum);
        ToastUtil.getInstance().toast(String.format("欢迎您注册为%s用户", getString(R.string.app_name)));
        finish();
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
