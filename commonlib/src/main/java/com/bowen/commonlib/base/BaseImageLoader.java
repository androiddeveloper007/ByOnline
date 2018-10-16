package com.bowen.commonlib.base;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片下载基类
 * Created by AwenZeng on 2016/12/7.
 */

public abstract class BaseImageLoader {

    public void show(Context context, String url, ImageView imageView){
        show(context,url,imageView,0);
    }
    public abstract void show(Context context,String url,ImageView imageView,int resId);

    public abstract void show(Context context,String url,ImageView imageView,int resId,boolean isShowRound);

    public void clearImageDiskCache() {};
    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(){};

    public void clearImageAllCache() {
        clearImageDiskCache();
        clearImageMemoryCache();
    }
}
