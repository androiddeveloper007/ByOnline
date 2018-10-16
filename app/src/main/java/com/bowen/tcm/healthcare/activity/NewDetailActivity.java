package com.bowen.tcm.healthcare.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/28.
 */

public class NewDetailActivity extends BaseActivity {

    @BindView(R.id.mTitleTv)
    TextView mTitleTv;
    @BindView(R.id.mDateTv)
    TextView mDateTv;
    @BindView(R.id.mReaderCountTv)
    TextView mReaderCountTv;
    @BindView(R.id.mContentTv)
    TextView mContentTv;
    @BindView(R.id.mSourceTipsTv)
    TextView mSourceTipsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        setTitle("辟谣");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("分享");
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
    }
}
