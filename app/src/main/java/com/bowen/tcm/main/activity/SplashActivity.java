package com.bowen.tcm.main.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.BowenApplication;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.dialog.ChooseServerDialog;
import com.bowen.tcm.common.util.ShareUtil;

/**
 * 闪屏页面
 * Created by AwenZeng on 2016/07/28
 */
public class SplashActivity extends BaseActivity {
    private PermissionsModel mPermissionsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setSystemStatusBar(R.color.color_00);
        setMarginStatusBar();
        mPermissionsModel = new PermissionsModel(this);
        mPermissionsModel.checkLocationPermission(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showChooseServerDialog();
            }
        }, 1500);
    }

    @Override
    protected boolean enableSlidingClose() {
        return false;
    }

    private void showChooseServerDialog(){
        //1.测试情况下弹出 2.应用只有进程完全退出时弹出
        if(BowenApplication.DEBUG){
            ChooseServerDialog chooseServerDialog = new ChooseServerDialog(SplashActivity.this);
            chooseServerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    firstStartApp();
                }
            });
            chooseServerDialog.show();
        }else{
            firstStartApp();
        }
    }

    private void firstStartApp() {
        if (!DataCacheUtil.getInstance().getBoolean(DataCacheUtil.FIRST_START_APP, false)) {
            DataCacheUtil.getInstance().putBoolean(DataCacheUtil.FIRST_START_APP, true);
            RouterActivityUtil.startActivity(SplashActivity.this, GuideActivity.class, true);
        } else {
            RouterActivityUtil.startActivity(SplashActivity.this, MainActivity.class, true);
        }
    }

}
