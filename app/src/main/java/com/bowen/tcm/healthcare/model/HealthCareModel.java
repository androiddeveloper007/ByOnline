package com.bowen.tcm.healthcare.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.bean.network.ColumnNews;
import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.NewsInfo;
import com.bowen.tcm.common.bean.network.NewsTop;
import com.bowen.tcm.common.bean.network.RecommendNews;
import com.bowen.tcm.common.bean.network.Tips;
import com.bowen.tcm.common.event.ColumnEvent;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class HealthCareModel extends BaseModel {


    public HealthCareModel(Context mContext) {
        super(mContext);
    }

    /**
     * 获取栏目列表
     *
     * @param mListener
     */
    public void getColumnsList(final HttpTaskCallBack<ArrayList<Column>> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        if (LoginStatusUtil.getInstance().isLogin()) {
            networkRequest.setParam("token", UserInfo.getInstance().getToken());
            networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        }
        networkRequest.setCache(true);
        networkRequest.requestSRV(HttpContants.CMD_GET_COLUMNS_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<Column>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<Column>>() {
                    }.getType();
                    ArrayList<Column> columns = GsonUtil.getGson().fromJson(jsonObject.getString("infoColumnList"), typelist);
                    ArrayList<Column> temps = handleColumn(columns);
                    if (EmptyUtils.isNotEmpty(temps)) {
                        result.setData(temps);
                        EventBus.getDefault().post(new ColumnEvent());
                    }
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<ArrayList<Column>> result = new HttpResult<ArrayList<Column>>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    private ArrayList<Column> handleColumn(ArrayList<Column> columns){
        //这里判断一下，如果是未登录且有排序缓存，利用缓存排序一下
        if(!LoginStatusUtil.getInstance().isLogin() && DataCacheUtil.getInstance().getBoolean(DataCacheUtil.CHOOSE_INTEREST_SUCCESS, false)) {
            String bwInterestStringBuffer = DataCacheUtil.getInstance().getString(DataCacheUtil.BW_INTEREST_STRING_BUFFER,"");
            String[] arr = bwInterestStringBuffer.split(",");
            Column c = new Column();
            c.setColumnName("推荐");
            c.setColumnId("recommend");
            columns.add(0, c);
            for(String str : arr) {
                for(Column column : columns) {
                    if(column.equals(str)){
                        columns.add(column);
                    }
                }
            }
            AppConfigInfo.getInstance().setColumnList(columns);
            return columns;
        } else {
            if (EmptyUtils.isNotEmpty(columns)) {
                Column column = new Column();
                column.setColumnName("推荐");
                column.setColumnId("recommend");
                columns.add(0, column);
                AppConfigInfo.getInstance().setColumnList(columns);
                 return columns;
            }
        }
       return null;
    }

    public void saveColumnsData(ArrayList<Column> list, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        StringBuffer buffer = new StringBuffer();
        boolean flag = false;
        for (Column item : list) {
            if (item.isCheckedBool() && !flag) {
                buffer.append(item.getColumnId());
                flag = true;
            } else if (item.isCheckedBool() && flag) {
                buffer.append("," + item.getColumnId());
            }
        }
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("columnIds", buffer.toString());
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

    /**
     * 获取首页NEWS列表
     *
     * @param mListener
     */
    public void getHomepageNewsList(final HttpTaskCallBack<NewsInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("isHome", true);
        networkRequest.setCache(true);
        networkRequest.requestSRV(HttpContants.CMD_GET_RECOMMEND_NEWS_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<NewsInfo> result = new HttpResult<>();
                result.copy(httpResult);
                NewsInfo newsInfo = new NewsInfo();
                ArrayList<News> list = new ArrayList<>();
                RecommendNews recommendNews = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), RecommendNews.class);
                if (recommendNews != null) {
                    newsInfo.setPage(recommendNews.getPage());
                    //推荐TOP信息
                    ArrayList<NewsTop> tempList = recommendNews.getTopNewsList();
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
                    List<News> tempList1 = recommendNews.getInfoQueryBeanList();
                    if (EmptyUtils.isNotEmpty(tempList1)) {
                        int size = tempList1.size();
                        for (int i = 0; i < size; i++) {
                            News news = tempList1.get(i);
                            if ((i + 1) % 5 == 0) {
                                news.setType(Constants.TYPE_NEWS_ITEM_BIG);
                            } else {
                                news.setType(Constants.TYPE_NEWS_ITEM_SMALL);
                            }
                            list.add(news);
                        }
                    }
                    newsInfo.setNewsList(list);
                    result.setData(newsInfo);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<NewsInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 获取推荐栏目NEWS列表
     *
     * @param pageNo
     * @param pageSize
     * @param isHome
     * @param mListener
     */
    public void getRecommendNewsList(int pageNo, int pageSize, final boolean isHome, final HttpTaskCallBack<NewsInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.setParam("isHome", isHome);
        networkRequest.setCache(true);
        networkRequest.requestSRV(HttpContants.CMD_GET_RECOMMEND_NEWS_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<NewsInfo> result = new HttpResult<>();
                result.copy(httpResult);
                NewsInfo newsInfo = new NewsInfo();
                ArrayList<News> list = new ArrayList<>();
                RecommendNews recommendNews = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), RecommendNews.class);
                if (recommendNews != null) {
                    newsInfo.setPage(recommendNews.getPage());
                    if (recommendNews.getPage().getPageNo() == 1) {//第一页才解析
                        //推荐TOP信息
                        ArrayList<NewsTop> tempList = recommendNews.getTopNewsList();
                        if (EmptyUtils.isNotEmpty(tempList)) {
                            News news = new News(Constants.TYPE_NEWS_ITEM_TOP);
                            news.setNewsTops(tempList);
                            list.add(news);
                        }

                        if (isHome) {//是否是首页,true:添加栏目，false:添加小贴士
                            ArrayList<Column> columns = AppConfigInfo.getInstance().getColumnList();
                            if (EmptyUtils.isNotEmpty(columns)) {
                                News newsItem = new News(Constants.TYPE_NEWS_ITEM_COLUMN);
                                newsItem.setColumns(columns);
                                list.add(newsItem);
                            }
                        } else {//小贴士
                            Tips tips = AppConfigInfo.getInstance().getSmallTips();
                            if (EmptyUtils.isNotEmpty(tips)) {
                                News newsItem = new News(Constants.TYPE_NEWS_ITEM_TIPS);
                                newsItem.setTips(tips);
                                list.add(newsItem);
                            }
                        }
                    }

                    //推荐News
                    List<News> tempList1 = recommendNews.getInfoQueryBeanList();
                    if (EmptyUtils.isNotEmpty(tempList1)) {
                        int size = tempList1.size();
                        for (int i = 0; i < size; i++) {
                            News news = tempList1.get(i);
                            if ((i + 1) % 5 == 0) {
                                news.setType(Constants.TYPE_NEWS_ITEM_BIG);
                            } else {
                                news.setType(Constants.TYPE_NEWS_ITEM_SMALL);
                            }
                            list.add(news);
                        }
                    }
                    newsInfo.setNewsList(list);
                    result.setData(newsInfo);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<NewsInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取栏目NEW列表
     *
     * @param pageNo
     * @param pageSize
     * @param columnId
     * @param mListener
     */
    public void getColumnNewsList(int pageNo, int pageSize, String columnId, final HttpTaskCallBack<NewsInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.setParam("columnId", columnId);
        networkRequest.setCache(true);
        networkRequest.requestSRV(HttpContants.CMD_GET_COLUMN_NEWS_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<NewsInfo> result = new HttpResult<>();
                result.copy(httpResult);
                NewsInfo newsInfo = new NewsInfo();
                ArrayList<News> list = new ArrayList<>();
                ColumnNews columnNews = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), ColumnNews.class);
                if (columnNews != null) {
                    newsInfo.setPage(columnNews.getPage());
                    //推荐News
                    List<News> tempList = columnNews.getInfoQueryBeanList();
                    if (EmptyUtils.isNotEmpty(tempList)) {
                        int size = tempList.size();
                        for (int i = 0; i < size; i++) {
                            News news = tempList.get(i);
                            if ((i + 1) % 5 == 0) {
                                news.setType(Constants.TYPE_NEWS_ITEM_BIG);
                            } else {
                                news.setType(Constants.TYPE_NEWS_ITEM_SMALL);
                            }
                            list.add(news);
                        }
                    }
                    newsInfo.setNewsList(list);
                    result.setData(newsInfo);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<NewsInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

}
