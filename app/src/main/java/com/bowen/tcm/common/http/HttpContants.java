package com.bowen.tcm.common.http;


import com.bowen.tcm.BowenApplication;

/**
 * PHP请求常量
 * Created by AwenZeng on 2016/7/8.
 */
public class HttpContants {

    /**
     * 服务器地址
     */
    public static String REQUEST_URL = BowenApplication.DEBUG?"http://192.168.0.241:8028/":"https://www.boyizaixian.com/";
    public static String CHAT_SERVER_URL = BowenApplication.DEBUG?"192.168.0.242":"boyizaixian.com";

    //请求状态
    public static final int HTTP_OK = 0000;//成功
    public static final int HTTP_FAIL = 9999;//失败
    public static final int HTTP_LOGIN_OVERDUE = 9911;//登录过期

    //登录
    public static final String CMD_LOGIN = "login";
    public static final String CMD_AUTHCODE_LOGIN = "verifyCodeLogin";
    public static final String CMD_THIRD_LOGIN = "thirdLogin";
    public static final String CMD_QUICK_LOGIN = "quickLogin";
    public static final String CMD_LOGIN_OUT = "logout";
    public static final String CMD_UPDATE_USER_NICK = "updateUserNickname";

    //注册
    public static final String CMD_REGIST = "register";
    public static final String CMD_REGIST_AND_LOGIN = "thirdRegAndLogin";
    public static final String CMD_CHECK_ACCOUNT = "checkAccount";
    public static final String CMD_GET_AUTHCODE = "verifycode";
    //找回密码
    public static final String CMD_FIND_SET_PASSWORD = "backPassword";
    public static final String CMD_SUBMIT_FEEDBACK = "saveFeedback";


    //家庭成员
    public static final String CMD_GET_ALL_FAMILY_MEMBERS = "showAllUserFamily";//获取所有家庭成员
    public static final String CMD_SAVE_USER_FAMILY_INFO = "saveUserFamilyInfo";//保存家庭成员信息
    public static final String CMD_UPDATE_USER_FAMILY_INFO = "updateUserFamilyInfo";//更新家庭成员信息
    public static final String CMD_DELETE_USER_FAMILY_INFO = "deleteFamilyAndCaseInfo";//删除家庭成员信息
    public static final String CMD_UPDATE_USER_FAMILY_HEADPORTRAITE = "updateHeadSculpture";//更新家庭成员头像信息
    public static final String CMD_FAMILY_USER_RELATIONSHIP = "familyTypeVoList";//遍历用户关系


    //文件
    public static final String CMD_UPLOAD_PHOTO = "uploadFile";//上传图片
    public static final String CMD_DELETE_PHOTO = "deleteFile";//删除图片

    //病历
    public static final String CMD_GET_MEDICAL_RECORD_list = "emrCaseFamily";//获取病历列表
    public static final String CMD_GET_MEDICAL_PROGRESS_LIST = "emrCourseList";//获取病程列表
    public static final String CMD_ADD_MEDICAL_RECORD_INFO = "saveEmrCaseInfo";//添加病历信息
    public static final String CMD_UPDATE_MEDICAL_RECORD_INFO = "updateEmrCaseInfo";//更新病历信息
    public static final String CMD_DELETE_MEDICAL_RECORD_INFO = "deleteOneCase";//删除病历信息
    public static final String CMD_GET_DISEASE_NAME = "caseNameVoList";//获取疾病名字
    public static final String CMD_GET_DIAGNOSE_STAGE = "doctorStageVoList";//获取疾病名字
    public static final String CMD_GET_VISIABLE_PERSON = "userFamilyPhone";//获取可见人信息

    //app配置数据
    public static final String CMD_GET_CONFIG_DATA = "getStaticInfo";
    public static final String CMD_GET_TIPS_INFO = "getInfoHealthTips";//随机获取小贴士
    public static final String CMD_GET_BANNER_INFO = "getBannerList";

    //首页
    public static final String CMD_GET_HOMEPAGE_DATA = "showUserIndexMsg";//首页数据

    //帮助
    public static final String CMD_GET_HELP_FAQ = "getFaqInfo";
    public static final String CMD_GET_DEAL_RECORD = "getPrdTraList";

    //提醒
    public static final String CMD_SAVE_ALARM_REMIND      = "saveRemindMedicine";
    public static final String CMD_GET_ALARM_REMIND_LIST  = "getRemindMedicineList";
    public static final String CMD_UPDATE_ALARM_REMIND    = "updateRemindMedicine";
    public static final String CMD_DELETE_ALARM_REMIND    = "deleteRemindMedicine";
    public static final String CMD_UPDATE_ALARM_STATUS    = "updateRemindStatus";

    //栏目咨询
    public static final String CMD_GET_COLUMNS_LIST = "infoColumnList";//获取栏目列表
    public static final String CMD_GET_RECOMMEND_NEWS_LIST = "topNewsList";//推荐阅读栏目新闻资讯
    public static final String CMD_GET_COLUMN_NEWS_LIST = "newsListByColumnId";//根据栏目id分页查询新闻资讯
    public static final String CMD_SAVE_INFO_COLUMN = "saveInfoColumn";//保存用户选中栏目列表


    //偏方
    public static final String CMD_GET_FOLK_PRESCRIPTION_LIST = "getFolkPrescriptionList";//根据偏方名称,主治功能,用法用量搜索偏方
    public static final String CMD_GET_LIST_BY_APPLY_DEPARTMENTS = "getListByApplyDepartments";//根据适应人群,所属科室搜索偏方
    public static final String CMD_SHOW_PRESCRIPTION_SECTION_LIST = "showPrescriptionSectionList";//所属科室列表
    public static final String CMD_SHOWAPPLYCROWDLIST = "showApplyCrowdList";//适应人群列表
    public static final String CMD_SHOW_HOT_SEARCH_LIST = "showHotSearchList";//热搜
    public static final String CMD_SHOW_USER_SERACH_LOG = "showUserSerachLog"; //搜索记录
    public static final String CMD_DELETE_USER_SEARCH_LOG = "deleteUserSearchLog"; //清除用户搜索记录
    public static final String CMD_GET_INFO_FOLK_PRESCRIPTION = "getInfoFolkPrescription";//展示单个偏方详情
    public static final String CMD_ADD_INFO_FOLK_PRESCRIPTION = "addInfoFolkPrescription";//上传偏方
    public static final String CMD_LIKE_INFO_DISEASE_DEPT = "likeInfoDiseaseDept";//模糊搜索常见疾病
    public static final String CMD_ADD_INFO_USER_PRESCRIPTION = "addInfoUserPrescription";//收藏偏方
    public static final String CMD_DELETE_INFO_USER_PRESCRIPTION = "deleteInfoUserPrescription";//取消收藏
    public static final String CMD_GET_USER_PRESCRIPTION_COMMENT_LIST = "getUserPrescriptionCommentList";//用户评论
    public static final String CMD_GET_DOCTOR_PRESCRIPTION_COMMENT_LIST = "getDoctorPrescriptionCommentList";//专家点评
    public static final String CMD_ADD_PRESCRIPTION_COMMENT = "addPrescriptionComment";//添加偏方评论

    //消息
    public static final String CMD_GET_MESSAGE_LIST = "getMsgList";
    public static final String CMD_CHANGE_MESSAGE_STATUS = "updateMsgStatus";
    public static final String CMD_CLEAR_MESSAGE = "clearMsg";
    public static final String CMD_ADD_MESSAGE = "addMsg";
    public static final String CMD_GET_MESSAGE_COUNT = "getMsgHandCount";

    //问诊
    public static final String CMD_FIND_DOCTOR_LIST = "getDoctorList";//找医生
    public static final String CMD_FIND_DOCTOR_LIST_BY_PRE_ID = "getDoctorListByPreId";//找医生
    public static final String CMD_GET_DOCTOR_DETAIL = "getDoctorDetail";//获取医生详情
    public static final String CMD_GET_DOCTOR_COMMENT_LIST = "getAllCommentToDoctor";//获取医生的评论信息
    public static final String CMD_GET_DEPARTMENT_LIST = "getDepList";//获取科室列表
    public static final String CMD_GET_DOCTOR_RANK_LIST = "positionVoList";//获取医生职级
    public static final String CMD_CHANGE_ATTENTION_STATUS = "userFenDoctor";//改变关注状态
    public static final String CMD_GET_DOCTOR_APPOINTMENT_INFO = "userGetLatelyApply";//获取医生的预约信息
    public static final String CMD_CHECK_IMMEDIATE_CONSULT = "checkImmediateConsult";//检测立即咨询

    public static final String CMD_GET_INIT_CHAT = "initChat";//立即咨询
    public static final String CMD_GET_DOCTOR_APPIONTMENT_PERIOD = "applyAppointTime";//获取医生的预约时间
    public static final String CMD_GET_HOME_FIND_DOCTOR = "showUserInquiryMsg";//获取寻医首页数据
    public static final String CMD_GET_CHAT_STATUS_INFO = "getChatStatusMsg";//获取聊天状态
    public static final String CMD_GET_CHAT_CLOSE_CONSULT = "endThisService";//结束聊天咨询
    public static final String CMD_CLEAR_CHAT_STATUS = "clearOfflineMsg";//清除聊天状态

    //支付
    public static final String CMD_CREATE_PAY_ORDER = "applyPayTraOrder";//创建订单
    public static final String CMD_GET_PAY_ORDER_INFO = "payTraOrder";
    public static final String CMD_CANCEL_TRA_ORDER = "cancelTraOrder";//取消订单



    //我的
    public static final String CMD_GET_COLLECT_PRESCRIPTION_LIST = "getCollectPrescriptionList";//分页获取收藏的偏方集合
    public static final String CMD_GET_MY_CONSULT = "getMyConsult";//我的咨询
    public static final String CMD_REMOVE_CONSULT_INFO = "removeChatSession";//删除聊天信息
    public static final String CMD_GET_EMR_APPOINT_PAGE = "getEmrAppointPage";//我的门诊预约列表
    public static final String CMD_GET_TRA_ORDER_PAGE = "getTraOrderPage";//分页查询我的订单明细
    public static final String CMD_GET_TRA_ORDER_BY_ID = "getTraOrderById";//获取订单详情
    public static final String CMD_GET_MY_PRESCRIPTION_LIST = "getMyPrescriptionList";//我的偏方列表
    public static final String CMD_GET_USER_FEN_PAGE = "getUserFenPage";//用户关注医生列表
    public static final String CMD_USER_FEN_DOCTOR = "userFenDoctor";//用户关注或取消关注医生
    public static final String CMD_USER_EVALUATE_VO_LIST = "userEvaluateVoList";//枚举用户评价
    public static final String CMD_SAVE_TRA_ORDER_BY_ID = "saveTraOrderById";//保存用户服务评价

    //问诊和医生搜索
    public static final String CMD_GET_DOCTOR_LIST = "getDoctorList";//所有的医生搜索并分页接口
    public static final String CMD_SHOW_HOT_SEARCH_DOCTOR_LIST = "showHotSearchDoctorList";//展示10条医生热搜关键词记录
    public static final String CMD_SHOW_USER_SERACH_DOCTOR_LOG = "showUserSerachDoctorLog";//用户搜索医生历史记录列表
    public static final String CMD_DELETE_USER_SEARCH_DOCTOR_LOG = "deleteUserSearchDoctorLog";//删除用户搜索医生历史记录
    public static final String CMD_USER_GET_LATELY_APPLY = "userGetLatelyApply";//获取医生最近三周预约人数List

    //医馆
    public static final String CMD_GET_ALL_HOSPITAL_PAGE = "getAllHospitalPage";//根据省市区搜索医馆分页(用户端)
    public static final String CMD_GET_ALL_CLINIC_LISTMAP = "getMapHospitalList";//获取医馆列表
    public static final String CMD_GET_CLINIC_MAP_DATA = "getMapHospitalModelList";//获取医馆列表
    public static final String CMD_GET_MAP_CLINIC_DETAIL = "getMapHospitalDetail";//获取医馆列表
    public static final String CMD_GET_HOSPITAL_DETAIL = "getHospitalDetail";//用户端医馆详情展示(用户端)
}