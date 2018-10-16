package com.bowen.tcm.remind.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.SwitchButton;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/2.
 */

public class RemindWayActivtiy extends BaseActivity {

    @BindView(R.id.mAlarmRingTv)
    TextView mAlarmRingTv;
    @BindView(R.id.mAlarmRingLayout)
    RelativeLayout mAlarmRingLayout;
    @BindView(R.id.mAlarmShakeSwitchBtn)
    SwitchButton mAlarmShakeSwitchBtn;

    private String mRingUri = "";

    private boolean isShake;

    public static final String REMIND_WAY_RING = "ringUrl";
    public static final String REMIND_WAY_ISSHAKE = "isShake";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_remind_way);
        ButterKnife.bind(this);

        setTitle("提醒方式");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("确定");

        mRingUri = RouterActivityUtil.getString(this);
        if(EmptyUtils.isNotEmpty(mRingUri)){
            mAlarmRingTv.setText(mRingUri);
        }
        isShake = mAlarmShakeSwitchBtn.isChecked();
        mAlarmShakeSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShake = isChecked;
            }
        });
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        Intent intent = new Intent();
        intent.putExtra(REMIND_WAY_RING,mRingUri);
        intent.putExtra(REMIND_WAY_ISSHAKE,isShake);
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);//获取用户选择的铃声数据
            String temp = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_TITLE);//获取用户选择的铃声数据
            ToastUtil.getInstance().toast(temp);
            mRingUri = pickedUri.toString();
            mAlarmRingTv.setText(mRingUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.mAlarmRingTv)
    public void onViewClicked() {
        chooseAlarmRing();
    }


    /**
     *  选择铃声
     */
    public void chooseAlarmRing() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置通知铃声");
        if (mRingUri != null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(mRingUri));//将已经勾选过的铃声传递给系统铃声界面进行显示
        }
        startActivityForResult(intent, 0);
    }
}
