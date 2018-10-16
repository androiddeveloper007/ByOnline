package com.bowen.tcm.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.BuildConfig;
import com.bowen.tcm.R;
import com.bowen.tcm.common.event.PayResultEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by AwenZeng on 2016/9/5.
 * Describe:
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        api = WXAPIFactory.createWXAPI(this, BuildConfig.APPID_WECHAT);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        int code = resp.errCode;
        if (code == 0){
            EventBus.getDefault().post(new PayResultEvent(true));
            ToastUtil.getInstance().toast("充值成功");
        }else{
            ToastUtil.getInstance().toast("充值失败");
            EventBus.getDefault().post(new PayResultEvent(false));
        }
        finish();
    }
}