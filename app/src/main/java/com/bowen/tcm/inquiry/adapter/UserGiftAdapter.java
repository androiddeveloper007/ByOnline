package com.bowen.tcm.inquiry.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/3.
 */
public class UserGiftAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mUserNameTv)
    TextView mUserNameTv;
    @BindView(R.id.mContentTv)
    TextView mContentTv;

    public UserGiftAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_user_gift;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this,helper.convertView);

    }
}
