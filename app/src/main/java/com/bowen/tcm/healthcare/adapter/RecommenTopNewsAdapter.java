package com.bowen.tcm.healthcare.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.NewsTop;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zzp on 2017/2/6.
 * 推荐栏，横向recyclerView的适配器
 */

public class RecommenTopNewsAdapter extends BaseQuickAdapter<NewsTop> {

    @BindView(R.id.mCardViewLayout)
    CardView mCardViewLayout;
    @BindView(R.id.mNewsImg)
    CircleImageView mNewsImg;
    @BindView(R.id.mNewsNameTv)
    TextView mNewsNameTv;
    @BindView(R.id.mNewsTimeTv)
    TextView mNewsTimeTv;
    @BindView(R.id.mNewsCountTv)
    TextView mNewsCountTv;


    public RecommenTopNewsAdapter(Context cxt) {
        super(cxt);
    }
    @Override
    protected int setItemLayout() {
        return R.layout.adapter_recommend_top_news;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsTop item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mNewsNameTv.setText(item.getNewsTitle());
        mNewsTimeTv.setText(item.getTimeStr());
        mNewsCountTv.setText(SpannableStringUtils.getBuilder(item.getReadNumStr()+"人")
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black))
                .append("阅读")
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_tips))
                .create());
        ImageLoaderUtil.getInstance().show(mContext.get(),item.getFileLink(),mNewsImg,R.drawable.top_news_default_bg);
    }

}
