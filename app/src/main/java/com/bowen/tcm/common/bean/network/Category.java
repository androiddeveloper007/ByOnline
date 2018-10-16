package com.bowen.tcm.common.bean.network;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/3.
 */

public class Category {
    private String code;
    private String familyType;

    private String stage;

    private String caseName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
