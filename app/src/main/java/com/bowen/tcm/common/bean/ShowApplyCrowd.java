package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 *适应人群列表item
 */
public class ShowApplyCrowd implements Serializable{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    private String applyName;

}
