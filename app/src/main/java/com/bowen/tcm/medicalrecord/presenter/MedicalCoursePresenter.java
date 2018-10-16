package com.bowen.tcm.medicalrecord.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.medicalrecord.contract.MedicalCourseContract;
import com.bowen.tcm.medicalrecord.model.MedicalRecordModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class MedicalCoursePresenter extends BasePresenter implements MedicalCourseContract.Presenter{
    private MedicalRecordModel mMedicalRecordModel;
    private MedicalCourseContract.View mView;

    public MedicalCoursePresenter(Context mContext, MedicalCourseContract.View view) {
        super(mContext);
        mView = view;
        mMedicalRecordModel = new MedicalRecordModel(mContext);
    }

    @Override
    public void getMedicalCourseList(int pageNo, int pageSize, String courseId) {
        mMedicalRecordModel.getMedicalAllCourse(pageNo, pageSize, courseId, new HttpTaskCallBack<HomeMedicalRecord>() {
            @Override
            public void onSuccess(HttpResult<HomeMedicalRecord> result) {
                mView.onGetMedicalCourseSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<HomeMedicalRecord> result) {
                mView.onGetMedicalCourseFail(result.getData());
            }
        });
    }

}
