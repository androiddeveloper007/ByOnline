package com.bowen.tcm.mine.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.BowenApplication;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.event.LoginOutEvent;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.model.LoginModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/1.
 */

public class SafeSettingActivity extends BaseActivity {

    @BindView(R.id.appVersionTv)
    TextView appVersionTv;
    private LoginModel mLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_safe_setting);
        ButterKnife.bind(this);
        setTitle("设置");

        mLoginModel = new LoginModel(this);
        if (BowenApplication.DEBUG) {
            appVersionTv.setText("版本号:v" + ApplicationUtils.getVersionName() + "." + ApplicationUtils.getVersionCode());
        } else {
            appVersionTv.setText("版本号:v" + ApplicationUtils.getVersionName());
        }
    }

    @OnClick(R.id.mExitBtn)
    public void onViewClicked() {
        LoginStatusUtil.getInstance().setLogin(false);
        UserInfo.getInstance().setChatServerLoginSuccess(false);
        DataCacheUtil.getInstance().putString(DataCacheUtil.LOGIN_TOKEN,"");
        EventBus.getDefault().post(new LoginOutEvent());
        ChatServerManager.closeConnection();
        finish();
    }
}
