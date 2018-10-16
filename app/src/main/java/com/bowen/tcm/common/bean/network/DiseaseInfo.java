package com.bowen.tcm.common.bean.network;

/**
 * Describe:病症bean
 * Created by zzp on 2018/6/1.
 */

public class DiseaseInfo {
    private String diseaseId;
    private String diseaseName;

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public DiseaseInfo(String diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }
}
