package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.commonlib.widget.CollapsedTextView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.UserComment;
import com.bowen.tcm.common.bean.UserPrescriptionComment;
import com.bowen.tcm.common.widget.GlideCircleTransform;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户评价列表adapter
 * created by zhuzhipeng at 2018/06/22
 */
public class UserCommentRecyclerAdapter extends BaseQuickAdapter<UserPrescriptionComment> {


    @BindView(R.id.userImg)
    CircleImageView userImg;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.commentTime)
    TextView commentTime;
    @BindView(R.id.userComment)
    CollapsedTextView userComment;
    @BindView(R.id.dividerUserComment)
    View dividerUserComment;

    public UserCommentRecyclerAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_user_comment;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserPrescriptionComment item, int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(userName.getContext(), item.getHeadImgUrl(), userImg, R.drawable.add_img, true);
        userName.setText(item.getUserNickname());
        commentTime.setText(DateUtil.date2String(item.getCreateTime(), DateUtil.DEFAULT_FORMAT_DATE));
        userComment.setText(item.getComment());
        if (position == getData().size() - 1) {
            dividerUserComment.setVisibility(View.GONE);
        }
    }

}
