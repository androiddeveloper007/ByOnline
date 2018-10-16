package com.bowen.tcm.main.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.awen.camera.util.ScreenSizeUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.CirclePageIndicator;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.main.adapter.GuidePagerAdapter;
import com.bowen.tcm.healthcare.activity.ChooseColumnsActivity;
import com.bowen.tcm.healthcare.model.HealthCareModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页
 * Created by AwenZeng on 2016/12/05.
 */
public class GuideActivity extends BaseActivity {
    @BindView(R.id.mSkipTv)
    TextView mSkipTv;
    @BindView(R.id.mViewPage)
    ViewPager mViewPage;
    @BindView(R.id.mViewpagerIndicator)
    CirclePageIndicator mViewpagerIndicator;

    private HealthCareModel mHealthCareModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        init();
    }


    protected void init() {
        mHealthCareModel = new HealthCareModel(this);
        mHealthCareModel.getColumnsList(null);
        ArrayList<View> viewLayouts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View viewLayout = null;
            switch (i) {
                case 0:
                    viewLayout = this.getLayoutInflater().inflate(R.layout.layout_guide_01, null);
                    break;
                case 1:
                    viewLayout = this.getLayoutInflater().inflate(R.layout.layout_guide_02, null);
                    break;
                case 2:
                    viewLayout = this.getLayoutInflater().inflate(R.layout.layout_guide_03, null);
                    break;
                case 3:
                    viewLayout = this.getLayoutInflater().inflate(R.layout.layout_guide_04, null);
                    TextView textView = (TextView) viewLayout.findViewById(R.id.mImmediateEnterTv);
                    textView.setOnClickListener(this);
                    textView.setVisibility(View.VISIBLE);
                    break;
            }
            viewLayouts.add(viewLayout);
        }
        GuidePagerAdapter adapter = new GuidePagerAdapter(viewLayouts);
        mViewPage.setAdapter(adapter);
        mViewpagerIndicator.setViewPager(mViewPage);
        mViewpagerIndicator.setPadding(ScreenSizeUtil.dp2px(7));
    }

    @Override
    protected boolean enableSlidingClose() {
        return false;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        //在此处打开关注选择页面
        RouterActivityUtil.startActivity(GuideActivity.this, ChooseColumnsActivity.class, true);
    }

    @OnClick(R.id.mSkipTv)
    public void onViewClicked() {
        //在此处打开关注选择页面
        RouterActivityUtil.startActivity(GuideActivity.this, ChooseColumnsActivity.class, true);
    }
}
