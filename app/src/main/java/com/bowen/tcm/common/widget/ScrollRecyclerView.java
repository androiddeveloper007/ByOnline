package com.bowen.tcm.common.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bowen.commonlib.utils.EmptyUtils;

/**
 * Describe:滑动监听scrollY的变化的RecyclerView
 * Created by AwenZeng on 2017/6/14.
 */

public class ScrollRecyclerView extends RecyclerView {

    private int lastScrollY;
    private OnScrollListener onScrollListener;

    public ScrollRecyclerView(Context context) {
        super(context);
        init();
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init(){
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollListener != null) {
                    onScrollListener.onScroll(lastScrollY = computeVerticalScrollOffset());
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(handler.obtainMessage(), 5);
        }
        if(computeVerticalScrollOffset()>100){
            
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float startX = 0;
        float startY = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > distanceY) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    public interface OnScrollListener {
        void onScroll(int scrollY);
    }

    public void setScrolListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int scrollY = computeVerticalScrollOffset();
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
        }
    };

    public void removeCallbacksAndMessages(){
        if(EmptyUtils.isNotEmpty(handler)){
            handler.removeCallbacksAndMessages(null);
        }
    }


}
