package com.bowen.tcm.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;

/**
 * Describe:左滑关闭Acitivty
 * Created by AwenZeng on 2018/7/2.
 */
public class SlidingExitLayout extends FrameLayout {

    private Activity mActivity;
    private Scroller mScroller;
    private Drawable mLeftShadow; // 页面边缘的阴影图
    private Drawable mBottomShadow; // 页面边缘的阴影图
    private int mShadowWidth; // 页面边缘阴影的宽度
    private int mShadowHeigh; // 页面边缘阴影的高度
    private int mInterceptDownX;
    private int mInterceptDownY;
    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mTouchDownX;
    private int mTouchDownY;
    private int mLastTouchX;
    private int mLastTouchY;
    private boolean isConsumed = false;
    private boolean isEnableUpSlide = false;//是否
    private int mSlideDirection;
    /**
     * 页面边缘阴影的宽度默认值
     */
    private static final int SHADOW_WIDTH = 5;

    private static final int SLIDE_LEFT = 0;//左滑
    private static final int SLIDE_RIGHT= 1;//上滑
    private static final int SLIDE_UP = 2;//上滑
    private static final int SLIDE_DOWN= 3;//上滑

    public SlidingExitLayout(Context context) {
        this(context, null);
    }

    public SlidingExitLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingExitLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mScroller = new Scroller(context);
        mLeftShadow = getResources().getDrawable(R.drawable.shape_left_shadow);
        mBottomShadow = getResources().getDrawable(R.drawable.shape_bottom_shadow);
        //密度适配
        int density = (int) getResources().getDisplayMetrics().density;
        mShadowWidth = SHADOW_WIDTH * density;
        mShadowHeigh = ScreenSizeUtil.dp2px(10);
    }

    /**
     * 绑定Activity
     */
    public void bindActivity(Activity activity) {
        mActivity = activity;
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        decorView.removeView(child);
        addView(child);
        decorView.addView(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                mInterceptDownX = x;
                mInterceptDownY = y;
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                // 手指处于屏幕边缘，且横向滑动距离大于纵向滑动距离时，拦截事件
                if (mInterceptDownX < (getWidth() / 10) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    mSlideDirection = SLIDE_LEFT;
                    intercept = true;
                }else if(mInterceptDownY > (getHeight()-getHeight()/10) && Math.abs(deltaY) > Math.abs(deltaX)){
                    if(isEnableUpSlide){
                        mSlideDirection = SLIDE_UP;
                        intercept = true;
                    }else{
                        intercept = false;
                    }

                }else{
                    intercept = false;
                }
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                mInterceptDownX = mInterceptDownY = mLastInterceptX = mLastInterceptY = 0;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mTouchDownY = y;
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastTouchX;
                int deltaY = y - mLastTouchY;

                if (!isConsumed && mTouchDownX < (getWidth() / 10) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    mSlideDirection = SLIDE_LEFT;
                    isConsumed = true;
                }else if(!isConsumed && mTouchDownY > (getHeight()-getHeight()/10) && Math.abs(deltaY) > Math.abs(deltaX)){
                    if(isEnableUpSlide){
                        mSlideDirection = SLIDE_UP;
                        isConsumed = true;
                    }
                }

                if (isConsumed) {
                    int rightMovedX = mLastTouchX - (int) ev.getX();
                    int upMovedY =    mLastTouchY - (int) ev.getY();
                   if(mSlideDirection == SLIDE_LEFT){
                       // 左侧即将滑出屏幕
                       if (getScrollX() + rightMovedX >= 0) {
                           scrollTo(0, 0);
                       } else {
                           scrollBy(rightMovedX, 0);
                       }
                   }else if(mSlideDirection == SLIDE_UP){
                       if (getScrollY() + upMovedY >= 0) {
                           scrollBy(0, upMovedY);
                       }
                   }
                }
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
                isConsumed = false;
                mTouchDownX = mTouchDownY = mLastTouchX = mLastTouchY = 0;

                if(mSlideDirection == SLIDE_LEFT){
                    if (-getScrollX() < getWidth() / 3) {//当手指向右滑动大于1/3屏幕宽，就关闭Activity
                        scrollBackX();
                    } else {
                        scrollCloseX();
                    }
                }else if(mSlideDirection == SLIDE_UP){
                    if (getScrollY() >= getHeight() / 3) {
                        scrollBackY();
                    } else {
                       scrollCloseY();
                    }
                }


                break;
        }
        return true;
    }

    public void setOpenUpSlide(boolean enableUPSlide) {
        isEnableUpSlide = enableUPSlide;
    }

    /**
     * 滑动返回
     */
    private void scrollBackX() {
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    /**
     * 滑动关闭
     */
    private void scrollCloseX() {
        int startX = getScrollX();
        int dx = -getScrollX() - getWidth();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    /**
     * 滑动返回
     */
    private void scrollBackY() {
        final int delta = getHeight() - getScrollY();
        mScroller.startScroll(0, getScrollY(), 0, delta - 1, 300);
        invalidate();

    }

    /**
     * 滑动关闭
     */
    private void scrollCloseY() {
        int delta = getScrollY();
        mScroller.startScroll(0, getScrollY(), 0, -delta,300);
        invalidate();
    }


    @Override
    public void computeScroll() {
        if(mSlideDirection == SLIDE_LEFT){
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.getCurrX(), 0);
                postInvalidate();
            } else if (-getScrollX() >= getWidth()) {
                mActivity.onBackPressed();
            }
        }else if(mSlideDirection == SLIDE_UP){
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                postInvalidate();
            }else if(getScrollY()>= getHeight()-1){
                mActivity.onBackPressed();
            }
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mSlideDirection == SLIDE_LEFT){
            drawShadow(canvas);
        }else if(mSlideDirection == SLIDE_UP){
//            drawBottomShadow(canvas);
        }
    }

    /**
     * 绘制边缘的阴影
     */
    private void drawShadow(Canvas canvas) {
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.save();
        canvas.translate(-mShadowWidth, 0);
        mLeftShadow.draw(canvas);
        canvas.restore();
    }


    /**
     * 绘制边缘的阴影
     */
    private void drawBottomShadow(Canvas canvas) {
        mBottomShadow.setBounds(0, getHeight(), getWidth(), getHeight() + 5);
        canvas.save();
        canvas.translate(0, -mShadowHeigh);
        mBottomShadow.draw(canvas);
        canvas.restore();
    }
}