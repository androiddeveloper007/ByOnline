package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.UserPrescriptionComment;
import com.bowen.tcm.common.widget.GlideCircleTransform;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 偏方列表adapter
 * created by zhuzp at 2018/05/22
 */
public class UserCommentPrescriptionRecyclerAdapter extends BaseQuickAdapter<UserPrescriptionComment> {
    @BindView(R.id.userImg)
    CircleImageView userImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.commentTime)
    TextView commentTime;
    @BindView(R.id.userComment)
    TextView userComment;

    public UserCommentPrescriptionRecyclerAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_user_comment_prescription;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UserPrescriptionComment item, final int position) {
        ButterKnife.bind(this,helper.convertView);
        ImageLoaderUtil.getInstance().show(userImg.getContext(), item.getHeadImgUrl(), userImg, R.drawable.add_img, true);
        doctorName.setText(item.getUserNickname());
        commentTime.setText(DateUtil.date2String(item.getCreateTime(), DateUtil.DEFAULT_FORMAT_DATE));
        userComment.setText(item.getComment());
    }
}
