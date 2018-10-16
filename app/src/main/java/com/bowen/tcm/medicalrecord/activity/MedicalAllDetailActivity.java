package com.bowen.tcm.medicalrecord.activity;


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
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.medicalrecord.adapter.MedicalAllDetailAdapter;
import com.bowen.tcm.medicalrecord.contract.MedicalCourseContract;
import com.bowen.tcm.medicalrecord.presenter.MedicalCoursePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */

public class MedicalAllDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, MedicalCourseContract.View {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private TextView mDiseaseNameTv;
    private String courseId;
    private String mDiseaseName;
    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    private MedicalAllDetailAdapter mAdapter;
    private MedicalCoursePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_medical_all_detail);
        ButterKnife.bind(this);
        setTitle("病情总览");

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            courseId = bundle.getString(RouterActivityUtil.FROM_TAG);
            mDiseaseName = bundle.getString(RouterActivityUtil.FROM_TAG1);
        }


        View headView = getLayoutInflater().inflate(R.layout.headview_medical_all_detail, null);
        mDiseaseNameTv =  headView.findViewById(R.id.mMedicalNameTv);
        mDiseaseNameTv.setText(mDiseaseName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MedicalAllDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(headView);

        mPresenter = new MedicalCoursePresenter(this,this);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
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
        mPresenter.getMedicalCourseList(page, 10, courseId);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
    }


    @Override
    public void onGetMedicalCourseSuccess(HomeMedicalRecord homeMedicalRecord) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = homeMedicalRecord.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(homeMedicalRecord.getEmrCaseInfoList(), true);
        } else {
            mAdapter.setNewData(homeMedicalRecord.getEmrCaseInfoList());
        }
        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
            mAdapter.notifyDataSetChanged();
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onGetMedicalCourseFail(HomeMedicalRecord homeMedicalRecord) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
      getData(true);
    }
}
