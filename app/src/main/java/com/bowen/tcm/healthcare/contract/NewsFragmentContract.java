package com.bowen.tcm.healthcare.contract;

import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.NewsInfo;

import java.util.ArrayList;

/**
 * Created by zzp on 2018/5/15.
 */

public interface NewsFragmentContract {

    interface View {
        void onGetRecommendNewsListSuccess(NewsInfo newsInfo);
        void onGetColumnNewsListSuccess(NewsInfo newsInfo);
        void onGetRecommendNewsListFailed();
        void onGetColumnNewsListFailed();
    }

    interface Presenter{
        void onGetRecommendNewsList(int pageNo, int pageSize);
        void onGetColumnNewsList(int pageNo, int pageSize, String columnId);
    }
}
