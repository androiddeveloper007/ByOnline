package com.bowen.tcm.common.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Describe:聊天消息
 * Created by AwenZeng on 2018/7/18.
 */
@Entity
public class ChatMessage {
    @Id(autoincrement = true)
    private Long id;
    private int type;
    private String userId;
    private String name;
    private String date;
    private String content;
    private String imgPath;

    public ChatMessage() {
    }

    public ChatMessage(int type, String content) {
        this.type = type;
        this.content = content;
    }

    @Generated(hash = 489220870)
    public ChatMessage(Long id, int type, String userId, String name, String date,
            String content, String imgPath) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.content = content;
        this.imgPath = imgPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
