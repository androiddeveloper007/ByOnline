package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class PhotoFile implements Serializable{

    /**
     * fileLink : http://192.168.0.242/tcm/image5ac22020d28f9c1fa453bfc2
     * businessId : 8aecc0aa19df47c5be15ac4b206f6957
     * fileCode : 4
     * fileType : 0
     * fileId : 5ac22020d28f9c1fa453bfc2
     */

    private String fileLink;
    private String businessId;
    private String fileCode;
    private String fileType;
    private String fileId;

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
