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
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.FolkPrescriptionCollectEvent;
import com.bowen.tcm.folkprescription.activity.FolkPrescriptionDetailActivity;
import com.bowen.tcm.mine.adapter.CollectedPrescriptionRvAdapter;
import com.bowen.tcm.mine.contract.CollectPrescriptionContract;
import com.bowen.tcm.mine.presenter.CollectPrescriptionPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:我的偏方：“收藏偏方”
 * Created by zhuzhipeng on 2018/6/29.
 */

public class CollectedPrescriptionPageFragment extends BaseFragment implements CollectPrescriptionContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.collectedPrescriptionRv)
    RecyclerView collectedPrescriptionRv;
    @BindView(R.id.mPtrFrameLayout_collect_prescription)
    PtrClassicFrameLayout mPtrFrameLayout;
    private CollectedPrescriptionRvAdapter mAdapter;
    private CollectPrescriptionPresenter mPresenter;
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private static final String CLICK_POSITION = "clickPosition";
    private View emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_collected_prescription, null);
        ButterKnife.bind(this, mView);
        mPresenter = new CollectPrescriptionPresenter(mActivity, this);
        initView();
    }

    private void initView() {
        mAdapter = new CollectedPrescriptionRvAdapter(mActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        collectedPrescriptionRv.setLayoutManager(layoutManager);
        collectedPrescriptionRv.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, collectedPrescriptionRv, header);
            }
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, mAdapter.getItem(position));
                bundle.putInt(CLICK_POSITION, position);
                RouterActivityUtil.startActivity(mActivity, FolkPrescriptionDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        //定义收藏或取消收藏的点击事件
        mAdapter.setOnCollectClickListener(new CollectedPrescriptionRvAdapter.collectClickListener() {
            @Override
            public void collectClick(String isCollect, String prescriptionId,int position) {
                mPresenter.folkPrescriptionCollectOrNot(false, prescriptionId,position);//根据点击偏方当前收藏状态传入bool值
            }
        });
        getData(false);
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
    public void onLoadSuccess(FolkPrescriptionRecord record) {
        mPtrFrameLayout.refreshComplete();
        //第一次时，加载筛选条件列表
        if(emptyView == null) {
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
        //添加空数据时的视图
        if(emptyView==null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void setCollectSuccess(int position) {
//        mAdapter.notifyItemRemoved(position);
        mAdapter.removeDataAndNotify(position);
    }

    @Override
    public void setCollectFail() {

    }

    /*加载下一页执行请求处*/
    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolkPrescriptionCollectEvent event) {
        mAdapter.removeDataAndNotify(event.getPosition());
    }
}