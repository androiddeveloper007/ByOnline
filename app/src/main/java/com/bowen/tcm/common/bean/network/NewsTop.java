package com.bowen.tcm.common.bean.network;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/10.
 */

public class NewsTop implements Serializable{

    private String newsId;
    private String newsTitle;
    private String columnId;
    private String newsSource;
    private String timeStr;
    private String isTopBool;
    private int readNum;
    private String readNumStr;
    private String content;
    private String fileLink;//缩略图url
    private String shareContent;//分享内容
    private String shareUrl;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getIsTopBool() {
        return isTopBool;
    }

    public void setIsTopBool(String isTopBool) {
        this.isTopBool = isTopBool;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getReadNumStr() {
        return readNumStr;
    }

    public void setReadNumStr(String readNumStr) {
        this.readNumStr = readNumStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
