package com.bowen.tcm.healthcare.model;

import android.content.Context;
import android.util.Log;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import java.util.List;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 * 排序栏目提交
 */

public class ColumnsSortModel extends BaseModel {


    public ColumnsSortModel(Context mContext) {
        super(mContext);
    }


    public void sortRequest(List<Column> list, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        StringBuilder buffer = new StringBuilder();
        boolean flag=false;
        for(Column item : list){
            if(!flag){
                buffer.append(item.getColumnId());
                flag = true;
            } else {
                buffer.append(","+item.getColumnId());
            }
        }
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("columnIds", buffer.toString());
        Log.e("ZP", "提交时的排序size:"+buffer.length()+"-----"+buffer.toString());
        networkRequest.requestSRV(HttpContants.CMD_SAVE_INFO_COLUMN, "正在提交,请稍后...", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onSuccess(httpResult);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onFail(httpResult);
                }
            }
        });
    }

}
