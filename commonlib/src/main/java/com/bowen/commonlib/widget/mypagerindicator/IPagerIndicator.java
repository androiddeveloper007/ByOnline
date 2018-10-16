package com.bowen.commonlib.widget.mypagerindicator;


import java.util.List;

/**
 * 抽象的viewpager指示器，适用于CommonNavigator
 * Created by zhuzhipeng on 2018/6/26
 */
public interface IPagerIndicator {
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onPositionDataProvide(List<PositionData> dataList);
}
