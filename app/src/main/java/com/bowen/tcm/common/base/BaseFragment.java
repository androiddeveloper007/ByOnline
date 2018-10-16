package com.bowen.tcm.common.base;


import com.bowen.commonlib.base.BaseLibFragment;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;

/**
 * Created by AwenZeng on 2017/3/31.
 */

public class BaseFragment extends BaseLibFragment {

    public boolean isLogin() {
        if (LoginStatusUtil.getInstance().isLogin()) {
            if (UserInfo.getInstance().isBindPhone()) {
                return true;
            } else {
                RouterActivityUtil.startActivity(mActivity, BindingPhoneActivity.class);
                return false;
            }

        } else {
            if (EmptyUtils.isNotEmpty(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""))) {
                RouterActivityUtil.startActivity(mActivity, QuickLoginActivity.class);
            } else {
                RouterActivityUtil.startActivity(mActivity, LoginActivity.class);
            }
            return false;
        }
    }
}
