package com.bowen.tcm.main.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Message;
import com.bowen.tcm.common.bean.network.MessageInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.main.adapter.MessageAdapter;
import com.bowen.tcm.main.contract.MessageContract;
import com.bowen.tcm.main.presenter.MessagePresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/3.
 */

public class MessageActivity extends BaseActivity implements MessageContract.View,BaseQuickAdapter.RequestLoadMoreListener,
        MessageAdapter.UpdateMessageStatusListener{


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private MessagePresenter mPresenter;
    private MessageAdapter mAdapter;

    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);
        setTitle("消息中心");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("清空");


        mPresenter = new MessagePresenter(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MessageAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

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
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setmUpdateMessageStatusListener(this);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        clearMessageList();
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
        mPresenter.getMessageList(page, 10);
    }

    @Override
    public void onUpdateMessageStatus(String msgId, int status) {
        mPresenter.changeMsgStatus(msgId,status+"");
    }


    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onGetMessageListSuccess(MessageInfo messageInfo) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = messageInfo.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(messageInfo.getMsgList(), true);
        } else {
            mAdapter.setNewData(messageInfo.getMsgList());
        }
        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }

        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
            TextView textView = (TextView)emptyView.findViewById(R.id.textView);
            textView.setText("暂无消息");
            mAdapter.addFooterView(emptyView);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    private void clearMessageList(){
        AffirmDialog affirmDialog = new AffirmDialog(this);
        affirmDialog.setmContentStr("确定要清空消息中心？");
        affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
            @Override
            public void onCancle() {

            }

            @Override
            public void onOK() {
                StringBuilder builder = new StringBuilder();
                for(Message item:(ArrayList<Message>)mAdapter.getData()){
                    builder.append(item.getMsgId()).append(",");
                }
                mPresenter.clearMessage(builder.toString());
            }
        });
        affirmDialog.show();
    }

    @Override
    public void onGetMessageListFailed() {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onChangeMessageStatusSuccess() {
        getData(false);
    }

    @Override
    public void onClearMessageSuccess() {
        getData(false);
    }
}
