package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.FolkPrescription;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的偏方fragment中rv的adapter
 * created by zhuzp at 2018/06/29
 */
public class MyPrescriptionRvAdapter extends BaseQuickAdapter<FolkPrescription> {

    @BindView(R.id.tv_rv_item_prescription_name)
    TextView tv_rv_item_prescription_name;
    @BindView(R.id.tvApplyCrowdStr)
    TextView tvApplyCrowdStr;
    @BindView(R.id.tvApplyDisease)
    TextView tvApplyDisease;
    @BindView(R.id.usageDosage)
    TextView usageDosage;
    @BindView(R.id.myPrescriptionExamineStatus)
    TextView myPrescriptionExamineStatus;
    @BindView(R.id.divider_folk_prescription)
    View divider_folk_prescription;

    public MyPrescriptionRvAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_my_prescription;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FolkPrescription item, final int position) {
        ButterKnife.bind(this,helper.convertView);
        tv_rv_item_prescription_name.setText(item.getPrescriptionName());
        tvApplyCrowdStr.setText(item.getApplyCrowdStr());
        tvApplyDisease.setText(item.getApplyDisease());
        usageDosage.setText(item.getUsageDosage());
        String examineStatus = item.getAuditStatus();//0:未审核 1: 审核通过 2:审核拒绝
        switch (examineStatus){
            case "0":
                myPrescriptionExamineStatus.setText("未审核");
                myPrescriptionExamineStatus.setTextColor(Color.parseColor("#F11818"));
                break;
            case "1":
                myPrescriptionExamineStatus.setText("审核通过");
                myPrescriptionExamineStatus.setTextColor(Color.parseColor("#30CB52"));
                break;
            case "2":
                myPrescriptionExamineStatus.setText("审核拒绝");
                myPrescriptionExamineStatus.setTextColor(Color.parseColor("#F11818"));
                break;
        }
    }
}