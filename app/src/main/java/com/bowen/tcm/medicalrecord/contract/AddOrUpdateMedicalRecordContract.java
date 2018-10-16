package com.bowen.tcm.medicalrecord.contract;

import com.bowen.tcm.common.bean.AddCaseInfo;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public interface AddOrUpdateMedicalRecordContract {

    interface View {
        void onAddOrUpdateMedicalRecordSuccess();
        void onDeleteSuccess();
    }

    interface Presenter{
        void addMedicalRecordInfo(AddCaseInfo addCaseInfo);
        void addUploadMedicalRecordInfo(AddCaseInfo addCaseInfo,ArrayList<String> pics);
        void updateMedicalRecordInfo(AddCaseInfo addCaseInfo);
        void updateUploadMedicalRecordInfo(AddCaseInfo addCaseInfo,ArrayList<String> pics);
        void deletePhoto(String fileId);
        void getVisablePerson();

    }
}
