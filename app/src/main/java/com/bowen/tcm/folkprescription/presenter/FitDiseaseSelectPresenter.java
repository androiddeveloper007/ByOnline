package com.bowen.tcm.folkprescription.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.DiseaseInfoRecord;
import com.bowen.tcm.folkprescription.contract.FitDiseaseSelectContract;
import com.bowen.tcm.folkprescription.model.FitDiseaseSelectModel;

/**
 * Created by zzp on 2017/5/21.
 * 选择病症，数据提供类
 */
public class FitDiseaseSelectPresenter extends BasePresenter {

    private FitDiseaseSelectModel FitDiseaseSelectModel;
    private FitDiseaseSelectContract.View mView;

    public FitDiseaseSelectPresenter(Context context, FitDiseaseSelectContract.View view) {
        super(context);
        FitDiseaseSelectModel = new FitDiseaseSelectModel(context);
        mContext = context;
        mView = view;
    }

    /***
     * 根据关键字搜病症
     * @param str
     */
    public void loadDataByStr(String str, int page, int pageSize) {
        FitDiseaseSelectModel.loadDataByStr(str, page, pageSize, new HttpTaskCallBack<DiseaseInfoRecord>() {
            @Override
            public void onSuccess(HttpResult<DiseaseInfoRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<DiseaseInfoRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
