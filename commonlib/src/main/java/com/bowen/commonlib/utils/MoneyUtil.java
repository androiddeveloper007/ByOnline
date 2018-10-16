package com.bowen.commonlib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 金钱转换工具类
 * Created by AwenZeng on 2017/1/3.
 */

public class MoneyUtil {
    private static MoneyUtil instance;

    public static MoneyUtil getInstance() {
        if (instance == null) {
            synchronized (MoneyUtil.class) {
                if (instance == null) {
                    instance = new MoneyUtil();
                }
            }
        }
        return instance;
    }

    public String parseMoney(String pattern,BigDecimal bd){
        DecimalFormat df=new DecimalFormat(pattern);
        return df.format(bd);
    }

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public float formatTwoDecimal(float d) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return bg.floatValue();
    }

    /**
     *
     * @param money
     * @return des: 123,456,789
     */
    public String formatIntMoney(String money){
        if(!TextUtils.isEmpty(money)){
            BigDecimal bd = new BigDecimal(money);
            return parseMoney("###,###",bd);
        }
      return "";
    }

    /**
     *
     * @param money
     * @return des: 123,456,789.00
     */
    public String formatFloatMoney(String money){
        if(!TextUtils.isEmpty(money)){
            BigDecimal bd = new BigDecimal(money);
            return parseMoney("###,##0.00",bd);
        }
       return "0";
    }


    /**
     *
     * @param money
     * @return des: 123,456,789.0000
     */
    public String formatDoubleMoney(String money){
        if(!TextUtils.isEmpty(money)){
            BigDecimal bd = new BigDecimal(money);
            return parseMoney("###,##0.0000",bd);
        }
        return "0";
    }

    public  String formatMoney(String money) {
        String num_str;
        DecimalFormat decimalFormat = null;
        long num = Long.parseLong(money);
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat("#.00");
        }
        if (num > 1000000) {
            num_str = decimalFormat.format((double) num/1000000) + "百万";
        } else if (num > 10000) {
            num_str = decimalFormat.format((double) num/10000) + "万";
        } else {
            num_str = num + "";
        }
        return num_str;
    }
}
