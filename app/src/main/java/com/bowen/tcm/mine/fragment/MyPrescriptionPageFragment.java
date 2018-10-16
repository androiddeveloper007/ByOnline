package com.bowen.tcm.mine.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseFragment;
import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.UploadPrescriptionSuccessEvent;
import com.bowen.tcm.folkprescription.activity.AddFolkPrescriptionActivity;
import com.bowen.tcm.folkprescription.activity.FolkPrescriptionDetailActivity;
import com.bowen.tcm.mine.adapter.MyPrescriptionRvAdapter;
import com.bowen.tcm.mine.contract.MyPrescriptionContract;
import com.bowen.tcm.mine.presenter.MyPrescriptionPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:我的偏方：“我的偏方”
 * Created by zhuzhipeng on 2018/6/29.
 */

public class MyPrescriptionPageFragment extends BaseFragment implements MyPrescriptionContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.myPrescriptionRv)
    RecyclerView myPrescriptionRv;
    @BindView(R.id.mPtrFrameLayout_my_prescription)
    PtrClassicFrameLayout mPtrFrameLayout;
    Unbinder unbinder;
    private MyPrescriptionRvAdapter mAdapter;
    private MyPrescriptionPresenter mPresenter;
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private View emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_my_prescription, null);
        unbinder = ButterKnife.bind(this, mView);
        mPresenter = new MyPrescriptionPresenter(mActivity, this);
        initView();
    }

    private void initView() {
        mAdapter = new MyPrescriptionRvAdapter(mActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        myPrescriptionRv.setLayoutManager(layoutManager);
        myPrescriptionRv.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, myPrescriptionRv, header);
            }
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                FolkPrescription folkBean = mAdapter.getItem(position);
                folkBean.setFromMine(true);
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, folkBean);
                RouterActivityUtil.startActivity(mActivity, FolkPrescriptionDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        getData(false);
    }

    @OnClick({R.id.createPrescription})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.createPrescription:
                Bundle bundle = new Bundle();
                bundle.putBoolean(RouterActivityUtil.FROM_TAG, true);
                RouterActivityUtil.startActivity(mActivity,AddFolkPrescriptionActivity.class, bundle);
                break;
        }
    }

    /***
     * 加载列表
     * @param isLoad
     */
    public void getData(boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.loadData(page, pageSize);
    }

    private void updateUI() {

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onLoadSuccess(FolkPrescriptionRecord record) {
        mPtrFrameLayout.refreshComplete();
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        Page pageBean = record.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(record.getFolkPrescriptionList(), true);
        } else {
            mAdapter.setNewData(record.getFolkPrescriptionList());
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
    public void onLoadFail(FolkPrescriptionRecord list) {
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        mPtrFrameLayout.refreshComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UploadPrescriptionSuccessEvent event) {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 30);//刷新列表收藏状态
    }
}