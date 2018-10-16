package com.bowen.tcm.healthcare.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.NewsInfo;
import com.bowen.tcm.healthcare.contract.HealthCareContract;
import com.bowen.tcm.healthcare.contract.NewsFragmentContract;
import com.bowen.tcm.healthcare.model.HealthCareModel;

import java.util.ArrayList;


public class NewsFragmentPresenter extends BasePresenter implements NewsFragmentContract.Presenter{
    private HealthCareModel mHealthCareModel;
    private NewsFragmentContract.View mView;

    public NewsFragmentPresenter(Context context, NewsFragmentContract.View view) {
        super(context);
        mHealthCareModel = new HealthCareModel(context);
        mContext = context;
        mView = view;
    }


    @Override
    public void onGetRecommendNewsList(int pageNo, int pageSize) {
        mHealthCareModel.getRecommendNewsList(pageNo, pageSize, false, new HttpTaskCallBack<NewsInfo>() {
            @Override
            public void onSuccess(HttpResult<NewsInfo> result) {
                mView.onGetRecommendNewsListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<NewsInfo> result) {
                mView.onGetRecommendNewsListFailed();
            }
        });

    }

    @Override
    public void onGetColumnNewsList(int pageNo, int pageSize, String columnId) {
        mHealthCareModel.getColumnNewsList(pageNo, pageSize, columnId, new HttpTaskCallBack<NewsInfo>() {
            @Override
            public void onSuccess(HttpResult<NewsInfo> result) {
                mView.onGetColumnNewsListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<NewsInfo> result) {
               mView.onGetColumnNewsListFailed();
            }
        });
    }
}
