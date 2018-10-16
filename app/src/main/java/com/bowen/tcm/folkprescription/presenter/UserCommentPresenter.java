package com.bowen.tcm.folkprescription.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;
import com.bowen.tcm.folkprescription.contract.AddFolkPrescriptionContract;
import com.bowen.tcm.folkprescription.contract.UserCommentContract;
import com.bowen.tcm.folkprescription.model.AddFolkPrescriptionModel;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionDetailModel;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionModel;

import java.util.List;

/**
 * Created by zzp on 2017/5/21.
 * 用户评论
 */
public class UserCommentPresenter extends BasePresenter {

    private FolkPrescriptionDetailModel folkPrescriptionDetailModel;
    private UserCommentContract.View mView;

    public UserCommentPresenter(Context context, UserCommentContract.View view) {
        super(context);
        folkPrescriptionDetailModel = new FolkPrescriptionDetailModel(context);
        mContext = context;
        mView = view;
    }

    /***
     * 偏方模块 / 分页获取用户偏方评论
     * @param pageNo
     * @param pageSize
     * @param prescriptionId
     */
    public void getUserPrescriptionCommentList(int pageNo, int pageSize, String prescriptionId) {
        folkPrescriptionDetailModel.getUserPrescriptionCommentList(pageNo, pageSize, prescriptionId,
                new HttpTaskCallBack<PrescriptionUserCommentRecord>() {
            @Override
            public void onSuccess(HttpResult<PrescriptionUserCommentRecord> result) {
                mView.getUserCommentSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<PrescriptionUserCommentRecord> result) {
                showToast(result.getMsg());
                mView.getUserCommentFail(result.getData());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
