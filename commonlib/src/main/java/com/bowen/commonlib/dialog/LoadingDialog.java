package com.bowen.commonlib.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.bowen.commonlib.R;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.NetworkRequestUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;

import butterknife.ButterKnife;


/**
 * Created by AwenZeng on 2016/12/17.
 */

public class LoadingDialog extends BaseDialog {

    private TextView mContentTv;
    private boolean isShowIcon;
    private String requestApi;
    private String contentStr;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog_transparent_bg_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mContentTv = (TextView)findViewById(R.id.mContentTv);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.CENTER;
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        mContentTv.setText(contentStr);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;

    }


    public void setRequestUrl(String requestUrl) {
        this.requestApi = requestUrl;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&!TextUtils.isEmpty(requestApi)){
            NetworkRequestUtil.getInstance().remove(requestApi);
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isShowIcon() {
        return isShowIcon;
    }

    public void setShowIcon(boolean showIcon) {
        isShowIcon = showIcon;
    }
}
