package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.VisiablePerson;
import com.bowen.tcm.common.dialog.adapter.ChooseVisiablePermissionAdapter;
import com.bowen.tcm.common.model.AppConfigInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/9.
 */

public class ChooseVisablePermissionDialog extends BaseDialog {


    @BindView(R.id.mFinishTv)
    TextView mFinishTv;
    @BindView(R.id.mSelfVisiableImg)
    ImageView mSelfVisiableImg;
    @BindView(R.id.mSelfVisiableTv)
    TextView mSelfVisiableTv;
    @BindView(R.id.mSelfVisiableLayout)
    LinearLayout mSelfVisiableLayout;
    @BindView(R.id.mLinkFamilyVisiableImg)
    ImageView mLinkFamilyVisiableImg;
    @BindView(R.id.mLinkFamilyVisiableTv)
    TextView mLinkFamilyVisiableTv;
    @BindView(R.id.mLinkFamilyVisiableLayout)
    RelativeLayout mLinkFamilyVisiableLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private boolean isChooseSelf;

    private ChooseVisiablePermissionAdapter mAdapter;

    public ChooseVisablePermissionDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }


    public ChooseVisablePermissionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_visiable_permission);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChooseVisiablePermissionAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        if (EmptyUtils.isNotEmpty(AppConfigInfo.getInstance().getVisiablePersonList())) {
            mAdapter.setNewData(AppConfigInfo.getInstance().getVisiablePersonList());
            mSelfVisiableImg.setSelected(true);
            changeData(false);
            isChooseSelf = true;
        } else {
            mLinkFamilyVisiableLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }

        setCanceledOnTouchOutside(true);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(!isChooseSelf){
                    mLinkFamilyVisiableImg.setSelected(false);
                    if(mAdapter.getItem(position).isChoose()){
                        mAdapter.getItem(position).setChoose(false);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        mAdapter.getItem(position).setChoose(true);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }


    @OnClick({R.id.mFinishTv, R.id.mSelfVisiableLayout, R.id.mLinkFamilyVisiableLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mFinishTv:
                if(!isChooseSelf){
                    mListener.onDataCallBack(choosePhone(),chooseNickName());
                }else{
                    mListener.onDataCallBack("");
                }
                dismiss();
                break;
            case R.id.mSelfVisiableLayout:
                mSelfVisiableImg.setSelected(true);
                mLinkFamilyVisiableImg.setSelected(false);
                isChooseSelf = true;
                changeData(false);
                break;
            case R.id.mLinkFamilyVisiableLayout:
                mSelfVisiableImg.setSelected(false);
                mLinkFamilyVisiableImg.setSelected(true);
                isChooseSelf = false;
                changeData(true);
                break;
        }
    }

    private void changeData(boolean isChoose) {
        List<VisiablePerson> list = mAdapter.getData();
        for (VisiablePerson person : list) {
            person.setChoose(isChoose);
        }
        mAdapter.setNewData(list);
    }

    private String choosePhone(){
        StringBuilder stringBuilder = new StringBuilder();
        List<VisiablePerson> list = mAdapter.getData();
        for(VisiablePerson person:list){
            if(person.isChoose()){
                stringBuilder.append(person.getFamilyPhone()+",");
            }
        }
        return stringBuilder.toString();

    }

    private String chooseNickName(){
        StringBuilder stringBuilder = new StringBuilder();
        List<VisiablePerson> list = mAdapter.getData();
        for(VisiablePerson person:list){
            if(person.isChoose()){
                stringBuilder.append(person.getFamilyNickname()+" ");
            }
        }
        return stringBuilder.toString();

    }
}
