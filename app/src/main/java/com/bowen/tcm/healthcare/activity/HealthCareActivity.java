package com.bowen.tcm.healthcare.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.event.ChangeColumnIdEvent;
import com.bowen.tcm.common.event.ColumnSortEvent;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.healthcare.adapter.HealthCarePagerAdapter;
import com.bowen.tcm.healthcare.contract.HealthCareContract;
import com.bowen.tcm.healthcare.fragment.NewsFragment;
import com.bowen.tcm.healthcare.presenter.HealthCareFragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/8/17.
 */
public class HealthCareActivity extends BaseActivity implements HealthCareContract.View{
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private HealthCareFragmentPresenter mPresenter;
    private HealthCarePagerAdapter mPagerAdapter;
    private ArrayList<Column> mColumnList = new ArrayList<>();
    private ArrayList<NewsFragment> mFragments = new ArrayList<>();

    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_health_care);
        ButterKnife.bind(this);

        setTitle("养生");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("排序");

        mPresenter = new HealthCareFragmentPresenter(this, this);
        mPresenter.loadColumnsList();
        pos = RouterActivityUtil.getInt(this);

    }

    private void updateUI() {

        if (EmptyUtils.isNotEmpty(mPagerAdapter) && mPagerAdapter.getCount() > 0) {
            for (int i = 0; i < mColumnList.size(); i++) {
                NewsFragment fragment = mPagerAdapter.getItem(i);
                String columnId = mColumnList.get(i).getColumnId();
                if (!fragment.getColumnId().equals(columnId)) {
                    fragment.setColumnId(columnId);
                    mFragments.set(i, fragment);
                    ChangeColumnIdEvent changeColumnIdEvent = new ChangeColumnIdEvent();
                    changeColumnIdEvent.setTypeString(columnId);
                    EventBus.getDefault().post(changeColumnIdEvent);
                }
            }

        } else {
            for (int i = 0; i < mColumnList.size(); i++) {
                NewsFragment fragment = new NewsFragment();
                fragment.setColumnId(mColumnList.get(i).getColumnId());
                mFragments.add(fragment);
            }
        }

        if (EmptyUtils.isEmpty(mPagerAdapter)) {
            mPagerAdapter = new HealthCarePagerAdapter(getSupportFragmentManager(), mFragments);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(mTabLayout));
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            int tabSize = mTabLayout.getTabCount() - mColumnList.size();
            if (tabSize > 0) {
                for (int i = 0; i < tabSize; i++) {
                    if (EmptyUtils.isNotEmpty(mTabLayout.getTabAt(mTabLayout.getTabCount() - 1))) {
                        mTabLayout.removeTabAt(mTabLayout.getTabCount() - 1);
                    }
                }
            }
            mPagerAdapter.setmFragments(mFragments);
        }

        if (EmptyUtils.isNotEmpty(mTabLayout.getTabAt(0))) {
            if (EmptyUtils.isNotEmpty(mTabLayout.getTabAt(0).getCustomView())) {
                for (int i = 0; i < mColumnList.size(); i++) {
                    if (EmptyUtils.isNotEmpty(mTabLayout.getTabAt(i))) {
                        updateTabData(mTabLayout.getTabAt(i), mColumnList.get(i));
                    }
                }

                int tabSize = mTabLayout.getTabCount() - mColumnList.size();
                if (tabSize > 0) {
                    for (int i = 0; i < tabSize; i++) {
                        if (EmptyUtils.isNotEmpty(mTabLayout.getTabAt(mTabLayout.getTabCount() - 1))) {
                            mTabLayout.removeTabAt(mTabLayout.getTabCount() - 1);
                        }
                    }
                }
            } else {
                for (int i = 0; i < mColumnList.size(); i++) {
                    if (EmptyUtils.isNotEmpty(mTabLayout.getTabAt(i))) {
                        mTabLayout.getTabAt(i).setCustomView(getTabView(i));
                    }
                }
            }
        }


        mTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                changeTabStatus(tab, true);
            }

            @Override
            public void onTabUnselected(Tab tab) {
                changeTabStatus(tab, false);
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
        setShowHealthCareTab(pos);
    }


    private void updateTabData(Tab tab, Column column) {
        View view = tab.getCustomView();
        TextView columnTitleTv = view.findViewById(R.id.mTitleTv);
        CircleImageView columnIconImg = view.findViewById(R.id.mTitleImg);
        if (EmptyUtils.isNotEmpty(column)) {
            if (column.getColumnId().equals("recommend")) {
                columnIconImg.setImageResource(R.drawable.recommend_default_bg);
            } else {
                ImageLoaderUtil.getInstance().show(this, column.getFileLink(), columnIconImg, R.drawable.news_defalult_bg);
            }
            columnTitleTv.setText(column.getColumnName());
        }
    }


    private void changeTabStatus(Tab tab, boolean selected) {
        View view = tab.getCustomView();
        TextView columnTitleTv = view.findViewById(R.id.mTitleTv);
        if (selected) {
            columnTitleTv.setTextColor(getResources().getColor(R.color.color_main));
        } else {
            columnTitleTv.setTextColor(getResources().getColor(R.color.color_main_black));
        }
    }

    public View getTabView(final int position) {
        final View view = LayoutInflater.from(this).inflate(R.layout.tablayout_titile_item, null);
        TextView columnTitleTv = view.findViewById(R.id.mTitleTv);
        CircleImageView columnIconImg = view.findViewById(R.id.mTitleImg);
        if (mColumnList.get(position).getColumnId().equals("recommend")) {
            columnIconImg.setImageResource(R.drawable.recommend_default_bg);
        } else {
            ImageLoaderUtil.getInstance().show(this, mColumnList.get(position).getFileLink(), columnIconImg, R.drawable.news_defalult_bg);
        }
        columnTitleTv.setText(mColumnList.get(position).getColumnName());
        if (position == 0) {
            columnTitleTv.setTextColor(getResources().getColor(R.color.color_main));
        } else {
            columnTitleTv.setTextColor(getResources().getColor(R.color.color_main_black));
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position);
            }
        });
        return view;
    }

    public void setShowHealthCareTab(int showHealthCareTab) {
        mViewPager.setCurrentItem(showHealthCareTab);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        if(isLogin()){
            Bundle bundle = new Bundle();
            bundle.putSerializable(RouterActivityUtil.FROM_TAG, mColumnList);//.remove(0)
            RouterActivityUtil.startActivity(HealthCareActivity.this, ColumnsSortActivity.class, bundle);//修改栏目排序
        }
    }

    @Override
    public void onLoadColumnsListSuccess(ArrayList<Column> columns) {
        if (EmptyUtils.isNotEmpty(mColumnList)) {
            mColumnList.clear();
        }
        mColumnList = columns;
        updateUI();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ColumnSortEvent event) {
        mPresenter.loadColumnsList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        mPresenter.loadColumnsList();
    }

}
