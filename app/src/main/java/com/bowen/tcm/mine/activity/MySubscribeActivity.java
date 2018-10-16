package com.bowen.tcm.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorFanRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.inquiry.activity.ChatActivity;
import com.bowen.tcm.inquiry.activity.DoctorDetailActivity;
import com.bowen.tcm.inquiry.activity.FamousDoctorDetailActivity;
import com.bowen.tcm.inquiry.activity.HospitalDetailActivity;
import com.bowen.tcm.inquiry.activity.ImgTextCosultDetailActivity;
import com.bowen.tcm.mine.adapter.MySubscribeRvAdapter;
import com.bowen.tcm.mine.contract.MySubscribeContract;
import com.bowen.tcm.mine.presenter.MySubscribePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author zhuzhipeng
 * @time 2018/7/9 9:54
 * 我的关注
 */
public class MySubscribeActivity extends BaseActivity implements MySubscribeContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mPtrFrameLayoutMySubscribe)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.mySubscribeRv)
    RecyclerView mySubscribeRv;
    private MySubscribeRvAdapter mAdapter;
    private MySubscribePresenter mPresenter;
    private int page = 1;
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private View emptyView;
    private Activity mActivity;
    private DoctorFanRecord mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_my_subscribe);
        ButterKnife.bind(this);
        mActivity = this;
        setTitle("我的关注");
        mPresenter = new MySubscribePresenter(this, this);
        initRv();
    }

    private void initRv() {
        mAdapter = new MySubscribeRvAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mySubscribeRv.setLayoutManager(layoutManager);
        mySubscribeRv.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mySubscribeRv, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG, mAdapter.getItem(position).getDoctorId());
                bundle.putInt(RouterActivityUtil.FROM_TAG1, mAdapter.getItem(position).getRecommended());
                RouterActivityUtil.startActivity(mActivity, DoctorDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnSubscribeCancelListener(new MySubscribeRvAdapter.MySubscribeCancelClick() {
            @Override
            public void mySubscribeCancel(final int pos) {
                if(mRecord.getEmrDoctorFanList().size()>0){
                    final AffirmDialog dialog = new AffirmDialog(mActivity, "提醒",
                            "确定要取消关注吗?", "取消", "确定");
                    dialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                        @Override
                        public void onCancle() {
                        }
                        @Override
                        public void onOK() {
                            dialog.dismiss();
                            mPresenter.userFenDoctor(mRecord.getEmrDoctorFanList().get(pos).getDoctorId(), "false");
                        }
                    });
                    dialog.show();
                }
            }
        });
        mAdapter.setOnSubscribeConsultListener(new MySubscribeRvAdapter.MySubscribeConsultClick() {
            @Override
            public void mySubscribeConsult(int pos) {
                mPresenter.checkImmediateConsult(mAdapter.getItem(pos).getDoctorId());
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
        mAdapter.setEmptyView(emptyView);
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
        mPresenter.loadData(page, pageSize);
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onLoadSuccess(DoctorFanRecord record) {
        mPtrFrameLayout.refreshComplete();
        mRecord = record;
        if (record.getEmrDoctorFanList() != null && record.getEmrDoctorFanList().size() > 0) {
            Page pageBean = record.getPage();
            if (isMore) {
                mAdapter.notifyDataChangedAfterLoadMore(record.getEmrDoctorFanList(), true);
            } else {
                if (record.getEmrDoctorFanList() != null) {
                    mAdapter.setNewData(record.getEmrDoctorFanList());
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
        } else {
            mAdapter.setNewData(record.getEmrDoctorFanList());
        }
    }

    @Override
    public void onLoadFail(DoctorFanRecord list) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void userFenDoctorSuccess() {
        startRefresh();
    }

    @Override
    public void onCheckImmediateConsultSuccess(Doctor doctor) {
        if(!doctor.isAsk()){
            if(doctor.isShowConsult()){
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, doctor);
                RouterActivityUtil.startActivity(MySubscribeActivity.this, ImgTextCosultDetailActivity.class,bundle);
            }else{
                ToastUtil.getInstance().showToastDialog("该医生暂不提供图文咨询服务");
            }
        }else{
            Bundle bundle = new Bundle();
            PayOrderInfo payOrderInfo = new PayOrderInfo();
            payOrderInfo.setEmrDoctor(doctor);
            payOrderInfo.setOrderId(doctor.getOrderId());
            bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo);
            RouterActivityUtil.startActivity(MySubscribeActivity.this, ChatActivity.class,bundle);
        }
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
