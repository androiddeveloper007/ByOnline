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
 * 筛选科室adapter
 * created by zhuzp at 2018/05/22
 */
public class DrawerDepartmentExpRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<HospitalDepartments> list;
    private int selectPosition;
    private boolean bToggle = false;
    private int limitCount = 8; //默认限制值

    private OnItemClickListener mOnItemClickListener = null;//声明自定义的监听接口

    public DrawerDepartmentExpRvAdapter(Context cxt, List<HospitalDepartments> list, int limitCount) {
        this.context = cxt;
        mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.limitCount = limitCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.drawer_rv_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).mContentTv.setText(list.get(position).getDepartmentsName());
        holder.itemView.setTag(position);
        if (selectPosition == position) {
            ((ItemViewHolder) holder).mContentTv.setSelected(true);
        } else {
            ((ItemViewHolder) holder).mContentTv.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        //先判断mList的size是否小于等于limitCount, 如果满足则直接返回size
        if (list.size() <= limitCount) {
            return list.size();
        }
        //如果大于8的话
        if (bToggle) {
            return list.size();
        } else {
            return limitCount;
        }
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
        if (this.mOnItemClickListener != null) {
            mOnItemClickListener.onItemClickListener(view, (int) view.getTag());
        }
        setItemSingleSelect((int) view.getTag());
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /*single select*/
    public void setItemSingleSelect(int selectPosition) {
        if (list != null && list.size() > 0) {
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }
    }

    /*获取当前选中的id*/
    public String getSelectedItemId() {
        return list.get(selectPosition).getId();
    }

    public void update(List<HospitalDepartments> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /** 切换展开和合起 */
    public void toggleExpand(boolean bToggle) {
        this.bToggle = bToggle;
        notifyDataSetChanged();
    }
}
