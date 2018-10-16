package com.bowen.tcm.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.SubmitAppointmentInfo;
import com.bowen.tcm.common.bean.network.AppiontmentPeriodInfo;
import com.bowen.tcm.common.bean.network.AppointmentDoctor;
import com.bowen.tcm.common.bean.network.AppointmentPeriod;
import com.bowen.tcm.common.dialog.adapter.ChooseReservationTimeAdapter;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.activity.SubmitReservationActivity;
import com.bowen.tcm.inquiry.model.DoctorModel;
import com.bowen.tcm.inquiry.model.OutpatientAppointmentModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:选择预约时间
 * Created by AwenZeng on 2018/4/9.
 */

public class ChooseReservationTimeDialog extends BaseDialog {

    @BindView(R.id.mChooseTimeTv)
    TextView mChooseTimeTv;
    @BindView(R.id.mCancleTv)
    TextView mCancleTv;
    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mClinicAddressTv)
    TextView mClinicAddressTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ChooseReservationTimeAdapter mAdapter;

    private OutpatientAppointmentModel mOutpatientAppointmentModel;
    private String mDoctorId;
    private long mDateTime;
    private int mTypeTime;
    private AppiontmentPeriodInfo mAppiontmentPeriodInfo;

    public ChooseReservationTimeDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }


    public ChooseReservationTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_reservation_time);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        setCanceledOnTouchOutside(true);
        init();
    }

    private void init(){
        mOutpatientAppointmentModel = new OutpatientAppointmentModel(mContext);
        mAdapter = new ChooseReservationTimeAdapter(mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppointmentPeriod item = mAdapter.getItem(position);
                if(item.getMaxNum()<=0||item.getApplyNum() == item.getMaxNum()
                        ||item.getAppStatus() == Constants.STATUS_APPIONTMENT_OUTDATE
                        ||item.getAppStatus() == Constants.STATUS_APPIONTMENT_FULL){
                    return;
                }
                SubmitAppointmentInfo  info = new SubmitAppointmentInfo();
                info.setDoctorId(mDoctorId);
                info.setClinicName(mAppiontmentPeriodInfo.getEmrAppointment().getHospital());
                info.setClinicAddress(mAppiontmentPeriodInfo.getEmrAppointment().getAddress());
                info.setDoctorPosRank(mAppiontmentPeriodInfo.getPositionStr());
                info.setProductPrice(mAppiontmentPeriodInfo.getRegisterFee());
                info.setTimePeriod(mAdapter.getItem(position).getTypeName());
                info.setAppointmentId(mAdapter.getItem(position).getAppointmentId());
                info.setDoctorName(mAppiontmentPeriodInfo.getName());
                info.setAppointmentDate(mDateTime);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG,info);
                RouterActivityUtil.startActivity((Activity) mContext, SubmitReservationActivity.class,bundle);
                dismiss();
            }
        });
        getAppointmentPeriod();
    }

    private void updateUI(){
        if(EmptyUtils.isNotEmpty(mAppiontmentPeriodInfo)){
            AppointmentDoctor doctor = mAppiontmentPeriodInfo.getEmrAppointment();
            mChooseTimeTv.setText(DateUtil.date2String(doctor.getAppointmentDate(),DateUtil.DATE_MONTH_FORMAT)
                    +"  "+doctor.getWeek() + "  " + doctor.getTypeStr());
            mClinicNameTv.setText(doctor.getHospital());
            mClinicAddressTv.setText(doctor.getAddress());
            mAdapter.setNewData(mAppiontmentPeriodInfo.getEmrAppointmentList());
        }
    }

    @OnClick(R.id.mCancleTv)
    public void onViewClicked() {
        dismiss();
    }

    public void setmDoctorId(String mDoctorId) {
        this.mDoctorId = mDoctorId;
    }


    public void setmDateTime(long mDateTime) {
        this.mDateTime = mDateTime;
    }


    public void setTypeTime(int typeTime) {
        this.mTypeTime = typeTime;
    }

    private void getAppointmentPeriod(){
        mOutpatientAppointmentModel.getAppointmentPeriod(mDoctorId, mDateTime, mTypeTime, new HttpTaskCallBack<AppiontmentPeriodInfo>() {
            @Override
            public void onSuccess(HttpResult<AppiontmentPeriodInfo> result) {
                mAppiontmentPeriodInfo = result.getData();
                updateUI();
            }

            @Override
            public void onFail(HttpResult<AppiontmentPeriodInfo> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }
}
