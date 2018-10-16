package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.VisiablePerson;
import com.bowen.tcm.common.dialog.adapter.ChooseVisiablePermissionAdapter;
import com.bowen.tcm.common.dialog.adapter.RemindPersonAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/2.
 */

public class RemindPersonDialog extends BaseDialog {


    @BindView(R.id.mFinishTv)
    TextView mFinishTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private boolean isChooseSelf;

    private ArrayList<FamilyMember> mFamilyMembers;

    private RemindPersonAdapter mAdapter;

    public void setmFamilyMembers(ArrayList<FamilyMember> mFamilyMembers) {
        this.mFamilyMembers = mFamilyMembers;
    }

    public RemindPersonDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }


    public RemindPersonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remind_person);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RemindPersonAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(mFamilyMembers);
        changeStatus(mAdapter.getItem(0),true);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                    FamilyMember familyMember = mAdapter.getItem(position);
                    if(!familyMember.isChoose()){
                        changeStatus(familyMember,true);
                    }
            }
        });
        setCanceledOnTouchOutside(true);
    }

    private void changeStatus(FamilyMember familyMember,boolean changeStatus){
        for(FamilyMember item:mFamilyMembers){
            if(item.equals(familyMember)){
               item.setChoose(changeStatus);
            }else{
                item.setChoose(!changeStatus);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private FamilyMember getChooseMember(){
        for(FamilyMember item:mFamilyMembers){
            if(item.isChoose()){
              return item;
            }
        }
        return null;
    }

    @OnClick({R.id.mFinishTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mFinishTv:
                if(EmptyUtils.isNotEmpty(mListener)){
                    mListener.onDataCallBack(getChooseMember());
                }
                dismiss();
                break;
        }
    }
}
