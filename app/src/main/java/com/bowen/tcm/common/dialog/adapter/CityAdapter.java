package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.location.City;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AwenZeng on 2016/12/17.
 */

public class CityAdapter extends BaseQuickAdapter<City> {

    @BindView(R.id.cityTv)
    TextView cityTv;
    @BindView(R.id.cityBgLayout)
    RelativeLayout cityBgLayout;

    private int selectPos;

    public CityAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_city;
    }

    @Override
    protected void convert(BaseViewHolder helper, City item, int position) {
        ButterKnife.bind(this,helper.convertView);
        cityTv.setText(item.getCity());
        if (selectPos == position) {
            cityTv.setTextColor(mContext.get().getResources().getColor(R.color.color_white));
            cityBgLayout.setBackgroundColor(mContext.get().getResources().getColor(R.color.color_main));
        } else {
            cityTv.setTextColor(mContext.get().getResources().getColor(R.color.color_main_black));
            cityBgLayout.setBackgroundColor(mContext.get().getResources().getColor(R.color.color_white));
        }
    }

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }
}
