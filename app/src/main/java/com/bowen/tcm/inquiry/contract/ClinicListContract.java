package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public interface ClinicListContract {

    interface View {
        void onGetAllClinicListOrMapSuccess(CliniclInfo info);
        void onGetAllClinicListOrMapFailed();
    }

    interface Presenter{
        void getAllClinicListOrMap(int pageNo, int pageSize,FindClinicParam param);
    }
}
