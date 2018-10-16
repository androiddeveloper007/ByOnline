package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.widget.wheel.TosGallery;
import com.bowen.commonlib.widget.wheel.WheelView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.dialog.adapter.SingleItemAdapter;
import com.bowen.tcm.common.model.DateCaculateModle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择时间框
 */
public class ChooseTimeDialog extends BaseDialog  {
    @BindView(R.id.mCancleTv)
    TextView mCancleTv;
    @BindView(R.id.mOkTv)
    TextView mTimeOkTv;
    @BindView(R.id.hourWheelView)
    WheelView hourWheelView;
    @BindView(R.id.minuteWheelView)
    WheelView minuteWheelView;
    private SingleItemAdapter hourAdapter;
    private SingleItemAdapter minuteAdapter;

    public ChooseTimeDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    public ChooseTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_time);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        hourAdapter = new SingleItemAdapter(mContext, DateCaculateModle.getHoursAndMinutes(24));
        minuteAdapter = new SingleItemAdapter(mContext, DateCaculateModle.getHoursAndMinutes(60));
        hourWheelView.setAdapter(hourAdapter);
        minuteWheelView.setAdapter(minuteAdapter);
        hourWheelView.setSelection(DateUtil.getNowHour());
        minuteWheelView.setSelection(DateUtil.getNowMinute());
        setCanceledOnTouchOutside(true);
    }


    @OnClick({R.id.mCancleTv, R.id.mOkTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCancleTv:
                dismiss();
                break;
            case R.id.mOkTv:
                TextView hourTv = (TextView) hourWheelView.getSelectedView();
                TextView minutetTv = (TextView) minuteWheelView.getSelectedView();
                String hourStr = hourTv.getText().toString();
                String minuteStr = minutetTv.getText().toString();
                mListener.onDataCallBack(hourStr + ":" + minuteStr);
                dismiss();
                break;
        }
    }
}