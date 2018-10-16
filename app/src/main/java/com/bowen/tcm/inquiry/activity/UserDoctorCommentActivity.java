package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorCommentInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.UserDoctorComment;
import com.bowen.tcm.inquiry.adapter.FindDoctorAdapter;
import com.bowen.tcm.inquiry.adapter.UserDoctorCommentAdapter;
import com.bowen.tcm.inquiry.contract.UserDoctorCommentContract;
import com.bowen.tcm.inquiry.presenter.UserDoctorCommentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/4.
 */
public class UserDoctorCommentActivity extends BaseActivity implements UserDoctorCommentContract.View,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;


    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    private String mDoctorId;
    private UserDoctorCommentAdapter mAdapter;
    private UserDoctorCommentPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_general);
        ButterKnife.bind(this);
        setTitle("用户评论");
        init();

    }

    private void init(){
        mDoctorId = RouterActivityUtil.getString(this);
        mPresenter = new UserDoctorCommentPresenter(this,this);
        mAdapter = new UserDoctorCommentAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 100);
    }


    private void getData(final boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getDoctorCommentList(mDoctorId);
    }

    @Override
    public void onGetDoctorCommentListSuccess(UserDoctorComment userDoctorComment) {
        mPtrFrameLayout.refreshComplete();
        if(EmptyUtils.isEmpty(userDoctorComment.getDoctorCommentList())){
            userDoctorComment.setDoctorCommentList(new ArrayList<DoctorCommentInfo>());
        }
        Page pageBean = userDoctorComment.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(userDoctorComment.getDoctorCommentList(), true);
        } else {
            mAdapter.setNewData(userDoctorComment.getDoctorCommentList());
        }

        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
            TextView textView = (TextView)emptyView.findViewById(R.id.textView);
            textView.setText("暂无评论");
            mAdapter.addFooterView(emptyView);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onGetDoctorCommentListFailed() {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

}
