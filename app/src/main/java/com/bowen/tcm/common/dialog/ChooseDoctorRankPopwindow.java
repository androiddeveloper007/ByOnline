package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.PopupWindow;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.dialog.adapter.ChooseDepartmentAdapter;
import com.bowen.tcm.common.dialog.adapter.ChooseDoctorRankAdapter;
import com.bowen.tcm.common.model.AppConfigInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AwenZeng on 2017/6/1.
 */

public class ChooseDoctorRankPopwindow extends BasePopWindow {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ChooseDoctorRankAdapter mAdapter;

    public ChooseDoctorRankPopwindow(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mView = mInflater.inflate(R.layout.pop_window_choose_doctor_rank, null);
        ButterKnife.bind(this, mView);
        mPopWindow = new PopupWindow(mView, ScreenSizeUtil.dp2px(360f), ScreenSizeUtil.dp2px(640f));
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);

        mAdapter = new ChooseDoctorRankAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(AppConfigInfo.getInstance().getDoctorRankList());

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(EmptyUtils.isNotEmpty(mListener)){
                    mListener.onDataCallBack(mAdapter.getItem(position).getCode());
                }
                disMissWindow();
            }
        });

        mView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    return true;
                }
                return false;
            }

        });

    }


    public void show(View v) {
        if (mPopWindow != null && !mPopWindow.isShowing()) {
            showAsDropDown(mPopWindow,v,0,0);
        }
    }

    public boolean isShowing() {
        if (!EmptyUtils.isEmpty(mPopWindow)) {
            return mPopWindow.isShowing();
        }
        return false;
    }
}
