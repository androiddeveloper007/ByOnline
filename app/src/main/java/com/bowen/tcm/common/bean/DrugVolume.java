package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class DrugVolume implements Serializable {
    private String drugName;
    private String dosage;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}
