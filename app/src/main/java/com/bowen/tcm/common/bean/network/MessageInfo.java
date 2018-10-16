package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/25.
 */
public class MessageInfo {
    private ArrayList<Message> msgList;
    private Page page;

    public ArrayList<Message> getMsgList() {
        return msgList;
    }

    public void setMsgList(ArrayList<Message> msgList) {
        this.msgList = msgList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
