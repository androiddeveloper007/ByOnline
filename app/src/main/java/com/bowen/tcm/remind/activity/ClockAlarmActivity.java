package com.bowen.tcm.remind.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseDialog.BaseDialogListener;
import com.bowen.commonlib.dialog.AlertDialog;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.DrugVolume;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.AlarmManagerUtil;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.main.activity.MainActivity;
import com.bowen.tcm.main.activity.SplashActivity;
import com.bowen.tcm.main.model.MessageModel;
import com.bowen.tcm.remind.model.ClockAlarmModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 闹钟响应界面
 */
public class ClockAlarmActivity extends BaseActivity {
    @BindView(R.id.mRemindTimeTv)
    TextView mRemindTimeTv;
    @BindView(R.id.mRemindPersonTv)
    TextView mRemindPersonTv;
    @BindView(R.id.mRemindMedicineDesTv)
    TextView mRemindMedicineDesTv;

    private WakeLock mWakelock;
    private ClockAlarmModel mClockAlarmModel;
    private MessageModel mMessageModel;
    private Alarm mAlarm;
    private int mAlarmType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win = getWindow();
        LayoutParams winParams = win.getAttributes();
        winParams.flags |= (LayoutParams.FLAG_DISMISS_KEYGUARD
                | LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | LayoutParams.FLAG_TURN_SCREEN_ON);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_clock_alarm);
        ButterKnife.bind(this);

        mClockAlarmModel = new ClockAlarmModel(this);
        mMessageModel = new MessageModel(this);

        Bundle bundle = getIntent().getBundleExtra(AlarmManagerUtil.ALARM_BUNDLE);
        mAlarm = (Alarm) bundle.getSerializable(AlarmManagerUtil.ALARM_INFO);
        String soundUri = getIntent().getStringExtra(AlarmManagerUtil.ALARM_RING_SOUND_URI);
        mAlarmType = getIntent().getIntExtra(AlarmManagerUtil.ALARM_RING_WAY, Constants.TYPE_ALARM_NOTHING);
        if(EmptyUtils.isNotEmpty(mAlarm)){
            mRemindTimeTv.setText(DateUtil.date2String(mAlarm.getRemindTime(),DateUtil.DEFAULT_FORMAT_HOUR));

            StringBuilder builder = new StringBuilder();
            builder.append("提醒");
            String familyType = mAlarm.getRemindFamilyType();
            if(EmptyUtils.isNotEmpty(familyType)&&Integer.parseInt(familyType)== 0){//0:为本人
                builder.append("自己");
            }else{
                builder.append(mAlarm.getRemindNickname());
            }
            mRemindPersonTv.setText(builder.toString());

            SpannableStringUtils.Builder builder1 = SpannableStringUtils.getBuilder("");
            for (DrugVolume drugVolume : mAlarm.getEmrMedicineList()) {
                builder1.append(drugVolume.getDrugName())
                        .setForegroundColor(getResources().getColor(R.color.color_main_black_p60))
                        .append(" " + drugVolume.getDosage()+"  ")
                        .setForegroundColor(getResources().getColor(R.color.color_main_black));
            }
            mRemindMedicineDesTv.setText(builder1.create());
        }
        startAlarm();
        uploadMessage();
    }

    private void startAlarm(){
        if(EmptyUtils.isEmpty(mAlarm)){
            return;
        }

        if (mAlarmType == Constants.TYPE_ALARM_SOUND || mAlarmType == Constants.TYPE_ALARM_SOUNDVIBRATOR) { //铃声或铃声加震动
            mClockAlarmModel.startPlaySound(mAlarm.getRingtone());
        }
        if (mAlarmType == Constants.TYPE_ALARM_VIBRATOR || mAlarmType == Constants.TYPE_ALARM_SOUNDVIBRATOR) { //震动或铃声加震动
            mClockAlarmModel.startVibrator();
        }
    }


    private void closeAlarm(){
        if (mAlarmType == Constants.TYPE_ALARM_VIBRATOR || mAlarmType == Constants.TYPE_ALARM_SOUNDVIBRATOR) {
            mClockAlarmModel.stopPlaySound();
        }
        if (mAlarmType == Constants.TYPE_ALARM_VIBRATOR || mAlarmType == Constants.TYPE_ALARM_SOUNDVIBRATOR) {
            mClockAlarmModel.stopVibrator();
        }
        if(LoginStatusUtil.getInstance().isLogin()){
            finish();
        }else{
            RouterActivityUtil.startActivity(this, MainActivity.class,true);
        }
    }

    private void uploadMessage(){
        mMessageModel.addMessage(mAlarm.getRemindId(), null);
    }


    @OnClick(R.id.mOkBtn)
    public void onViewClicked() {
        closeAlarm();
    }


    @Override
    protected void onResume() {
        super.onResume();
        acquireWakeLock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }

    /**
     * 唤醒屏幕
     */
    private void acquireWakeLock() {
        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
                    .getCanonicalName());
            mWakelock.acquire();
        }
    }

    /**
     * 释放锁屏
     */
    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }
}
