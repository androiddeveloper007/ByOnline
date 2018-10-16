package com.bowen.tcm.common.model;

import com.awen.camera.util.ScreenSizeUtil;

/**
 * 常量类
 * Created by AwenZeng on 2016/12/2.
 */

public class Constants {

    // app 根目录
    public static final String FILE_SYSTEM_CAMERA_ROOT = "/DCIM";
    // app 根目录
    public static final String FILE_DIR_ROOT = "/BoWen";
    /**
     * 图片保存路径
     */
    public static final String FILE_DIR_TEMP_PIC = FILE_DIR_ROOT + "/Temp/Pic/";

    /**
     * 默认照片的宽
     */
    public static final int DEFAULT_PHOTO_WIDTH = ScreenSizeUtil.getScreenWidth();
    /**
     * 默认照片高
     */
    public static final int DEFAULT_PHOTO_HEIGHT = ScreenSizeUtil.getScreenHeight();

    public static final String CHAT_OVER_TAG = "#*C#*O#*L#*S#*E#*";


    public static final int NULL = 999;

    public static final String RMB = "¥";

    /**
     * 获取验证码 业务类型：0注册,1修改密码,2找回密码,3设置交易密码,4修改交易密码
     */
    public static final int AUTHCODE_LOGIN_PASSWORD_REGIST = 0;//注册
    public static final int AUTHCODE_LOGIN_PASSWORD_FIND = 1;//找回密码
    public static final int AUTHCODE_LOGIN_AUTHCODE = 3;//验证码登录

    //上传图片类型
    public static final int TYPE_UPLOAD_PHOTO_FAMILY_MEMBER = 1;//家庭成员头像
    public static final int TYPE_UPLOAD_PHOTO_MEDICAL_FILE = 2;//病历文件
    public static final int TYPE_UPLOAD_PHOTO_FEEDBACK = 3;//反馈意见


    //病历查看权限
    public static final int MEDICAL_RECORD_PERMISSION_ALL = 1;//所有权限
    public static final int MEDICAL_RECORD_PERMISSION_LOOL = 2;//查看权限


    //检测账号类型
    public static final String TYPE_CHECK_ACCOUNT_PHONE = "0";
    public static final String TYPE_CHECK_ACCOUNT_WECHAT = "1";
    public static final String TYPE_CHECK_ACCOUNT_QQ = "2";

    //第三方登录类型
    public static final int TYPE_LOGIN_ACCOUNT_WECHAT = 1;
    public static final int TYPE_LOGIN_ACCOUNT_QQ = 2;

    //提醒重复周期
    public static final int TYPE_REMIND_REPEAT_ONETIME      = 0;//一次
    public static final int TYPE_REMIND_REPEAT_EVERYDAY     = 1;//每天
    public static final int TYPE_REMIND_REPEAT_INTERVAL1DAY = 2;//每隔1天
    public static final int TYPE_REMIND_REPEAT_INTERVAL2DAY = 3;//每隔2天
    public static final int TYPE_REMIND_REPEAT_INTERVAL3DAY = 4;//每隔3天
    public static final int TYPE_REMIND_REPEAT_INTERVAL4DAY = 5;//每隔4天
    public static final int TYPE_REMIND_REPEAT_INTERVAL5DAY = 6;//每隔5天
    public static final int TYPE_REMIND_REPEAT_EVERYWEEK    = 7;//每周
    public static final int TYPE_REMIND_REPEAT_EVERYMONTH   = 8;//每月

    //闹铃类型
    public static final int TYPE_ALARM_NOTHING           = 0;//铃声
    public static final int TYPE_ALARM_SOUND             = 1;//铃声
    public static final int TYPE_ALARM_VIBRATOR          = 2;//震动
    public static final int TYPE_ALARM_SOUNDVIBRATOR     = 3;//铃声 + 震动


    //咨询
    public static final int TYPE_NEWS_ITEM_TOP       = 0;
    public static final int TYPE_NEWS_ITEM_TIPS      = 1;
    public static final int TYPE_NEWS_ITEM_BIG       = 2;
    public static final int TYPE_NEWS_ITEM_SMALL     = 3;
    public static final int TYPE_NEWS_ITEM_COLUMN    = 4;


    //消息状态
    public static final int MESSAGE_STATUS_NOT_HANDLE    = 1;
    public static final int MESSAGE_STATUS_FINISH        = 2;
    public static final int MESSAGE_STATUS_IGNORE        = 3;

    //消息类型
    public static final int MESSAGE_TYPE_ALARM_REMIND    = 1;//闹钟提醒
    public static final int MESSAGE_TYPE_NOTICE          = 2;//通知消息

    //医生名医
    public static final int TYPE_DOCTOR_FAMOUS    = 1;//名医


    //产品
    public static final int TYPE_PRODUCT_CONSULT          = 1;//图文咨询
    public static final int TYPE_PRODUCT_SEND_GIFT        = 2;//送心意
    public static final int TYPE_PRODUCT_APPIONTMENT      = 3;//门诊预约

    //支付方式
    public static final int TYPE_PAYMENT_NO              = 0;//无
    public static final int TYPE_PAYMENT_ALIPAY          = 1;//支付宝
    public static final int TYPE_PAYMENT_WECHAPAY        = 2;//微信支付

    //支付订单状态
    public static final int PAY_ORDER_STATUS_ALL           = 0;//全部
    public static final int PAY_ORDER_STATUS_WAIT          = 1;//待支付
    public static final int PAY_ORDER_STATUS_FINISH        = 2;//已支付
    public static final int PAY_ORDER_STATUS_CANCEL        = 3;//已取消
    public static final int PAY_ORDER_STATUS_OVER          = 4;//已就诊
    public static final int PAY_ORDER_STATUS_DELETE        = 5;//已删除
    public static final int PAY_ORDER_STATUS_COMMENT        = 6;//已评价

    //预约状态1：待支付 ，2：待就诊 ，3：已取消 ，4：已就诊 ，5：已过期
    public static final int APPOINTMENT_PAY_WAIT           = 1;//待支付
    public static final int APPOINTMENT_WAIT_SERVICE          = 2;//待就诊
    public static final int APPOINTMENT_CANCEL        = 3;//已取消
    public static final int APPOINTMENT_HAD_SERVICE        = 4;//已就诊
    public static final int APPOINTMENT_END          = 5;//已过期

    //预约状态
    //预约状态
    public static final int STATUS_APPIONTMENT          = 1;//预约
    public static final int STATUS_APPIONTMENT_FULL     = 2;//约满
    public static final int STATUS_APPIONTMENT_NOT      = 3;//不接诊
    public static final int STATUS_APPIONTMENT_SET      = 4;//已设置
    public static final int STATUS_APPIONTMENT_NOT_SET  = 5;//设置
    public static final int STATUS_APPIONTMENT_OUTDATE  = 6;//过期
    public static final int STATUS_APPIONTMENT_SELECTED  = 10;//选中状态

    //医馆类型
    public static final int TYPE_CLINIC_OTHER             = 1;//导入数据
    public static final int TYPE_CLINIC_BOWEN             = 2;//后台医馆

    public static final String SEX_BOY = "1";
    public static final String SEX_GIRL = "2";

}
