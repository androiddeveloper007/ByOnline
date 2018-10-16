package com.bowen.tcm.inquiry.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.commonlib.widget.CollapsedTextView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.DoctorCommentInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/2.
 */
public class UserDoctorCommentAdapter extends BaseQuickAdapter<DoctorCommentInfo> {

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mUserNameTv)
    TextView mUserNameTv;
    @BindView(R.id.commentTime)
    TextView commentTime;
    @BindView(R.id.mRatingBarSmall)
    RatingBar mRatingBarSmall;
    @BindView(R.id.userComment)
    CollapsedTextView userComment;
    @BindView(R.id.mCommentProductNameTv)
    TextView mCommentProductNameTv;
    @BindView(R.id.mCommentProductPriceTv)
    TextView mCommentProductPriceTv;

    public UserDoctorCommentAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_user_doctor_comment;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorCommentInfo item, int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext.get(), item.getUserImg(), mHeadPortraitImg, R.drawable.man);
        mUserNameTv.setText(item.getUserName());
        commentTime.setText(DateUtil.date2String(item.getUserEvaluateTime(), DateUtil.DEFAULT_FORMAT_DATETIME));
        userComment.setText(item.getUserEvaluate());
        mCommentProductNameTv.setText(SpannableStringUtils.getBuilder("问诊方式：")
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray_01))
                .append(item.getAppointmentTypeStr())
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black))
                .create());

        mCommentProductPriceTv.setText(SpannableStringUtils.getBuilder("问诊费用：")
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray_01))
                .append(item.getAmount() + "元")
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black))
                .create());
        mRatingBarSmall.setRating(item.getScore());
    }
}
