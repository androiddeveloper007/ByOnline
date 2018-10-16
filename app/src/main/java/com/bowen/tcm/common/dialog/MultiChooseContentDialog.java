package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.dialog.adapter.MultiChooseContentAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by zhuzhipeng on 2018/7/4.
 * description:多选适用人群
 */

public class MultiChooseContentDialog extends BaseDialog {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mTitleTv)
    TextView mTitleTv;
    @BindView(R.id.submitBtn)
    TextView submitBtn;

    private ArrayList<ShowApplyCrowd> list;
    private String titleStr;
    private String selectStr = "";
    private MultiChooseContentAdapter mAdapter;

    public MultiChooseContentDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
        list = new ArrayList<>();
    }

    public MultiChooseContentDialog(Context context, int themeResId) {
        super(context, themeResId);
        list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_disease_name);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        mAdapter = new MultiChooseContentAdapter(mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position==0){
                    if(!mAdapter.isSelectedByPos(position)){
                        mAdapter.setBooleanListByPos(position,true);
                        for(int i=1;i<mAdapter.getItemCount();i++){
                            mAdapter.setBooleanListByPos(i,false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    if(mAdapter.isSelectedByPos(position)){
                        mAdapter.setBooleanListByPos(position,false);
                        if(mAdapter.isAllDisSelected()){
                            mAdapter.setBooleanListByPos(0,true);
                        }
                    }else{
                        mAdapter.setBooleanListByPos(position,true);
                        mAdapter.setBooleanListByPos(0,false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        if(EmptyUtils.isNotEmpty(titleStr)){
            mTitleTv.setText(titleStr);
        }
        setCanceledOnTouchOutside(true);
    }

    @OnClick({R.id.submitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                dismiss();
                break;
        }
    }

    public void setTitleStr(String mTitleStr) {
        this.titleStr = mTitleStr;
    }

    public void setSubmitBtnGone(){
        submitBtn.setVisibility(View.GONE);
    }

    public String getSelectedId(){
        String tempId = mAdapter.getSelectedId();
        String tempStr = mAdapter.getSelectedStr();
        mListener.onDataCallBack(tempStr);
        return tempId;
    }

    public void setList(ArrayList<ShowApplyCrowd> mList) {
        this.list = mList;
    }

    public void setSelectStr(String selectStr){
        this.selectStr = selectStr;
    }
}