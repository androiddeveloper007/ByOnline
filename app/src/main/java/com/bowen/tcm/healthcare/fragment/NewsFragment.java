package com.bowen.tcm.healthcare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseFragment;
import com.bowen.tcm.common.bean.network.NewsInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.ChangeColumnIdEvent;
import com.bowen.tcm.common.widget.FixConflictRecyclerView;
import com.bowen.tcm.common.widget.ScrollRecyclerView;
import com.bowen.tcm.healthcare.adapter.NewsAdapter;
import com.bowen.tcm.healthcare.contract.NewsFragmentContract;
import com.bowen.tcm.healthcare.presenter.NewsFragmentPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.SlideInLeftAnimation;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe: 新闻展示
 * Created by AwenZeng on 2018/5/10.
 */

public class NewsFragment extends BaseFragment implements NewsFragmentContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    FixConflictRecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private String columnId;
    private NewsAdapter mAdapter;
    private NewsFragmentPresenter mPresenter;

    private int recommendPageNo = 1;//页码
    private boolean isRecommendMore = false;
    private boolean isRecommendLoadMore = false;

    private int columnPageNo = 1;//页码
    private boolean isColumnMore = false;
    private boolean isColumnLoadMore = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_show_news, null);
        ButterKnife.bind(this, mView);

        mPresenter = new NewsFragmentPresenter(mActivity, this);
        mAdapter = new NewsAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (EmptyUtils.isNotEmpty(columnId)) {
                    if (columnId.contains("recommend")) {
                        getRecommendNewsData(false);
                    } else {
                        getColumnNewsData(false);
                    }
                }
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        autoRefresh();
    }

    private void autoRefresh(){
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 100);
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void goBackTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void getRecommendNewsData(final boolean isLoad) {
        isRecommendMore = isLoad;
        if (isRecommendMore && isRecommendLoadMore) {
            return;
        }
        if (isRecommendMore) {
            isRecommendLoadMore = true;
        } else {
            recommendPageNo = 1;
        }
        mPresenter.onGetRecommendNewsList(recommendPageNo, 20);
    }

    private void getColumnNewsData(final boolean isLoad) {
        isColumnMore = isLoad;
        if (isColumnMore && isColumnLoadMore) {
            return;
        }
        if (isColumnMore) {
            isColumnLoadMore = true;
        } else {
            columnPageNo = 1;
        }
        mPresenter.onGetColumnNewsList(recommendPageNo, 20, columnId);
    }

    @Override
    public void onLoadMoreRequested() {
        if (columnId.contains("recommend")) {
            getRecommendNewsData(true);
        } else {
            getColumnNewsData(true);
        }
    }


    @Override
    public void onGetRecommendNewsListSuccess(NewsInfo newsInfo) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = newsInfo.getPage();
        if (isRecommendMore) {
            mAdapter.addData(mAdapter.getData().size(), newsInfo.getNewsList());
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setNewData(newsInfo.getNewsList());
        }

        if (pageBean.getTotalCount() != 0 && pageBean.getPageNo() == pageBean.getTotalPages()) {
            mAdapter.loadMoreEnd();
        }
        recommendPageNo = pageBean.getNextPage();
        isRecommendLoadMore = false;
    }

    @Override
    public void onGetColumnNewsListSuccess(NewsInfo newsInfo) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = newsInfo.getPage();
        if (isColumnLoadMore) {
            mAdapter.addData(mAdapter.getData().size(), newsInfo.getNewsList());
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setNewData(newsInfo.getNewsList());
        }

        if (pageBean.getTotalCount() != 0 && pageBean.getPageNo() == pageBean.getTotalPages()) {
            mAdapter.loadMoreEnd();
        }
        columnPageNo = pageBean.getNextPage();
        isColumnLoadMore = false;
    }


    @Override
    public void onGetRecommendNewsListFailed() {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onGetColumnNewsListFailed() {
        mPtrFrameLayout.refreshComplete();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeColumnIdEvent event) {
        if(columnId.equals(event.getTypeString())){
            autoRefresh();
        }
    }
}
