package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.network.ChatStatusInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.database.entity.ChatMessage;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public interface ChatContract {

    interface View {
        void onSendMessage(ChatMessage message);
        void getChatDataBaseSuccess(ArrayList<ChatMessage> list);
        void getChatDataBaseFailed();
        void getChatStatusInfoSuccess(ChatStatusInfo info);
        void onCloseChatConsultSuccess();
        void onGetDoctorDetailSuccess(DoctorDetail doctorDetail);
        void onCheckImmediateConsultSuccess(Doctor doctor);
    }

    interface Presenter{
        void sendMessage(String content);
        void sendImgMessage(String imgPath);
        void getChatStatusInfo(String doctorId,String orderId);
        void closeChatConsult(String orderId);
        void clearChatStatus(String userId);
        void checkImmediateConsult(String doctorId);
    }

}
