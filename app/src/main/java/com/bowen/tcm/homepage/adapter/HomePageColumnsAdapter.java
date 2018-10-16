package com.bowen.tcm.homepage.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Column;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe: 选择关注栏目列表的适配器
 * Created by AwenZeng on 2018/5/4.
 * edit by zhuzp on 2018/5/22
 */

public class HomePageColumnsAdapter extends BaseQuickAdapter<Column> {

    @BindView(R.id.mContentTv)
    TextView mContentTv;

    public HomePageColumnsAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_homepage_columns;
    }

    @Override
    protected void convert(BaseViewHolder helper, Column item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mContentTv.setText(item.getColumnName());
    }
}
