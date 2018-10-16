package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.folkprescription.presenter.FolkPrescriptionPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 偏方列表adapter
 * created by zhuzp at 2018/05/22
 */
public class FolkPrescriptionRecyclerAdapter extends BaseQuickAdapter<FolkPrescription> {


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
    @BindView(R.id.folkPrescriptionListCollect)
    ImageView folkPrescriptionListCollect;
    private collectClickListener collectClick;
    private FolkPrescriptionPresenter mPresenter;

    public FolkPrescriptionRecyclerAdapter(Context cxt, FolkPrescriptionPresenter mPresenter) {
        super(cxt);
        this.mPresenter = mPresenter;
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_folk_prescription;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FolkPrescription item, final int position) {
        ButterKnife.bind(this,helper.convertView);

        tv_rv_item_prescription_name.setText(item.getPrescriptionName());
        tvApplyCrowdStr.setText(item.getApplyCrowdStr());
        tvApplyDisease.setText(item.getApplyDisease());
        usageDosage.setText(item.getUsageDosage());
        if(position==0){
            divider_folk_prescription.setVisibility(View.GONE);
        }
        boolean isCollect = TextUtils.equals("1", item.getIsCollect());
        helper.getView(R.id.folkPrescriptionListCollect).setSelected(isCollect);
        helper.getView(R.id.folkPrescriptionListCollect).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!mPresenter.hasNotLogin()){
                    boolean isCollect = TextUtils.equals("1", item.getIsCollect());
                    helper.getView(R.id.folkPrescriptionListCollect).setSelected(!isCollect);
                    item.setIsCollect(!isCollect ?"1":"0");
                    collectClick.collectClick(!isCollect?"1":"0", item.getPrescriptionId());
                }
            }
        });
    }

    public interface collectClickListener {
        void collectClick(String isCollect, String prescriptionId);
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
}
