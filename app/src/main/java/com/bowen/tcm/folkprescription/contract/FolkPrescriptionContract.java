package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface FolkPrescriptionContract {

    interface View {
        void onLoadSuccess(FolkPrescriptionRecord list);

        void onLoadFail(FolkPrescriptionRecord list);

        void onLoadByApplyDepartmentsSuccess(FolkPrescriptionRecord record);

        void onLoadByApplyDepartmentsFail(FolkPrescriptionRecord record);

        void loadRightDrawerListsSuccess(List<HospitalDepartments> list);

        void loadRightDrawerLists1Success(List<ShowApplyCrowd> list);
    }

    interface Presenter {

    }
}
