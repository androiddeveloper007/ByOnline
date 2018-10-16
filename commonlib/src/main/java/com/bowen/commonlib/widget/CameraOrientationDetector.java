package com.bowen.commonlib.widget;

import android.content.Context;
import android.view.OrientationEventListener;

/**
 * 方向变化监听器，监听传感器方向的改变
 * Created by AwenZeng on 2017/2/21.
 */

public class CameraOrientationDetector extends OrientationEventListener {
    int mOrientation;

    public CameraOrientationDetector(Context context, int rate) {
        super(context, rate);
    }

    public CameraOrientationDetector(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        this.mOrientation = orientation;
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        }

        //保证只返回四个方向
        int newOrientation = ((orientation + 45) / 90 * 90) % 360;
        if (newOrientation != mOrientation) {
            mOrientation = newOrientation;
            //返回的mOrientation就是手机方向，为0°、90°、180°和270°中的一个
        }
    }

    public int getOrientation() {
        return mOrientation;
    }
}
