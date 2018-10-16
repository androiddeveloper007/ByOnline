package com.bowen.tcm.medicalrecord.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */

public class MedicalRecordPhotoAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mPhotoImg)
    CircleImageView mPhotoImg;

    @BindView(R.id.addImg)
    ImageView addImg;
    @BindView(R.id.mDeleteImg)
    ImageView mDeleteImg;

    public interface DeletePhotoListener {
        void onDelete(View view);
    }


    private DeletePhotoListener mListener;

    private boolean isShowDelete = false;

    public MedicalRecordPhotoAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_medical_record_photo;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mDeleteImg.setTag(position);
        if (item.equals("拍照")) {
            mPhotoImg.setImageResource(R.drawable.add_photo);
            addImg.setVisibility(View.VISIBLE);
            mDeleteImg.setVisibility(View.GONE);
        } else {
            addImg.setVisibility(View.GONE);
            if (isShowDelete) {
                mDeleteImg.setVisibility(View.VISIBLE);
            } else {
                mDeleteImg.setVisibility(View.GONE);
            }
            ImageLoaderUtil.getInstance().show(mContext.get(), item, mPhotoImg,R.drawable.img_bg);
        }
    }

    public void setmListener(DeletePhotoListener mListener) {
        this.mListener = mListener;
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }


    @OnClick({R.id.mDeleteImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDeleteImg:
                mListener.onDelete(view);
                break;
        }
    }
}
