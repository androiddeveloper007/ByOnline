package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.AppointmentListItem;
import com.bowen.tcm.common.bean.EmrDoctor;
import com.bowen.tcm.common.bean.MyOrder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:门诊预约
 * Created by zhuzhipeng on 2018/4/4.
 */

public class OutpatientAppointmentRecord implements Serializable{

    private ArrayList<AppointmentListItem> appointmentList;
    private EmrDoctor emrDoctor;

    public ArrayList<AppointmentListItem> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ArrayList<AppointmentListItem> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public EmrDoctor getEmrDoctor() {
        return emrDoctor;
    }

    public void setEmrDoctor(EmrDoctor emrDoctor) {
        this.emrDoctor = emrDoctor;
    }
}
