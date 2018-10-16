package com.bowen.tcm.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;


/**
 * Created by ZY on 2016/8/10.
 * 悬浮球
 */
public class ContactView extends View {

    public int width = ScreenSizeUtil.dp2px(63.3f);

    public int height = ScreenSizeUtil.dp2px(63.3f);

    private Bitmap bitmap;

    public ContactView(Context context) {
        super(context);
        init();
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.contact_icon);
        //将图片裁剪到指定大小
        bitmap = Bitmap.createScaledBitmap(src, width, height, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

}
