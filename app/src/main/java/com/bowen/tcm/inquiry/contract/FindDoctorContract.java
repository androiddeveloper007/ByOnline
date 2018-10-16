package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.DoctorInfo;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public interface FindDoctorContract {

    interface View {
        void onGetDoctorListSuccess(DoctorInfo doctorInfo);
        void onGetDoctorListFailed();
    }

    interface Presenter{
        void findDoctorList(int pageNo, int pageSize, SearchField searchField);
        void findDoctorListByPreId(int pageNo, int pageSize, String preId);
    }
}
