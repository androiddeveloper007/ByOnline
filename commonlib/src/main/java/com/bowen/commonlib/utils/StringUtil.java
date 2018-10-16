package com.bowen.commonlib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AwenZeng on 2017/1/5.
 */

public class StringUtil {
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)  {
        return TextUtils.isEmpty(str);
    }

    /**
     * 如果为空，返回""
     * @param str
     * @return
     */
    public static String formatString(String str) {
        if(TextUtils.isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 如果为空，返回""
     * @param str
     * @return
     */
    public static String formatString(String str,String defaultStr) {
        if(TextUtils.isEmpty(str)) {
            return defaultStr;
        } else {
            return str;
        }
    }

    /**
     * 字符串转int
     * @param str
     * @return
     */
    public static int formatInt(String str) {
        if(TextUtils.isEmpty(str)) {
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * 字符串转int
     * @param str
     * @return
     */
    public static int formatInt(String str,int def) {
        if(TextUtils.isEmpty(str)) {
            return def;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * 字符串转int
     * @param str
     * @return
     */
    public static long formatLong(String str) {
        if(TextUtils.isEmpty(str)) {
            return 0;
        } else {
            return Long.parseLong(str);
        }
    }


    /**
     * 字符串转float
     * @param str
     * @return
     */
    public static double formatDouble(String str) {
        if(TextUtils.isEmpty(str)) {
            return 0;
        } else {
            return Double.parseDouble(str);
        }
    }

    /**
     * 字符串转float
     * @param str
     * @return
     */
    public static float formatFloat(String str) {
        if(TextUtils.isEmpty(str)) {
            return 0;
        } else {
            return Float.parseFloat(str);
        }
    }

    /**
     * 字符串转float
     * @param str
     * @return
     */
    public static float formatFloat(String str,float def) {
        if(TextUtils.isEmpty(str)) {
            return def;
        } else {
            return Float.parseFloat(str);
        }
    }
    public static String encryptShow(String str,int befrore,int after){
        return encryptShow(str,befrore,after,false);
    }
    /**
     * 字符串加*号显示
     * @param str  需要加*的字符串
     * @param befrore 前befrore位正常显示
     * @param after   后after位正常显示
     * @return
     */
    public static String encryptShow(String str,int befrore,int after,boolean isNeedBlank){
        if(TextUtils.isEmpty(str)){
            return "";
        }
        if(str.length()<befrore+after){
            return str;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0,befrore));
        builder.append(" ");
        String temp2 = str.substring(befrore,str.length()-after);
        int size = temp2.length();
        for(int i = 0;i<size;i++){
            if(isNeedBlank){
                builder.append(" ");
            }
           builder.append("*");
        }
        builder.append(" ");
        builder.append(str.substring(str.length()-after,str.length()));
        return builder.toString();
    }

    /**
     * 字符串加*号显示
     * @param str  需要加*的字符串
     * @param befrore 前befrore位正常显示
     * @param after   后after位正常显示
     * @return
     */
    public static String encryptNameShow(String str,int befrore,int after,boolean isNeedBlank){
        if(TextUtils.isEmpty(str)){
            return "";
        }
        if(str.length()<befrore+after){
            return str;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0,befrore));
        builder.append(" ");
        String temp2 = str.substring(befrore,str.length()-after);
        int size = temp2.length();
        for(int i = 0;i<size;i++){
            if(isNeedBlank){
                builder.append(" ");
            }
            if(size<=3){
                builder.append("*");
            }else{
                break;
            }
        }
        if(temp2.length() == 0&&str.length()==2){//两个字的时候
            builder.append("*");
        }else{
            builder.append(" ");
            builder.append(str.substring(str.length()-after,str.length()));
        }

        return builder.toString();
    }


    /**
     * 隐藏显示银行卡号
     * @param str
     * @return
     */
    public static String encryptBankNumShow(String str){
        if(TextUtils.isEmpty(str)){
            return "";
        }
        int size = str.length();
        int encryptSize = size - 4;
        int index = encryptSize/4;
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<index;i++){
            if(i == 0){
                builder.append(str.substring(0,4));
            }else{
                for(int j=0;j<4;j++){
                    builder.append("*");
                }
            }
            builder.append("   ");
        }
        if((encryptSize-index*4)>0){
            for(int i =0;i<encryptSize-index*4;i++){
                builder.append("*");
            }
            builder.append("   ");
        }
        builder.append(str.substring(encryptSize,size));
        return builder.toString();
    }


    /**
     * 显示银行卡号(不加密)
     * @param str
     * @return
     */
    public static String showBankNum(String str){
        if(TextUtils.isEmpty(str)){
            return "";
        }
        int size = str.length();
        int index = size/4;
        StringBuilder builder = new StringBuilder();
        for(int i = 0;i<index;i++){
            builder.append(str.substring(i*4,4*(i+1)));
            builder.append(" ");
            if(i == (index-1)){
                builder.append(str.substring(4*(i+1),size));
            }
        }
        return builder.toString();
    }

    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }

    /**
     * 显示距离
     * @param distance
     * @return
     */
    public static String showDistance(float distance){
        String distanceStr = "";
        if(distance>=0&&distance<1000){
            distanceStr = distance +"米";
        }else if(distance >= 1000){
            distanceStr = formatDistance(distance/1000+"") +"千米";
        }
        return distanceStr;
    }

    private static String formatDistance(String distance){
        if(!TextUtils.isEmpty(distance)){
            BigDecimal bd = new BigDecimal(distance);
            DecimalFormat df=new DecimalFormat("#####0.0");
            return df.format(bd);
        }
        return "0";
    }
}
