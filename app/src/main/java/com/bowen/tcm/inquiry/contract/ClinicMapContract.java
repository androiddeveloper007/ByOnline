package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public interface ClinicMapContract {

    interface View {
        void onGetAllCliniMapDataSuccess(CliniclInfo info);
        void onGetCliniMapDetailSuccess(Clinic clinic);
        void jumpAnimOver(double longitude,double latitude);
    }

    interface Presenter{
        void getAllCliniMapData(double longitude, double latitude,String areaCode);
        void getClinicMapDetail(String clinicId);
    }
}
