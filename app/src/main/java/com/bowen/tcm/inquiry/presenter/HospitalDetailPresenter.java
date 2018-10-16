package com.bowen.tcm.inquiry.presenter;

import android.app.Activity;
import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.ClinicDetailInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.HospitalDetailInfo;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.inquiry.contract.HospitalDetailContract;
import com.bowen.tcm.inquiry.model.DoctorModel;
import com.bowen.tcm.inquiry.model.HospitalDetailModel;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:根据省市区搜索医馆分页(用户端)
 * Created by zhuzhipeng on 2018/7/9.
 */
public class HospitalDetailPresenter extends BasePresenter {
    private HospitalDetailModel hospitalDetailModel;
    private DoctorModel mDoctorModel;
    private HospitalDetailContract.View mView;

    public HospitalDetailPresenter(Context mContext, HospitalDetailContract.View view) {
        super(mContext);
        hospitalDetailModel = new HospitalDetailModel(mContext);
        mDoctorModel = new DoctorModel(mContext);
        mView = view;
    }

    public void getHospitalDetail(String hospitalId, int page, int pageSize) {
        hospitalDetailModel.getHospitalDetail(hospitalId, page, pageSize, new HttpTaskCallBack<HospitalDetailInfo>() {
            @Override
            public void onSuccess(HttpResult<HospitalDetailInfo> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<HospitalDetailInfo> result) {
               mView.onLoadFail(result.getData());
            }
        });
    }

    public void checkImmediateConsult(String doctorId){
        mDoctorModel.checkImmediateConsult(doctorId, new HttpTaskCallBack<Doctor>() {
            @Override
            public void onSuccess(HttpResult<Doctor> result) {
                mView.onCheckImmediateConsultSuccess(result.getData());

            }

            @Override
            public void onFail(HttpResult<Doctor> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }

    public List<PhotoFile> transfromPhotos(String photos){
        ArrayList<PhotoFile> photoFiles = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(photos)){
            PhotoFile photoFile = new PhotoFile();
            photoFile.setFileLink(photos);
            photoFiles.add(photoFile);
            return photoFiles;
        }
        return photoFiles;
    }
}
