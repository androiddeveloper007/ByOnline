package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.tcm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/26.
 */
public class ClinicGallerAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mPhotoImg)
    ImageView mPhotoImg;

    public ClinicGallerAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_clinic_gallery;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this,helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext.get(),item,mPhotoImg,R.drawable.img_bg);
    }
}
