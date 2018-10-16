package com.bowen.tcm.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * 描述：我的偏方viewpager的adapter
 * Created by zhuzhipeng on 2018/6/26
 */

public class MyPrescriptionPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private final String[] TITLES = {"收藏偏方", "我的偏方"};


    public MyPrescriptionPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
