package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.ChatStatusInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.database.entity.ChatMessage;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.inquiry.contract.ChatContract;
import com.bowen.tcm.inquiry.model.DoctorModel;
import com.bowen.tcm.inquiry.model.chat.ChatMessageManager.ChatMessageListener;
import com.bowen.tcm.inquiry.model.chat.ChatModel;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager.ChatServerLoginListener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;

import java.util.ArrayList;

import static com.bowen.tcm.common.http.HttpContants.CHAT_SERVER_URL;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class ChatPresenter extends BasePresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private Chat mChat;
    private ChatManager mChatManager;
    private ChatModel mChatModel;
    private DoctorModel mDoctorModel;

    private String toUserID;
    private String orderNo;

    public ChatPresenter(Context mContext, ChatContract.View view, String userId,String orderId) {
        super(mContext);
        mView = view;
        toUserID = userId;
        orderNo = orderId;
        mChatModel = new ChatModel(mContext);
        mDoctorModel = new DoctorModel(mContext);
        if (ChatServerManager.getConnection().isConnected()) {
            mChatManager = ChatServerManager.getConnection().getChatManager();
            mChat = mChatManager.createChat(toUserID+"@"+CHAT_SERVER_URL, null);
        }else{
            ChatServerManager.login(new ChatServerLoginListener() {
                @Override
                public void backLoginSucessStatus(boolean isSuccess) {
                    if (isSuccess) {
                        mChatModel.addChatListener();
                        mChatManager = ChatServerManager.getConnection().getChatManager();
                        mChat = mChatManager.createChat(toUserID+"@"+CHAT_SERVER_URL, null);
                    }
                }
            });
        }
    }

    @Override
    public void sendMessage(String content) {
        if (EmptyUtils.isEmpty(mChat)) {
            return;
        }
        mChatModel.sendMessage(mChat, toUserID, orderNo, content, new ChatMessageListener() {
            @Override
            public void onRecieveMessage(ChatMessage msg) {
                mView.onSendMessage(msg);
            }
        });
    }

    @Override
    public void sendImgMessage(String imgPath) {
        mChatModel.sendImgMessage( mChat, toUserID, orderNo,  imgPath, new ChatMessageListener() {
            @Override
            public void onRecieveMessage(ChatMessage msg) {
                mView.onSendMessage(msg);
            }
        });
    }

    /**
     * 从数据库获取聊天数据
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public void queryChatData(String toUserID, int pageNo, int pageSize) {
        ArrayList<ChatMessage> list = new ArrayList<>();
        if (pageNo >= 0) {
            list.addAll(mChatModel.queryChatData(toUserID, pageNo, pageSize));
            if (EmptyUtils.isNotEmpty(list)) {
                mView.getChatDataBaseSuccess(list);
                return;
            }
        }
        mView.getChatDataBaseFailed();
    }

    /**
     * 插入数据
     * @param message
     */
    public void insertData(ChatMessage message) {
        mChatModel.insertData(message);
    }



    public int getDataSize(String userId) {
        return mChatModel.getDataSize(userId + UserInfo.getInstance().getUserId());
    }

    public void showNotification(String content){
        mChatModel.showNotification(content);
    }

    @Override
    public void getChatStatusInfo(String doctorId, String orderId) {
        mChatModel.getChatStatusInfo(doctorId, orderId, new HttpTaskCallBack<ChatStatusInfo>() {
            @Override
            public void onSuccess(HttpResult<ChatStatusInfo> result) {
                mView.getChatStatusInfoSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ChatStatusInfo> result) {

            }
        });
    }


    @Override
    public void closeChatConsult(String orderId) {
        mChatModel.colseChatConsult(orderId, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onCloseChatConsultSuccess();
            }

            @Override
            public void onFail(HttpResult result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }

    @Override
    public void clearChatStatus(String userId) {
        mChatModel.clearChatStatus(userId, new HttpTaskCallBack<String>() {
            @Override
            public void onSuccess(HttpResult<String> result) {

            }

            @Override
            public void onFail(HttpResult<String> result) {

            }
        });
    }

    @Override
    public void checkImmediateConsult(String doctorId){
        mDoctorModel.checkImmediateConsult(doctorId, new HttpTaskCallBack<Doctor>() {
            @Override
            public void onSuccess(HttpResult<Doctor> result) {
                mView.onCheckImmediateConsultSuccess(result.getData());

            }

            @Override
            public void onFail(HttpResult<Doctor> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }
}
