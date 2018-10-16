package com.bowen.tcm.inquiry.model.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.CheckStringUtl;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.ChatStatusInfo;
import com.bowen.tcm.common.database.entity.ChatMessage;
import com.bowen.tcm.common.database.util.DataBaseOperateManager;
import com.bowen.tcm.common.event.ChatMessageEvent;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.inquiry.model.chat.ChatMessageManager.ChatMessageListener;
import com.bowen.tcm.mine.activity.MyConsultActivity;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.Chat;

import java.util.List;


/**
 * Describe:
 * Created by AwenZeng on 2018/7/19.
 */
public class ChatModel extends BaseModel {

    private NotificationManager mNotificationManager;
    private DataBaseOperateManager mDBOperateManager;

    public ChatModel(Context mContext) {
        super(mContext);
        mDBOperateManager = new DataBaseOperateManager();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /***
     * 结束聊天咨询
     * @param orderId
     * @param mListener
     */
    public void colseChatConsult(String orderId, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("orderId", orderId);
        networkRequest.requestSRV(HttpContants.CMD_GET_CHAT_CLOSE_CONSULT, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<Object>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取聊天状态信息
     * @param doctorId
     * @param orderId
     * @param mListener
     */
    public void getChatStatusInfo(String doctorId,String orderId, final HttpTaskCallBack<ChatStatusInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);

        if(CheckStringUtl.isAllNumber(orderId)){
            networkRequest.setParam("orderId", orderId);
        }else{
            networkRequest.setParam("appointmentOrderId", orderId);
        }
        networkRequest.requestSRV(HttpContants.CMD_GET_CHAT_STATUS_INFO, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ChatStatusInfo> result = new HttpResult<>();
                result.copy(httpResult);
                ChatStatusInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), ChatStatusInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<ChatStatusInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 清除聊天消息状态
     * @param userId
     */
    public void clearChatStatus(String userId,final HttpTaskCallBack<String> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("sendUserId", userId);
        networkRequest.requestSRV(HttpContants.CMD_CLEAR_CHAT_STATUS, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<String> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<String> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    public void showNotification(String content) {
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("您有新的消息！！！");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("聊天消息");
        if(content.contains(Constants.CHAT_OVER_TAG)){
            builder.setContentText("结束服务");
        }else{
            builder.setContentText(content);
        }
        Intent intent = new Intent(mContext, MyConsultActivity.class);
        PendingIntent ma = PendingIntent.getActivity(mContext, 0, intent, 0);
        builder.setContentIntent(ma);//设置点击过后跳转的activity
        builder.setDefaults(Notification.DEFAULT_ALL);//设置全部

        Notification notification = builder.build();//4.1以上用.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉
        mNotificationManager.notify(0, notification);

    }


    public  void addChatListener() {
         ChatMessageManager.getInstance().addChatListener(new ChatMessageListener() {
             @Override
             public void onRecieveMessage(ChatMessage msg) {
                 insertData(msg);
                 if (UserInfo.getInstance().isStartChat()) {
                     EventBus.getDefault().post(new ChatMessageEvent(msg));
                 } else {
                     showNotification(msg.getContent());
                 }
             }
         });
    }

    public void addFileListener() {
        ChatMessageManager.getInstance().addFileListener(new ChatMessageListener() {
            @Override
            public void onRecieveMessage(ChatMessage message) {
                insertData(message);
                if (UserInfo.getInstance().isStartChat()) {
                    EventBus.getDefault().post(new ChatMessageEvent(message));
                } else {
                    showNotification("图片信息");
                }
            }
        });
    }

    public void sendMessage(final Chat chat, final String toUserId, final String orderNo, String content, final ChatMessageListener listener) {
        ChatMessageManager.getInstance().sendMessage(chat, toUserId, orderNo, content, new ChatMessageListener() {
            @Override
            public void onRecieveMessage(ChatMessage msg) {
                insertData(msg);
                if (EmptyUtils.isNotEmpty(listener)) {
                    listener.onRecieveMessage(msg);
                }
            }
        });
    }

    public void sendImgMessage(final String toUserId,String imgPath,final ChatMessageListener listener) {
        ChatMessageManager.getInstance().sendImgMessage(toUserId, imgPath, new ChatMessageListener() {
            @Override
            public void onRecieveMessage(ChatMessage msg) {
                insertData(msg);
                if (EmptyUtils.isNotEmpty(listener)) {
                    listener.onRecieveMessage(msg);
                }
            }
        });
    }

    public void sendImgMessage(final Chat chat,final String toUserId,final String orderNo,String imgPath,final ChatMessageListener listener) {
        ChatMessageManager.getInstance().sendImgMessage(chat,toUserId,orderNo,imgPath, new ChatMessageListener() {
            @Override
            public void onRecieveMessage(ChatMessage msg) {
                insertData(msg);
                if (EmptyUtils.isNotEmpty(listener)) {
                    listener.onRecieveMessage(msg);
                }
            }
        });
    }

    /**
     * 插入数据
     * @param message
     */
    public void insertData(ChatMessage message) {
        mDBOperateManager.insert(message);
    }

    /**
     * 根据userId获取消息条数
     * @param userId
     * @return
     */
    public int getDataSize(String userId) {
        return mDBOperateManager.getSize(userId);
    }

    /**
     * 分页获取消息数
     * @param toUserID
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<ChatMessage> queryChatData(String toUserID, int pageNo, int pageSize) {
       return mDBOperateManager.queryAll(toUserID, pageNo, pageSize);
    }

}
