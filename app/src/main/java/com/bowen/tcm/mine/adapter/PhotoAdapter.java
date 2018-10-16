package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.medicalrecord.adapter.MedicalRecordPhotoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/3.
 */

public class PhotoAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mPhotoImg)
    CircleImageView mPhotoImg;
    @BindView(R.id.addImg)
    ImageView addImg;
    @BindView(R.id.mDeleteImg)
    ImageView mDeleteImg;

    private MedicalRecordPhotoAdapter.DeletePhotoListener mListener;


    public PhotoAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_photo;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mDeleteImg.setTag(position);
        if(item.equals("拍照")){
            mPhotoImg.setImageResource(R.drawable.add_photo);
            addImg.setVisibility(View.VISIBLE);
            mDeleteImg.setVisibility(View.GONE);
        }else{
            addImg.setVisibility(View.GONE);
            mDeleteImg.setVisibility(View.VISIBLE);
            ImageLoaderUtil.getInstance().show(mContext.get(),item,mPhotoImg,R.drawable.img_bg);
        }
    }

    @OnClick({R.id.mDeleteImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDeleteImg:
                mListener.onDelete(view);
                break;
        }
    }

    public void setmListener(MedicalRecordPhotoAdapter.DeletePhotoListener mListener) {
        this.mListener = mListener;
    }
}
