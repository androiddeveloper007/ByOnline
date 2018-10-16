package com.bowen.tcm.folkprescription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.common.widget.SearchActionBar;
import com.bowen.tcm.folkprescription.adapter.FolkPrescriptionSearchRvAdapter;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionSearchContract;
import com.bowen.tcm.folkprescription.presenter.FolkPrescriptionSearchPresenter;
import com.bowen.tcm.login.activity.BindingPhoneActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:偏方搜索页面
 * Created by zzp on 2018/5/22.
 */

public class FolkPrescriptionSearchActivity extends BaseActivity implements FolkPrescriptionSearchContract.View {

    @BindView(R.id.search_action_bar)
    SearchActionBar search_action_bar;
    @BindView(R.id.hot_search_rv)
    RecyclerView hot_search_rv;
    @BindView(R.id.hot_search_history_rv)
    RecyclerView hot_search_history_rv;
    @BindView(R.id.search_delete)
    LinearLayout search_delete;
    @BindView(R.id.rl_search_log)
    RelativeLayout rl_search_log;
    @BindView(R.id.view_search_log)
    View view_search_log;
    private FolkPrescriptionSearchPresenter mPresenter;
    private FolkPrescriptionSearchRvAdapter mAdapter, mAdapterLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_folk_prescription_search);
        ButterKnife.bind(this);
        initView();

        mPresenter = new FolkPrescriptionSearchPresenter(this, this);
        mPresenter.showHotSearchList();
        //如果未登录的话，将不访问此接口，并隐藏该块视图
        if (LoginStatusUtil.getInstance().isLogin()) {
            if (UserInfo.getInstance().isBindPhone()) {
                mPresenter.showUserSearchLog();
            } else {
                RouterActivityUtil.startActivity(this, BindingPhoneActivity.class);
            }
        } else {
            rl_search_log.setVisibility(View.GONE);
            view_search_log.setVisibility(View.GONE);//上面的灰色区域也隐藏掉
        }
    }

    private void initView() {
        search_action_bar.getRightTextButton().setVisibility(View.VISIBLE);
        search_action_bar.getRightTextButton().setText("取消");

        search_action_bar.getRightTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search_action_bar.action_bar_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (search_action_bar.action_bar_edit_text.getText().length() > 0) {
                        Intent intent = new Intent();
                        intent.putExtra("searchInfo", search_action_bar.action_bar_edit_text.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.getInstance().showToastDialog("搜索内容不能为空");
                        ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.search_delete, R.id.root_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_delete:
                //删除搜索记录
                mPresenter.deleteUserSearchLog();
                break;
            case R.id.root_search:
                //收起键盘
                ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHotSearchLoadSuccess(List<String> list) {
        mAdapter = new FolkPrescriptionSearchRvAdapter(this, list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        hot_search_rv.setLayoutManager(layoutManager);
        hot_search_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FolkPrescriptionSearchRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("searchInfo", mAdapter.getItemName(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onSearchLogLoadSuccess(List<String> list) {
        if (list.size() == 0) {
            search_delete.setVisibility(View.GONE);
        }
        mAdapterLog = new FolkPrescriptionSearchRvAdapter(this, list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        hot_search_history_rv.setLayoutManager(layoutManager);
        hot_search_history_rv.setAdapter(mAdapterLog);
        mAdapterLog.setOnItemClickListener(new FolkPrescriptionSearchRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("searchInfo", mAdapterLog.getItemName(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void deleteUserSearchLogSuccess() {
        ToastUtil.getInstance().showToastDialog("用户搜索记录删除成功");
        mAdapterLog.clearAndNotify();
    }
}
