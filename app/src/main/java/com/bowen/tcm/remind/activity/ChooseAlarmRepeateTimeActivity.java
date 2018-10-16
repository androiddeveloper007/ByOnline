package com.bowen.tcm.remind.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.camera.util.ToastUtil;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.dialog.ChooseDateDialog;
import com.bowen.tcm.common.dialog.adapter.ChooseContentAdapter;
import com.bowen.tcm.common.model.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */

public class ChooseAlarmRepeateTimeActivity extends BaseActivity {
    @BindView(R.id.mTimeTv)
    TextView mTimeTv;
    @BindView(R.id.mTimeLayout)
    RelativeLayout mTimeLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ChooseContentAdapter mAdapter;

    private String mDateStr;
    private int mRepeatType  = 1;

    public static final String REPEAT_DATE = "RepeatDate";
    public static final String REPEAT_PERIOD = "RepeatPeriod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_choose_alarm_repeate_time);
        ButterKnife.bind(this);

        setTitle("重复周期");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("确定");


        mAdapter = new ChooseContentAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        final List<String> list = new ArrayList<>();
        list.add("每天");
        list.add("每隔1天");
        list.add("每隔2天");
        list.add("每隔3天");
        list.add("每隔4天");
        list.add("每隔5天");
        list.add("每周");
//        list.add("每月");
        mAdapter.setNewData(list);
        mTimeTv.setText(DateUtil.date2String(System.currentTimeMillis(),DateUtil.DEFAULT_FORMAT_DATE));
        mDateStr = mTimeTv.getText().toString();
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.setChoosePos(position);
                mRepeatType = position + 1;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        Intent intent = new Intent();
        intent.putExtra(REPEAT_DATE,mDateStr);
        intent.putExtra(REPEAT_PERIOD,mRepeatType);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick(R.id.mTimeLayout)
    public void onViewClicked() {
        ChooseDateDialog chooseDateDialog = new ChooseDateDialog(this);
        chooseDateDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
            @Override
            public void onDataCallBack(Object... obj) {
                mDateStr = (String) obj[0];
                mTimeTv.setText(mDateStr);

            }
        });
        chooseDateDialog.show();
    }
}
