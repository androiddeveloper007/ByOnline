package com.bowen.tcm.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.awen.contact.model.ContactsInfo;
import com.awen.contact.view.ContactSelectorActivity;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.event.ChooseFamilyMemberEvent;
import com.bowen.tcm.mine.adapter.FamilyMemberAdapter;
import com.bowen.tcm.mine.adapter.OnLinkListener;
import com.bowen.tcm.mine.contract.FamilyMemberContract;
import com.bowen.tcm.mine.presenter.FamilyMemberPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:家庭成员列表
 * Created by AwenZeng on 2018/3/26.
 */

public class FamilyMemberActivity extends BaseActivity implements FamilyMemberContract.View{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private TextView mAddFamilyMemberTv;
    private FamilyMemberPresenter mPresenter;
    private FamilyMemberAdapter mAdapter;
    private String mFamilyId;
    private int fromType;
    public static final int FROM_HOMEPAGE_MEDICAL_RECORD = 0;//从病历首页
    public static final int FROM_FAMILY_MEMBER = 1;//从家庭成员
    private View footView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_family_member);
        ButterKnife.bind(this);
        setTitle("家庭成员");
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            fromType = bundle.getInt(RouterActivityUtil.FROM_TAG,FROM_HOMEPAGE_MEDICAL_RECORD);
        }
        init();
    }

    private void init(){
        mPresenter = new FamilyMemberPresenter(this,this);
        footView = getLayoutInflater().inflate(R.layout.footview_family_member, null);
        mAddFamilyMemberTv = footView.findViewById(R.id.mAddFamilyMemberTv);
        mAddFamilyMemberTv.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FamilyMemberAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
             if(fromType == FROM_HOMEPAGE_MEDICAL_RECORD){
                 ChooseFamilyMemberEvent event = new ChooseFamilyMemberEvent();
                 event.setData(mAdapter.getItem(position));
                 EventBus.getDefault().post(event);
                 finish();
             }else{
                 Bundle bundle = new Bundle();
                 bundle.putSerializable(RouterActivityUtil.FROM_TAG,mAdapter.getItem(position));
                 RouterActivityUtil.startActivity(FamilyMemberActivity.this,FamilyMemberInfoActivity.class,bundle);
             }
            }
        });
        mAdapter.setmLinkListener(new OnLinkListener() {
            @Override
            public void onLinkFamilyMember(String familyId) {
                mFamilyId = familyId;
            }
        });
        mPresenter.getFamilyUserRelation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getFamilyMembers();
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        RouterActivityUtil.startActivity(this,AddFamilyMemberActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode){
                case ContactSelectorActivity.REQUEST_CONTACT:
                    ArrayList<ContactsInfo> contactsInfos = (ArrayList<ContactsInfo>) data.getSerializableExtra(ContactSelectorActivity.REQUEST_OUTPUT);
                    String phone = contactsInfos.get(0).getPhone();
                    mPresenter.updateFamilyMemberInfo(mFamilyId,null,null,phone,null,null);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list) {
        mAdapter.addFooterView(footView);
        if(EmptyUtils.isNotEmpty(list)){
            mAdapter.setNewData(list);
        }
    }

    @Override
    public void onGetFamilyMembersFail(ArrayList<FamilyMember> list) {
        mAdapter.addFooterView(footView);
    }

    @Override
    public void onUpdateInfoSuccess() {
        mPresenter.getFamilyMembers();
    }
}
