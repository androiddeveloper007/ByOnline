package com.bowen.tcm.common.bean.network;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/9.
 */

public class VisiablePerson {


    private boolean isChoose;
    private String familyNickname;
    private String familyPhone;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getFamilyNickname() {
        return familyNickname;
    }

    public void setFamilyNickname(String familyNickname) {
        this.familyNickname = familyNickname;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }
}
