package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.AppointmentPeriod;
import com.bowen.tcm.common.model.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseReservationTimeAdapter extends BaseQuickAdapter<AppointmentPeriod> {

    @BindView(R.id.timeZone)
    TextView timeZone;
    @BindView(R.id.peopleAmount)
    TextView peopleAmount;
    @BindView(R.id.mItemBg)
    RelativeLayout mItemBg;

    public ChooseReservationTimeAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_reservation_time;
    }

    @Override
    protected void convert(BaseViewHolder helper, AppointmentPeriod item, int position) {
        ButterKnife.bind(this, helper.convertView);
        timeZone.setText(item.getTypeName());
        peopleAmount.setText("(" + item.getApplyNum() + "/" + item.getMaxNum() + ")");
        if (item.getAppStatus()!= Constants.STATUS_APPIONTMENT_OUTDATE&&item.getAppStatus()!= Constants.STATUS_APPIONTMENT_FULL&&item.getMaxNum() != 0&&item.getMaxNum()!=item.getApplyNum()) {
            mItemBg.setBackgroundResource(R.drawable.button_remind_repeat);
        } else {
            mItemBg.setBackgroundResource(R.drawable.corner_reservation_gray);
        }
    }

}