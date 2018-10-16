package com.bowen.tcm.main.contract;

import com.bowen.tcm.common.bean.network.MessageInfo;
import com.bowen.tcm.common.bean.network.NewsInfo;

/**
 * Created by zzp on 2018/5/15.
 */

public interface MessageContract {

    interface View {
        void onGetMessageListSuccess(MessageInfo messageInfo);
        void onChangeMessageStatusSuccess();
        void onClearMessageSuccess();
        void onGetMessageListFailed();
    }

    interface Presenter{
        void getMessageList(int pageNo,int pageSize);
        void changeMsgStatus(String msgId,String msgStatus);
        void clearMessage(String msgId);
        void addMessage(String remindId);
    }
}
