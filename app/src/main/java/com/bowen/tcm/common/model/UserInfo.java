package com.bowen.tcm.common.model;

/**
 * 用户信息：<P>
 * isAuthentication:是否资格认证
 * userId : 用户查询ID<P>
 * token : 登陆标识串<P>
 * userLoginName : 登录手机号<P>
 * userMobile : 验证手机号<P>
 * Created by AwenZeng on 2016/12/26.
 */

public class UserInfo {
    private static UserInfo instance = null;
    private String userMobile;
    private String userLoginName;
    private String userId;
    private String token;
    private String userNickname;
    private String picUrl;
    private String familyId;
    private boolean isChatServerLoginSuccess = false;
    private String sex;
    private long birthday;

    private boolean isBindPhone = false;
    private boolean isStartChat = false;//是否开始聊天

    public static UserInfo getInstance() {
        if (instance == null) {
            synchronized (UserInfo.class) {
                if (instance == null) {
                    instance = new UserInfo();
                }
            }
        }
        return instance;
    }

    public boolean isSelf(String userId){
        return userId.equals(userId);
    }


    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public boolean isBindPhone() {
        return isBindPhone;
    }

    public void setBindPhone(boolean bindPhone) {
        isBindPhone = bindPhone;
    }

    public boolean isStartChat() {
        return isStartChat;
    }

    public void setStartChat(boolean startChat) {
        isStartChat = startChat;
    }

    public boolean isChatServerLoginSuccess() {
        return isChatServerLoginSuccess;
    }

    public void setChatServerLoginSuccess(boolean chatServerLoginSuccess) {
        isChatServerLoginSuccess = chatServerLoginSuccess;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
}
