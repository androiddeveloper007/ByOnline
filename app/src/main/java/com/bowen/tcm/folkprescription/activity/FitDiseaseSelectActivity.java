package com.bowen.tcm.folkprescription.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.DiseaseInfo;
import com.bowen.tcm.common.bean.network.DiseaseInfoRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.AddFitDiseaseEvent;
import com.bowen.tcm.common.widget.SearchActionBar;
import com.bowen.tcm.folkprescription.adapter.FitDiseasePopupWindowRvAdapter;
import com.bowen.tcm.folkprescription.contract.FitDiseaseSelectContract;
import com.bowen.tcm.folkprescription.presenter.FitDiseaseSelectPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:选择病症，搜索页面
 * Created by zzp on 2018/5/22.
 */

public class FitDiseaseSelectActivity extends BaseActivity implements FitDiseaseSelectContract.View ,BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.search_action_bar)
    SearchActionBar search_action_bar;
    @BindView(R.id.popupFitDiseaseRv)
    RecyclerView popupFitDiseaseRv;
    private FitDiseaseSelectPresenter mPresenter;
    private Context mContext;
    private FitDiseasePopupWindowRvAdapter mAdapter;
    private View emptyView;
    private int page = 1;
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_fit_disease_search);
        ButterKnife.bind(this);
        mContext = this;
        mPresenter = new FitDiseaseSelectPresenter(this, this);
        initView();
        getData(false);
    }

    private void initView() {
        search_action_bar.setEditTextHint("输入病症关键字查找");
        search_action_bar.getRightTextButton().setVisibility(View.VISIBLE);
        search_action_bar.getRightTextButton().setText("取消");
        search_action_bar.getRightTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationUtils.isKeyboardOpen()) {
                    popupFitDiseaseRv.requestFocus();
                    ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                }
                onBackPressed();
            }
        });
        search_action_bar.action_bar_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getData(false);
                    return true;
                }
                return false;
            }
        });
        search_action_bar.action_bar_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getData(false);
            }
        });
        initRv();
    }

    @OnClick({R.id.root_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.root_search:
                ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                break;
        }
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        popupFitDiseaseRv.setLayoutManager(manager);
        mAdapter = new FitDiseasePopupWindowRvAdapter(mContext);
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.empty_view_disease_search, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
            }
        });
        popupFitDiseaseRv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.setBooleanListByPos(position, !mAdapter.isSelectedByPos(position));
                mAdapter.notifyDataSetChanged();
                selectedItem();
            }
        });
        //触摸到列表时，隐藏键盘
        popupFitDiseaseRv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    if (ApplicationUtils.isKeyboardOpen()) {
                        popupFitDiseaseRv.requestFocus();
                        ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        popupFitDiseaseRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    if (ApplicationUtils.isKeyboardOpen()) {
                        popupFitDiseaseRv.requestFocus();
                        ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                    }
                }
                return false;
            }
        });
        mAdapter.setOnLoadMoreListener(this);
    }

    private void selectedItem(){
        if (mAdapter.hasChecked()) {
            search_action_bar.setRightButtonText("确定");
            search_action_bar.getRightTextButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new AddFitDiseaseEvent(mAdapter.getDiseaseList()));
                    if (ApplicationUtils.isKeyboardOpen()) {
                        popupFitDiseaseRv.requestFocus();
                        ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                    }
                    finish();
                }
            });
        } else {
            search_action_bar.setRightButtonText("取消");
            search_action_bar.getRightTextButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ApplicationUtils.isKeyboardOpen()) {
                        popupFitDiseaseRv.requestFocus();
                        ApplicationUtils.closeKeyboard(search_action_bar.action_bar_edit_text);//收起键盘
                    }
                    onBackPressed();
                }
            });
        }
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
        mPresenter.loadDataByStr(search_action_bar.action_bar_edit_text.getText().toString(), page, pageSize);
    }

    @Override
    public void onLoadSuccess(DiseaseInfoRecord record) {
        Page pageBean = record.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(record.getDiseaseInfoList(), true);
        } else {
            if (record.getDiseaseInfoList() != null && record.getDiseaseInfoList().size() > 0) {
                mAdapter.setNewData(record.getDiseaseInfoList());
            }
        }
        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onLoadFail(DiseaseInfoRecord list) {

    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

}