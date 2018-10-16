package com.bowen.commonlib.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * ScrollView自定义，监听滑动
 * Created by AwenZeng on 2016/12/5.
 */

public class CustomScrollView extends ScrollView {

    private int lastScrollY;

    private OnScrollListener onScrollListener;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(handler.obtainMessage(), 20);
        }
        return super.onTouchEvent(ev);
    }

    public interface OnScrollListener {
        void onScroll(int scrollY);
    }

    public void setScrolListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int scrollY = CustomScrollView.this.getScrollY();
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
        }
    };

}
