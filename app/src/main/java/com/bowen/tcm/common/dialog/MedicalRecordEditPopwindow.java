package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AwenZeng on 2017/6/1.
 */

public class MedicalRecordEditPopwindow extends BasePopWindow {


    public MedicalRecordEditPopwindow(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mView = mInflater.inflate(R.layout.pop_window_medical_record_edit, null);
        ButterKnife.bind(this, mView);
        mPopWindow = new PopupWindow(mView, ScreenSizeUtil.dp2px(100f),
                ScreenSizeUtil.dp2px(120f));
        mPopWindow.setOutsideTouchable(true);
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


    @OnClick({R.id.mEditTv, R.id.mDeleteTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mEditTv:
                mListener.onDataCallBack("编辑");
                disMissWindow();
                break;
            case R.id.mDeleteTv:
                mListener.onDataCallBack("删除");
                disMissWindow();
                break;
        }
    }
}
