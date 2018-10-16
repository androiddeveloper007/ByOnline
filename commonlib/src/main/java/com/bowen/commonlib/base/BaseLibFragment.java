package com.bowen.commonlib.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Fragment基类
 * Created by AwenZeng on 2016/3/16.
 */
public class BaseLibFragment extends Fragment implements View.OnClickListener {
    public String TAG = this.getClass().getSimpleName();
    public View mView;
    public FragmentActivity mActivity;
    public LayoutInflater mInflater;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mInflater = mActivity.getLayoutInflater();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) mView.getParent();
        if (vg != null) {
            vg.removeAllViewsInLayout();
        }
        return mView;
    }

    protected <T extends View> T getView(int resId){
        return (T)mView.findViewById(resId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event){
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}