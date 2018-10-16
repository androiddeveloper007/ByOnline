package com.bowen.tcm.homepage.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.bean.network.HomePageInfo;
import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.NewsInfo;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.homepage.contract.HomePageFgContract;
import com.bowen.tcm.main.model.AppModel;
import com.bowen.tcm.main.model.MessageModel;
import com.bowen.tcm.healthcare.model.HealthCareModel;
import com.bowen.tcm.medicalrecord.model.MedicalRecordModel;

import java.util.ArrayList;
import java.util.List;


public class HomePageFgPresenter extends BasePresenter implements HomePageFgContract.Presenter{
    private HealthCareModel mHealthCareModel;
    private MedicalRecordModel mMedicalRecordModel;

    private MessageModel mMessageModel;
    private AppModel mAppModel;
    private HomePageFgContract.View mView;

    public HomePageFgPresenter(Context context, HomePageFgContract.View view) {
        super(context);
        mHealthCareModel = new HealthCareModel(context);
        mMessageModel = new MessageModel(context);
        mMedicalRecordModel = new MedicalRecordModel(context);
        mAppModel = new AppModel(context);
        mContext = context;
        mView = view;
    }

    public void getMessageCount(){
        mMessageModel.getMessageCount(new HttpTaskCallBack<Integer>() {
            @Override
            public void onSuccess(HttpResult<Integer> result) {
                 mView.onGetMessageCountSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<Integer> result) {

            }
        });
    }

    @Override
    public void getHomePageInfo(double longitude, double latitude) {
        mAppModel.getHomepageNewsList(longitude, latitude, new HttpTaskCallBack<HomePageInfo>() {
            @Override
            public void onSuccess(HttpResult<HomePageInfo> result) {
                  mView.onGetHomePageInfoSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<HomePageInfo> result) {
                 mView.onGetHomePageInfoFailed();
            }
        });
    }

    /**
     * 更新栏目数据
     * @param list
     * @return
     */
    public List<News> updateNewsColumns(List<News> list){
        ArrayList<Column> columns = AppConfigInfo.getInstance().getColumnList();
        for(int i = 0;i < list.size(); i++){
            News news = list.get(i);
            if(news.getType() == Constants.TYPE_NEWS_ITEM_COLUMN){
                news.setColumns(columns);
                list.set(i,news);
                break;
            }
        }
        return list;
    }

    public void getDiseaseNameCategory() {
        mMedicalRecordModel.getDiseaseNameCategory();
    }

    public void getDiagnoseStage() {
        mMedicalRecordModel.getDiagnoseStage();
    }
}
