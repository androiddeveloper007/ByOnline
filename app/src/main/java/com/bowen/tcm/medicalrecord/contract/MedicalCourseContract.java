package com.bowen.tcm.medicalrecord.contract;

import com.bowen.tcm.common.bean.network.HomeMedicalRecord;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public interface MedicalCourseContract {
    interface View {
        void onGetMedicalCourseSuccess(HomeMedicalRecord homeMedicalRecord);
        void onGetMedicalCourseFail(HomeMedicalRecord homeMedicalRecord);
    }

    interface Presenter{
        void getMedicalCourseList(int pageNo, int pageSize, String courseId);
    }
}
