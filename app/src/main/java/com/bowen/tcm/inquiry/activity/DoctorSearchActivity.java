package com.bowen.tcm.inquiry.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.event.DoctorSearchEvent;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.common.widget.SearchActionBar;
import com.bowen.tcm.folkprescription.adapter.FolkPrescriptionSearchRvAdapter;
import com.bowen.tcm.inquiry.contract.DoctorSearchContract;
import com.bowen.tcm.inquiry.presenter.DoctorSearchPresenter;
import com.bowen.tcm.login.activity.BindingPhoneActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:医生搜索
 * Created by zzp on 2018/5/22.
 */

public class DoctorSearchActivity extends BaseActivity implements DoctorSearchContract.View {

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


    private int fromType;
    private String searchContent;
    private DoctorSearchPresenter mPresenter;
    private FolkPrescriptionSearchRvAdapter mAdapter, mAdapterLog;

    public static final int FROM_FIND_DOCTOR = 0;
    public static final int FROM_HOMEPAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_doctor_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        fromType = RouterActivityUtil.getInt(this);
        mPresenter = new DoctorSearchPresenter(this, this);
        mPresenter.showHotSearchList();

        search_action_bar.setEditTextHint("按姓名、疾病、症状、医院搜索医生");
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
                    searchContent = search_action_bar.action_bar_edit_text.getText().toString();
                    if (EmptyUtils.isNotEmpty(searchContent)) {
                        backToFindDoctor(searchContent);
                    } else {
                        ToastUtil.getInstance().showToastDialog("搜索内容不能为空");
                        ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                    }
                    return true;
                }
                return false;
            }
        });

        //如果未登录的话，将不访问此接口，并隐藏搜索记录模块
        if (LoginStatusUtil.getInstance().isLogin()) {
            if (UserInfo.getInstance().isBindPhone()) {
                mPresenter.showUserSearchLog();
            } else {
                RouterActivityUtil.startActivity(this, BindingPhoneActivity.class);
            }
        } else {
//            rl_search_log.setVisibility(View.GONE);
//            view_search_log.setVisibility(View.GONE);//上面的灰色区域也隐藏掉
        }
    }

    @OnClick({R.id.search_delete, R.id.root_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_delete:
                mPresenter.deleteUserSearchLog();
                break;
            case R.id.root_search:
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
                backToFindDoctor(mAdapter.getItemName(position));
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
                backToFindDoctor(mAdapterLog.getItemName(position));
            }
        });
    }


    private void backToFindDoctor(String searchContent) {
        if (fromType == FROM_HOMEPAGE) {
            SearchField searchField = new SearchField();
            searchField.setSearchInfo(searchContent);
            Bundle bundle = new Bundle();
            bundle.putSerializable(RouterActivityUtil.FROM_TAG, searchField);
            RouterActivityUtil.startActivity(DoctorSearchActivity.this, FindDoctorActivity.class, bundle, true);
        } else {
            EventBus.getDefault().post(new DoctorSearchEvent(searchContent));
            finish();
        }

    }

    @Override
    public void deleteUserSearchLogSuccess() {
        ToastUtil.getInstance().showToastDialog("用户搜索记录删除成功");
        mAdapterLog.clearAndNotify();
    }
}
