package com.bowen.tcm.folkprescription.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.DoctorComment;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.folkprescription.adapter.DoctorCommentRecyclerAdapter;
import com.bowen.tcm.folkprescription.contract.DoctorCommentContract;
import com.bowen.tcm.folkprescription.presenter.DoctorCommentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:专家点评
 * Created by zzp on 2018/6/22.
 */

public class DoctorCommentActivity extends BaseActivity implements DoctorCommentContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rvDoctorComment)
    RecyclerView rvDoctorComment;
    @BindView(R.id.mPtrFrameLayout_doctor_comment)
    PtrClassicFrameLayout mPtrFrameLayout;
    private PrescriptionDoctorCommentRecord prescriptionDoctorCommentRecord;
    private DoctorCommentPresenter mPresenter;
    private final static String PrescriptionDoctorCommentRecord = "PrescriptionDoctorCommentRecord";
    private final static String PrescriptionId = "PrescriptionId";
    private String prescriptionId;
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private DoctorCommentRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_doctor_comment);
        ButterKnife.bind(this);
        Bundle bundle = RouterActivityUtil.getBundle(this);
        mPresenter = new DoctorCommentPresenter(this, this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            prescriptionDoctorCommentRecord = (PrescriptionDoctorCommentRecord) bundle.getSerializable(PrescriptionDoctorCommentRecord);
            prescriptionId = bundle.getString(PrescriptionId);
            if (EmptyUtils.isNotEmpty(prescriptionDoctorCommentRecord)) {
                initRv();
            }
        }
    }

    private void initRv() {
        rvDoctorComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DoctorCommentRecyclerAdapter(this);
        rvDoctorComment.setAdapter(mAdapter);
        getDoctorCommentSuccess(prescriptionDoctorCommentRecord);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rvDoctorComment, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                getDoctorCommentData(false);
            }
        });
        mAdapter.setOnLoadMoreListener(this);
    }

    public void getDoctorCommentData(boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getDoctorPrescriptionCommentList(page, pageSize, prescriptionId);
    }

    @Override
    public void getDoctorCommentSuccess(PrescriptionDoctorCommentRecord record) {
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
    public void getDoctorCommentFail(PrescriptionDoctorCommentRecord list) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getDoctorCommentData(true);
    }
}