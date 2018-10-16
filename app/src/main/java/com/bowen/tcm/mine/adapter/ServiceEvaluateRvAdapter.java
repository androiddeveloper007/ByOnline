package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.VoListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务评价列表adapter
 * created by zhuzp at 2018/05/22
 */
public class ServiceEvaluateRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<VoListBean> list;
    private OnItemClickListener mOnItemClickListener = null;//声明自定义的监听接口
//    private List<Boolean> booleanList;

    public ServiceEvaluateRvAdapter(Context cxt, List<VoListBean> list) {
        this.context = cxt;
        mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
//        booleanList = new ArrayList<>(list.size());
//        for(int i=0;i<list.size();i++){
//            booleanList.add(i,false);
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.service_evaluate_rv_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).mContentTv.setText(list.get(position).getEvaluate());
        holder.itemView.setTag(position);
//        if(booleanList.size()>0) {
//            if (booleanList.get(position)) {
//                ((ItemViewHolder) holder).mContentTv.setSelected(true);
//            } else {
//                ((ItemViewHolder) holder).mContentTv.setSelected(false);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mContentTv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mContentTv = itemView.findViewById(R.id.mContentTv);
        }
    }

    @Override
    public void onClick(View view) {
        if(this.mOnItemClickListener != null) {
            mOnItemClickListener.onItemClickListener(view, list.get((int) view.getTag()).getEvaluate());
        }
//        setBooleanListByPos((int) view.getTag(), !booleanList.get((int) view.getTag()));
    }

//    public void setBooleanListByPos(int pos, boolean isSelected) {
//        if(booleanList!=null && booleanList.size()>0){
//            booleanList.set(pos, isSelected);
//        }
//        notifyDataSetChanged();
//    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, String str);
    }

    public void setOnItemClickListener (OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void clearAndNotify() {
        if(list != null){
            this.list.clear();
        }
        notifyDataSetChanged();
    }

    public String getItemName(int position) {
        return list.get(position).getEvaluate();
    }

}
