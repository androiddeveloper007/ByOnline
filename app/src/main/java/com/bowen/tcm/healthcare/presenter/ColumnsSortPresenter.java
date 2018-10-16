package com.bowen.tcm.healthcare.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.healthcare.contract.ColumnsSortContract;
import com.bowen.tcm.healthcare.model.ColumnsSortModel;

import java.util.List;

/**
 * 修改栏目排序
 *
 * Created by zzp on 2017/5/21.
 */

public class ColumnsSortPresenter extends BasePresenter {
    private ColumnsSortModel columnsSortModel;
    private ColumnsSortContract.View mView;

    public ColumnsSortPresenter(Context context, ColumnsSortContract.View view) {
        super(context);
        columnsSortModel = new ColumnsSortModel(context);
        mContext = context;
        mView = view;
    }

    public void sortRequest(List<Column> list) {
        columnsSortModel.sortRequest(list, new HttpTaskCallBack<String>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                mView.onSortSuccess();
            }

            @Override
            public void onFail(HttpResult<String> result) {
                showToast(result.getMsg());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }

}
