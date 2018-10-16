package com.bowen.tcm.common.model;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/21.
 */

public class DateCaculateModle {

    public static final int START_YEAR = 1900;

    public static final int END_YEAR = 2100;

    private static String[] mMonths = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};

    public static String[] getHoursAndMinutes(int size) {
        String[] temp = new String[size];
        for(int i=0;i<size;i++){
            if(i<10){
                temp[i] = "0"+i;
            }else{
                temp[i] = i+"";
            }
        }
        return temp;
    }

    /**
     * 是否是平年
     * @param year
     * @return
     */
    public static boolean isCommonYear(int year) {

        if((year%4==0 && year%100!=0) || year%400==0 ){//闰年需要满足的条件：能被4整除但不能被100整除，或者能被400整除，满足其中一个即可
           return false;
        }else{
            return true;
        }
    }

    public static int getMaxDay(int year,int month){
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return isCommonYear(year)?28:29;//平年二月就28天，闰年29天
        }
        return 0;
    }

    public static String[] getYears(int startYear,int endYear){
        int yearCount = endYear-startYear + 1;
        String[] temp = new String[yearCount];
        for(int i = 0;i < yearCount;i++){
            temp[i] = startYear + i + "年";
        }
        return temp;
    }

    public static String[] getMonths(int endMonth){
        int monthCount = endMonth;
        String[] temp = new String[monthCount];
        for(int i = 0;i < monthCount;i++){
            temp[i] = i + 1 + "月";
        }
        return temp;
    }

    public static String[] getMonths(){
        return mMonths;
    }


    public static String[] getDays(int year,int month){
        int maxDay = getMaxDay(year,month);
        String[] temp = new String[maxDay];
        for(int i = 0;i < maxDay;i++){
            temp[i] = i+1 + "日";
        }
        return temp;
    }


    public static int getYear(String yearStr){
        return Integer.parseInt(yearStr.substring(0,yearStr.indexOf("年")));
    }


    public static String getMonth(String monthStr){
        int temp = Integer.parseInt(monthStr.substring(0,monthStr.indexOf("月")));
        if(temp>=10){
            return temp+"";
        }else{
            return "0"+temp;
        }
    }

    public static String getDay(String dayStr){
        int temp = Integer.parseInt(dayStr.substring(0,dayStr.indexOf("日")));
        if(temp>=10){
            return temp+"";
        }else{
            return "0"+temp;
        }
    }

}
