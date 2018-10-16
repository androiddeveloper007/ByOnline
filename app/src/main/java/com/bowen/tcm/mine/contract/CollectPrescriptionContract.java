package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface CollectPrescriptionContract {

    interface View {
        void onLoadSuccess(FolkPrescriptionRecord list);

        void onLoadFail(FolkPrescriptionRecord list);

        void setCollectSuccess(int position);

        void setCollectFail();
    }

    interface Presenter {

    }
}
