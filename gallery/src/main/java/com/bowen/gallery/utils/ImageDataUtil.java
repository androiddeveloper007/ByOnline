package com.bowen.gallery.utils;

import com.bowen.gallery.model.LocalMedia;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ImageDataUtil {
    private static ImageDataUtil mImageDataUtil = null;
    private List<LocalMedia> images;
    private ImageDataUtil(){}
    public static ImageDataUtil getInstance(){
          synchronized (ImageDataUtil.class){
             if(mImageDataUtil == null){
                mImageDataUtil = new ImageDataUtil();
             }
          }
          return mImageDataUtil;
    }

    public List<LocalMedia> getImages() {
        return images;
    }

    public void setImages(List<LocalMedia> images) {
        this.images = images;
    }
}
