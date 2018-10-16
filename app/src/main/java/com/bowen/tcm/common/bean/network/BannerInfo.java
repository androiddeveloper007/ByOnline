package com.bowen.tcm.common.bean.network;

/**
 * Describe: Banner信息
 * Created by AwenZeng on 2018/5/24.
 */
public class BannerInfo {
    /**
     * fileLink : http://192.168.0.242/tcm/image/5afd43f106b9f00d178987f6
     * bannerPosition : 4
     * bannerTitle : 特朗
     * linkUrl : www.baidu.com
     */
    private String fileLink;
    private String bannerPosition;
    private String bannerTitle;
    private String linkUrl;

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getBannerPosition() {
        return bannerPosition;
    }

    public void setBannerPosition(String bannerPosition) {
        this.bannerPosition = bannerPosition;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
