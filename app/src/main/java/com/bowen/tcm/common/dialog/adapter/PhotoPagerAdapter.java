package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.photoview.PhotoView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.PhotoFile;

import java.util.List;


/**
 * 图片浏览PagerAdapter
 * Created by AwenZeng on 2017/1/18.
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ImageView imageView;
    private int position;
    private View.OnClickListener mListener;
    private View.OnKeyListener mKeyListener;
    private List<PhotoFile> choosePicList;

    public PhotoPagerAdapter(Context context, ImageView imageView, int position, List<PhotoFile> list) {
        mContext = context;
        choosePicList = list;
        this.imageView = imageView;
        this.position = position;
    }

    @Override
    public int getCount() {
        return choosePicList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final PhotoView photoView = new PhotoView(mContext);
        photoView.enable();
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageLoaderUtil.getInstance().show(mContext, choosePicList.get(position).getFileLink(), photoView, R.drawable.img_bg);
//        if (imageView != null) {
//            final Info mInfo = PhotoView.getImageViewInfo(imageView);
//            photoView.animaFrom(mInfo);
//            photoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    photoView.animaTo(mInfo, new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mListener != null) {
//                                mListener.onClick(null);
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(null);
                    }
                }
            });
//        }
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnclickListener(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

}
