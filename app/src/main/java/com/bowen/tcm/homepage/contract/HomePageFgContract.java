package com.bowen.tcm.homepage.contract;

import com.bowen.tcm.common.bean.network.HomePageInfo;
import com.bowen.tcm.common.bean.network.NewsInfo;

/**
 * Created by zzp on 2018/5/15.
 */

public interface HomePageFgContract {

    interface View {
        void onGetHomePageInfoSuccess(HomePageInfo info);
        void onGetHomePageInfoFailed();
        void onGetMessageCountSuccess(int msgCount);
    }

    interface Presenter{
        void getHomePageInfo(double longitude,double latitude);
    }
}
