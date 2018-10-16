package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.Appointment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:门诊预约
 * Created by AwenZeng on 2018/4/4.
 */

public class OutpatientAppointRecord implements Serializable{

    private ArrayList<Appointment> appointmentList;
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public ArrayList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ArrayList<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
