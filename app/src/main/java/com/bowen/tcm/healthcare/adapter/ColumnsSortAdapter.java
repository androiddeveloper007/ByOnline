package com.bowen.tcm.healthcare.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Column;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/8.
 * 栏目排序的适配器
 */

public class ColumnsSortAdapter extends BaseQuickAdapter<Column> {

    @BindView(R.id.mColumsNameTv)
    TextView mColumsNameTv;
    @BindView(R.id.mColumnImg)
    ImageView mColumnImg;
    private Context context;

    public ColumnsSortAdapter(Context cxt) {
        super(cxt);
        this.context = cxt;
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_columns_sort;
    }

    @Override
    protected void convert(BaseViewHolder helper, Column item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mColumsNameTv.setText(item.getColumnName());
        ImageLoaderUtil.getInstance().show(mContext.get(),item.getFileLink(),mColumnImg,R.drawable.news_defalult_bg);
    }

}
