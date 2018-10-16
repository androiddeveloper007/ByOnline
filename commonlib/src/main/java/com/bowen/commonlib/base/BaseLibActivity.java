package com.bowen.commonlib.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.StackUtils;
import com.bowen.commonlib.widget.ActionTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Activity基类
 * Created by AwenZeng on 2016/12/02
 */
public class BaseLibActivity extends FragmentActivity implements View.OnClickListener {
    public final String TAG = this.getClass().getName();
    protected ActionTitleBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StackUtils.getInstanse().addActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getTitleBar();
        if (mBar != null) {
            setTitle(getTitle());
            mBar.getLeftButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftButtonPressed();
                }
            });
            mBar.getBackTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftButtonPressed();
                }
            });
            mBar.getRightButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightButtonPressed();
                }
            });
            mBar.getRightTextButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightButtonPressed();
                }
            });
        }

    }
    /**
     * 右侧button点击
     */
    public void onRightButtonPressed() {

    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 左侧button点击
     */
    public void onLeftButtonPressed() {
        onBackPressed();
    }

    //ActionTitleBar
    public ActionTitleBar getTitleBar() {
        if (mBar == null) {
            View root = getWindow().getDecorView();
            mBar = (ActionTitleBar) root.findViewWithTag(ActionTitleBar.TAG);
        }
        return mBar;
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mBar.getTextView().setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mBar.getTextView().setText(title);
    }


    /**
     * 修改系统状态栏
     *
     * @param color_id_statusbar 状态栏的颜色id
     */
    public void setSystemStatusBar(int color_id_statusbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color_id_statusbar);
        } else if (mBar != null) {
            mBar.getTitleView().setVisibility(View.GONE);
        }

    }

    /**
     * 获取根视图
     *
     * @return
     */
    private View getRootView() {
        return ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    }


    /**
     * 全屏状态，软键盘调整界面adjustResize失效，必须使用fitsSystemWindows
     * 因此只能设置全屏，状态栏透明，并手动设置layout_marginTop高度为负的状态栏高度statusBarHeight
     */
    public void setMarginStatusBar() {
        FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) getRootView().getLayoutParams();
        par.setMargins(0, -ScreenSizeUtil.getStatusBarHeight(this), 0, 0);
    }

    /**
     * 修改状态栏颜色兼容
     *
     * @param on
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected boolean isNeedSystemResConfig() {
        return true;
    }


    public <T extends View> T getView(int resId) {
        return (T) findViewById(resId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        StackUtils.getInstanse().removeActivity(this);
        super.onDestroy();
    }
}
