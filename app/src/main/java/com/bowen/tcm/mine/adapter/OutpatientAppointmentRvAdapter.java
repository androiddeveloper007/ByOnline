package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.Appointment;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.widget.GlideCircleTransform;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 门诊预约中rv的adapter
 * created by zhuzp at 2018/05/22
 */
public class OutpatientAppointmentRvAdapter extends BaseQuickAdapter<Appointment> {

    @BindView(R.id.appointmentTime)
    TextView appointmentTime;
    @BindView(R.id.appointmentStatus)
    TextView appointmentStatus;//1: 待就诊，2：已就诊，3：已过期，4：已结束
    @BindView(R.id.orderFee)
    TextView orderFee;
    @BindView(R.id.doctorImg)
    CircleImageView doctorImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.doctorLevel)
    TextView doctorLevel;
    @BindView(R.id.orderDepartment)
    TextView orderDepartment;
    @BindView(R.id.hospitalName)
    TextView hospitalName;
    @BindView(R.id.visitTime)
    TextView visitTime;
    @BindView(R.id.orderType)
    TextView orderType;

    public OutpatientAppointmentRvAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_outpatient_appointment;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Appointment item, final int position) {
        ButterKnife.bind(this, helper.convertView);
        appointmentTime.setText(DateUtil.date2String(item.getPayTime(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND));
        String appointmentTypeStr = "";
        switch (item.getAppointmentStatusStr()) {//1：待支付 ，2：待就诊 ，3：已取消 ，4：已就诊 ，5：已过期
            case "1":
                appointmentTypeStr = "待支付";
                appointmentStatus.setTextColor(Color.parseColor("#f22727"));
                break;
            case "2":
                appointmentTypeStr = "待就诊";
                appointmentStatus.setTextColor(Color.parseColor("#f22727"));
                break;
            case "3":
                appointmentTypeStr = "已取消";
                appointmentStatus.setTextColor(Color.parseColor("#a4a4a4"));
                break;
            case "4":
                appointmentTypeStr = "已就诊";
                appointmentStatus.setTextColor(Color.parseColor("#00c22e"));
                break;
            case "5":
                appointmentTypeStr = "已过期";
                appointmentStatus.setTextColor(Color.parseColor("#a4a4a4"));
                break;
        }
        appointmentStatus.setText(appointmentTypeStr);
        orderType.setText("门诊预约");
        setTextViewDrawableLeft(orderType, R.drawable.appointment);
        orderFee.setText(Constants.RMB + item.getRealAmount());
        doctorName.setText(item.getName());
        doctorLevel.setText(item.getPositionStr());
        orderDepartment.setText(item.getHospitalDepartments());
        hospitalName.setText(item.getHospital());
        visitTime.setText(item.getVisitTime());
        ImageLoaderUtil.getInstance().show(visitTime.getContext(), item.getFileLink(), doctorImg, R.drawable.add_img, true);
    }

    private void setTextViewDrawableLeft(TextView tv, int resId) {
        Drawable drawable = tv.getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 这一步必须要做,否则不会显示.
        tv.setCompoundDrawables(drawable, null, null, null);
    }
}
