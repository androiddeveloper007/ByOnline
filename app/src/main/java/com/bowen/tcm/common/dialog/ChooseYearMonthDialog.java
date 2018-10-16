package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.widget.wheel.WheelView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.dialog.adapter.SingleItemAdapter;
import com.bowen.tcm.common.model.DateCaculateModle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择年月弹框
 */
public class ChooseYearMonthDialog extends BaseDialog{
    @BindView(R.id.yearWheelView)
    WheelView yearWheelView;
    @BindView(R.id.monthWheelView)
    WheelView monthWheelView;
    private SingleItemAdapter yearAdapter;
    private SingleItemAdapter monthAdapter;
    private int year, month;

    public ChooseYearMonthDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    public ChooseYearMonthDialog(Context context, int year, int month) {
        super(context, R.style.dialog_transparent_style);
        this.year = year;
        this.month = month;
    }

    public ChooseYearMonthDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_year_month);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        yearAdapter = new SingleItemAdapter(mContext, DateCaculateModle.getYears(DateCaculateModle.START_YEAR, DateUtil.getNowYear()));
        monthAdapter = new SingleItemAdapter(mContext,  DateCaculateModle.getMonths());
        yearWheelView.setAdapter(yearAdapter);
        monthWheelView.setAdapter(monthAdapter);
        if(year!=0){
            yearWheelView.setSelection(getYearPos(year));
        }else{
            yearWheelView.setSelection(getYearPos(DateUtil.getNowYear()));
        }
        if(month!=0){
            monthWheelView.setSelection(getMonthPos(month));
        }else{
            monthWheelView.setSelection(getMonthPos(DateUtil.getNowMonth()));
        }
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

    @OnClick({R.id.dialogChooseContentCancle, R.id.dialogChooseContentOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogChooseContentCancle:
                dismiss();
                break;
            case R.id.dialogChooseContentOk:
                TextView monthTv = (TextView) monthWheelView.getSelectedView();
                TextView yearTv = (TextView) yearWheelView.getSelectedView();
                String yearStr = yearTv.getText().toString();
                String monthStr = "";
                if(yearWheelView.getSelectedItemPosition()+1 == yearWheelView.getCount() && monthWheelView.getSelectedItemPosition()+1> DateUtil.getNowMonth()){
                    if(DateUtil.getNowMonth()<10){
                        monthStr = "0"+DateUtil.getNowMonth();
                    }else{
                        monthStr = DateUtil.getNowMonth()+"";
                    }
                    mListener.onDataCallBack(DateCaculateModle.getYear(yearStr)+"-"+monthStr);
                }else{
                    monthStr = monthTv.getText().toString();
                    mListener.onDataCallBack(DateCaculateModle.getYear(yearStr)+"-"+DateCaculateModle.getMonth(monthStr));
                }
                dismiss();
                break;
        }
    }
}