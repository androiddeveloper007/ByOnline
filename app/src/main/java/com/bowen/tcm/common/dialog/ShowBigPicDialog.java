package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.dialog.adapter.PhotoPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AwenZeng on 2016/12/15.
 */

public class ShowBigPicDialog extends BaseDialog {
    @BindView(R.id.mViewPage)
    ViewPager mViewPage;
    @BindView(R.id.mPhotoIndexTv)
    TextView mPhotoIndexTv;

    private ImageView imageView;
    private String imgPath;
    private int position;
    private int size;
    private PhotoPagerAdapter adapter;
    private List<PhotoFile> choosePicList;

    public ShowBigPicDialog(Context context, ImageView imageView, int pos, List<PhotoFile> list) {
        super(context, R.style.dialog_transparent_style);
        this.choosePicList = list;
        position = pos;
        this.imageView = imageView;
        size = choosePicList.size();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_big_picture);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        if(size == 1){
            mPhotoIndexTv.setVisibility(View.INVISIBLE);
        }else{
            mPhotoIndexTv.setVisibility(View.VISIBLE);
        }
        mPhotoIndexTv.setText(position + "/" + size);
        adapter = new PhotoPagerAdapter(mContext, imageView, position, choosePicList);
        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mViewPage.setAdapter(adapter);
        if (position > 1) {
            mViewPage.setCurrentItem(position - 1);
        }
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPhotoIndexTv.setText((position + 1) + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //设置对话框显示和消失时无动画
        getWindow().setWindowAnimations(R.style.dialog_no_animation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }
}
