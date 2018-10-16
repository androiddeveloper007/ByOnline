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
 * 确定弹框
 * Created by AwenZeng on 2016/12/17.
 */

public class AffirmDialog extends BaseDialog {

    private TextView mTitleTv;
    private TextView mContentTv;
    private TextView mCancleTv;
    private TextView mOkTv;
    private String mTitleStr  = "温馨提示";
    private String mContentStr = "";
    private String mCancleStr = "取消";
    private String mOkStr  = "确定";

    private AffirmDialogListenner affirmDialogListenner;

    public interface AffirmDialogListenner{
        void onCancle();
        void onOK();
    }


    public AffirmDialog(Context context) {
        super(context,R.style.dialog_transparent_style);
    }

    public AffirmDialog(Context context,String mContentStr) {
        super(context,R.style.dialog_transparent_style);
        this.mContentStr = mContentStr;
    }
    public AffirmDialog(Context context,String mContentStr,String mCancleStr,String mOkStr) {
        super(context,R.style.dialog_transparent_style);
        this.mContentStr = mContentStr;
        this.mCancleStr = mCancleStr;
        this.mOkStr = mOkStr;
    }

    public AffirmDialog(Context context,String mTitleStr,String mContentStr,String mCancleStr,String mOkStr) {
        super(context,R.style.dialog_transparent_style);
        this.mTitleStr = mTitleStr;
        this.mContentStr = mContentStr;
        this.mCancleStr = mCancleStr;
        this.mOkStr = mOkStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_affirm);
        mTitleTv = (TextView)findViewById(R.id.mTitleTv);
        mContentTv = (TextView)findViewById(R.id.mContentTv);
        mCancleTv = (TextView)findViewById(R.id.mCancleTv);
        mOkTv = (TextView)findViewById(R.id.mOkTv);

        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.CENTER;
        ButterKnife.bind(this);
        if(!TextUtils.isEmpty(mTitleStr)){
            mTitleTv.setText(mTitleStr);
        }
        if(!TextUtils.isEmpty(mContentStr)){
            mContentTv.setText(mContentStr);
        }
        if(!TextUtils.isEmpty(mOkStr)){
            mOkTv.setText(mOkStr);
        }
        if(!TextUtils.isEmpty(mCancleStr)){
            mCancleTv.setText(mCancleStr);
        }
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(affirmDialogListenner!=null){
                    affirmDialogListenner.onOK();
                }
                dismiss();
            }
        });
        mCancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(affirmDialogListenner!=null){
                    affirmDialogListenner.onCancle();
                }
                dismiss();
            }
        });
    }


    public AffirmDialogListenner getAffirmDialogListenner() {
        return affirmDialogListenner;
    }

    public void setAffirmDialogListenner(AffirmDialogListenner affirmDialogListenner) {
        this.affirmDialogListenner = affirmDialogListenner;
    }

    public String getmTitleStr() {
        return mTitleStr;
    }

    public void setmTitleStr(String mTitleStr) {
        this.mTitleStr = mTitleStr;
    }

    public String getmContentStr() {
        return mContentStr;
    }

    public void setmContentStr(String mContentStr) {
        this.mContentStr = mContentStr;
    }

    public String getmCancleStr() {
        return mCancleStr;
    }

    public void setmCancleStr(String mCancleStr) {
        this.mCancleStr = mCancleStr;
    }

    public String getmOkStr() {
        return mOkStr;
    }

    public void setmOkStr(String mOkStr) {
        this.mOkStr = mOkStr;
    }


}
