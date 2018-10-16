package com.bowen.commonlib.widget.mypagerindicator;

/**
 * 可测量内容区域的指示器标题
 * Created by zhuzhipeng on 2018/6/26
 */
public interface IMeasurablePagerTitleView extends IPagerTitleView {
    int getContentLeft();

    int getContentTop();

    int getContentRight();

    int getContentBottom();
}
