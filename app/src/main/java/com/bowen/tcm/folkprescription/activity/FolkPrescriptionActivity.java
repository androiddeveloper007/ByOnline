package com.bowen.tcm.folkprescription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.FolkPrescriptionCollectEvent;
import com.bowen.tcm.common.event.UploadPrescriptionSuccessEvent;
import com.bowen.tcm.folkprescription.adapter.DrawerApplyCrowdRvAdapter;
import com.bowen.tcm.folkprescription.adapter.DrawerDepartmentExpRvAdapter;
import com.bowen.tcm.folkprescription.adapter.FolkPrescriptionRecyclerAdapter;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionContract;
import com.bowen.tcm.folkprescription.presenter.FolkPrescriptionPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:偏方
 * Created by zzp on 2018/5/22.
 *
 */

public class FolkPrescriptionActivity extends BaseActivity implements FolkPrescriptionContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_folk_prescription)
    RecyclerView rv_folk_prescription;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.rv_drawer_department)
    RecyclerView rv_drawer_department;
    @BindView(R.id.rv_drawer_symptom)
    RecyclerView rv_drawer_symptom;
    @BindView(R.id.ll_drawer_right)
    RelativeLayout ll_drawer_right;
    @BindView(R.id.mPtrFrameLayout_folk_prescription)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_check_all_grid_depart)
    TextView tv_check_all_grid_depart;
    @BindView(R.id.tv_check_all_grid_symptom)
    TextView tv_check_all_grid_symptom;
    @BindView(R.id.fabAddFolkPrescription)
    FloatingActionButton fabAddFolkPrescription;
    private ActionBarDrawerToggle mDrawerToggle;
    private FolkPrescriptionPresenter mPresenter;
    private FolkPrescriptionRecyclerAdapter mAdapter;
    private boolean hasRefreshData;//设置一个状态值，记录是否提交表单
    private DrawerDepartmentExpRvAdapter drawerDepartmentAdapter;
    private DrawerApplyCrowdRvAdapter drawerApplyCrowdAdapter;
    private boolean isFirstLoad = true;//是否第一次加载数据，在搜索内容时，不再加载侧滑中的列表
    private static final int REQUEST_CODE_SEARCH = 10001;
    private static final String CLICK_POSITION = "clickPosition";
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private View emptyView;
    private TextView et_search;//列表headerView的搜索框控件，全局方便在设置文字的时候使用
//    private List<ShowApplyCrowd> applyCrowdlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_folk_prescription);
        ButterKnife.bind(this);
        setTitle("找偏方");
        getTitleBar().getRightButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightButton().setBackgroundResource(R.drawable.search_right_btn_bg);
        mPresenter = new FolkPrescriptionPresenter(this, this);
        initDrawer();
        initRecyclerView();
        getData(false, "");

        //添加创建偏方按钮
        fabAddFolkPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mPresenter.hasNotLogin()){
                    RouterActivityUtil.startActivity(FolkPrescriptionActivity.this, AddFolkPrescriptionActivity.class);
                }
            }
        });
    }

    private void initRecyclerView() {
        View headerView = getLayoutInflater().inflate(R.layout.rv_header_folk_prescription, null);
        et_search = headerView.findViewById(R.id.et_search);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouterActivityUtil.startActivityResult(FolkPrescriptionActivity.this,FolkPrescriptionSearchActivity.class,REQUEST_CODE_SEARCH);
            }
        });
        mAdapter = new FolkPrescriptionRecyclerAdapter(this, mPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_folk_prescription.setLayoutManager(layoutManager);
        rv_folk_prescription.setAdapter(mAdapter);
        mAdapter.addHeaderView(headerView);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rv_folk_prescription, header);
            }
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false, "");
                if(et_search!=null) {
                    et_search.setText("按名称、症状、用量搜索");
                }
                //重置筛选状态
                if(drawerDepartmentAdapter !=null && drawerApplyCrowdAdapter!=null) {
                    drawerDepartmentAdapter.setItemSingleSelect(0);
                    drawerApplyCrowdAdapter.setItemSingleSelect(0);
                }
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, mAdapter.getItem(position));
                bundle.putInt(CLICK_POSITION, position);
                RouterActivityUtil.startActivity(FolkPrescriptionActivity.this, FolkPrescriptionDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(this);
        //定义收藏或取消收藏的点击事件
        mAdapter.setOnCollectClickListener(new FolkPrescriptionRecyclerAdapter.collectClickListener() {
            @Override
            public void collectClick(String isCollect, String prescriptionId) {
                mPresenter.folkPrescriptionCollectOrNot(TextUtils.equals("1", isCollect), prescriptionId);//根据点击偏方当前收藏状态传入bool值
            }
        });
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                fabAddFolkPrescription.setVisibility(View.GONE);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                fabAddFolkPrescription.setVisibility(View.VISIBLE);
                //如果已经选择了非全部的选项，侧滑界面关闭时，重置选中项为全部
                if(!hasRefreshData) {
                    drawerDepartmentAdapter.setItemSingleSelect(0);
                    drawerApplyCrowdAdapter.setItemSingleSelect(0);
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        if (mDrawerLayout.isDrawerOpen(ll_drawer_right)) {
            mDrawerLayout.closeDrawer(ll_drawer_right);
        } else {
            mDrawerLayout.openDrawer(ll_drawer_right);
        }
    }

    /**
     * 默认加载列表的方法，输入文字搜索也走此代码
     *
     */
    public void getData(boolean isLoad, String searchInfo) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.loadData(searchInfo, page, pageSize);
    }

    /**
     * 点击右侧侧滑菜单时，刷新列表的请求
     */
    public void getDataByApplyDepartments() {
        if(et_search!=null) {
            et_search.setText("按名称、症状、用量搜索");
        }
        isMore = false;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getListByApplyDepartments(drawerDepartmentAdapter.getSelectedItemId(), drawerApplyCrowdAdapter.getSelectedItemId(), page, pageSize);
    }

    @Override
    public void onLoadSuccess(FolkPrescriptionRecord record) {
        mPtrFrameLayout.refreshComplete();
        //第一次时，加载筛选条件列表
        if(isFirstLoad) {
            if(emptyView == null) {
                emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
                mAdapter.setEmptyView(true, emptyView);
            }
            mPresenter.loadRightDrawerLists();
            isFirstLoad=false;
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
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        mPtrFrameLayout.refreshComplete();
    }

    /**
     * 根据适应人群,所属科室搜索偏方,加载成功
     */
    @Override
    public void onLoadByApplyDepartmentsSuccess(FolkPrescriptionRecord record) {
        mPtrFrameLayout.refreshComplete();
        mAdapter.setNewData(record.getFolkPrescriptionList());//刷新列表
        Page pageBean = record.getPage();
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
    public void onLoadByApplyDepartmentsFail(FolkPrescriptionRecord record) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void loadRightDrawerListsSuccess(List<HospitalDepartments> list) {
        mPresenter.loadRightDrawerLists1();
        ArrayList<HospitalDepartments> newList = new ArrayList<>();
        for(HospitalDepartments item : list) {
            newList.add(item);
        }
        drawerDepartmentAdapter = new DrawerDepartmentExpRvAdapter(FolkPrescriptionActivity.this, newList, 15);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rv_drawer_department.setLayoutManager(layoutManager);
        rv_drawer_department.setAdapter(drawerDepartmentAdapter);
        if(list.size()<15) {
            tv_check_all_grid_depart.setVisibility(View.GONE);
        } else {
            tv_check_all_grid_depart.setVisibility(View.VISIBLE);
        }
        tv_check_all_grid_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView) view).getText().equals("查看全部")) {
                    ((TextView) view).setText("收起列表");
                    drawerDepartmentAdapter.toggleExpand(true);
                } else {
                    ((TextView) view).setText("查看全部");
                    drawerDepartmentAdapter.toggleExpand(false);
                }
            }
        });
    }

    @Override
    public void loadRightDrawerLists1Success(List<ShowApplyCrowd> list) {
//        applyCrowdlist = list;//用于传递数据到添加偏方页面
        drawerApplyCrowdAdapter = new DrawerApplyCrowdRvAdapter(FolkPrescriptionActivity.this, list, 15);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rv_drawer_symptom.setLayoutManager(layoutManager);
        rv_drawer_symptom.setAdapter(drawerApplyCrowdAdapter);
        if(list.size()<15) {
            tv_check_all_grid_symptom.setVisibility(View.INVISIBLE);
        } else {
            tv_check_all_grid_symptom.setVisibility(View.VISIBLE);
        }
        tv_check_all_grid_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView) view).getText().equals("查看全部")) {
                    ((TextView) view).setText("收起列表");
                    drawerApplyCrowdAdapter.toggleExpand(true);
                } else {
                    ((TextView) view).setText("查看全部");
                    drawerApplyCrowdAdapter.toggleExpand(false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
            String stringExtra = "";
            if(data!=null && data.hasExtra("searchInfo")){
                stringExtra = data.getStringExtra("searchInfo");
                getData(false, stringExtra);
                if(et_search != null) {
                    et_search.setText(stringExtra);
                }
            }
        }
    }

    /*加载下一页执行请求处*/
    @Override
    public void onLoadMoreRequested() {
        getData(true, "");
    }

    @OnClick({R.id.btn_drawer_reset, R.id.btn_drawer_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer_reset:
                drawerDepartmentAdapter.setItemSingleSelect(0);
                drawerApplyCrowdAdapter.setItemSingleSelect(0);
                break;
            case R.id.btn_drawer_commit:
                if (mDrawerLayout.isDrawerOpen(ll_drawer_right)) {
                    mDrawerLayout.closeDrawer(ll_drawer_right);
                    getDataByApplyDepartments();
                    hasRefreshData = true;
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolkPrescriptionCollectEvent event) {
        mAdapter.setCollect(event.getPosition());//刷新列表收藏状态
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UploadPrescriptionSuccessEvent event) {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 30);
    }
}