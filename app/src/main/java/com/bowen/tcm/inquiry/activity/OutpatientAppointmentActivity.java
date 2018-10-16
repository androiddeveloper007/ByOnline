package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.ActionTitleBar;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.DateNum;
import com.bowen.tcm.common.bean.ReservationBean;
import com.bowen.tcm.common.bean.network.AppointmentInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.dialog.ChooseReservationTimeDialog;
import com.bowen.tcm.common.widget.ReservationView;
import com.bowen.tcm.common.widget.ReservationView.ReservationTimeListener;
import com.bowen.tcm.inquiry.contract.OutpatientAppointmentContract;
import com.bowen.tcm.inquiry.presenter.OutpatientAppointmentPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:门诊预约
 * Created by zhuzhipeng on 2018/7/10.
 */
public class OutpatientAppointmentActivity extends BaseActivity implements OutpatientAppointmentContract.View {
    @BindView(R.id.mTitleBar)
    ActionTitleBar mTitleBar;
    @BindView(R.id.mDoctorHeadPortraitImg)
    CircleImageView mDoctorHeadPortraitImg;
    @BindView(R.id.mDoctorNameTv)
    TextView mDoctorNameTv;
    @BindView(R.id.mDoctorDepartmentTv)
    TextView mDoctorDepartmentTv;
    @BindView(R.id.mDoctorRankTv)
    TextView mDoctorRankTv;
    @BindView(R.id.mDoctorClinicTv)
    TextView mDoctorClinicTv;
    @BindView(R.id.layoutTop)
    RelativeLayout layoutTop;
    @BindView(R.id.mLastWeek)
    TextView mLastWeek;
    @BindView(R.id.mNextWeek)
    TextView mNextWeek;
    @BindView(R.id.mReservationView)
    ReservationView mReservationView;

    private int operateNums = 0;//初始为0 ,1:位下周 2：下下周 3：下下下周
    private Doctor mDoctor;
    private OutpatientAppointmentPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_inquire_outpatient_appointment);
        ButterKnife.bind(this);
        setTitle("门诊预约");

        mDoctor = (Doctor) RouterActivityUtil.getSerializable(this);
        mPresenter = new OutpatientAppointmentPresenter(this, this);
        mReservationView.setListener(new ReservationTimeListener() {
            @Override
            public void onReservationTime(DateNum dateNum, int time) {
                ChooseReservationTimeDialog chooseReservationTimeDialog = new ChooseReservationTimeDialog(OutpatientAppointmentActivity.this);
                chooseReservationTimeDialog.setmDoctorId(mDoctor.getDoctorId());
                chooseReservationTimeDialog.setmDateTime(dateNum.getDateTime());
                chooseReservationTimeDialog.setTypeTime(dateNum.getDayType());
                chooseReservationTimeDialog.show();
            }
        });
        getAppointmentInfo(operateNums);
        updateUI();
    }

    private void updateUI(){
        if(EmptyUtils.isNotEmpty(mDoctor)){
            ImageLoaderUtil.getInstance().show(this,mDoctor.getHeadImgUrl(),mDoctorHeadPortraitImg,R.drawable.man);
            mDoctorNameTv.setText(mDoctor.getName());
            mDoctorDepartmentTv.setText(mDoctor.getHospitalDepartments());
            mDoctorRankTv.setText(mDoctor.getPositionStr());
            mDoctorClinicTv.setText(mDoctor.getHospital());
        }

    }

    @Override
    public void onGetDoctorAppointMentInfoSuccess(ArrayList<AppointmentInfo> appointments) {
        ArrayList<ReservationBean> reserveList = new ArrayList<>();
        for(AppointmentInfo info:appointments){
            ReservationBean bean = new ReservationBean();
            bean.setWeek(info.getWeek());
            bean.setDate(DateUtil.date2String(info.getAppointmentDate(), DateUtil.DATE_MONTH_FORMAT));
            bean.setDateTime(info.getAppointmentDate());
            bean.setDayTime(info.getTypeStr().split("，"));
            String[] status = info.getAppStatus().split(",");
            int[] reserveStats = new int[3];
            for(int i = 0;i<status.length;i++){
                reserveStats[i] = Integer.parseInt(status[i]);
            }
            bean.setReserveStatus(reserveStats);
            reserveList.add(bean);
        }
        mReservationView.setReservationBeans(reserveList);
    }

    private void getAppointmentInfo(int weekStatus){
        mPresenter.getDoctorAppointmentInfo(mDoctor.getDoctorId(),weekStatus+"");
    }


    @OnClick({R.id.mLastWeek, R.id.mNextWeek})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLastWeek:
                operateNums--;
                mReservationView.caculateWeekDates(operateNums);
                getAppointmentInfo(operateNums);
                break;
            case R.id.mNextWeek:
                operateNums++;
                mReservationView.caculateWeekDates(operateNums);
                getAppointmentInfo(operateNums);
                break;
        }
    }
}
