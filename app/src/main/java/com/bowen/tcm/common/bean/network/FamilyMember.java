package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class FamilyMember implements Serializable{


    /**
     * familyId : fc4275641ca84e769e29c8130d4784e0
     * familyType : 0
     * familyTypeTxt : 本人
     * familyNickname : by589989
     * userId : 8aecc0aa19df47c5be15ac4b206f6957
     * familyPhone : 18665859332
     */

    private String familyId;
    private String familyType;
    private String familyTypeTxt;
    private String familyNickname;
    private String userId;
    private String familyPhone;
    private String headSculptureUrl;
    private String recentUploadTime;
    private String sex;
    private long birthday;
    private String age;
    private boolean isChoose;



    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getFamilyTypeTxt() {
        return familyTypeTxt;
    }

    public void setFamilyTypeTxt(String familyTypeTxt) {
        this.familyTypeTxt = familyTypeTxt;
    }

    public String getFamilyNickname() {
        return familyNickname;
    }

    public void setFamilyNickname(String familyNickname) {
        this.familyNickname = familyNickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getHeadSculptureUrl() {
        return headSculptureUrl;
    }

    public void setHeadSculptureUrl(String headSculptureUrl) {
        this.headSculptureUrl = headSculptureUrl;
    }

    public String getRecentUploadTime() {
        return recentUploadTime;
    }

    public void setRecentUploadTime(String recentUploadTime) {
        this.recentUploadTime = recentUploadTime;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
