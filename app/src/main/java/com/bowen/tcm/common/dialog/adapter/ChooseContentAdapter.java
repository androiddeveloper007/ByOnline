package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/21.
 */

public class ChooseContentAdapter extends BaseQuickAdapter<String> {

    @BindView(R.id.mContentTv)
    TextView mContentTv;

    private int choosePos = 0;

    public ChooseContentAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_disease_name;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        ButterKnife.bind(this, helper.convertView);
        if(choosePos == position){
            mContentTv.setSelected(true);
            mContentTv.setTextColor(mContext.get().getResources().getColor(R.color.color_white));
        }else{
            mContentTv.setSelected(false);
            mContentTv.setTextColor(mContext.get().getResources().getColor(R.color.color_main_gray_01));
        }
        mContentTv.setText(item);
    }

    public void setChoosePos(int choosePos) {
        this.choosePos = choosePos;
    }
    public void setChoosePosStr(String choosePosStr) {
        Iterator<String> iterator = mData.iterator();
        int i = 0;
        while(iterator.hasNext()){
            i++;
            String item = iterator.next();
            if(TextUtils.equals(item, choosePosStr)) {
                this.choosePos=i;
            }
        }
    }
}
