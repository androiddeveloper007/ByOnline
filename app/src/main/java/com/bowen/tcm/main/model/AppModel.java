package com.bowen.tcm.main.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.tcm.common.bean.network.BannerInfo;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.bean.network.ContantsNet;
import com.bowen.tcm.common.bean.network.HomePageInfo;
import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.NewsTop;
import com.bowen.tcm.common.bean.network.SmallTips;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.healthcare.model.ChooseColumnsModel;
import com.bowen.tcm.healthcare.model.HealthCareModel;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AwenZeng on 2017/1/11.
 */

public class AppModel extends BaseModel {

    private HealthCareModel mHealthCareModel;
    private ChooseColumnsModel mChooseColumnsModel;

    public AppModel(Context mContext){
        super(mContext);
        mHealthCareModel= new HealthCareModel(mContext);
        mChooseColumnsModel = new ChooseColumnsModel(mContext);
    }

    /**
     * 获取应用配置
     */
    public void getAppConfig() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setCache(true);
        networkRequest.requestSRV(HttpContants.CMD_GET_CONFIG_DATA, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                ContantsNet contantsNet = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), ContantsNet.class);
                if(EmptyUtils.isNotEmpty(contantsNet)){
                    AppConfigInfo.getInstance().setWarmWishes(contantsNet.getWarmWishes());
                    AppConfigInfo.getInstance().setLicenseUrl(contantsNet.getLicenseUrl());
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {

            }
        });
    }

    /**
     * 获取小贴士
     */
    public void getTipsInfo() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.requestSRV(HttpContants.CMD_GET_TIPS_INFO, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                SmallTips smallTips = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), SmallTips.class);
                if (EmptyUtils.isNotEmpty(smallTips)) {
                    AppConfigInfo.getInstance().setSmallTips(smallTips.getInfoQueryBean());
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {

            }
        });
    }

    /**
     * 获取Banner
     */
    public void getBannerInfo() {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.requestSRV(HttpContants.CMD_GET_BANNER_INFO, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<BannerInfo>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<ArrayList<BannerInfo>>() {
                    }.getType();
                    ArrayList<BannerInfo> bannerInfos = GsonUtil.getGson().fromJson(jsonObject.getString("bannerlist"), typelist);
                    if (EmptyUtils.isNotEmpty(bannerInfos)) {
                        AppConfigInfo.getInstance().setBannerList(bannerInfos);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {

            }
        });
    }


    /**
     * 获取首页信息
     *
     * @param mListener
     */
    public void getHomepageNewsList(double longitude,double latitude,final HttpTaskCallBack<HomePageInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("longitude", longitude);
        networkRequest.setParam("latitude", latitude);
        networkRequest.setCache(true);
        networkRequest.requestSRV(HttpContants.CMD_GET_HOMEPAGE_DATA, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<HomePageInfo> result = new HttpResult<>();
                result.copy(httpResult);
                ArrayList<News> list = new ArrayList<>();
                HomePageInfo homePageInfo = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), HomePageInfo.class);
                if (homePageInfo != null) {
                    if(EmptyUtils.isNotEmpty(homePageInfo.getInfoQueryBean())){
                        AppConfigInfo.getInstance().setSmallTips(homePageInfo.getInfoQueryBean());
                    }
                    //推荐TOP信息
                    ArrayList<NewsTop> tempList = homePageInfo.getTopNewsList();
                    if (EmptyUtils.isNotEmpty(tempList)) {
                        News news = new News(Constants.TYPE_NEWS_ITEM_TOP);
                        news.setNewsTops(tempList);
                        list.add(news);
                    }

                    ArrayList<Column> columns = AppConfigInfo.getInstance().getColumnList();
                    if (EmptyUtils.isNotEmpty(columns)) {
                        News newsItem = new News(Constants.TYPE_NEWS_ITEM_COLUMN);
                        newsItem.setColumns(columns);
                        list.add(newsItem);
                    }

                    //推荐News
                    List<News> tempList1 = homePageInfo.getInfoQueryBeanList();
                    if (EmptyUtils.isNotEmpty(tempList1)) {
                        int size = tempList1.size();
                        for (int i = 0; i < size; i++) {
                            News news = tempList1.get(i);
                            news.setType(Constants.TYPE_NEWS_ITEM_SMALL);
                            list.add(news);
                        }
                    }
                    homePageInfo.setHomeNewsList(list);
                    result.setData(homePageInfo);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<HomePageInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 获取栏目列表
     */
    public void getColumnsList(){
        mHealthCareModel.getColumnsList(null);
    }

    public void submitChooseColumns() {
        String bwInterestStringBuffer = DataCacheUtil.getInstance().getString(DataCacheUtil.BW_INTEREST_STRING_BUFFER,"");
        if(!TextUtils.isEmpty(bwInterestStringBuffer)) {
            mChooseColumnsModel.submitChooseColumns(bwInterestStringBuffer, new HttpTaskCallBack() {
                @Override
                public void onSuccess(HttpResult result) {
                    DataCacheUtil.getInstance().putBoolean(DataCacheUtil.UPLOAD_INTEREST_SUCCESS, true);//保存同步成功的状态到本地
                }

                @Override
                public void onFail(HttpResult result) {
                    LogUtil.show(result.getMsg());
                }
            });
        }
    }

}
