package com.bowen.tcm.login.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.StringUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Login;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.login.model.LoginModel;
import com.bowen.tcm.main.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/10.
 */

public class QuickLoginActivity extends BaseActivity {
    @BindView(R.id.mBackLayout)
    LinearLayout mBackLayout;
    @BindView(R.id.mLoginTv)
    TextView mLoginTv;
    @BindView(R.id.mChangeAccountTv)
    TextView mChangeAccountTv;

    private LoginModel mLoginModel;
    private boolean goMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_quick_login);
        ButterKnife.bind(this);
        mLoginModel = new LoginModel(this);
        mLoginTv.setText("账号：" + StringUtil.encryptShow(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_ACCOUNT,""),3,4));
        mChangeAccountTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            goMainActivity = bundle.getBoolean(RouterActivityUtil.FROM_TAG,true);
        }
    }

    @OnClick({R.id.mBackLayout, R.id.mLoginTv, R.id.mChangeAccountTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackLayout:
                onBackPressed();
                break;
            case R.id.mLoginTv:
                quickLogin();
                break;
            case R.id.mChangeAccountTv:
                RouterActivityUtil.startActivity(this,LoginActivity.class,true);
                break;
        }
    }

    private void quickLogin(){
        mLoginModel.quickLogin(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""), new HttpTaskCallBack<Login>() {
            @Override
            public void onSuccess(HttpResult<Login> result) {
                if(goMainActivity){
                    RouterActivityUtil.startActivity(QuickLoginActivity.this, MainActivity.class,true);
                } else {
                    finish();
                }
                EventBus.getDefault().post(new LoginEvent());
            }

            @Override
            public void onFail(HttpResult<Login> result) {

            }
        });
    }
}
