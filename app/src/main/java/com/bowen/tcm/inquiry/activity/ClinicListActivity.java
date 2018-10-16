package com.bowen.tcm.inquiry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BasePopWindow.BasePopWindowListener;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.dialog.ChooseAddressPopWindow;
import com.bowen.tcm.inquiry.adapter.ClinicListAdapter;
import com.bowen.tcm.inquiry.contract.ClinicListContract;
import com.bowen.tcm.inquiry.presenter.ClinicListPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class ClinicListActivity extends BaseActivity implements ClinicListContract.View,BaseQuickAdapter.RequestLoadMoreListener{
    @BindView(R.id.mCurrentPositionTv)
    TextView mCurrentPositionTv;
    @BindView(R.id.mChangeCityTv)
    TextView mChangeCityTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    private ChooseAddressPopWindow chooseAddressPopWindow;
    private FindClinicParam mFindClinicParam;
    private ClinicListPresenter mPresenter;
    private ClinicListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_clinic_list);
        ButterKnife.bind(this);
        setTitle("中医馆");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("地图模式");
        init();

    }

    private void init(){
        mPresenter = new ClinicListPresenter(this,this);
        mFindClinicParam = new FindClinicParam();
        mAdapter = new ClinicListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });

        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mAdapter.getItem(position).getHospitalSource() == Constants.TYPE_CLINIC_BOWEN){
                    Intent intent = new Intent(ClinicListActivity.this, HospitalDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("hospitalBean", mAdapter.getItem(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString(RouterActivityUtil.FROM_TAG,mAdapter.getItem(position).getHospitalId());
                    RouterActivityUtil.startActivity(ClinicListActivity.this,ClinicMapDetailActivity.class,bundle);
                }
            }
        });

        LocationEvent event = AppConfigInfo.getInstance().getLocationEvent();
        mCurrentPositionTv.setText("当前位置："+event.getProvince()+event.getCity());
        mFindClinicParam.setAreaCode(event.getAreaCode());
        mFindClinicParam.setLatitude(event.getLatitude()+"");
        mFindClinicParam.setLongitude(event.getLongitude()+"");
        startRefresh();
    }

    /**
     * 开始刷新
     */
    private void startRefresh(){
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
        mPresenter.getAllClinicListOrMap(page, 10, mFindClinicParam);
    }


    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        RouterActivityUtil.startActivity(this,ClinicMapActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
         getData(true);
    }

    @Override
    public void onGetAllClinicListOrMapSuccess(CliniclInfo info) {
        mPtrFrameLayout.refreshComplete();
        if(EmptyUtils.isEmpty(info.getMapHospitalList())){
            info.setHospitalList(new ArrayList<Clinic>());
        }
        Page pageBean = info.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(info.getMapHospitalList(), true);
        } else {
            mAdapter.setNewData(info.getMapHospitalList());
        }

        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
            TextView textView = (TextView)emptyView.findViewById(R.id.textView);
            textView.setText("暂无医馆");
            mAdapter.addFooterView(emptyView);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onGetAllClinicListOrMapFailed() {
        mPtrFrameLayout.refreshComplete();
    }



    @OnClick(R.id.mChangeCityTv)
    public void onViewClicked(View view) {
        if(EmptyUtils.isNotEmpty(chooseAddressPopWindow)){
            chooseAddressPopWindow.disMissWindow();
            chooseAddressPopWindow = null;
        }
        chooseAddressPopWindow = new ChooseAddressPopWindow(this);
        chooseAddressPopWindow.show(mChangeCityTv);
        chooseAddressPopWindow.setBaseDialogListener(new BasePopWindowListener() {
            @Override
            public void onDataCallBack(Object... obj) {
                mCurrentPositionTv.setText("当前位置："+chooseAddressPopWindow.getSelectedPositionStr());
                mFindClinicParam.setProvinceCode((String) obj[0]);
                mFindClinicParam.setCityCode((String) obj[1]);
                mFindClinicParam.setAreaCode((String) obj[2]);
                startRefresh();
            }
        });
    }
}
