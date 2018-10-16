package com.bowen.tcm.mine.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.MyOrder;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.dialog.MyOrderTypeSelectPopwindow;
import com.bowen.tcm.common.event.CancelOrDeleteOrderSuccessEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.activity.ChatActivity;
import com.bowen.tcm.inquiry.activity.DoctorDetailActivity;
import com.bowen.tcm.inquiry.activity.PayDetaitActivity;
import com.bowen.tcm.mine.adapter.MyOrderRvAdapter;
import com.bowen.tcm.mine.contract.MyOrderContract;
import com.bowen.tcm.mine.presenter.MyOrderPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:我的订单
 * Created by zhuzhipeng on 2018/6/29.
 */

public class MyOrderActivity extends BaseActivity implements MyOrderContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.mPtrFrameLayout_my_order)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.myOrderRv)
    RecyclerView myOrderRv;
    private MyOrderRvAdapter mAdapter;
    private MyOrderPresenter mPresenter;
    private int page = 1;
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private View emptyView;
    private int orderStatus = 0;
    private MyOrderTypeSelectPopwindow popwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        setTitle("我的订单");
        getTitleBar().getRightButton().setBackgroundResource(R.drawable.more_white);
        getTitleBar().getRightButton().setVisibility(View.VISIBLE);
        initRv();
    }

    private void initRv() {
        mPresenter = new MyOrderPresenter(this, this);
        popwindow = new MyOrderTypeSelectPopwindow(this);
        mAdapter = new MyOrderRvAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myOrderRv.setLayoutManager(layoutManager);
        myOrderRv.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, myOrderRv, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG,  mAdapter.getItem(position).getOrderId());
                RouterActivityUtil.startActivity(MyOrderActivity.this, MyOrderDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnButton0Click(new MyOrderRvAdapter.button0Click() {
            @Override
            public void onButton0Click(int pos, int orderStatus) {
                Bundle bundle;
                switch (orderStatus) {
                    case Constants.PAY_ORDER_STATUS_WAIT://待支付
                        bundle = new Bundle();
                        MyOrder order = mAdapter.getItem(pos);
                        PayOrderInfo payOrderInfo = new PayOrderInfo();
                        payOrderInfo.setOrderType(order.getOrderType());
                        payOrderInfo.setAmount(order.getRealAmount());
                        payOrderInfo.setOrderId(order.getOrderId());
                        payOrderInfo.setPayChannel(order.getPayChannel());
                        Doctor doctorInfo = new Doctor();
                        doctorInfo.setDoctorId(order.getDoctorId());
                        doctorInfo.setName(order.getName());
                        doctorInfo.setHospitalDepartments(order.getHospitalDepartments());
                        doctorInfo.setPositionStr(order.getPositionStr());
                        doctorInfo.setHospital(order.getHostipal());
                        doctorInfo.setFileLink(order.getFileLink());
                        payOrderInfo.setEmrDoctor(doctorInfo);
                        bundle.putSerializable(RouterActivityUtil.FROM_TAG, payOrderInfo);
                        RouterActivityUtil.startActivity(MyOrderActivity.this, PayDetaitActivity.class, bundle);
                        break;
                    case Constants.PAY_ORDER_STATUS_FINISH://已支付
                        MyOrder item = mAdapter.getItem(pos);
                        PayOrderInfo payOrderInfo1 = new PayOrderInfo();
                        Doctor doctor = new Doctor();
                        doctor.setHeadImgUrl(item.getFileLink());
                        doctor.setDoctorId(item.getDoctorId());
                        doctor.setName(item.getName());
                        payOrderInfo1.setEmrDoctor(doctor);
                        payOrderInfo1.setOrderId(item.getOrderId());
                        bundle = new Bundle();
                        bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo1);
                        RouterActivityUtil.startActivity(MyOrderActivity.this, ChatActivity.class,bundle);
                        break;
                    case Constants.PAY_ORDER_STATUS_CANCEL://已取消
                        if (mAdapter.getData().size() > 0) {
                            mPresenter.cancelTraOrder(mAdapter.getItem(pos).getOrderId(), "5");
                        }
                        break;
                    case Constants.PAY_ORDER_STATUS_OVER://已结束
                        bundle = new Bundle();
                        bundle.putString(RouterActivityUtil.FROM_TAG, mAdapter.getItem(pos).getDoctorId());
                        RouterActivityUtil.startActivity(MyOrderActivity.this, DoctorDetailActivity.class,bundle);
                        break;
                    case Constants.PAY_ORDER_STATUS_COMMENT://已评价
                        bundle = new Bundle();
                        bundle.putString(RouterActivityUtil.FROM_TAG, mAdapter.getItem(pos).getDoctorId());
                        RouterActivityUtil.startActivity(MyOrderActivity.this, DoctorDetailActivity.class,bundle);
                        break;
                }
            }
        });
        mAdapter.setOnButton1Click(new MyOrderRvAdapter.button1Click() {
            @Override
            public void onButton1Click(int pos, int orderStatus) {
                switch (orderStatus) {
                    case Constants.PAY_ORDER_STATUS_WAIT://取消订单
                        mPresenter.cancelTraOrder(((MyOrder)mAdapter.getData().get(pos)).getOrderId(), "3");
                        break;
                    case Constants.PAY_ORDER_STATUS_OVER://服务评论
                        Bundle  bundle = new Bundle();
                        bundle.putString(RouterActivityUtil.FROM_TAG, ((MyOrder)mAdapter.getData().get(pos)).getOrderId());
                        RouterActivityUtil.startActivity(MyOrderActivity.this, ServiceEvaluateActivity.class, bundle);
                        break;
                }
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        startRefresh();
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
        mPresenter.loadData(page, pageSize, orderStatus);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        showEditWindow(getTitleBar().getRightButton());
    }

    private void showEditWindow(View v) {
        popwindow.setBaseDialogListener(new BasePopWindow.BasePopWindowListener() {
            @Override
            public void onDataCallBack(Object... obj) {
                String temp = (String) obj[0];/*0:全部， 1：待支付，2已支付，3：已取消，4：已结束，6：已评价*/
                if (temp.equals("全部")) {
                    orderStatus = Constants.PAY_ORDER_STATUS_ALL;
                } else if (temp.equals("待支付")) {
                    orderStatus = Constants.PAY_ORDER_STATUS_WAIT;
                } else if (temp.equals("已支付")) {
                    orderStatus = Constants.PAY_ORDER_STATUS_FINISH;
                } else if (temp.equals("已取消")) {
                    orderStatus = Constants.PAY_ORDER_STATUS_CANCEL;
                } else if (temp.equals("已结束")) {
                    orderStatus = Constants.PAY_ORDER_STATUS_OVER;
                } else if (temp.equals("已评价")) {
                    orderStatus = Constants.PAY_ORDER_STATUS_COMMENT;
                }
                startRefresh();
            }
        });
        popwindow.show(v);
    }

    @Override
    public void onLoadSuccess(MyOrderRecord record) {
        mPtrFrameLayout.refreshComplete();
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        if (record.getTraOrderList() != null && record.getTraOrderList().size() > 0) {
            Page pageBean = record.getPage();
            if (isMore) {
                mAdapter.notifyDataChangedAfterLoadMore(record.getTraOrderList(), true);
            } else {
                mAdapter.setNewData(record.getTraOrderList());
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
        } else {
            mAdapter.setNewData(record.getTraOrderList());
            if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                mAdapter.addFooterView(null);
            }
        }
    }

    @Override
    public void onLoadFail(MyOrderRecord list) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void cancelOrderSucc() {
        startRefresh();
    }

    @Override
    public void cancelOrderFail() {

    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CancelOrDeleteOrderSuccessEvent event) {
        startRefresh();
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