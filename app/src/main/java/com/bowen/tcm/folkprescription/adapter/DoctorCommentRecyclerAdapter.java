package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.commonlib.widget.CollapsedTextView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.DoctorComment;
import com.bowen.tcm.common.bean.DoctorPrescriptionComment;
import com.bowen.tcm.common.widget.GlideCircleTransform;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 医生点评列表adapter
 * created by zhuzp at 2018/06/22
 */
public class DoctorCommentRecyclerAdapter extends BaseQuickAdapter<DoctorPrescriptionComment> {


    @BindView(R.id.doctorImg)
    CircleImageView doctorImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.doctorLevel)
    TextView doctorLevel;
    @BindView(R.id.hospitalName)
    TextView hospitalName;
    @BindView(R.id.commentTime)
    TextView commentTime;
    @BindView(R.id.doctorComment)
    CollapsedTextView doctorComment;
    @BindView(R.id.divider_folk_prescription)
    View dividerFolkPrescription;

    public DoctorCommentRecyclerAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.rv_item_doctor_comment;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorPrescriptionComment item, int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(doctorName.getContext(), item.getHeadImgUrl(), doctorImg, R.drawable.add_img, true);
        doctorName.setText(item.getName());
        doctorLevel.setText(item.getPosition());
        hospitalName.setText(item.getHostipal());
        commentTime.setText(item.getCreateTime());
        doctorComment.setText(item.getComment());
        if(position==getData().size()-1){
            dividerFolkPrescription.setVisibility(View.GONE);
        }
    }

}
