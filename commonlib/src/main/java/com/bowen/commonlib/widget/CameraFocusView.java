package com.bowen.commonlib.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.bowen.commonlib.utils.ScreenSizeUtil;


/**
 * Created by AwenZeng on 2016/12/5.
 */
public class CameraFocusView extends View {
    private static final String TAG = "CameraSurfaceView";
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mPaint;
    private RectF mRectF;
    // 圆
    private Point centerPoint;
    private int radio;
    private int lastPos;
    private ValueAnimator lineAnimator;
    private RelativeLayout topLayout;
    private RelativeLayout bottomLayout;
    private boolean isShow = false;
    private final int DURATION_TIME = 1500;

    private IAutoFocus mIAutoFocus;

    /**
     * 聚焦的回调接口
     */
    public interface IAutoFocus {
        void autoFocus();
    }

    public void setIAutoFocus(IAutoFocus mIAutoFocus) {
        this.mIAutoFocus = mIAutoFocus;
    }


    public CameraFocusView(Context context) {
        this(context, null);
    }

    public CameraFocusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraFocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenMetrix(context);
        initView(context);
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        int marginLeft = (int) (mScreenWidth * 0.15);
        int marginTop = (int) (mScreenHeight * 0.25);
        mRectF = new RectF(marginLeft, marginTop, mScreenWidth - marginLeft, mScreenHeight - marginTop);
        centerPoint = new Point(mScreenWidth / 2, mScreenHeight / 2);
        radio = (int) (mScreenWidth * 0.1);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow){
            if(radio == 87){
                mPaint.setColor(Color.GREEN);
            }
            canvas.drawCircle(centerPoint.x, centerPoint.y, radio, mPaint);// 外圆
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                lastPos = 0;
                mPaint.setColor(Color.WHITE);
                radio = (int) (mScreenWidth * 0.1);
                centerPoint = null;
                if(y> ScreenSizeUtil.dp2px(50)&&y<ScreenSizeUtil.getScreenHeight()-ScreenSizeUtil.dp2px(105)){
                    centerPoint = new Point(x, y);
                    showView();
                }
                return true;
        }
        if (mIAutoFocus != null) {
            mIAutoFocus.autoFocus();
        }
        return true;
    }

    public void setLimitRangeLayout(RelativeLayout topLayout,RelativeLayout bottomLayout){
        this.topLayout = topLayout;
        this.bottomLayout = bottomLayout;
    }


    private void showView() {
        isShow = true;
        if (lineAnimator == null) {
            lineAnimator = ValueAnimator.ofInt(0, 20);
            lineAnimator.setDuration(DURATION_TIME);
            lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animationValue = (Integer) animation
                            .getAnimatedValue();
                    if(lastPos!=animationValue&&radio>=(int) ((mScreenWidth * 0.1)-20)){
                        radio = radio - animationValue;
                        lastPos = animationValue;
                    }
                    isShow = true;
                    invalidate();
                }
            });
            lineAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isShow = false;
                    lastPos = 0;
                    mPaint.setColor(Color.WHITE);
                    radio = (int) (mScreenWidth * 0.1);
                    invalidate();
                }
            });
        }else{
            lineAnimator.end();
            lineAnimator.cancel();
            lineAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            lineAnimator.start();
        }
    }
}
