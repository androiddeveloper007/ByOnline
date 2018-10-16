package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe: 小贴士
 * Created by AwenZeng on 2018/5/23.
 */
public class Tips implements Serializable {
    /**
     * fileLink : http://192.168.0.241/tcm/image/5ad7f6a7ec2ea96ca2d89ae6
     * appPicUrl : http://192.168.0.241:8028/images/seo/seo_az_pic.jpg
     * tipsWriter : 曾国藩
     * tips : 且苟能发奋自立，则家塾可读书，即旷野之地，热闹之场，亦可读书，负薪牧豕，皆可读书。苟不能发奋自立，则家塾不宜读书，即清净之乡，神仙之境，皆不能读书。何必择地，何必择时，但自问立志之真不真耳。
     */

    private String fileLink;
    private String appPicUrl;
    private String tipsWriter;
    private String tips;

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getAppPicUrl() {
        return appPicUrl;
    }

    public void setAppPicUrl(String appPicUrl) {
        this.appPicUrl = appPicUrl;
    }

    public String getTipsWriter() {
        return tipsWriter;
    }

    public void setTipsWriter(String tipsWriter) {
        this.tipsWriter = tipsWriter;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
