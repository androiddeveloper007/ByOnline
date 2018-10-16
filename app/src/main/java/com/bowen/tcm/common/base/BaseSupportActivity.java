package com.bowen.tcm.common.base;

import android.content.Intent;
import android.os.Bundle;

import com.bowen.commonlib.base.BaseLibSupportActivity;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.common.widget.SlidingExitLayout;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by AwenZeng on 2017/3/30.
 */

public class BaseSupportActivity extends BaseLibSupportActivity {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (enableSlidingClose()) {
            SlidingExitLayout rootView = new SlidingExitLayout(this);
            rootView.bindActivity(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 是否滑动关闭Activity
     * @return true:可以  false：不可以
     */
    protected boolean enableSlidingClose() {
        return true;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (enableSlidingClose()) {
            overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_out);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (enableSlidingClose()) {
            overridePendingTransition(R.anim.activity_slide_out, R.anim.activity_slide_right_out);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 登录判断
     * @return
     */
    public boolean isLogin(){
        if (LoginStatusUtil.getInstance().isLogin()) {
            if(UserInfo.getInstance().isBindPhone()){
                return true;
            }else {
                RouterActivityUtil.startActivity(this, BindingPhoneActivity.class);
                return false;
            }
        } else {
            if (EmptyUtils.isNotEmpty(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""))) {
                RouterActivityUtil.startActivity(this, QuickLoginActivity.class);
            } else {
                RouterActivityUtil.startActivity(this, LoginActivity.class);
            }
            return false;
        }
    }




}
