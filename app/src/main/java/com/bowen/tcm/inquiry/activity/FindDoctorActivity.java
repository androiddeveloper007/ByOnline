package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.bowen.commonlib.base.BasePopWindow.BasePopWindowListener;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.tcm.common.util.LocationUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.dialog.ChooseAddressPopWindow;
import com.bowen.tcm.common.dialog.ChooseDepartmentPopwindow;
import com.bowen.tcm.common.dialog.ChooseDoctorRankPopwindow;
import com.bowen.tcm.common.dialog.ChooseSortPopwindow;
import com.bowen.tcm.common.event.DoctorSearchEvent;
import com.bowen.tcm.inquiry.adapter.FindDoctorAdapter;
import com.bowen.tcm.inquiry.contract.FindDoctorContract;
import com.bowen.tcm.inquiry.presenter.FindDoctorPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:找中医
 * Created by AwenZeng on 2018/7/2.
 */
public class FindDoctorActivity extends BaseActivity implements FindDoctorContract.View,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.mAddressImg)
    ImageView mAddressImg;
    @BindView(R.id.mAddressLayout)
    LinearLayout mAddressLayout;
    @BindView(R.id.mDepartmentImg)
    ImageView mDepartmentImg;
    @BindView(R.id.mDepartmentLayout)
    LinearLayout mDepartmentLayout;
    @BindView(R.id.mRankImg)
    ImageView mRankImg;
    @BindView(R.id.mRankLayout)
    LinearLayout mRankLayout;
    @BindView(R.id.mSortImg)
    ImageView mSortImg;
    @BindView(R.id.mSortLayout)
    LinearLayout mSortLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.mAddressTv)
    TextView mAddressTv;
    @BindView(R.id.mDepartmentTv)
    TextView mDepartmentTv;
    @BindView(R.id.mRankTv)
    TextView mRankTv;
    @BindView(R.id.mSortTv)
    TextView mSortTv;

    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    private FindDoctorAdapter mAdapter;
    private FindDoctorPresenter mPresenter;
    private SearchField mSearchField;
    private ChooseAddressPopWindow chooseAddressPopWindow;
    private ChooseDoctorRankPopwindow chooseDoctorRankPopwindow;
    private ChooseDepartmentPopwindow chooseDepartmentPopwindow;
    private ChooseSortPopwindow chooseSortPopwindow;
    private String prescriptionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_find_doctor);
        ButterKnife.bind(this);
        setTitle("找中医");
        getTitleBar().getRightButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightButton().setBackgroundResource(R.drawable.search_icon);
        prescriptionId = RouterActivityUtil.getString(this);
        if(TextUtils.isEmpty(prescriptionId)){
            mSearchField = (SearchField) RouterActivityUtil.getSerializable(this);
        }
        init();
    }

    private void init(){
        mPresenter = new FindDoctorPresenter(this,this);
        if(EmptyUtils.isEmpty(mSearchField)){
            mSearchField = new SearchField();
        }
        mAdapter = new FindDoctorAdapter(this);
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
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG,mAdapter.getItem(position).getDoctorId());
                bundle.putInt(RouterActivityUtil.FROM_TAG1,mAdapter.getItem(position).getRecommended());
               RouterActivityUtil.startActivity(FindDoctorActivity.this,DoctorDetailActivity.class,bundle);
            }
        });
        if(TextUtils.isEmpty(prescriptionId)){
            startRefresh();
        }else{
            mPresenter.findDoctorListByPreId(page, 10, prescriptionId);
        }
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

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        Bundle bundle = new Bundle();
        bundle.putInt(RouterActivityUtil.FROM_TAG,DoctorSearchActivity.FROM_FIND_DOCTOR);
        RouterActivityUtil.startActivity(this,DoctorSearchActivity.class,bundle);
    }

    @OnClick({R.id.mAddressLayout, R.id.mDepartmentLayout, R.id.mRankLayout, R.id.mSortLayout})
    public void onViewClicked(View view) {
        mRecyclerView.smoothScrollToPosition(0);
        closeAllWindow();
        switch (view.getId()) {
            case R.id.mAddressLayout:
                mAddressTv.setSelected(true);
                mAddressImg.setSelected(true);
                if(EmptyUtils.isEmpty(chooseAddressPopWindow)){
                    chooseAddressPopWindow = new ChooseAddressPopWindow(this);
                }
                chooseAddressPopWindow.show(mAddressLayout);
                chooseAddressPopWindow.setBaseDialogListener(new BasePopWindowListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mSearchField.clearAll();
                        mSearchField.setProviceCode((String)obj[0]);
                        mSearchField.setCityCode((String)obj[1]);
                        mSearchField.setAreaCode((String)obj[2]);
                        mAddressTv.setSelected(false);
                        mAddressImg.setSelected(false);
                        startRefresh();
                    }
                });
                chooseAddressPopWindow.mPopWindow.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mAddressTv.setSelected(false);
                        mAddressImg.setSelected(false);
                    }
                });
                break;
            case R.id.mDepartmentLayout:
                mDepartmentTv.setSelected(true);
                mDepartmentImg.setSelected(true);
                if(EmptyUtils.isEmpty(chooseDepartmentPopwindow)){
                    chooseDepartmentPopwindow = new ChooseDepartmentPopwindow(this);
                }
                chooseDepartmentPopwindow.show(mDepartmentLayout);
                chooseDepartmentPopwindow.setBaseDialogListener(new BasePopWindowListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mSearchField.clearAll();
                        mSearchField.setDepartmentsId((String)obj[0]);
                        mDepartmentTv.setSelected(false);
                        mDepartmentImg.setSelected(false);
                        startRefresh();
                    }
                });
                chooseDepartmentPopwindow.mPopWindow.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mDepartmentTv.setSelected(false);
                        mDepartmentImg.setSelected(false);
                    }
                });
                break;
            case R.id.mRankLayout:
                mRankTv.setSelected(true);
                mRankImg.setSelected(true);
                if(EmptyUtils.isEmpty(chooseDoctorRankPopwindow)){
                    chooseDoctorRankPopwindow = new ChooseDoctorRankPopwindow(this);
                }
                chooseDoctorRankPopwindow.show(mRankLayout);
                chooseDoctorRankPopwindow.setBaseDialogListener(new BasePopWindowListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mSearchField.clearAll();
                        mSearchField.setPosition((String)obj[0]);
                        mRankTv.setSelected(false);
                        mRankImg.setSelected(false);
                        startRefresh();
                    }
                });
                chooseDoctorRankPopwindow.mPopWindow.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mRankTv.setSelected(false);
                        mRankImg.setSelected(false);
                    }
                });
                break;
            case R.id.mSortLayout:
                mSortTv.setSelected(true);
                mSortImg.setSelected(true);
                if(EmptyUtils.isEmpty(chooseSortPopwindow)){
                    chooseSortPopwindow = new ChooseSortPopwindow(this);
                }
                chooseSortPopwindow.show(mSortLayout);
                chooseSortPopwindow.setBaseDialogListener(new BasePopWindowListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mSearchField.clearAll();
                        mSearchField.setSortType((String)obj[0]);
                        mSortTv.setSelected(false);
                        mSortImg.setSelected(false);
                        startRefresh();
                    }
                });
                chooseSortPopwindow.mPopWindow.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mSortTv.setSelected(false);
                        mSortImg.setSelected(false);
                    }
                });
                break;
        }
    }

    private void closeAllWindow(){
        if(EmptyUtils.isNotEmpty(chooseAddressPopWindow)){
            chooseAddressPopWindow.disMissWindow();
            chooseAddressPopWindow = null;
        }
        if(EmptyUtils.isNotEmpty(chooseDepartmentPopwindow)){
            chooseDepartmentPopwindow.disMissWindow();
            chooseDepartmentPopwindow = null;
        }
        if(EmptyUtils.isNotEmpty(chooseDoctorRankPopwindow)){
            chooseDoctorRankPopwindow.disMissWindow();
            chooseDoctorRankPopwindow = null;
        }
        if(EmptyUtils.isNotEmpty(chooseSortPopwindow)){
            chooseSortPopwindow.disMissWindow();
            chooseSortPopwindow = null;
        }
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
        mPresenter.findDoctorList(page, 10, mSearchField);
    }

    @Override
    public void onGetDoctorListSuccess(DoctorInfo doctorInfo) {
        mPtrFrameLayout.refreshComplete();
        if(EmptyUtils.isEmpty(doctorInfo.getDoctorList())){
           doctorInfo.setDoctorList(new ArrayList<Doctor>());
        }
        Page pageBean = doctorInfo.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(doctorInfo.getDoctorList(), true);
        } else {
            mAdapter.setNewData(doctorInfo.getDoctorList());
        }

        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            mAdapter.addFooterView(null);
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
            View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
            TextView textView = emptyView.findViewById(R.id.textView);
            textView.setText("暂无符合医生");
            mAdapter.setEmptyView(emptyView);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onGetDoctorListFailed() {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DoctorSearchEvent event) {
        mSearchField.clearAll();
        mSearchField.setSearchInfo(event.getTypeString());
        startRefresh();
    }

}
