package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.inquiry.adapter.GiveGiftAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:送心意
 * Created by AwenZeng on 2018/7/3.
 */
public class GiveGiftActivity extends BaseActivity {
    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDesTv)
    TextView mDoctorDesTv;
    @BindView(R.id.mGiftImg)
    ImageView mGiftImg;
    @BindView(R.id.mGiftPriceTv)
    TextView mGiftPriceTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private GiveGiftAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_give_gift);
        ButterKnife.bind(this);
        setTitle("送心意");

        mAdapter = new GiveGiftAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
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

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.setSelectPos(position);
                switch (list.get(position)) {
                    case "鲜花":
                        mGiftImg.setImageResource(R.drawable.gift_flower);
                        break;
                    case "杏林春暖":
                        mGiftImg.setImageResource(R.drawable.gift_xlcn);
                        break;
                    case "妙手回春":
                        mGiftImg.setImageResource(R.drawable.gift_mshc);
                        break;
                    case "术精岐黄":
                        mGiftImg.setImageResource(R.drawable.gift_sjqh);
                        break;
                    case "妙手仁心":
                        mGiftImg.setImageResource(R.drawable.gift_msrx);
                        break;
                    case "大医精诚":
                        mGiftImg.setImageResource(R.drawable.gift_dyjc);
                        break;
                    case "医家圣手":
                        mGiftImg.setImageResource(R.drawable.gift_yjss);
                        break;
                    case "悬壶济世":
                        mGiftImg.setImageResource(R.drawable.gift_xhjs);
                        break;
                    default:

                        break;
                }
            }
        });
    }

    @OnClick(R.id.mGiveGiftBtn)
    public void onViewClicked() {
    }
}
