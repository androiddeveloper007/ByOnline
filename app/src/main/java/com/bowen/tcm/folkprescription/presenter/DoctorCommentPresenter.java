package com.bowen.tcm.folkprescription.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.folkprescription.contract.DoctorCommentContract;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionDetailModel;

/**
 * Created by zzp on 2018/7/5.
 * 偏方医生评价presenter
 */
public class DoctorCommentPresenter extends BasePresenter {

    private FolkPrescriptionDetailModel folkPrescriptionDetailModel;
    private DoctorCommentContract.View mView;

    public DoctorCommentPresenter(Context context, DoctorCommentContract.View view) {
        super(context);
        folkPrescriptionDetailModel = new FolkPrescriptionDetailModel(context);
        mContext = context;
        mView = view;
    }

    /***
     * 偏方模块 / 分页获取医生偏方评论
     * @param pageNo
     * @param pageSize
     * @param prescriptionId
     */
    public void getDoctorPrescriptionCommentList(int pageNo, int pageSize, String prescriptionId) {
        folkPrescriptionDetailModel.getDoctorPrescriptionCommentList(pageNo, pageSize, prescriptionId, new HttpTaskCallBack<PrescriptionDoctorCommentRecord>() {
            @Override
            public void onSuccess(HttpResult<PrescriptionDoctorCommentRecord> result) {
                mView.getDoctorCommentSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<PrescriptionDoctorCommentRecord> result) {
                showToast(result.getMsg());
                mView.getDoctorCommentFail(result.getData());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
