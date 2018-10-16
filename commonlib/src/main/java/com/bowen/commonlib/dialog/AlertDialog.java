package com.bowen.commonlib.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import com.bowen.commonlib.R;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.ScreenSizeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 警告弹框
 * Created by AwenZeng on 2016/12/19.
 */

public class AlertDialog extends BaseDialog {
    private TextView mMainTitleTv;
    private TextView mTipsTitleTv;
    private TextView mContentTv;
    private TextView mOkTv;
    private String mMainTitleStr  = "";
    private String mTipsTitleStr = "";
    private CharSequence mContentStr = "";
    private String mOkStr  = "我知道了";
    private boolean isContentTextCenter = false;

    public AlertDialog(Context context) {
        super(context,R.style.dialog_transparent_style);
    }

    public AlertDialog(Context context,CharSequence mContentStr) {
        super(context,R.style.dialog_transparent_style);
        this.mContentStr = mContentStr;
    }
    public AlertDialog(Context context,CharSequence mContentStr,boolean isCenter) {
        super(context,R.style.dialog_transparent_style);
        this.mContentStr = mContentStr;
        isContentTextCenter = isCenter;
    }

    public AlertDialog(Context context,String mTitleStr,String mContentStr,String mOkStr) {
        super(context,R.style.dialog_transparent_style);
        this.mMainTitleStr = mTitleStr;
        this.mContentStr = mContentStr;
        this.mOkStr = mOkStr;
    }
    public AlertDialog(Context context,String mTitleStr,String mTipsTitleStr,String mContentStr,String mOkStr) {
        super(context,R.style.dialog_transparent_style);
        this.mMainTitleStr = mTitleStr;
        this.mContentStr = mContentStr;
        this.mTipsTitleStr = mTipsTitleStr;
        this.mOkStr = mOkStr;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        mMainTitleTv = (TextView)findViewById(R.id.mMainTitleTv);
        mContentTv = (TextView)findViewById(R.id.mContentTv);
        mTipsTitleTv = (TextView)findViewById(R.id.mTipsTitleTv);
        mOkTv = (TextView)findViewById(R.id.mOkTv);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.CENTER;
        ButterKnife.bind(this);
        if(!TextUtils.isEmpty(mMainTitleStr)){
            mMainTitleTv.setText(mMainTitleStr);
        }
        if(!TextUtils.isEmpty(mTipsTitleStr)){
            mTipsTitleTv.setText(mTipsTitleStr);
            mTipsTitleTv.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(mContentStr)){
            mContentTv.setText(mContentStr);
        }
        if(isContentTextCenter){
            mContentTv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        if(!TextUtils.isEmpty(mOkStr)){
            mOkTv.setText(mOkStr);
        }
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mListener!=null){
                    mListener.onDataCallBack("");
                }
            }
        });
    }


    public void setmContentStr(CharSequence mContentStr) {
        this.mContentStr = mContentStr;
        mContentTv.setText(mContentStr);
    }
}
