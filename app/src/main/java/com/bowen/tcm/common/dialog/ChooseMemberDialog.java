package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.VisiablePerson;
import com.bowen.tcm.common.dialog.adapter.ChooseMemberAdapter;
import com.bowen.tcm.common.dialog.adapter.ChooseVisiablePermissionAdapter;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.mine.model.FamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:选择家庭成员
 * Created by AwenZeng on 2018/4/9.
 */

public class ChooseMemberDialog extends BaseDialog {


    @BindView(R.id.mFinishTv)
    TextView mFinishTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private ChooseMemberAdapter mAdapter;

    private FamilyMemberModel mMemberModel;

    public ChooseMemberDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }


    public ChooseMemberDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_member);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        mMemberModel = new FamilyMemberModel(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChooseMemberAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        setCanceledOnTouchOutside(true);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                    if (mAdapter.getItem(position).isChoose()) {
                        mAdapter.getItem(position).setChoose(false);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.getItem(position).setChoose(true);
                        mAdapter.notifyDataSetChanged();
                    }
                changeData(false,mAdapter.getItem(position));
            }
        });

        getFamilyMembers();
    }


    private void getFamilyMembers(){
        mMemberModel.getFamilyMembers(new HttpTaskCallBack<ArrayList<FamilyMember>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<FamilyMember>> result) {
                mAdapter.setNewData(result.getData());
                changeData(false,null);
            }

            @Override
            public void onFail(HttpResult<ArrayList<FamilyMember>> result) {

            }
        });
    }


    @OnClick({R.id.mFinishTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mFinishTv:
                FamilyMember familyMember = chooseMember();
                if(EmptyUtils.isNotEmpty(familyMember)){
                    mListener.onDataCallBack(familyMember);
                    dismiss();
                }else{
                    ToastUtil.getInstance().showToastDialog("请选择家庭成员");
                }
                break;
        }
    }

    private void changeData(boolean isChoose,FamilyMember item) {
        List<FamilyMember> list = mAdapter.getData();
        if(EmptyUtils.isNotEmpty(item)){
            for (FamilyMember person : list) {
                if(!item.getFamilyId().equals(person.getFamilyId())){
                    person.setChoose(false);
                }
            }
            mAdapter.setNewData(list);
        }else{
            for (FamilyMember person : list) {
                person.setChoose(isChoose);
            }
            mAdapter.setNewData(list);
        }

    }

    private FamilyMember chooseMember() {
        List<FamilyMember> list = mAdapter.getData();
        for (FamilyMember person : list) {
            if (person.isChoose()) {
              return person;
            }
        }
        return null;

    }
}
