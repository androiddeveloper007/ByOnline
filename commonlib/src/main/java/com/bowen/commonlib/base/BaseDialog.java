package com.bowen.commonlib.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.DialogManagerUtil;


/**
 * 弹框基类
 * Created by AwenZeng on 2016/8/20.
 */
public class BaseDialog extends Dialog implements View.OnClickListener{

    public Context mContext;

    public BaseDialogListener mListener;


    public interface BaseDialogListener {
        void onDataCallBack(Object... obj);
    }


    public void setBaseDialogListener(BaseDialogListener listener){
        mListener = listener;
    }

    public BaseDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void show() {
        super.show();
        DialogManagerUtil.getInstance().add(this);
    }

    public <T extends View> T getView(int resId){
        return (T)this.findViewById(resId);
    }


    @Override
    public void dismiss() {
        if (isShowing()) {
            DialogManagerUtil.getInstance().remove(this);
        }
        super.dismiss();
    }

    public void setTranslucentStatus() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
