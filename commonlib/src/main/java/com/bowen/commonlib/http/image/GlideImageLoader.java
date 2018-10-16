package com.bowen.commonlib.http.image;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.base.BaseImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Glide图片网络框架实现类
 * Created by AwenZeng on 2016/12/7.
 */

public class GlideImageLoader extends BaseImageLoader {

    @Override
    public void show(Context context, String url, ImageView imageView, int resId) {
        Glide.with(context).load(url).dontAnimate().placeholder(resId).into(imageView);
    }

    @Override
    public void show(Context context,String url, ImageView imageView, int resId, boolean isShowRound) {
        Glide.with(context).load(url).dontAnimate().placeholder(resId).transform(new GlideCircleTransform(context)).into(imageView);
    }

    @Override
    public void clearImageDiskCache() {
        super.clearImageDiskCache();
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Observable.just("").observeOn(Schedulers.io()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Glide.get(CommonLibApplication.getCommonLibContext()).clearDiskCache();
                    }
                });
            } else {
                Glide.get(CommonLibApplication.getCommonLibContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearImageMemoryCache() {
        super.clearImageMemoryCache();
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(CommonLibApplication.getCommonLibContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
