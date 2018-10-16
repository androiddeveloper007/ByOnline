package com.bowen.commonlib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseImageLoader;
import com.bowen.commonlib.http.image.GlideImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 图片加载工具类
 * Created by AwenZeng on 2016/12/7.
 */

public class ImageLoaderUtil extends BaseImageLoader {

    private static ImageLoaderUtil instance;
    private BaseImageLoader imageLoader;

    public ImageLoaderUtil() {
        imageLoader = new GlideImageLoader();
    }

    public static ImageLoaderUtil getInstance(){
        if(instance == null){
            synchronized (ImageLoaderUtil.class){
                if(instance == null){
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }

    @Override
    public void show(Context context,String url, ImageView imageView, int resId) {
        imageLoader.show(context,url,imageView,resId);
    }

    @Override
    public void show(Context context,String url, ImageView imageView, int resId, boolean isShowRound) {
        imageLoader.show(context,url,imageView,resId,isShowRound);
    }


    @Override
    public void clearImageDiskCache() {
        super.clearImageDiskCache();
        imageLoader.clearImageDiskCache();
    }

    @Override
    public void clearImageMemoryCache() {
        super.clearImageMemoryCache();
        clearImageMemoryCache();
    }

    public void setTextViewDrawableLeft(Context context, TextView tv, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 这一步必须要做,否则不会显示.
        tv.setCompoundDrawables(drawable, null, null, null);
    }
}
