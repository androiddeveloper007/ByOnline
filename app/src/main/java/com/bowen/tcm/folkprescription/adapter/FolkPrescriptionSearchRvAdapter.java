package com.bowen.tcm.folkprescription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.HospitalDepartments;

import java.util.List;

/**
 * 偏方搜索列表adapter
 * created by zhuzp at 2018/05/22
 */
public class FolkPrescriptionSearchRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<String> list;
    //声明自定义的监听接口
    private OnItemClickListener mOnItemClickListener = null;

    public FolkPrescriptionSearchRvAdapter(Context cxt, List<String> list) {
        this.context = cxt;
        mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.search_rv_history_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).mContentTv.setText(list.get(position));
        holder.itemView.setTag(position);
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
            mOnItemClickListener.onItemClickListener(view, (int) view.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public void setOnItemClickListener (OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void update(List<String> list) {
        if(list != null){
            this.list.clear();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void clearAndNotify() {
        if(list != null){
            this.list.clear();
        }
        notifyDataSetChanged();
    }

    public String getItemName(int position) {
        return list.get(position);
    }
}
