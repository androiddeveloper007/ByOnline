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
 * 单选选择弹框
 */
public class ChooseDateDialog extends BaseDialog implements TosGallery.OnEndFlingListener{
    @BindView(R.id.dialogChooseContentCancle)
    TextView dialogChooseContentCancle;
    @BindView(R.id.dialogChooseContentOk)
    TextView dialogChooseContentOk;
    @BindView(R.id.yearWheelView)
    WheelView yearWheelView;
    @BindView(R.id.monthWheelView)
    WheelView monthWheelView;
    @BindView(R.id.dayWheelView)
    WheelView dayWheelView;
//    private WheelView mWheelView;
//    private TextView mCancleBtn;
//    private TextView mOkBtn;
    private SingleItemAdapter yearAdapter;
    private SingleItemAdapter monthAdapter;
    private SingleItemAdapter dayAdapter;
//    private String[] mYears;
//    private String[] mDays;

    public ChooseDateDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    public ChooseDateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_date);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        yearAdapter = new SingleItemAdapter(mContext, DateCaculateModle.getYears(DateCaculateModle.START_YEAR,DateCaculateModle.END_YEAR));
        monthAdapter = new SingleItemAdapter(mContext,  DateCaculateModle.getMonths());
        dayAdapter = new SingleItemAdapter(mContext,  DateCaculateModle.getDays(DateUtil.getNowYear(),DateUtil.getNowMonth()));
        yearWheelView.setAdapter(yearAdapter);
        monthWheelView.setAdapter(monthAdapter);
        dayWheelView.setAdapter(dayAdapter);
        yearWheelView.setOnEndFlingListener(this);
        monthWheelView.setOnEndFlingListener(this);

        yearWheelView.setSelection(getYearPos(DateUtil.getNowYear()));
        monthWheelView.setSelection(getMonthPos(DateUtil.getNowMonth()));
        dayWheelView.setSelection(getDayPos(DateUtil.getNowDay()));
        setCanceledOnTouchOutside(true);
    }

    private int getYearPos(int year){
         String[] years = yearAdapter.getDatas();
         int i = 0;
         for (String temp:years){
             if(DateCaculateModle.getYear(temp) == year){
                 return i;
             }
             i++;
         }
         return 0;
    }

    private int getMonthPos(int month){
        String[] months = monthAdapter.getDatas();
        int i = 0;
        for (String temp:months){
            if(Integer.parseInt(DateCaculateModle.getMonth(temp)) == month){
                return i;
            }
            i++;
        }
        return 0;
    }

    private int getDayPos(int day){
        String[] days = dayAdapter.getDatas();
        int i = 0;
        for (String temp:days){
            if(Integer.parseInt(DateCaculateModle.getDay(temp)) == day){
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public void onEndFling(TosGallery v) {
        TextView monthTv = (TextView) monthWheelView.getSelectedView();
        TextView yearTv = (TextView) yearWheelView.getSelectedView();
        String yearStr = yearTv.getText().toString();
        String monthStr = monthTv.getText().toString();
        dayAdapter.setmDatas(DateCaculateModle.getDays(DateCaculateModle.getYear(yearStr),Integer.parseInt(DateCaculateModle.getMonth(monthStr))));
        dayAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.dialogChooseContentCancle, R.id.dialogChooseContentOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogChooseContentCancle:
                dismiss();
                break;
            case R.id.dialogChooseContentOk:
                TextView monthTv = (TextView) monthWheelView.getSelectedView();
                TextView yearTv = (TextView) yearWheelView.getSelectedView();
                TextView dayTv = (TextView) dayWheelView.getSelectedView();
                String yearStr = yearTv.getText().toString();
                String monthStr = monthTv.getText().toString();
                String dayStr =  dayTv.getText().toString();
                mListener.onDataCallBack(DateCaculateModle.getYear(yearStr)+"-"+DateCaculateModle.getMonth(monthStr)+"-"+DateCaculateModle.getDay(dayStr));
                dismiss();
                break;
        }
    }
}