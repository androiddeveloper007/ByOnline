package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.ConsultListItem;
import com.bowen.tcm.common.widget.SwipeMenuLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的咨询中rv的adapter
 * created by zhuzhipeng at 2018/05/22
 */
public class MyConsultRvAdapter extends BaseQuickAdapter<ConsultListItem> {

    @BindView(R.id.doctorImg)
    CircleImageView doctorImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.doctorLevel)
    TextView doctorLevel;
    @BindView(R.id.consultTime)
    TextView consultTime;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.divider_my_consult)
    View divider_my_consult;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    @BindView(R.id.mSwipeMenuLayout)
    SwipeMenuLayout mSwipeMenuLayout;
    @BindView(R.id.mDeleteTv)
    TextView mDeleteTv;

    private OnRecyclerViewItemClickListener mItemOnClickListener;


    public void setItemOnClickListener(OnRecyclerViewItemClickListener mItemOnClickListener) {
        this.mItemOnClickListener = mItemOnClickListener;
    }

    public interface DeleteListener {
        void onDeleteListener(ConsultListItem item);
    }

    private DeleteListener mDeleteListener;

    public MyConsultRvAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_my_consult;
    }

    @Override
    protected void convert(BaseViewHolder helper, ConsultListItem item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mSwipeMenuLayout.setTag(position);
        mDeleteTv.setTag(item);
        ImageLoaderUtil.getInstance().show(mContext.get(), item.getHeadImgUrl(), doctorImg, R.drawable.img_bg, true);
        doctorName.setText(item.getName());
        doctorLevel.setText(item.getPositionStr());
        consultTime.setText(DateUtil.date2String(item.getLastConsultTime(), DateUtil.DEFAULT_FORMAT_DATE));
        String typeStr = item.getIsRead() ? "[已读] " : "[未读] ";
        message.setText(SpannableStringUtils.getBuilder(typeStr)
                .setForegroundColor(mContext.get().getResources().getColor(item.getIsRead() ? R.color.color_main_black : R.color.color_main_red))
                .append(item.getContent())
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray))
                .create());
        if (position == getData().size() - 1) {
            divider_my_consult.setVisibility(View.GONE);
        }

        mSwipeMenuLayout.smoothToCloseMenu();
    }

    public void setmDeleteListen(DeleteListener mDeleteListener) {
        this.mDeleteListener = mDeleteListener;
    }

    @OnClick({R.id.mDeleteTv, R.id.mSwipeMenuLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDeleteTv:
                if (mSwipeMenuLayout.isMenuOpen()) {
                    mSwipeMenuLayout.smoothToCloseMenu();
                }
                if (EmptyUtils.isNotEmpty(mDeleteListener)) {
                    mDeleteListener.onDeleteListener((ConsultListItem) view.getTag());
                }
                break;
            case R.id.mSwipeMenuLayout:
                if (mSwipeMenuLayout.isMenuOpen()) {
                    mSwipeMenuLayout.smoothToCloseMenu();
                }
                notifyDataSetChanged();
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onItemClick(view, (int) view.getTag());
                }
                break;
        }
    }
}
