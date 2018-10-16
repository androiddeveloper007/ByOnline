package com.bowen.tcm.mine.contract;

import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/16.
 */

public interface FeedbackContract {
    interface View {
        void onFeedBackSuccess();
    }

    interface Presenter{
        void uploadFeedBackData(String content, List<String> pics);
        void submitFeedBackData(String content);
    }

}
