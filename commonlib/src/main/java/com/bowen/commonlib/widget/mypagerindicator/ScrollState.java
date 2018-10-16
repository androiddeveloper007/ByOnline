package com.bowen.commonlib.widget.mypagerindicator;

/**
 * 自定义滚动状态，消除对ViewPager的依赖
 * Created by zhuzhipeng on 2018/6/26
 */

public interface ScrollState {
    int SCROLL_STATE_IDLE = 0;
    int SCROLL_STATE_DRAGGING = 1;
    int SCROLL_STATE_SETTLING = 2;
}
