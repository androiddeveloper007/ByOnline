package com.bowen.tcm.folkprescription.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.folkprescription.contract.AddFolkPrescriptionContract;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionDetailContract;
import com.bowen.tcm.folkprescription.model.AddFolkPrescriptionModel;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionDetailModel;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionModel;

import java.util.List;

/**
 * Created by zzp on 2017/5/21.
 * 添加偏方，数据提供类
 */
public class AddFolkPrescriptionPresenter extends BasePresenter {

    private AddFolkPrescriptionModel addFolkPrescriptionModel;
    private FolkPrescriptionModel folkPrescriptionModel;
    private AddFolkPrescriptionContract.View mView;

    public AddFolkPrescriptionPresenter(Context context, AddFolkPrescriptionContract.View view) {
        super(context);
        addFolkPrescriptionModel = new AddFolkPrescriptionModel(context);
        folkPrescriptionModel = new FolkPrescriptionModel(context);
        mContext = context;
        mView = view;
    }

    /***
     * 上传偏方
     */
    public void uploadPrescription(String prescriptionName, String usageDosage,String applyCrowd,
                                   String applyDisease) {
        addFolkPrescriptionModel.uploadPrescription(prescriptionName, usageDosage,applyCrowd,
                applyDisease,new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                showToast(result.getMsg());
                mView.uploadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<Object> result) {
                showToast(result.getMsg());
                mView.uploadFail(result.getData());
            }
        });
    }

    /**
     * 加载适应人群
     *
     */
    public void loadApplyCrowd() {
        folkPrescriptionModel.loadRightDrawerLists1(new HttpTaskCallBack<List<ShowApplyCrowd>>() {
            @Override
            public void onSuccess(HttpResult<List<ShowApplyCrowd>> result) {
                mView.loadApplyCrowdSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<List<ShowApplyCrowd>> result) {
                showToast(result.getMsg());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
