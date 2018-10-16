package com.bowen.tcm.remind.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.DrugVolume;
import com.bowen.tcm.common.bean.MedicineInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */

public class AddRemindAdapter extends BaseQuickAdapter<DrugVolume> {

    @BindView(R.id.mMedicineNameTv)
    TextView mMedicineNameTv;
    @BindView(R.id.mMedicineVolmeTv)
    TextView mMedicineVolmeTv;
    @BindView(R.id.mLineView)
    View mLineView;

    public AddRemindAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_add_medicine_info;
    }

    @Override
    protected void convert(BaseViewHolder helper, DrugVolume item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mMedicineNameTv.setText(item.getDrugName());
        mMedicineVolmeTv.setText(item.getDosage());
        if(position == getData().size()){
            mLineView.setVisibility(View.INVISIBLE);
        }else{
            mLineView.setVisibility(View.VISIBLE);
        }
    }
}
