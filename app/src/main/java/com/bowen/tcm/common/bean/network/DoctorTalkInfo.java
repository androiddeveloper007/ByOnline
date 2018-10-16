package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.model.UserInfo;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/11.
 */
public class DoctorTalkInfo {
    /**
     * articleId :
     * title :
     * content :
     * imgUrl :
     * createTime :
     */
    private String articleId;
    private String title;
    private String content;
    private String imgUrl;
    private String createTime;


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
