package com.bowen.tcm.medicalrecord.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.AddCaseInfo;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.bean.network.VisiablePerson;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.main.model.DataUploadModel;
import com.bowen.tcm.medicalrecord.contract.AddOrUpdateMedicalRecordContract;
import com.bowen.tcm.medicalrecord.model.MedicalRecordModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class AddOrUpdateMedicalRecordPresenter extends BasePresenter implements AddOrUpdateMedicalRecordContract.Presenter{

    private DataUploadModel mDataUploadModel;
    private MedicalRecordModel mMedicalRecordModel;
    private AddOrUpdateMedicalRecordContract.View mView;

    public AddOrUpdateMedicalRecordPresenter(Context mContext, AddOrUpdateMedicalRecordContract.View view) {
        super(mContext);
        mView = view;
        mDataUploadModel = new DataUploadModel(mContext);
        mMedicalRecordModel = new MedicalRecordModel(mContext);
    }


    public boolean checkInputContent(String mDiagnoseResult,String mDiagnoseClinic,String mDianoseDoctor){
        if(EmptyUtils.isEmpty(mDiagnoseResult)){
            ToastUtil.getInstance().showToastDialog("请输入诊疗效果");
            return false;
        }

        if(EmptyUtils.isEmpty(mDiagnoseClinic)){
            ToastUtil.getInstance().showToastDialog("请输入诊疗诊所");
            return false;
        }

        if(EmptyUtils.isEmpty(mDianoseDoctor)){
            ToastUtil.getInstance().showToastDialog("请输入诊疗医生");
            return false;
        }
        return true;
    }

    @Override
    public void addMedicalRecordInfo(AddCaseInfo addCaseInfo) {
        mMedicalRecordModel.addMedicalRecordDetail(addCaseInfo, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onAddOrUpdateMedicalRecordSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });

    }


    @Override
    public void addUploadMedicalRecordInfo(AddCaseInfo addCaseInfo,ArrayList<String> pics) {
        mDataUploadModel.uploadAddMedicalRecordInfo(addCaseInfo, pics, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onAddOrUpdateMedicalRecordSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }
    @Override
    public void updateMedicalRecordInfo(AddCaseInfo addCaseInfo) {
        mMedicalRecordModel.updateMedicalRecordDetail(addCaseInfo, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onAddOrUpdateMedicalRecordSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

    @Override
    public void updateUploadMedicalRecordInfo(AddCaseInfo addCaseInfo, ArrayList<String> pics) {
        mDataUploadModel.uploadUpdateMedicalRecordInfo(addCaseInfo, pics, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onAddOrUpdateMedicalRecordSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

    @Override
    public void deletePhoto(final String fileId) {
        mDataUploadModel.deletePhoto(fileId, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onDeleteSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

    @Override
    public void getVisablePerson() {
        mMedicalRecordModel.getVisiablePerson();
    }

    public ArrayList<String> handlePhotoList(ArrayList<String> list){
        ArrayList<String> temp = new ArrayList<>();
        temp.iterator();
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals("拍照")){
                iterator.remove();
            }
        }
        temp.addAll(list);
        temp.add("拍照");
        return temp;
    }

    /**
     * 获取权限人的电话号码
     * @return
     */
    public String getLimitSeePhone(MedicalRecord medicalRecord){
        StringBuilder builder = new StringBuilder();
        String[] temp = medicalRecord.getSeePhone().split(",");
        List<VisiablePerson> list = AppConfigInfo.getInstance().getVisiablePersonList();
        for(int i = 0;i<temp.length;i++){
            for(VisiablePerson person:list){
                if(person.getFamilyNickname().equals(temp[i])){
                    builder.append(person.getFamilyPhone()+",");
                }
            }
        }
        return builder.toString();
    }
}
