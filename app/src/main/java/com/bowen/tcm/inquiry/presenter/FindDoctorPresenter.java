package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.DoctorInfo;
import com.bowen.tcm.inquiry.contract.FindDoctorContract;
import com.bowen.tcm.inquiry.model.DoctorModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class FindDoctorPresenter extends BasePresenter implements FindDoctorContract.Presenter{
    private DoctorModel mDoctorModel;
    private FindDoctorContract.View mView;

    public FindDoctorPresenter(Context mContext,FindDoctorContract.View view) {
        super(mContext);
        mView = view;
        mDoctorModel = new DoctorModel(mContext);
        mDoctorModel.getDepartmentsList();
        mDoctorModel.getDoctorRankList();
    }

    @Override
    public void findDoctorList(int pageNo, int pageSize, SearchField searchField) {
        mDoctorModel.findDoctorList(pageNo, pageSize, searchField, new HttpTaskCallBack<DoctorInfo>() {
            @Override
            public void onSuccess(HttpResult<DoctorInfo> result) {
                mView.onGetDoctorListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<DoctorInfo> result) {
               mView.onGetDoctorListFailed();
            }
        });
    }

    @Override
    public void findDoctorListByPreId(int pageNo, int pageSize, String preId) {
        mDoctorModel.findDoctorListByPreId(pageNo, pageSize, preId, new HttpTaskCallBack<DoctorInfo>() {
            @Override
            public void onSuccess(HttpResult<DoctorInfo> result) {
                mView.onGetDoctorListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<DoctorInfo> result) {
               mView.onGetDoctorListFailed();
            }
        });
    }
}
