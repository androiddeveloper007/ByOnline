package com.bowen.tcm.folkprescription.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.UserComment;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;
import com.bowen.tcm.folkprescription.adapter.DoctorCommentRecyclerAdapter;
import com.bowen.tcm.folkprescription.adapter.UserCommentRecyclerAdapter;
import com.bowen.tcm.folkprescription.contract.UserCommentContract;
import com.bowen.tcm.folkprescription.presenter.UserCommentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:用户评价
 * Created by zzp on 2018/6/25.
 */

public class UserCommentActivity extends BaseActivity implements UserCommentContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rvUserComment)
    RecyclerView rvUserComment;
    @BindView(R.id.mPtrFrameLayout_user_comment)
    PtrClassicFrameLayout mPtrFrameLayout;
    private final static String PrescriptionUserCommentRecord = "PrescriptionUserCommentRecord";
    private final static String PrescriptionId = "PrescriptionId";
    private PrescriptionUserCommentRecord prescriptionUserCommentRecord;
    private String prescriptionId;
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private UserCommentRecyclerAdapter mAdapter;
    private UserCommentPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_user_comment);
        ButterKnife.bind(this);
        setTitle("用户评论");
        mPresenter = new UserCommentPresenter(this, this);
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            prescriptionUserCommentRecord = (PrescriptionUserCommentRecord) bundle.getSerializable(PrescriptionUserCommentRecord);
            prescriptionId = bundle.getString(PrescriptionId);
            if (EmptyUtils.isNotEmpty(prescriptionUserCommentRecord)) {
                initRv();
            }
        }
    }

    private void initRv() {
        rvUserComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserCommentRecyclerAdapter(this);
        rvUserComment.setAdapter(mAdapter);
        getUserCommentSuccess(prescriptionUserCommentRecord);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rvUserComment, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                getUserCommentData(false);
            }
        });
        mAdapter.setOnLoadMoreListener(this);
    }

    public void getUserCommentData(boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getUserPrescriptionCommentList(page, pageSize, prescriptionId);
    }

    @OnClick(R.id.userCommentCommit)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.userCommentCommit:
                RouterActivityUtil.startActivity(UserCommentActivity.this, UserCommentEditActivity.class);
                break;
        }
    }

    @Override
    public void getUserCommentSuccess(PrescriptionUserCommentRecord record) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = record.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(record.getPrescriptionCommentList(), true);
        } else {
            mAdapter.setNewData(record.getPrescriptionCommentList());
        }
        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void getUserCommentFail(PrescriptionUserCommentRecord list) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getUserCommentData(true);
    }
}
