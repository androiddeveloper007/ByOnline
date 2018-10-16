package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AwenZeng on 2017/6/1.
 * 选择订单状态
 */
public class MyOrderTypeSelectPopwindow extends BasePopWindow {
    TextView all, waitPay, payed, finished, canceled, commended;
    private List<View> viewList;

    public MyOrderTypeSelectPopwindow(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mView = mInflater.inflate(R.layout.pop_window_my_order_state, null);
        ButterKnife.bind(this, mView);
        mPopWindow = new PopupWindow(mView);//, ScreenSizeUtil.dp2px(130f), ScreenSizeUtil.dp2px(250f)
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setOutsideTouchable(true);
        all = mView.findViewById(R.id.all);
        waitPay = mView.findViewById(R.id.waitPay);
        payed = mView.findViewById(R.id.payed);
        canceled = mView.findViewById(R.id.canceled);
        finished = mView.findViewById(R.id.finished);
        commended = mView.findViewById(R.id.commended);
        viewList = new ArrayList<>();
        viewList.add(all);
        viewList.add(waitPay);
        viewList.add(payed);
        viewList.add(canceled);
        viewList.add(finished);
        viewList.add(commended);
        all.setSelected(true);
    }


    public void show(View v) {
        if (mPopWindow != null && !mPopWindow.isShowing()) {
            mPopWindow.showAsDropDown(v,ScreenSizeUtil.dp2px(-50),ScreenSizeUtil.dp2px(-20));
        }
    }

    public boolean isShowing() {
        if (!EmptyUtils.isEmpty(mPopWindow)) {
            return mPopWindow.isShowing();
        }
        return false;
    }


    @OnClick({R.id.all, R.id.waitPay, R.id.payed, R.id.canceled, R.id.finished, R.id.commended})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all:
                mListener.onDataCallBack("全部");
                setPositionState(0);
                break;
            case R.id.waitPay:
                mListener.onDataCallBack("待支付");
                setPositionState(1);
                break;
            case R.id.payed:
                mListener.onDataCallBack("已支付");
                setPositionState(2);
                break;
            case R.id.canceled:
                mListener.onDataCallBack("已取消");
                setPositionState(3);
                break;
            case R.id.finished:
                mListener.onDataCallBack("已结束");
                setPositionState(4);
                break;
            case R.id.commended:
                mListener.onDataCallBack("已评价");
                setPositionState(6);
                break;
        }
        mPopWindow.dismiss();
    }

    public void setPositionState(int position){
        for(int i=0;i<5;i++){
            if(i==position){
                viewList.get(i).setSelected(true);
            }else{
                viewList.get(i).setSelected(false);
            }
        }
    }
}
