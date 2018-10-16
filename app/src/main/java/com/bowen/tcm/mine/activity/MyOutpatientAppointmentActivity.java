package com.bowen.tcm.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.mine.adapter.OutpatientAppointmentRvAdapter;
import com.bowen.tcm.mine.contract.OutpatientAppointmentContract;
import com.bowen.tcm.mine.presenter.OutpatientAppointmentPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author zhuzhipeng
 * @time 2018/7/9 9:54
 * 门诊预约
 */
public class MyOutpatientAppointmentActivity extends BaseActivity implements OutpatientAppointmentContract.View, BaseQuickAdapter.RequestLoadMoreListener  {

    @BindView(R.id.mPtrFrameLayout_outpatient_appointment)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.outpatientAppointmentRv)
    RecyclerView outpatientAppointmentRv;
    private OutpatientAppointmentRvAdapter mAdapter;
    private OutpatientAppointmentPresenter mPresenter;
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
        setContentView(R.layout.activity_outpatient_appointment);
        ButterKnife.bind(this);
        mActivity = this;
        setTitle("门诊预约");
        mPresenter = new OutpatientAppointmentPresenter(this,this);
        initRv();
        startRefresh();
    }

    private void initRv() {
        mAdapter = new OutpatientAppointmentRvAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        outpatientAppointmentRv.setLayoutManager(layoutManager);
        outpatientAppointmentRv.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, outpatientAppointmentRv, header);
            }
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, mAdapter.getItem(position).getOrderId());//传orderId
                RouterActivityUtil.startActivity(mActivity, OutpatientAppointmentDetailActivity.class, bundle);//调到预约详情界面
            }
        });
        mAdapter.setOnLoadMoreListener(this);
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

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onLoadSuccess(OutpatientAppointRecord record) {
        mPtrFrameLayout.refreshComplete();
        //第一次时，加载筛选条件列表
        if(emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        Page pageBean = record.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(record.getAppointmentList(), true);
        } else {
            if(record.getAppointmentList()!=null) {
                mAdapter.setNewData(record.getAppointmentList());
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
    public void onLoadFail(OutpatientAppointRecord list) {
        //添加空数据时的视图
        if(emptyView==null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        mPtrFrameLayout.refreshComplete();
    }

    private void startRefresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 50);
    }
}
