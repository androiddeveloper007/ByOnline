package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.FolkPrescription;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收藏偏方fragment中rv的adapter
 * created by zhuzp at 2018/05/22
 */
public class CollectedPrescriptionRvAdapter extends BaseQuickAdapter<FolkPrescription> {

    @BindView(R.id.tv_rv_item_prescription_name)
    TextView tv_rv_item_prescription_name;
    @BindView(R.id.tvApplyCrowdStr)
    TextView tvApplyCrowdStr;
    @BindView(R.id.tvApplyDisease)
    TextView tvApplyDisease;
    @BindView(R.id.usageDosage)
    TextView usageDosage;
    @BindView(R.id.divider_folk_prescription)
    View divider_folk_prescription;
    private collectClickListener collectClick;

    public CollectedPrescriptionRvAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_collected_prescription;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FolkPrescription item, final int position) {
        ButterKnife.bind(this,helper.convertView);

        tv_rv_item_prescription_name.setText(item.getPrescriptionName());
        tvApplyCrowdStr.setText(item.getApplyCrowdStr());
        tvApplyDisease.setText(item.getApplyDisease());
        usageDosage.setText(item.getUsageDosage());
        helper.getView(R.id.folkPrescriptionListCollect).setSelected(true);
        helper.getView(R.id.folkPrescriptionListCollect).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                helper.getView(R.id.folkPrescriptionListCollect).setSelected(false);
                collectClick.collectClick("0", item.getPrescriptionId(), position);
            }
        });
    }

    public interface collectClickListener {
        void collectClick(String isCollect, String prescriptionId,int position);
    }

    public void setOnCollectClickListener(collectClickListener listener) {
        this.collectClick = listener;
    }

    //切换指定position的收藏状态
    public void setCollect(int position) {
        FolkPrescription item = ((FolkPrescription)getData().get(position));
        boolean isCollect = TextUtils.equals("1", item.getIsCollect());
        item.setIsCollect(!isCollect?"1":"0");
        notifyDataSetChanged();
    }

    public void removeDataAndNotify(int position) {
        getData().remove(position);
        notifyDataSetChanged();
    }
}
