package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.DoctorRank;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/3.
 */
public class ChooseDoctorRankAdapter extends BaseQuickAdapter<DoctorRank> {

    @BindView(R.id.mDepartmentTv)
    TextView mDepartmentTv;

    private int selectPos = 9999;

    public ChooseDoctorRankAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_department;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorRank item, int position) {
        ButterKnife.bind(this,helper.convertView);
        mDepartmentTv.setText(item.getPosition());
        if (selectPos == position) {
            mDepartmentTv.setTextColor(mContext.get().getResources().getColor(R.color.color_white));
            mDepartmentTv.setSelected(true);
        } else {
            mDepartmentTv.setTextColor(mContext.get().getResources().getColor(R.color.split_line_gray));
            mDepartmentTv.setSelected(false);
        }
    }

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }
}
