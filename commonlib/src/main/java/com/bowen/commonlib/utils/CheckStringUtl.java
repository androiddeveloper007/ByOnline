package com.bowen.commonlib.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段检测工具类
 * Created by AwenZeng on 2016/12/7.
 */

public class CheckStringUtl {


    /**
     * 检测身份证号码是否合法
     * @param idCardNum
     * @return
     */
    public static boolean isIDCardNum(String idCardNum){
        try{
            String result = IDCardCheck.IDCardValidate(idCardNum);
            if(TextUtils.isEmpty(result)){
                return true;
            }
        }catch (Exception e){
           e.printStackTrace();
        }
        return false;
    }
    /**
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     * @param bankCard
     * @return
     */
    public static boolean isBankCardNum(String bankCard) {
        if(bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        return true;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    /**
     * 检测Email是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\.][A-Za-z]{2,3}([\\\\.][A-Za-z]{2})?$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 检测手机号码是否合法
     * 移动：139、138、137、136、135、134、159、158、157、150、151、152、147(数据卡)、188、187、182、183、184、178
     * 联通：130、131、132、156、155、186、185、145(数据卡)、176
     * 电信：133、153、189、180、181、177、173(待放)
     * @param phoneNum
     * @return
     */
    public static boolean isMobileNum(String phoneNum) {
        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[03678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    /**
     * 营业执照（15位）
     * @param num
     * @return
     */
    public static boolean isBLisence(String num){
        int size = num.length();
        if(size == 15){
            return true;
        }
        return false;
    }

    /**
     * 检测登录密码是否正确
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        int size = password.length();
        if(size>=6&&size<=20&&!isAllNumber(password)&&!isAllLetter(password)){
            return true;
        }
        return false;
    }

    /**
     * 检测验证码是否正确
     * @param authCode
     * @return
     */
    public static boolean isAuthCode(String authCode){
        int size = authCode.length();
        if(size == 6){
            return true;
        }
        return false;
    }

    /**
     * 检测交易密码是否正确
     * @param password
     * @return
     */
    public static boolean isDealPassword(String password){
        int size = password.length();
        if(size == 6){
            return true;
        }
        return false;
    }

    /**
     * 检测邮编是否合法
     * @param zipCode
     * @return
     */
    public static boolean isZipCode(String zipCode){
        Pattern p = Pattern.compile("^[1-9][0-9]{5}$");
        Matcher m = p.matcher(zipCode);
        return m.matches();
    }

    /**
     * 检测是否都是数字
     * @param str
     * @return
     */
    public static boolean isAllNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * 检测是否都是字母
     * @param str
     * @return
     */
    public static boolean isAllLetter(String str) {
        Pattern pattern = Pattern.compile("[A-Za-z]*");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }




    /**
     * 检测是否含有特殊符号
     * @param str
     * @return
     */
    public static boolean isHaveSpecialSymbol(String str) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检测是否含有特殊符号
     * @param str
     * @return
     */
    public static boolean isHtml(String str) {
        if(TextUtils.isEmpty(str)||!str.contains("http")){
            return false;
        }else if(str.contains("http")){
            return true;
        }
        return false;
    }

}
