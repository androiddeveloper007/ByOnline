package com.bowen.tcm.common.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.dialog.AlertDialog;
import com.bowen.commonlib.http.BaseNetworkRequest;
import com.bowen.commonlib.http.HttpRequestOverdueCallBack;
import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.DialogManagerUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.login.activity.LoginActivity;

/**
 * Created by AwenZeng on 2017/3/30.
 */

public class NetworkRequest extends BaseNetworkRequest {
    public NetworkRequest(Context context) {
        super(context);
        reqParam.put("versionCode", ApplicationUtils.getVersionName() + "." + ApplicationUtils.getVersionCode());
        this.setmOverdueCallBack(new HttpRequestOverdueCallBack() {
            @Override
            public void callback() {
                DialogManagerUtil.getInstance().closeAll();
                DataCacheUtil.getInstance().putString(DataCacheUtil.LOGIN_TOKEN,"");
                final AlertDialog alertDialog = new AlertDialog(mContext.get(), "您的登录已失效，请重新登录", true);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                            RouterActivityUtil.startActivity((Activity)mContext.get(), LoginActivity.class);
                    }
                });
                alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });
                alertDialog.show();
            }
        });
    }


}
