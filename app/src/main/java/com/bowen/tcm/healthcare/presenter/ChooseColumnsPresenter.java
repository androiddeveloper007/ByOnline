package com.bowen.tcm.healthcare.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.healthcare.contract.ChooseColumnsContract;
import com.bowen.tcm.healthcare.model.ChooseColumnsModel;
import com.bowen.tcm.healthcare.model.HealthCareModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzp on 2017/5/27.
 * 选择兴趣的内容
 */

public class ChooseColumnsPresenter extends BasePresenter implements ChooseColumnsContract.Presenter {
    private HealthCareModel mHealthCareModel;
    private ChooseColumnsContract.View mView;

    public ChooseColumnsPresenter(Context context, ChooseColumnsContract.View view) {
        super(context);
        mHealthCareModel = new HealthCareModel(context);
        mContext = context;
        mView = view;
    }

    public void loadColumList() {
        mHealthCareModel.getColumnsList(new HttpTaskCallBack<ArrayList<Column>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<Column>> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ArrayList<Column>> result) {
                showToast(result.getMsg());
            }
        });
    }

    /**
     * 处理栏目列表
     * @param columns
     * @return
     */
    public String handleColumnList(List<Column> columns){
        StringBuilder buffer = new StringBuilder();
        StringBuilder buffer1 = new StringBuilder();//未选择的
        boolean flag=false;
        for(Column item : columns){
            if(item.isCheckedBool()){
                if(!flag){
                    buffer.append(item.getColumnId());
                    flag = true;
                } else {
                    buffer.append(","+item.getColumnId());
                }
            } else {
                buffer1.append(","+item.getColumnId());
            }
        }
        buffer.append(buffer1);
        return buffer.toString();
    }


    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
