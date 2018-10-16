package com.bowen.tcm.common.util;

import com.bowen.tcm.common.bean.network.ThirdLogin;

/**
 * 登录状态工具类
 * Created by AwenZeng on 2016/12/21.
 */

public class LoginStatusUtil {

    private static LoginStatusUtil instance;

    private String platfrom;

    private ThirdLogin thirdLogin;

    private int loginErroCount;

    private boolean isLogin = false;

    public static LoginStatusUtil getInstance() {
        if (instance == null) {
            synchronized (LocationUtil.class) {
                if (instance == null) {
                    instance = new LoginStatusUtil();
                }
            }
        }
        return instance;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }


    public int getLoginErroCount() {
        return loginErroCount;
    }

    public void setLoginErroCount(int loginErroCount) {
        this.loginErroCount = loginErroCount;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }


    public ThirdLogin getThirdLogin() {
        return thirdLogin;
    }

    public void setThirdLogin(ThirdLogin thirdLogin) {
        this.thirdLogin = thirdLogin;
    }
}
