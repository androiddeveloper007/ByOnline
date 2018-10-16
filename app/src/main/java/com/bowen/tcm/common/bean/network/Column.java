package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 *  兴趣栏目
 *
 * Created by zzp on 2018/5/15.
 */

public class Column implements Serializable{
    private String columnId;
    private String columnName;
    private boolean isCheckedBool;
    private String fileLink;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isCheckedBool() {
        return isCheckedBool;
    }

    public void setCheckedBool(boolean checkedBool) {
        isCheckedBool = checkedBool;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
}
