package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.widget.wheel.WheelView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.dialog.adapter.SingleItemAdapter;

import java.util.ArrayList;

/**
 * 单选选择弹框
 */
public class ChooseSingleItemDialog extends BaseDialog {
    private WheelView mWheelView;
    private TextView mCancleBtn;
    private TextView mOkBtn;
    private SingleItemAdapter wheelViewAdapter;
    private String[] mContentStrArra;
    private String mChooseContentStr;

    public ChooseSingleItemDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    public ChooseSingleItemDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_single_item);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        mWheelView = getView(R.id.wheelView);
        mCancleBtn = getView(R.id.dialogChooseContentCancle);
        mOkBtn = getView(R.id.dialogChooseContentOk);
        wheelViewAdapter = new SingleItemAdapter(mContext, mContentStrArra);
        mWheelView.setAdapter(wheelViewAdapter);
        mCancleBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }


    public void setmContentStrArra(String[] mContentStrArra) {
        this.mContentStrArra = mContentStrArra;
    }

    public void setmContentStrList(ArrayList<String> list) {
        mContentStrArra = new String[list.size()];
        for(int i=0;i<list.size();i++){
            mContentStrArra[i] = list.get(i);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.dialogChooseContentCancle:
                dismiss();
                break;
            case R.id.dialogChooseContentOk:
                TextView textView = (TextView) mWheelView.getSelectedView();
                mListener.onDataCallBack(textView.getText().toString(), textView.getTag());
                dismiss();
                break;
        }
    }
}