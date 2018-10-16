package com.bowen.tcm.healthcare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseFragment;
import com.bowen.tcm.medicalrecord.activity.AddMedicalDetailActivity;
import com.bowen.tcm.medicalrecord.activity.MedicalRecordActivity;
import com.bowen.tcm.remind.activity.AlarmRemindActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Describe: 养生界面
 * Created by AwenZeng on 2018/5/9.
 */

public class HealthFragment extends BaseFragment {


    @BindView(R.id.mHealthMedicalRecordImg)
    ImageView mHealthMedicalRecordImg;
    @BindView(R.id.mHealthMedicalRecordLayout)
    RelativeLayout mHealthMedicalRecordLayout;
    @BindView(R.id.mHealthRemindImg)
    ImageView mHealthRemindImg;
    @BindView(R.id.mHealthRemindLayout)
    RelativeLayout mHealthRemindLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_health, null);
        ButterKnife.bind(this, mView);
    }

    @OnClick({R.id.mHealthMedicalRecordLayout, R.id.mHealthRemindLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mHealthMedicalRecordLayout:
                if(isLogin()){
                    RouterActivityUtil.startActivity(mActivity, MedicalRecordActivity.class);
                }
                break;
            case R.id.mHealthRemindLayout:
                if(isLogin()){
                    RouterActivityUtil.startActivity(mActivity, AlarmRemindActivity.class);
                }
                break;
        }
    }
}
