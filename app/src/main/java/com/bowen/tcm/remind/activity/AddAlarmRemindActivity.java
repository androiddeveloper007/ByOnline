package com.bowen.tcm.remind.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.DrugVolume;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.AlarmId;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.dialog.AddMedicineDialog;
import com.bowen.tcm.common.dialog.ChooseTimeDialog;
import com.bowen.tcm.common.dialog.RemindPersonDialog;
import com.bowen.tcm.common.event.UpdateAlarmEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.remind.adapter.AddRemindAdapter;
import com.bowen.tcm.remind.contract.AddAlarmRemindContract;
import com.bowen.tcm.remind.presenter.AddAlarmRemindPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */
public class AddAlarmRemindActivity extends BaseActivity implements AddAlarmRemindContract.View{
    @BindView(R.id.mTimeTv)
    TextView mTimeTv;
    @BindView(R.id.mTimeLayout)
    RelativeLayout mTimeLayout;
    @BindView(R.id.mRepeatPeriodTv)
    TextView mRepeatPeriodTv;
    @BindView(R.id.mRepeatRateLayout)
    RelativeLayout mRepeatRateLayout;
    @BindView(R.id.mRemindWayTv)
    TextView mRemindWayTv;
    @BindView(R.id.mRemindWayLayout)
    RelativeLayout mRemindWayLayout;
    @BindView(R.id.mRemindPersonTv)
    TextView mRemindPersonTv;
    @BindView(R.id.mRemindPersonLayout)
    RelativeLayout mRemindPersonLayout;

    private RecyclerView mRecyclerView;
    private TextView mAddMedicineTv;

    private AddRemindAdapter mAdapter;

    private ArrayList<DrugVolume> mDrugList;

    private ArrayList<FamilyMember> mFamilyMembers;
    private AddAlarmRemindPresenter mPresenter;

    private AddAlarmInfo mAddAlarmInfo;
    private boolean isSetTime = false;
    private boolean isSetRepeatPeriod = false;
    private boolean isSetRemindWay = false;
    private boolean isSetRemindPerson = false;

    public static final int REPEAT_PERIOD_RESULT_CODE = 200;
    public static final int REMIND_WAY_RESULT_CODE = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_remind);

        setTitle("添加用药提醒");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("保存");

        init();

    }


    private void init(){

        mAddAlarmInfo = new AddAlarmInfo();
        mPresenter = new AddAlarmRemindPresenter(this,this);
        mRecyclerView = getView(R.id.mRecyclerView);
        View headView = getLayoutInflater().inflate(R.layout.headview_add_remind, null);
        ButterKnife.bind(this,headView);
        View footView = getLayoutInflater().inflate(R.layout.footview_add_remind, null);
        mAddMedicineTv = footView.findViewById(R.id.mAddMedicineTv);
        mAddMedicineTv.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new AddRemindAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mDrugList = new ArrayList<>();
        mAdapter.addHeaderView(headView);
        mAdapter.addFooterView(footView);
        mPresenter.getFamilyMembers();
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        remindUserAddAppWiget();
    }

    private void remindUserAddAppWiget(){
        if(DataCacheUtil.getInstance().getBoolean(DataCacheUtil.IS_ADD_REMIND_APPWIDGET,false)){
            if(checkContent()){
                mAddAlarmInfo.setStartAlarm(true);
                mPresenter.saveAlarmRemindInfo(mAddAlarmInfo);
            }
        }else{
            AffirmDialog affirmDialog = new AffirmDialog(this);
            affirmDialog.setmContentStr("为保证用药提醒功能的正常使用，请添加用药提醒桌面小工具");
            affirmDialog.setmOkStr("确认添加");
            affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                @Override
                public void onCancle() {

                }

                @Override
                public void onOK() {
                    if(checkContent()){
                        mAddAlarmInfo.setStartAlarm(true);
                        mPresenter.saveAlarmRemindInfo(mAddAlarmInfo);
                    }
                }
            });
            affirmDialog.show();
        }
    }

    private boolean checkContent(){
        if(!isSetTime){
            showToast("用药时间");
            return false;
        }
        if(!isSetRepeatPeriod){
            showToast("重复周期");
            return false;
        }
        if(!isSetRemindWay){
            showToast("提醒方式");
            return false;
        }
        if(!isSetRemindPerson){
            showToast("提醒谁");
            return false;
        }
        if(EmptyUtils.isEmpty(mDrugList)){
            showToast("用药设置");
            return false;
        }

        return true;
    }

    private void showToast(String content){
        ToastUtil.getInstance().showToastDialog(String.format("你还没有填写%s呢",content));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case REPEAT_PERIOD_RESULT_CODE:
                    String mStatDateStr = data.getStringExtra(ChooseAlarmRepeateTimeActivity.REPEAT_DATE);
                    int mRepeatPeriod = data.getIntExtra(ChooseAlarmRepeateTimeActivity.REPEAT_PERIOD, Constants.TYPE_REMIND_REPEAT_EVERYDAY);
                    mRepeatPeriodTv.setText(ChooseTypeUtil.getRemindPeriodStr(mRepeatPeriod) + "\n从" + mStatDateStr + "起");
                    mAddAlarmInfo.setRemindDate(DateUtil.dateToLong(mStatDateStr));
                    mAddAlarmInfo.setRepeatPeriod(mRepeatPeriod+"");
                    isSetRepeatPeriod = true;
                    break;
                case REMIND_WAY_RESULT_CODE:
                    String mRemindWayRingUri = data.getStringExtra(RemindWayActivtiy.REMIND_WAY_RING);
                    boolean isRemindWayShake = data.getBooleanExtra(RemindWayActivtiy.REMIND_WAY_ISSHAKE,true);
                    mAddAlarmInfo.setRemindRingUri(mRemindWayRingUri);
                    mAddAlarmInfo.setShake(isRemindWayShake);
                    boolean isHaveRing = EmptyUtils.isNotEmpty(mRemindWayRingUri);
                    if(isHaveRing&&isRemindWayShake){
                        mRemindWayTv.setText("铃声 + 震动");
                    }else if(isHaveRing&&!isRemindWayShake){
                        mRemindWayTv.setText("铃声");
                    }else if(!isHaveRing&&isRemindWayShake){
                        mRemindWayTv.setText("震动");
                    }else{
                        mRemindWayTv.setText("无");
                    }
                    isSetRemindWay = true;
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onClick(View view) {
        super.onClick(view);
        AddMedicineDialog addMedicineDialog = new AddMedicineDialog(this);
        addMedicineDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
            @Override
            public void onDataCallBack(Object... obj) {
                DrugVolume info = new DrugVolume();
                info.setDrugName((String)obj[0]);
                info.setDosage((String)obj[1]);
                mDrugList.add(info);
                mAdapter.setNewData(mDrugList);
                mAddAlarmInfo.setDrugList(mDrugList);
            }
        });
        addMedicineDialog.show();
    }

    @OnClick({R.id.mTimeLayout, R.id.mRepeatRateLayout, R.id.mRemindWayLayout, R.id.mRemindPersonLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mTimeLayout:
                ChooseTimeDialog chooseTimeDialog = new ChooseTimeDialog(this);
                chooseTimeDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        String mRemindTimeStr = (String) obj[0];
                        mTimeTv.setText(mRemindTimeStr);
                        mAddAlarmInfo.setRemindTime(DateUtil.hourToLong(mRemindTimeStr));
                        isSetTime = true;
                    }
                });
                chooseTimeDialog.show();
                break;
            case R.id.mRepeatRateLayout:
                RouterActivityUtil.startActivityResult(this,ChooseAlarmRepeateTimeActivity.class,REPEAT_PERIOD_RESULT_CODE);
                break;
            case R.id.mRemindWayLayout:
                RouterActivityUtil.startActivityResult(this,RemindWayActivtiy.class,REMIND_WAY_RESULT_CODE);
                break;
            case R.id.mRemindPersonLayout:
                RemindPersonDialog remindPersonDialog = new RemindPersonDialog(this);
                remindPersonDialog.setmFamilyMembers(mFamilyMembers);
                remindPersonDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        FamilyMember familyMember = (FamilyMember)obj[0];
                        if(UserInfo.getInstance().getUserNickname().equals(familyMember.getFamilyNickname())){
                            mRemindPersonTv.setText("自己");
                        }else{
                            mRemindPersonTv.setText(familyMember.getFamilyNickname());
                        }
                        mAddAlarmInfo.setRemindPersonId(familyMember.getFamilyId());
                        mAddAlarmInfo.setRemindPersonType(familyMember.getFamilyType());
                        isSetRemindPerson = true;
                    }
                });
                remindPersonDialog.show();
                break;
        }
    }

    @Override
    public void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list) {
        mFamilyMembers = list;
    }

    @Override
    public void onSaveAlarmSuccess(AlarmId alarmId) {
        Alarm alarm = mPresenter.copyToAlarm(mAddAlarmInfo);
        alarm.setRemindId(alarmId.getRemindId());
        alarm.setRemindNickname(mRemindPersonTv.getText().toString());
        alarm.setId(alarmId.getId());
        mPresenter.setAlarm(alarm);
        EventBus.getDefault().post(new UpdateAlarmEvent());
        finish();
    }
}
