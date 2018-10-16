package com.bowen.tcm.healthcare.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.main.activity.MainActivity;
import com.bowen.tcm.healthcare.adapter.ChooseColumnsAdapter;
import com.bowen.tcm.healthcare.contract.ChooseColumnsContract;
import com.bowen.tcm.healthcare.presenter.ChooseColumnsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe: 选择关注项目
 * Created by AwenZeng on 2018/5/4.
 *
 */

public class ChooseColumnsActivity extends BaseActivity implements ChooseColumnsContract.View{

    @BindView(R.id.mSkipTv)
    TextView mSkipTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mOkBtn)
    TextView mOkBtn;

    private ChooseColumnsAdapter mAdapter;
    private ChooseColumnsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_choose_columns);
        ButterKnife.bind(this);

        mPresenter = new ChooseColumnsPresenter(this, this);
        mAdapter = new ChooseColumnsAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mAdapter.getSelectedItemCount()==5 && !mAdapter.getItemSelectedState(position)){
                    ToastUtil.getInstance().showToastDialog("关注得太多啦，最多只能选择5个栏目哦~");
                    return;
                }
                mAdapter.refreshSelectedList(position);
            }
        });

        ArrayList<Column> columns = AppConfigInfo.getInstance().getColumnList();
        if(EmptyUtils.isNotEmpty(columns)){
            for(Column column:columns){//删除推荐栏目
                if(column.getColumnId().equals("recommend")){
                    columns.remove(column);
                    break;
                }
            }
            mAdapter.setNewData(columns);
        }else{
            mPresenter.loadColumList();
        }
    }
    @Override
    protected boolean enableSlidingClose() {
        return false;
    }

    @OnClick({R.id.mSkipTv, R.id.mOkBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSkipTv:
                RouterActivityUtil.startActivity(this, MainActivity.class, true);
                break;
            case R.id.mOkBtn:
                if(mAdapter.getSelectedItemCount()==0){
                    ToastUtil.getInstance().showToastDialog("至少关注一个嘛~");
                }else{
                    if(EmptyUtils.isNotEmpty(mAdapter.getData())){
                        DataCacheUtil.getInstance().putString(DataCacheUtil.BW_INTEREST_STRING_BUFFER, mPresenter.handleColumnList(mAdapter.getData()));
                        DataCacheUtil.getInstance().putBoolean(DataCacheUtil.CHOOSE_INTEREST_SUCCESS, true);
                        RouterActivityUtil.startActivity(this, MainActivity.class, true);
                    }
                }
                break;
        }
    }

    @Override
    public void onChooseSuccess() {
        finish();
    }

    @Override
    public void onLoadSuccess(List<Column> list) {
        for(Column column:list){//删除推荐栏目
            if(column.getColumnId().equals("recommend")){
                list.remove(column);
                break;
            }
        }
        mAdapter.setNewData(list);
    }
}
