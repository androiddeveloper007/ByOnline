package com.bowen.tcm.healthcare.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bowen.tcm.healthcare.fragment.NewsFragment;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/10.
 * 养生栏目的viewPager适配器
 */

public class HealthCarePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<NewsFragment> mFragments;

    public HealthCarePagerAdapter(FragmentManager fm, ArrayList<NewsFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;

    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public NewsFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    public void setmFragments(ArrayList<NewsFragment> mFragments) {
        this.mFragments = mFragments;
    }
}