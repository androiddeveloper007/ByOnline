package com.bowen.tcm.common.util;

import android.text.TextUtils;

import com.bowen.commonlib.utils.CheckStringUtl;
import com.bowen.commonlib.utils.ToastUtil;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class CheckFieldUtil {

    /**
     * 检测输入的字段
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("请输入手机号码");
            return false;
        } else if (!CheckStringUtl.isMobileNum(phoneNum)) {
            showToast("请输入11位数字的手机号码");
            return false;
        }
        return true;
    }

    /**
     * 检测输入的字段
     *
     * @param authCode
     * @return
     */
    public static boolean checkAuthCode(String authCode) {
        if (TextUtils.isEmpty(authCode)) {
            showToast("请输入验证码");
            return false;
        }else if(!CheckStringUtl.isAuthCode(authCode)){
            showToast("请输入正确格式的验证码");
            return false;
        }
        return true;
    }

    /**
     * 检测输入的字段
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            showToast("请输入登录密码");
            return false;
        } else if (!CheckStringUtl.isPassword(password)) {
            showToast("请输入6-20位数字、字母组合密码");
            return false;
        }
        return true;
    }




    private static void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
