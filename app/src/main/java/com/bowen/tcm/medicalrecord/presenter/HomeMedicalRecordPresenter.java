package com.bowen.tcm.medicalrecord.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.medicalrecord.contract.HomeMedicalRecordContract;
import com.bowen.tcm.medicalrecord.model.MedicalRecordModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class HomeMedicalRecordPresenter extends BasePresenter implements HomeMedicalRecordContract.Presenter{
    private MedicalRecordModel mMedicalRecordModel;
    private HomeMedicalRecordContract.View mView;

    public HomeMedicalRecordPresenter(Context mContext,HomeMedicalRecordContract.View view) {
        super(mContext);
        mView = view;
        mMedicalRecordModel = new MedicalRecordModel(mContext);
    }

    @Override
    public void getMedicalRecordList(int pageNo, int pageSize, String familyId) {
        mMedicalRecordModel.getFamilyMembersMedicalRecord(pageNo, pageSize, familyId, new HttpTaskCallBack<HomeMedicalRecord>() {
            @Override
            public void onSuccess(HttpResult<HomeMedicalRecord> result) {
                mView.onGetMedicalRecordSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<HomeMedicalRecord> result) {
                mView.onGetMedicalRecordFail(result.getData());
            }
        });
    }

    public void getDiseaseNameCategory() {
        mMedicalRecordModel.getDiseaseNameCategory();
    }

    public void getDiagnoseStage() {
        mMedicalRecordModel.getDiagnoseStage();
    }

    public void getVisablePerson() {
        mMedicalRecordModel.getVisiablePerson();
    }
}
