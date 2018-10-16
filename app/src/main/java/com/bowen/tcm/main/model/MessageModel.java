package com.bowen.tcm.main.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.MessageInfo;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe:消息Model
 * Created by AwenZeng on 2018/5/18.
 */

public class MessageModel extends BaseModel {
    public MessageModel(Context mContext) {
        super(mContext);
    }

    public void getMessageCount(final HttpTaskCallBack<Integer> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_GET_MESSAGE_COUNT, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Integer> result = new HttpResult<Integer>();
                result.copy(httpResult);
                try{
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    int count = jsonObject.optInt("count");
                    result.setData(count);
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Integer> result = new HttpResult<Integer>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void getMessageList(int pageNo,int pageSize,final HttpTaskCallBack<MessageInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_MESSAGE_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<MessageInfo> result = new HttpResult<MessageInfo>();
                result.copy(httpResult);
                MessageInfo messageInfo = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), MessageInfo.class);
                if(EmptyUtils.isNotEmpty(messageInfo)){
                    result.setData(messageInfo);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<MessageInfo> result = new HttpResult<MessageInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }
        });
    }

    public void changeMsgStatus(String msgId,String msgStatus,final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("msgId", msgId);
        networkRequest.setParam("msgStatus", msgStatus);
        networkRequest.requestSRV(HttpContants.CMD_CHANGE_MESSAGE_STATUS, new HttpTaskCallBack<Object>() {
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

    public void clearMessage(String msgId,final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("msgIds", msgId);
        networkRequest.requestSRV(HttpContants.CMD_CLEAR_MESSAGE, new HttpTaskCallBack<Object>() {
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

    public void addMessage(String remindId,final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("remindId", remindId);
        networkRequest.setParam("msgType", 1);
        networkRequest.requestSRV(HttpContants.CMD_ADD_MESSAGE, new HttpTaskCallBack<Object>() {
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

}
