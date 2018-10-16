package com.bowen.tcm.medicalrecord.activity;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.dialog.MedicalRecordEditPopwindow;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.event.UpdateUIEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.main.activity.MainActivity;
import com.bowen.tcm.medicalrecord.adapter.MedicalRecordPhotoAdapter;
import com.bowen.tcm.medicalrecord.model.MedicalRecordModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */

public class MedicalDetailActivity extends BaseActivity {

    @BindView(R.id.mMedicalNameTv)
    TextView mMedicalNameTv;
    @BindView(R.id.mMoreImg)
    ImageView mMoreImg;
    @BindView(R.id.mMedicalDesTv)
    TextView mMedicalDesTv;
    @BindView(R.id.mDiagnoseTimeTv)
    TextView mDiagnoseTimeTv;
    @BindView(R.id.mDiagnoseResultTv)
    TextView mDiagnoseResultTv;
    @BindView(R.id.mDiagnoseHospitalTv)
    TextView mDiagnoseHospitalTv;
    @BindView(R.id.mDiagnoseDoctorTv)
    TextView mDiagnoseDoctorTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mDiseaseTimeLayout)
    LinearLayout mDiseaseTimeLayout;
    @BindView(R.id.mAddMedicalRecoreBtn)
    TextView mAddMedicalRecoreBtn;

    private MedicalRecord mMedicalRecord;
    private MedicalRecordPhotoAdapter mAdapter;
    private List<PhotoFile> photoList;
    private MedicalRecordModel mMedicalRecordModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_medical_detail);
        ButterKnife.bind(this);
        setTitle("详情");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("病情总览");

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            mMedicalRecord = (MedicalRecord) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MedicalRecordPhotoAdapter(this);
        mAdapter.setShowDelete(false);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView showImg = (ImageView) view.findViewById(R.id.addImg);
                ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(MedicalDetailActivity.this, showImg, position + 1, photoList);
                showBigPicDialog.show();
            }
        });

        mMedicalRecordModel = new MedicalRecordModel(this);

        updateUI();
    }

    private void updateUI() {
        if (EmptyUtils.isNotEmpty(mMedicalRecord)) {
            mMedicalNameTv.setText(mMedicalRecord.getCaseName());
            mMedicalDesTv.setText(mMedicalRecord.getCaseDetails());
            if (mMedicalRecord.getDoctorTime() != 0) {
                mDiagnoseTimeTv.setText(DateUtil.date2String(mMedicalRecord.getDoctorTime(), DateUtil.DEFAULT_FORMAT_DATE));
            }
            mDiagnoseResultTv.setText(mMedicalRecord.getDoctorResult());
            mDiagnoseHospitalTv.setText(mMedicalRecord.getClinicName());
            mDiagnoseDoctorTv.setText(mMedicalRecord.getDoctorName());
            mAdapter.setNewData(ChooseTypeUtil.getPhotoList(mMedicalRecord.getComFileInfoList()));
            photoList = mMedicalRecord.getComFileInfoList();
            if (mMedicalRecord.getRightType() != Constants.MEDICAL_RECORD_PERMISSION_ALL) {
                mMoreImg.setVisibility(View.INVISIBLE);
            } else {
                mMoreImg.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        Bundle bundle = new Bundle();
        bundle.putString(RouterActivityUtil.FROM_TAG, mMedicalRecord.getCourseId());
        bundle.putString(RouterActivityUtil.FROM_TAG1, mMedicalRecord.getCaseName());
        RouterActivityUtil.startActivity(this, MedicalAllDetailActivity.class, bundle);
    }

    @OnClick({R.id.mMoreImg, R.id.mAddMedicalRecoreBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mMoreImg:
                showEditWindow(view);
                break;
            case R.id.mAddMedicalRecoreBtn:
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG, mMedicalRecord.getFamilyId());
                bundle.putSerializable(RouterActivityUtil.FROM_TAG1, mMedicalRecord);
                RouterActivityUtil.startActivity(MedicalDetailActivity.this, AddMedicalInfoActivity.class, bundle);
                break;
        }
    }

    private void showEditWindow(View v) {
        MedicalRecordEditPopwindow popwindow = new MedicalRecordEditPopwindow(this);
        popwindow.setBaseDialogListener(new BasePopWindow.BasePopWindowListener() {
            @Override
            public void onDataCallBack(Object... obj) {
                String temp = (String) obj[0];
                if (temp.equals("编辑")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG, mMedicalRecord);
                    RouterActivityUtil.startActivity(MedicalDetailActivity.this, EditMedicalInfoActivity.class, bundle);
                } else {
                    AffirmDialog affirmDialog = new AffirmDialog(MedicalDetailActivity.this, "您确认要删除病历吗？");
                    affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                        @Override
                        public void onCancle() {

                        }

                        @Override
                        public void onOK() {
                            deleteMedicalRecord();
                        }
                    });
                    affirmDialog.show();
                }

            }
        });
        popwindow.show(v);
    }

    private void deleteMedicalRecord() {
        mMedicalRecordModel.deleteMedicalRecord(mMedicalRecord.getCaseId(), new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                RouterActivityUtil.startActivity(MedicalDetailActivity.this, MainActivity.class);
                EventBus.getDefault().post(new UpdateUIEvent());
                finish();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });

    }
}
