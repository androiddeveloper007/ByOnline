package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.MyOrder;
import com.bowen.tcm.common.model.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的订单中rv的adapter
 * created by zhuzp at 2018/05/22
 */
public class MyOrderRvAdapter extends BaseQuickAdapter<MyOrder> {
    @BindView(R.id.orderNum)
    TextView orderNum;
    @BindView(R.id.orderStatus)
    TextView orderStatus;
    @BindView(R.id.orderType)
    TextView orderType;
    @BindView(R.id.orderFee)
    TextView orderFee;
    @BindView(R.id.doctorImg)
    CircleImageView doctorImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.orderDepartment)
    TextView orderDepartment;
    @BindView(R.id.doctorLevel)
    TextView doctorLevel;
    @BindView(R.id.hospitalName)
    TextView hospitalName;
    @BindView(R.id.orderHasPayTitle)
    TextView orderHasPayTitle;
    @BindView(R.id.orderHasPay)
    TextView orderHasPay;
    @BindView(R.id.orderBtn0)
    TextView orderBtn0;
    @BindView(R.id.orderBtn1)
    TextView orderBtn1;
    private final Context mContext;
    private button0Click click0;
    private button1Click click1;

    public MyOrderRvAdapter(Context cxt) {
        super(cxt);
        mContext = cxt;
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_my_order;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyOrder item, final int position) {
        ButterKnife.bind(this, helper.convertView);
        orderNum.setText(item.getOrderId());
        switch (item.getOrderStatus()) {
            case Constants.PAY_ORDER_STATUS_WAIT:
                orderStatus.setText("待支付");
                orderStatus.setTextColor(Color.parseColor("#f22727"));
                orderHasPayTitle.setText("待支付：");
                orderHasPay.setText(item.getRealAmount());
                orderHasPayTitle.setVisibility(View.VISIBLE);
                orderHasPay.setVisibility(View.VISIBLE);
                orderBtn0.setVisibility(View.VISIBLE);
                orderBtn1.setVisibility(View.VISIBLE);
                orderBtn0.setText("去支付");
                orderBtn1.setText("取消订单");
                orderBtn1.setTextColor(Color.parseColor("#b8b8b8"));
                orderBtn0.setBackgroundResource(R.drawable.button_go_pay);
                orderBtn1.setBackgroundResource(R.drawable.button_cancel_order);
                break;
            case Constants.PAY_ORDER_STATUS_FINISH:
                orderStatus.setText("已支付");
                orderStatus.setTextColor(Color.parseColor("#00c22e"));
                orderHasPayTitle.setText("已支付：");
                orderHasPay.setText(item.getRealAmount());
                orderHasPayTitle.setVisibility(View.VISIBLE);
                orderHasPay.setVisibility(View.VISIBLE);
                orderBtn0.setVisibility(View.VISIBLE);
                orderBtn1.setVisibility(View.GONE);
                orderBtn0.setText("立即咨询");
                orderBtn1.setTextColor(Color.parseColor("#ffffff"));
                orderBtn0.setBackgroundResource(R.drawable.button_guide_enter);
                break;
            case Constants.PAY_ORDER_STATUS_CANCEL:
                orderStatus.setText("已取消");
                orderStatus.setTextColor(Color.parseColor("#a4a4a4"));
                orderHasPayTitle.setVisibility(View.INVISIBLE);
                orderHasPay.setVisibility(View.INVISIBLE);
                orderBtn0.setVisibility(View.VISIBLE);
                orderBtn1.setVisibility(View.GONE);
                orderBtn0.setText("删除订单");
                orderBtn1.setTextColor(Color.parseColor("#ffffff"));
                orderBtn0.setBackgroundResource(R.drawable.button_guide_enter);
                break;
            case Constants.PAY_ORDER_STATUS_OVER://未评价
                orderStatus.setText("已结束");
                orderStatus.setTextColor(Color.parseColor("#a4a4a4"));
                orderHasPayTitle.setVisibility(View.INVISIBLE);
                orderHasPay.setVisibility(View.INVISIBLE);
                orderBtn0.setVisibility(View.VISIBLE);
                orderBtn0.setText("再次咨询");
                orderBtn0.setBackgroundResource(R.drawable.button_go_pay);
                orderBtn0.setTextColor(Color.parseColor("#ffffff"));
                orderBtn1.setVisibility(View.VISIBLE);
//                if(EmptyUtils.isEmpty(item.getHostipal())){
//                }
                orderBtn1.setClickable(true);
                orderBtn1.setText("评价");
                orderBtn1.setBackgroundResource(R.drawable.button_guide_enter);
                orderBtn1.setTextColor(Color.parseColor("#ffffff"));
                break;
            case Constants.PAY_ORDER_STATUS_COMMENT://已评价
                orderStatus.setText("已评价");
                orderStatus.setTextColor(Color.parseColor("#a4a4a4"));
                orderHasPayTitle.setVisibility(View.INVISIBLE);
                orderHasPay.setVisibility(View.INVISIBLE);
                orderBtn0.setClickable(true);
                orderBtn0.setVisibility(View.VISIBLE);
                orderBtn0.setText("再次咨询");
                orderBtn0.setBackgroundResource(R.drawable.button_go_pay);
                orderBtn0.setTextColor(Color.parseColor("#ffffff"));
                if(EmptyUtils.isEmpty(item.getHostipal())){
                    orderBtn1.setVisibility(View.VISIBLE);
                }
                orderBtn1.setClickable(false);
                orderBtn1.setText("已评价");
                orderBtn1.setBackgroundResource(R.drawable.button_cancel_order);
                orderBtn1.setTextColor(Color.parseColor("#b8b8b8"));
                break;
        }
        switch (item.getOrderType()) {
            case Constants.TYPE_PRODUCT_CONSULT:
                orderType.setText("图文问诊");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mContext, orderType, R.drawable.picture_word_commend);
                break;
            case Constants.TYPE_PRODUCT_SEND_GIFT:
                orderType.setText("送心意");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mContext, orderType, R.drawable.send_present);
                break;
            case Constants.TYPE_PRODUCT_APPIONTMENT:
                orderType.setText("门诊预约");
                ImageLoaderUtil.getInstance().setTextViewDrawableLeft(mContext, orderType, R.drawable.appointment);
                break;
        }
        orderFee.setText(Constants.RMB + item.getTotalAmount());
        doctorName.setText(item.getName());
        orderDepartment.setText(item.getHospitalDepartments());
        doctorLevel.setText(item.getPositionStr());
        hospitalName.setText(item.getHostipal());
        ImageLoaderUtil.getInstance().show(mContext,item.getFileLink(),doctorImg,R.drawable.img_bg,true);
        helper.convertView.findViewById(R.id.orderBtn0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click0 != null) {
                    click0.onButton0Click(position, item.getOrderStatus());
                }
            }
        });
        helper.convertView.findViewById(R.id.orderBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click1 != null) {
                    click1.onButton1Click(position, item.getOrderStatus());
                }
            }
        });
    }

    public interface button0Click {
        void onButton0Click(int pos, int orderStatus);
    }

    public interface button1Click {
        void onButton1Click(int pos, int orderStatus);
    }

    public void setOnButton0Click(button0Click listener) {
        this.click0 = listener;
    }

    public void setOnButton1Click(button1Click listener) {
        this.click1 = listener;
    }
}
