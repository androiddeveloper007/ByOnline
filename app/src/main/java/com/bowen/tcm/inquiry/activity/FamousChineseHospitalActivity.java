package com.bowen.tcm.inquiry.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.dialog.ChooseAddressPopWindow;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.inquiry.adapter.FamousHospitalRvAdapter;
import com.bowen.tcm.inquiry.contract.FamousHospitalContract;
import com.bowen.tcm.inquiry.presenter.FamousHospitalPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:中医名馆
 * Created by zhuzhipeng on 2018/7/11.
 */
public class FamousChineseHospitalActivity extends BaseActivity implements FamousHospitalContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rvFamousHospital)
    RecyclerView rvFamousHospital;
    @BindView(R.id.mPtrFrameLayoutFamousHospital)
    PtrClassicFrameLayout mPtrFrameLayout;
    private FamousHospitalRvAdapter mAdapter;
    private FamousHospitalPresenter mPresenter;
    private Activity mActivity;
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private String provinceCode, cityCode, areaCode;
    private double longitude;
    private double latitude;
    private LocationEvent location;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_famous_chinese_hospital);
        ButterKnife.bind(this);
        mActivity = this;
        mPresenter = new FamousHospitalPresenter(this, this);
        if (AppConfigInfo.getInstance().getLocationEvent() != null) {
            location = AppConfigInfo.getInstance().getLocationEvent();
//            cityCode = location.getCityCode();
            areaCode = location.getAreaCode();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        initView();
        getData(false);
    }

    private void initView() {
        setTitle("中医名馆");
        mAdapter = new FamousHospitalRvAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFamousHospital.setLayoutManager(layoutManager);
        rvFamousHospital.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rvFamousHospital, header);
            }
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        mAdapter.setOnMyItemClickListener(new FamousHospitalRvAdapter.myItemClickListener() {
            @Override
            public void onItemClick(int position, ImageView imageView) {
                int realPosition = position - 1;
                Intent intent = new Intent(mActivity, HospitalDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hospitalBean", (Clinic) mAdapter.getData().get(realPosition));
                intent.putExtras(bundle);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.putExtra("position", realPosition);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(mActivity, imageView, "shareImageName");
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        final View headerView = getLayoutInflater().inflate(R.layout.header_famous_chinese_hospital, null);
        mAdapter.addHeaderView(headerView);
        emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
        final TextView chooseAreaText = headerView.findViewById(R.id.chooseAreaText);
        if (location != null) {
            chooseAreaText.setText(location.getProvince()+location.getCity()+location.getArea());
        }
        headerView.findViewById(R.id.chooseArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ChooseAddressPopWindow chooseAddressPopWindow;
                chooseAddressPopWindow = new ChooseAddressPopWindow(mActivity);
                chooseAddressPopWindow.show(headerView.findViewById(R.id.chooseArea));
                chooseAddressPopWindow.setBaseDialogListener(new BasePopWindow.BasePopWindowListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        provinceCode = ((String) obj[0]);
                        cityCode = ((String) obj[1]);
                        areaCode = ((String) obj[2]);
                        chooseAreaText.setText(chooseAddressPopWindow.getSelectedPositionStr());
                        startRefresh();
                    }
                });
            }
        });
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
        mPresenter.getAllHospitalPage(page, pageSize, provinceCode, cityCode, areaCode, longitude, latitude);
    }

    @Override
    public void onLoadSuccess(CliniclInfo record) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = record.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(record.getEmrHospitalList(), true);
        } else {
            mAdapter.setNewData(record.getEmrHospitalList());
        }
        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            mAdapter.addFooterView(null);
//            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
//            mAdapter.addFooterView(footView);
        }else if(record!=null &&record.getEmrHospitalList().size()==0 && mAdapter.getData().size()==0){
            mAdapter.addFooterView(emptyView);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onLoadFail(CliniclInfo info) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
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