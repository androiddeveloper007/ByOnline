package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bowen.commonlib.utils.ScreenSizeUtil;

import java.lang.ref.WeakReference;

/**
 * Created by AwenZeng on 2017/1/5.
 */

public class SingleItemAdapter extends BaseAdapter {

    private WeakReference<Context> mContext;

    private String[] mDatas;

    private int mHeight = 40;

    public SingleItemAdapter(Context context, String[] datas) {
        this.mContext = new WeakReference<Context>(context);
        this.mDatas = datas;
        mHeight = ScreenSizeUtil.dp2px(mHeight);
    }

    @Override
    public int getCount() {
        return (null != mDatas) ? mDatas.length : 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtView = null;
        if (null == convertView) {
            convertView = new TextView(mContext.get());
            txtView = (TextView) convertView;
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            txtView.setTextColor(Color.BLACK);
            txtView.setGravity(Gravity.CENTER);
            txtView.setMinHeight(mHeight);
        }
        String text = String.valueOf(mDatas[position]);
        if (null == txtView) {
            txtView = (TextView) convertView;
        }
        txtView.setText(text);
        txtView.setTag(position);
        return convertView;
    }

    /**
     * @return Get data list
     */
    public String[] getDatas() {
        return mDatas;
    }

    public void setmDatas(String[] mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
}