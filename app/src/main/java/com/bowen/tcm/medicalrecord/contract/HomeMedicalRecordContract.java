package com.bowen.tcm.medicalrecord.contract;

import com.bowen.tcm.common.bean.network.HomeMedicalRecord;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public interface HomeMedicalRecordContract {
    interface View {
        void onGetMedicalRecordSuccess(HomeMedicalRecord homeMedicalRecord);
        void onGetMedicalRecordFail(HomeMedicalRecord homeMedicalRecord);
    }

    interface Presenter{
        void getMedicalRecordList(int pageNo, int pageSize,String familyId);
    }
}
