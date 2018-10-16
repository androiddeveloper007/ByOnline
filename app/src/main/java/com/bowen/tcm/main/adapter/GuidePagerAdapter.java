package com.bowen.tcm.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by AwenZeng on 2017/1/14.
 */

public class GuidePagerAdapter extends PagerAdapter {

    private ArrayList<View> viewLayouts;

    public GuidePagerAdapter(ArrayList<View> viewLayouts) {
        this.viewLayouts = viewLayouts;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return viewLayouts.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(viewLayouts.get(position));
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        container.addView(viewLayouts.get(position));
        return viewLayouts.get(position);
    }
}
