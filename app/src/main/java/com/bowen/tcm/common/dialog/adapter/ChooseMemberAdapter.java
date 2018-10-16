package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.VisiablePerson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/9.
 */

public class ChooseMemberAdapter extends BaseQuickAdapter<FamilyMember> {

    @BindView(R.id.mChooseImg)
    ImageView mChooseImg;
    @BindView(R.id.mNameTv)
    TextView mNameTv;
    @BindView(R.id.mAgeTv)
    TextView mAgeTv;
    @BindView(R.id.mRelationShipTv)
    TextView mRelationShipTv;

    public ChooseMemberAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_member;
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyMember item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mChooseImg.setSelected(item.isChoose());
        mNameTv.setText(item.getFamilyNickname());
        if(EmptyUtils.isNotEmpty(item.getAge())){
            mAgeTv.setText(item.getAge()+"岁");
        }else{
            mAgeTv.setText("未知");
        }
        mRelationShipTv.setText("("+item.getFamilyTypeTxt()+")");
    }
}
