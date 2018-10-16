package com.bowen.tcm.healthcare.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.dialog.SmallTipsDialog;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.main.activity.BrowserActivity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/23.
 */
public class NewsAdapter extends BaseMultiItemQuickAdapter<News, BaseViewHolder> {
    @BindView(R.id.mNewsImg)
    CircleImageView mNewsImg;
    @BindView(R.id.mNewsNameTv)
    TextView mNewsNameTv;
    @BindView(R.id.mNewsTimeTv)
    TextView mNewsTimeTv;
    @BindView(R.id.mNewsCountTv)
    TextView mNewsCountTv;
    @BindView(R.id.mNewsItemLayout)
    RelativeLayout mNewsItemLayout;


    private RecyclerView mRecyclerView;
    private PagerSnapHelper mPagerSnapHelper;

    private ImageView mTipsBgImg;
    private TextView mTipsContentTv;
    private TextView mTipsAuthorTv;


    public NewsAdapter() {
        super(null);
        addItemType(Constants.TYPE_NEWS_ITEM_TOP, R.layout.item_health_care_top_card);
        addItemType(Constants.TYPE_NEWS_ITEM_TIPS, R.layout.item_health_care_tips);
        addItemType(Constants.TYPE_NEWS_ITEM_BIG, R.layout.item_health_care_big);
        addItemType(Constants.TYPE_NEWS_ITEM_SMALL, R.layout.item_health_care_normal);
    }

    public <T extends View> T getView(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    @Override
    protected void convert(BaseViewHolder helper,News item) {

        switch (helper.getItemViewType()) {
            case Constants.TYPE_NEWS_ITEM_TOP:
                mRecyclerView = getView(helper.convertView,R.id.mRecyclerView);
                showTopNews(item);
                break;
            case Constants.TYPE_NEWS_ITEM_TIPS:
                mTipsBgImg = getView(helper.convertView,R.id.mTipsBgImg);
                mTipsContentTv = getView(helper.convertView,R.id.mTipsContentTv);
                mTipsAuthorTv = getView(helper.convertView,R.id.mTipsAuthorTv);
                showSmallTips(item);
                break;
            case Constants.TYPE_NEWS_ITEM_BIG:
                ButterKnife.bind(this, helper.convertView);
                showNews(item);
                break;
            case Constants.TYPE_NEWS_ITEM_SMALL:
                ButterKnife.bind(this, helper.convertView);
                showNews(item);
                break;
        }
    }

    /**
     * 展示推荐topNews
     * @param item
     */
    private void showTopNews(final News item){
        RecommenTopNewsAdapter adapter = new RecommenTopNewsAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        if(EmptyUtils.isEmpty(mPagerSnapHelper)){
            mPagerSnapHelper = new PagerSnapHelper();
            mPagerSnapHelper.attachToRecyclerView(mRecyclerView);
        }
        adapter.setNewData(item.getNewsTops());
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(RouterActivityUtil.FROM_TAG, BrowserActivity.FROM_NEWS_DETAIL_TOP);
                bundle.putString(BrowserActivity.URL,item.getNewsTops().get(position).getShareUrl());
                bundle.putSerializable(BrowserActivity.DATA,item.getNewsTops().get(position));
                RouterActivityUtil.startActivity((Activity) mContext, BrowserActivity.class, bundle);
            }
        });
    }


    private void showSmallTips(final News item){
        mTipsContentTv.setText(item.getTips().getTips());
        mTipsAuthorTv.setText(item.getTips().getTipsWriter()+"(作)");
        ImageLoaderUtil.getInstance().show(mContext, item.getTips().getFileLink(), mTipsBgImg, R.drawable.small_tips_default_bg);
        mTipsBgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new SmallTipsDialog(mContext,item.getTips()).show();
            }
        });
    }



    private void showNews(News news) {
        mNewsNameTv.setText(news.getNewsTitle());
        mNewsTimeTv.setText(news.getTimeStr());
        mNewsCountTv.setText(SpannableStringUtils.getBuilder(news.getReadNumStr()+"人")
                .setForegroundColor(mContext.getResources().getColor(R.color.color_main_black))
                .append("阅读")
                .setForegroundColor(mContext.getResources().getColor(R.color.color_main_tips))
                .create());
        mNewsItemLayout.setTag(news);
        mNewsItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News item = (News)v.getTag();
                Bundle bundle = new Bundle();
                bundle.putInt(RouterActivityUtil.FROM_TAG, BrowserActivity.FROM_NEWS_DETAIL);
                bundle.putString(BrowserActivity.URL,item.getShareUrl());
                bundle.putSerializable(BrowserActivity.DATA,item);
                RouterActivityUtil.startActivity((Activity) mContext, BrowserActivity.class, bundle);
            }
        });
        ImageLoaderUtil.getInstance().show(mContext, news.getFileLink(), mNewsImg, R.drawable.news_defalult_bg);
    }


    /**
     * 抽取成一个方法不然每一个都要重新写setOnClickListener(new View.OnClickListener()
     *
     * @param helper
     * @param id
     * @param urlId
     */
    public void onItemClick(BaseViewHolder helper, int id, final int urlId) {
        helper.getView(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(urlId, v);
            }
        });
    }

    public void OnItemThemeClick(BaseViewHolder helper, int id, final int urlId) {
        helper.getView(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemThemeClickListener(urlId, v);
            }
        });
    }

    public void OnItemSectionClick(BaseViewHolder helper, int id, final int urlId) {
        helper.getView(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemSectionClickListener(urlId, v);
            }
        });
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int id, View view);

        void OnItemThemeClickListener(int id, View view);

        void OnItemSectionClickListener(int id, View view);
    }
}
