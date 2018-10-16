package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.FamilyMember;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/9.
 */

public class RemindPersonAdapter extends BaseQuickAdapter<FamilyMember> {

    @BindView(R.id.mPhoneNumTv)
    TextView mPhoneNumTv;
    @BindView(R.id.mChooseImg)
    ImageView mChooseImg;
    @BindView(R.id.mNameTv)
    TextView mNameTv;

    public RemindPersonAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_remind_person;
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyMember item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mChooseImg.setSelected(item.isChoose());
        mNameTv.setText(item.getFamilyNickname());
        mPhoneNumTv.setText("("+item.getFamilyTypeTxt()+")");
    }
}
