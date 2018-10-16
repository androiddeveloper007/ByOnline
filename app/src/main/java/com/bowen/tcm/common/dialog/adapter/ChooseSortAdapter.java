package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/3.
 */
public class ChooseSortAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mContentTv)
    TextView mContentTv;


    public ChooseSortAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_sort;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mContentTv.setText(item);
    }

}
