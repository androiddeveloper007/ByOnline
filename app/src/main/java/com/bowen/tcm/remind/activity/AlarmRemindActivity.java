package com.bowen.tcm.remind.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.UpdateAlarmEvent;
import com.bowen.tcm.common.widget.SwipeDividerDecoration;
import com.bowen.tcm.main.model.MessageModel;
import com.bowen.tcm.remind.adapter.AlarmRemindAdapter;
import com.bowen.tcm.remind.contract.AlarmRemindContract;
import com.bowen.tcm.remind.presenter.AlarmRemindPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */

public class AlarmRemindActivity extends BaseActivity implements AlarmRemindContract.View, AlarmRemindAdapter.DeleteListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.mSwipeRecyclerView)
    RecyclerView mSwipeRecyclerView;
    @BindView(R.id.mNoAlarmRemindViewStup)
    ViewStub mNoAlarmRemindViewStup;

    private TextView mAddAlarmRemindTv;
    private LinearLayout mNoAlarmRemindLayout;

    private AlarmRemindAdapter mAdapter;

    private AlarmRemindPresenter mPresenter;

    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    public final static String REQUEST_CONTENT = "content";
    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_alarm_remind);
        ButterKnife.bind(this);

        setTitle("用药提醒");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("添加");

        init();
    }

    private void init() {
        mPresenter = new AlarmRemindPresenter(this, this);
        mNoAlarmRemindViewStup.inflate();
        mAddAlarmRemindTv = this.findViewById(R.id.mAddAlarmRemindTv);
        mNoAlarmRemindLayout = this.findViewById(R.id.mNoAlarmRemindLayout);
        mNoAlarmRemindLayout.setVisibility(View.GONE);
        mAddAlarmRemindTv.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSwipeRecyclerView.setLayoutManager(layoutManager);
        mSwipeRecyclerView.addItemDecoration(new SwipeDividerDecoration());
        mAdapter = new AlarmRemindAdapter(this, mPresenter);
        mAdapter.setmDeleteListen(this);
        mSwipeRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemOnClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Alarm alarm = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, alarm);
                RouterActivityUtil.startActivity(AlarmRemindActivity.this, EditAlarmRemindActivity.class, bundle);
            }
        });
        mAdapter.setmOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Alarm alarm = (Alarm) buttonView.getTag();
                mPresenter.changeAlarmStatus(alarm.getRemindId(), isChecked);
            }
        });
        getData(false);
    }

    private void getData(boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getAlarmRemindList(page, 10);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        RouterActivityUtil.startActivity(this, AddAlarmRemindActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        RouterActivityUtil.startActivityResult(this, AddAlarmRemindActivity.class, REQUEST_CODE);
    }

    @Override
    public void onDeleteListener(Alarm alarm) {
        mPresenter.deleteAlarm(alarm);
    }

    @Override
    public void onGetAlarmRemindListSuccess(AlarmInfo info) {
        Page pageBean = info.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(info.getRemindMedicineList(), true);
        } else {
            mAdapter.setNewData(info.getRemindMedicineList());
        }

        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() >= 3 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        } else {
            mAdapter.addFooterView(null);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
            mNoAlarmRemindLayout.setVisibility(View.VISIBLE);
        } else {
            mNoAlarmRemindLayout.setVisibility(View.GONE);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
//        resetAlarm();
    }

    private void resetAlarm(){
        ArrayList<Alarm> alarms = (ArrayList<Alarm>) mAdapter.getData();
        for(Alarm alarm:alarms){
            if(alarm.isStatusBool()){
              mPresenter.setAlarm(alarm);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateAlarmEvent event) {
        getData(false);
    }

    @Override
    public void onUpdateAlarmStatusSuccess(Alarm alarm) {
        if (alarm.isStatusBool()) {
            mPresenter.setAlarm(alarm);
        } else {
            mPresenter.cancelAlarm(alarm.getId());
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Override
    public void onDeleteAlarmSuccess(int alarmId) {
        getData(false);
        mPresenter.cancelAlarm(alarmId);
    }

}
