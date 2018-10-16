package com.bowen.tcm.inquiry.adapter;

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
public class GiveGiftAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mGiftNameTv)
    TextView mGiftNameTv;

    private int selectPos = 9999;

    public GiveGiftAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_give_gift;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mGiftNameTv.setText(item);
        if (selectPos == position) {
            mGiftNameTv.setTextColor(mContext.get().getResources().getColor(R.color.color_white));
            mGiftNameTv.setSelected(true);
        } else {
            mGiftNameTv.setTextColor(mContext.get().getResources().getColor(R.color.split_line_gray));
            mGiftNameTv.setSelected(false);
        }
    }

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
        notifyDataSetChanged();
    }
}
