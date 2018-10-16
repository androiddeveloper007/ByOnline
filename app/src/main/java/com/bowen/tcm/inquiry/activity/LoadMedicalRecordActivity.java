package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.ChooseMedicalRecordEvent;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.inquiry.adapter.LoadMedicalRecordAdapter;
import com.bowen.tcm.medicalrecord.contract.HomeMedicalRecordContract;
import com.bowen.tcm.medicalrecord.presenter.HomeMedicalRecordPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:加载病历
 * Created by AwenZeng on 2018/7/3.
 */
public class LoadMedicalRecordActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, HomeMedicalRecordContract.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private LoadMedicalRecordAdapter mAdapter;
    private HomeMedicalRecordPresenter mPresenter;
    private String mFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_load_medical_record);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        setTitle("加载病历");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("确认");

        mFamilyId = RouterActivityUtil.getString(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoadMedicalRecordAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new HomeMedicalRecordPresenter(this, this);
        mAdapter.setOnLoadMoreListener(this);
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

        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mAdapter.getItem(position).isChoose()) {
                    mAdapter.getItem(position).setChoose(false);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.getItem(position).setChoose(true);
                    mAdapter.notifyDataSetChanged();
                }
                changeData(false,mAdapter.getItem(position));
            }
        });
    }


    private void changeData(boolean isChoose,MedicalRecord item) {
        List<MedicalRecord> list = mAdapter.getData();
        if(EmptyUtils.isNotEmpty(item)){
            for (MedicalRecord medicalRecord : list) {
                if(!item.getCaseId().equals(medicalRecord.getCaseId())){
                    medicalRecord.setChoose(false);
                }
            }
            mAdapter.setNewData(list);
        }else{
            for (MedicalRecord person : list) {
                person.setChoose(isChoose);
            }
            mAdapter.setNewData(list);
        }

    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        MedicalRecord medicalRecord = getChooseMedicalRecord();
        if(EmptyUtils.isNotEmpty(medicalRecord)){
            ChooseMedicalRecordEvent chooseMedicalRecordEvent = new ChooseMedicalRecordEvent();
            chooseMedicalRecordEvent.setData(medicalRecord);
            EventBus.getDefault().post(chooseMedicalRecordEvent);
            finish();
        }else{
            ToastUtil.getInstance().showToastDialog("您还未选择病历");
        }
    }

    private MedicalRecord getChooseMedicalRecord(){
        List<MedicalRecord> dataList = mAdapter.getData();
        for (MedicalRecord medicalRecord : dataList) {
            if(medicalRecord.isChoose()){
              return medicalRecord;
            }
        }
        return null;
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
        mPresenter.getMedicalRecordList(page, 10,mFamilyId);
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onGetMedicalRecordSuccess(HomeMedicalRecord homeMedicalRecord) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = homeMedicalRecord.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(homeMedicalRecord.getEmrCaseInfoList(), true);
        } else {
            mAdapter.setNewData(homeMedicalRecord.getEmrCaseInfoList());
        }

        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
            TextView textView = (TextView)emptyView.findViewById(R.id.textView);
            textView.setText("暂无病历");
            mAdapter.addFooterView(emptyView);
        }else{
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onGetMedicalRecordFail(HomeMedicalRecord homeMedicalRecord) {
        mPtrFrameLayout.refreshComplete();
    }
}
