package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.FindDoctorItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/27.
 */
public class FindDoctorDepartmentAdapter extends BaseQuickAdapter<FindDoctorItem> {

    @BindView(R.id.mPhotoImg)
    CircleImageView mPhotoImg;
    @BindView(R.id.mNameTv)
    TextView mNameTv;

    public FindDoctorDepartmentAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_dialog_find_doctor;
    }


    @Override
    protected void convert(BaseViewHolder helper, FindDoctorItem item, int position) {
        ButterKnife.bind(this,helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext.get(),item.getFileLink(),mPhotoImg,R.drawable.img_bg);
        mNameTv.setText(item.getDepartmentsName());
    }
}
