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
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.AddAlarmInfo;
import com.bowen.tcm.common.bean.DrugVolume;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.dialog.AddMedicineDialog;
import com.bowen.tcm.common.dialog.ChooseTimeDialog;
import com.bowen.tcm.common.dialog.RemindPersonDialog;
import com.bowen.tcm.common.event.UpdateAlarmEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.remind.adapter.AddRemindAdapter;
import com.bowen.tcm.remind.contract.EditAlarmRemindContract;
import com.bowen.tcm.remind.presenter.EditAlarmRemindPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */
public class EditAlarmRemindActivity extends BaseActivity implements EditAlarmRemindContract.View{
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
    private EditAlarmRemindPresenter mPresenter;

    private AddAlarmInfo mAddAlarmInfo;
    private Alarm mAlarm;

    public static final int REPEAT_PERIOD_RESULT_CODE = 200;
    public static final int REMIND_WAY_RESULT_CODE = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_remind);

        setTitle("编辑用药提醒");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("保存");

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            mAlarm = (Alarm) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
        }

        init();

    }


    private void init(){
        mAddAlarmInfo = new AddAlarmInfo();

        mAddAlarmInfo.setRemindTime(mAlarm.getRemindTime());
        mAddAlarmInfo.setRemindDate(mAlarm.getRemindDate());
        mAddAlarmInfo.setRepeatPeriod(mAlarm.getDrugCycle());
        mAddAlarmInfo.setRemindRingUri(mAlarm.getRingtone());
        mAddAlarmInfo.setShake(mAlarm.isIsShockBool());
        mAddAlarmInfo.setStartAlarm(mAlarm.isStatusBool());
        mAddAlarmInfo.setRemindPersonId(mAlarm.getRemindUserId());
        mAddAlarmInfo.setRemindPersonType(mAlarm.getRemindFamilyType());
        mAddAlarmInfo.setDrugList(mAlarm.getEmrMedicineList());

        mPresenter = new EditAlarmRemindPresenter(this,this);
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
        updateUI();
    }

    private void updateUI(){
        mTimeTv.setText(DateUtil.date2String(mAlarm.getRemindTime(),DateUtil.DEFAULT_FORMAT_HOUR));
        mRepeatPeriodTv.setText(ChooseTypeUtil.getRemindPeriodStr(Integer.parseInt(mAlarm.getDrugCycle())) + "\n从"
                + DateUtil.date2String(mAlarm.getRemindDate(),DateUtil.DEFAULT_FORMAT_DATE) + "起");

        mRemindPersonTv.setText(mAlarm.getRemindFamilyTypeStr());
        mDrugList = mAlarm.getEmrMedicineList();
        mAdapter.setNewData(mDrugList);

        boolean isHaveRing = EmptyUtils.isNotEmpty(mAlarm.getRingtone());
        if(isHaveRing&&mAlarm.isIsShockBool()){
            mRemindWayTv.setText("铃声 + 震动");
        }else if(isHaveRing&&!mAlarm.isIsShockBool()){
            mRemindWayTv.setText("铃声");
        }else if(!isHaveRing&&mAlarm.isIsShockBool()){
            mRemindWayTv.setText("震动");
        }else{
            mRemindWayTv.setText("无");
        }

    }


    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        if(DataCacheUtil.getInstance().getBoolean(DataCacheUtil.IS_ADD_REMIND_APPWIDGET,false)){
            mPresenter.updateAlarmRemindInfo(mAlarm.getRemindId(),mAddAlarmInfo);
        }else {
            AffirmDialog affirmDialog = new AffirmDialog(this);
            affirmDialog.setmContentStr("为保证用药提醒功能的正常使用，请添加用药提醒桌面小工具");
            affirmDialog.setmOkStr("确认添加");
            affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                @Override
                public void onCancle() {

                }

                @Override
                public void onOK() {
                    mPresenter.updateAlarmRemindInfo(mAlarm.getRemindId(), mAddAlarmInfo);
                }
            });
            affirmDialog.show();
        }
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
                    }
                });
                chooseTimeDialog.show();
                break;
            case R.id.mRepeatRateLayout:
                RouterActivityUtil.startActivityResult(this,ChooseAlarmRepeateTimeActivity.class,REPEAT_PERIOD_RESULT_CODE);
                break;
            case R.id.mRemindWayLayout:
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG,mAlarm.getRingtone());
                RouterActivityUtil.startActivityResult(this,RemindWayActivtiy.class,REMIND_WAY_RESULT_CODE,bundle);
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
    public void onUpdateAlarmSuccess() {
        Alarm alarm = mPresenter.copyToAlarm(mAddAlarmInfo);
        alarm.setRemindId(mAlarm.getRemindId());
        alarm.setRemindNickname(mAlarm.getRemindNickname());
        alarm.setId(mAlarm.getId());
        mPresenter.setAlarm(alarm);
        EventBus.getDefault().post(new UpdateAlarmEvent());
        finish();
    }
}
