package com.bowen.tcm.common.dialog.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.ShowApplyCrowd;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:多选弹框
 * Created by zhuzhipeng on 2018/7/4.
 */

public class MultiChooseContentAdapter extends BaseQuickAdapter<ShowApplyCrowd> {

    private List<Boolean> booleanList;
    public MultiChooseContentAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    public void setNewData(List<ShowApplyCrowd> data) {
        super.setNewData(data);
        booleanList = new ArrayList<>(getData().size());
        for(int i=0;i<getData().size();i++){
            if(i==0){
                booleanList.add(i,true);
            }else{
                booleanList.add(i,false);
            }
        }
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_choose_disease_name;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShowApplyCrowd item, int position) {
        final TextView mContentTv = helper.getView(R.id.mContentTv);
        if(booleanList.size()>0){
            if(booleanList.get(position)){
                mContentTv.setSelected(true);
                mContentTv.setTextColor(mContext.get().getResources().getColor(R.color.color_white));
            }else{
                mContentTv.setSelected(false);
                mContentTv.setTextColor(mContext.get().getResources().getColor(R.color.color_main_gray_01));
            }
        }
        mContentTv.setText(item.getApplyName());
    }

    public void setBooleanListByPos(int pos, boolean isSelected){
        if(booleanList!=null&& booleanList.size()>0){
            booleanList.set(pos, isSelected);
        }
    }

    public boolean isSelectedByPos(int pos) {
        if(booleanList!=null&& booleanList.size()>0){
            return booleanList.get(pos);
        }
        return false;
    }

    public boolean isAllDisSelected(){
        boolean isAllDisSelected = true;
        for(boolean b :booleanList){
            if(b){
                isAllDisSelected = false;
            }
        }
        return isAllDisSelected;
    }

    public String getSelectedStr(){
        StringBuilder str = new StringBuilder();
        for(int i=0;i<getItemCount();i++){
            if(booleanList.get(i)){
                str.append(((ShowApplyCrowd)getData().get(i)).getApplyName());
                str.append(",");
            }
        }
        if(str.length()>0){
            str.deleteCharAt(str.length()-1);
        }
        return str.toString();
    }

    public String getSelectedId(){
        StringBuilder str = new StringBuilder();
        for(int i=0;i<getItemCount();i++){
            if(booleanList.get(i)){
                str.append(((ShowApplyCrowd)getData().get(i)).getId());
                str.append(",");
            }
        }
        if(str.length()>0){
            str.deleteCharAt(str.length()-1);
        }
        return str.toString();
    }
}