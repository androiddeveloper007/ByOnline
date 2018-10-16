package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.DiseaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 病症选择popupWindow列表adapter
 * created by zhuzp at 2018/05/22
 */
public class FitDiseasePopupWindowRvAdapter extends BaseQuickAdapter<DiseaseInfo> {
    private List<Boolean> booleanList;
    public FitDiseasePopupWindowRvAdapter(Context c){
        super(c);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.fit_disease_popup_rv_item;
    }

    @Override
    protected void convert(final BaseViewHolder helper, DiseaseInfo item, final int position) {
        ((TextView) helper.convertView.findViewById(R.id.mContentTv)).setText(item.getDiseaseName());
        CheckBox checkFitDiseaseItem = helper.convertView.findViewById(R.id.checkFitDiseaseItem);
        if(booleanList.size()>0){
            if(booleanList.get(position)){
                checkFitDiseaseItem.setChecked(true);
            }else{
                checkFitDiseaseItem.setChecked(false);
            }
        }
    }

    @Override
    public void setNewData(List<DiseaseInfo> data) {
        super.setNewData(data);
        booleanList = new ArrayList<>(getData().size());
        for(int i=0;i<getData().size();i++){
            booleanList.add(i,false);
        }
    }

    @Override
    public void notifyDataChangedAfterLoadMore(List<DiseaseInfo> data, boolean isNextLoad) {
        for(int i=0;i<data.size();i++){
            booleanList.add(i+mData.size(),false);
        }
        super.notifyDataChangedAfterLoadMore(data, isNextLoad);
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

    public boolean hasChecked(){
        boolean hasChecked = false;
        for(int i=0;i<getData().size();i++){
            if(booleanList.get(i)) hasChecked=true;
        }
        return hasChecked;
    }

    public List<DiseaseInfo> getDiseaseList(){
        List<DiseaseInfo> list = new ArrayList<>();
        for(int i=0;i<getData().size();i++){
            if(booleanList.get(i)){
                list.add(((DiseaseInfo)getData().get(i)));
            }
        }
        return list;
    }
}
