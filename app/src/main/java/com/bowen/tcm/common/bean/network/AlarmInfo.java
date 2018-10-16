package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class AlarmInfo {
    private ArrayList<Alarm> remindMedicineList;
    private Page page;

    public ArrayList<Alarm> getRemindMedicineList() {
        return remindMedicineList;
    }

    public void setRemindMedicineList(ArrayList<Alarm> remindMedicineList) {
        this.remindMedicineList = remindMedicineList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
