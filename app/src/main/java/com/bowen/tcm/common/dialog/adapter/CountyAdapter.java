package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.location.County;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AwenZeng on 2016/12/17.
 */

public class CountyAdapter extends BaseQuickAdapter<County> {

    @BindView(R.id.cityTv)
    TextView cityTv;

    public CountyAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_city;
    }

    @Override
    protected void convert(BaseViewHolder helper, County item, int position) {
        ButterKnife.bind(this, helper.convertView);
        cityTv.setText(item.getCounty());
    }
}
