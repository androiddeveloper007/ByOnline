package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.DiseaseInfo;

import java.util.List;

/**
 * 添加偏方中选择病症列表adapter
 * created by zhuzp at 2018/05/22
 */
public class AddPrescriptionFitDiseaseRvAdapter extends BaseQuickAdapter<DiseaseInfo> {
    public AddPrescriptionFitDiseaseRvAdapter(Context c){
        super(c);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.add_disease_rv_item;
    }

    @Override
    protected void convert(final BaseViewHolder helper, DiseaseInfo item, final int position) {
        ((TextView) helper.convertView.findViewById(R.id.mContentTv)).setText(item.getDiseaseName());
        helper.convertView.findViewById(R.id.iv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                remove(helper.getAdapterPosition());
                removeAndNotify(helper.getAdapterPosition());
            }
        });
    }

    public String getSelectedDisease(){
        List<DiseaseInfo> list = getData();
        if(list!=null && list.size()>0){
            StringBuilder stringBuilder = new StringBuilder();
            for(DiseaseInfo item : list){
                stringBuilder.append(item.getDiseaseId());
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            return stringBuilder.toString();
        }
        return "";
    }

    private void removeAndNotify(int position){
        getData().remove(position);
        notifyDataSetChanged();
    }
}
