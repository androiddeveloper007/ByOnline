package com.bowen.tcm.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.ConsultListItem;
import com.bowen.tcm.common.bean.network.ConsultListRecord;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.widget.SwipeDividerDecoration;
import com.bowen.tcm.inquiry.activity.ChatActivity;
import com.bowen.tcm.mine.adapter.MyConsultRvAdapter;
import com.bowen.tcm.mine.contract.MyConsultContract;
import com.bowen.tcm.mine.presenter.MyConsultPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author zhuzhipeng
 * @time 2018/7/9 9:54
 * 我的咨询
 */
public class MyConsultActivity extends BaseActivity implements MyConsultContract.View, BaseQuickAdapter.RequestLoadMoreListener ,
        MyConsultRvAdapter.DeleteListener{

    @BindView(R.id.mPtrFrameLayoutMyConsult)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.myConsultRv)
    RecyclerView myConsultRv;
    private MyConsultRvAdapter mAdapter;
    private MyConsultPresenter mPresenter;
    private int page = 1;
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private View emptyView;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_my_consult);
        ButterKnife.bind(this);
        mActivity = this;
        setTitle("我的咨询");
        mPresenter = new MyConsultPresenter(this,this);
        initRv();
    }

    private void initRv() {
        mAdapter = new MyConsultRvAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myConsultRv.setLayoutManager(layoutManager);
        myConsultRv.addItemDecoration(new SwipeDividerDecoration());
        myConsultRv.setAdapter(mAdapter);
        mAdapter.setmDeleteListen(this);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, myConsultRv, header);
            }
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setItemOnClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ConsultListItem item = mAdapter.getItem(position);
                PayOrderInfo payOrderInfo = new PayOrderInfo();
                Doctor doctor = new Doctor();
                doctor.setHeadImgUrl(item.getHeadImgUrl());
                doctor.setDoctorId(item.getDoctorId());
                doctor.setName(item.getName());
                payOrderInfo.setEmrDoctor(doctor);
                payOrderInfo.setOrderId(item.getOrderId());
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo);
                RouterActivityUtil.startActivity(mActivity, ChatActivity.class,bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        startRefresh();
    }

    private void startRefresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 100);
    }


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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onLoadSuccess(ConsultListRecord record) {
        mPtrFrameLayout.refreshComplete();
        if(emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription,
                    (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        Page pageBean = record.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(record.getConsultList(), true);
        } else {
            if(record!=null) {
                mAdapter.setNewData(record.getConsultList());
            }
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
    public void onLoadFailed() {
        mPtrFrameLayout.refreshComplete();
        if(emptyView==null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup)
                    findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
    }

    @Override
    public void onDeleteListener(ConsultListItem item) {
         mPresenter.removeConsultInfo(item.getDoctorId());
    }

    @Override
    public void onRemoveConsultInfo() {
        getData(false);
    }
}
