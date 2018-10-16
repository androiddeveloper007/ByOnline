package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.inquiry.adapter.UserGiftAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:用户心意
 * Created by AwenZeng on 2018/7/3.
 */
public class UserGiftActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private UserGiftAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_user_gift);
        ButterKnife.bind(this);
        setTitle("用户心意");

        mAdapter = new UserGiftAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        final ArrayList<String> list = new ArrayList<>();
        list.add("鲜花");
        list.add("杏林春暖");
        list.add("妙手回春");
        list.add("术精岐黄");
        list.add("妙手仁心");
        list.add("大医精诚");
        list.add("医家圣手");
        list.add("悬壶济世");
        mAdapter.setNewData(list);
    }

    @OnClick(R.id.mGiveGiftBtn)
    public void onViewClicked() {
        RouterActivityUtil.startActivity(this,GiveGiftActivity.class);
    }
}
