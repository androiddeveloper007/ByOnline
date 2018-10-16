package com.bowen.tcm.main.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.MessageInfo;
import com.bowen.tcm.main.contract.MessageContract;
import com.bowen.tcm.main.model.MessageModel;


public class MessagePresenter extends BasePresenter implements MessageContract.Presenter{

    private MessageModel mMessageModel;
    private MessageContract.View mView;

    public MessagePresenter(Context context, MessageContract.View view) {
        super(context);
        mMessageModel = new MessageModel(context);
        mView = view;
    }


    @Override
    public void getMessageList(int pageNo, int pageSize) {
        mMessageModel.getMessageList(pageNo, pageSize, new HttpTaskCallBack<MessageInfo>() {
            @Override
            public void onSuccess(HttpResult<MessageInfo> result) {
                mView.onGetMessageListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<MessageInfo> result) {
               mView.onGetMessageListFailed();
            }
        });

    }

    @Override
    public void changeMsgStatus(String msgId, String msgStatus) {
       mMessageModel.changeMsgStatus(msgId, msgStatus, new HttpTaskCallBack() {
           @Override
           public void onSuccess(HttpResult result) {
               mView.onChangeMessageStatusSuccess();

           }

           @Override
           public void onFail(HttpResult result) {

           }
       });
    }

    @Override
    public void clearMessage(String msgId) {
        mMessageModel.clearMessage(msgId, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                 mView.onClearMessageSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

    @Override
    public void addMessage(String remindId) {
        mMessageModel.addMessage(remindId, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {

            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }
}
