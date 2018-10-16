package com.bowen.tcm.medicalrecord.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.camera.view.TakePhotoActivity;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.SpannableStringClickSpan;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.AddCaseInfo;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.dialog.ChooseImageDialog;
import com.bowen.tcm.common.dialog.ChooseSingleItemDialog;
import com.bowen.tcm.common.dialog.ChooseDateDialog;
import com.bowen.tcm.common.dialog.ChooseVisablePermissionDialog;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.event.UpdateUIEvent;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.main.activity.MainActivity;
import com.bowen.tcm.medicalrecord.adapter.MedicalRecordPhotoAdapter;
import com.bowen.tcm.medicalrecord.contract.AddOrUpdateMedicalRecordContract;
import com.bowen.tcm.medicalrecord.presenter.AddOrUpdateMedicalRecordPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/21.
 */

public class EditMedicalDetailActivity extends BaseActivity implements AddOrUpdateMedicalRecordContract.View{

    @BindView(R.id.mDiseaseNameLayout)
    LinearLayout mDiseaseNameLayout;
    @BindView(R.id.mDiagnoseTimeTv)
    TextView mDiagnoseTimeTv;
    @BindView(R.id.mDiagnoseCountTv)
    TextView mDiagnoseCountTv;
    @BindView(R.id.mDiseaseTimeLayout)
    LinearLayout mDiseaseTimeLayout;
    @BindView(R.id.mDiagnoseResultEdit)
    EditText mDiagnoseResultEdit;
    @BindView(R.id.mDiagnoseHospitalEdit)
    EditText mDiagnoseHospitalEdit;
    @BindView(R.id.mDiagnoseDoctorEdit)
    EditText mDiagnoseDoctorEdit;
    @BindView(R.id.mLimitedTv)
    TextView mLimitedTv;
    @BindView(R.id.mLimitedLayout)
    RelativeLayout mLimitedLayout;
    @BindView(R.id.mNoRecordTv)
    TextView mNoRecordTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<String> mPhotoList;
    private MedicalRecordPhotoAdapter mAdapter;
    private AddOrUpdateMedicalRecordPresenter mPresenter;
    private AddCaseInfo mAddCaseInfo;
    private MedicalRecord mMedicalRecord;
    private String mDiagnoseTime;
    private String mDiagnoseStage;
    private String mDiagnoseResult;
    private String mDiagnoseClinic;
    private String mDiagnoseDoctor;
    private String mSeeRange;
    private int deletePos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_medical_detail);
        ButterKnife.bind(this);
        setTitle("编辑诊疗信息");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("完成");
        init();

    }

    private void init(){
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            mAddCaseInfo = (AddCaseInfo) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
            mMedicalRecord = (MedicalRecord)bundle.getSerializable(RouterActivityUtil.FROM_TAG1);
        }

        mPresenter = new AddOrUpdateMedicalRecordPresenter(this, this);
        if(EmptyUtils.isNotEmpty(mMedicalRecord)){
            mPhotoList = ChooseTypeUtil.getPhotoList(mMedicalRecord.getComFileInfoList());
            mPhotoList.add("拍照");

            if(mMedicalRecord.getDoctorTime()!=0){
                mDiagnoseTimeTv.setText(DateUtil.date2String(mMedicalRecord.getDoctorTime(),DateUtil.DEFAULT_FORMAT_DATE));
            }else{
                mDiagnoseTimeTv.setText(DateUtil.date2String(System.currentTimeMillis(),DateUtil.DEFAULT_FORMAT_DATE));
            }

            if(Integer.parseInt(mMedicalRecord.getDoctorStage())!=0){
                mDiagnoseCountTv.setText(ChooseTypeUtil.getDiagnoseStage(mMedicalRecord.getDoctorStage()));
            }else{
                mDiagnoseCountTv.setText("初诊");
            }

            mDiagnoseResultEdit.setText(mMedicalRecord.getDoctorResult());
            mDiagnoseHospitalEdit.setText(mMedicalRecord.getClinicName());
            mDiagnoseDoctorEdit.setText(mMedicalRecord.getDoctorName());
            GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new MedicalRecordPhotoAdapter(this);
            mAdapter.setShowDelete(true);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setNewData(mPhotoList);
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mAdapter.getItem(position).equals("拍照")){
                        ChooseImageDialog chooseImageDialog = new ChooseImageDialog(EditMedicalDetailActivity.this);
                        chooseImageDialog.setChoosePicCount(9);
                        chooseImageDialog.show();
                    }else{
                        ImageView showImg = (ImageView) view.findViewById(R.id.mPhotoImg);
                        ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(EditMedicalDetailActivity.this, showImg,
                                position + 1, ChooseTypeUtil.getShowBigPhotoList(mPhotoList));
                        showBigPicDialog.show();
                    }
                }
            });
            mAdapter.setmListener(new MedicalRecordPhotoAdapter.DeletePhotoListener() {
                @Override
                public void onDelete(View view) {
                    deletePos = (int)view.getTag();
                    String path = mPhotoList.get(deletePos);
                    if(path.contains("http")){
                        mPresenter.deletePhoto(ChooseTypeUtil.getPhotoFileId(mMedicalRecord.getComFileInfoList(),path));
                    }else{
                        mPhotoList.remove(deletePos);
                        mPhotoList = mPresenter.handlePhotoList(mPhotoList);
                        mAdapter.setNewData(mPhotoList);
                    }
                }
            });

            if(EmptyUtils.isNotEmpty(mMedicalRecord.getDoctorTime())){
                mNoRecordTv.setVisibility(View.INVISIBLE);
            }else{
                mNoRecordTv.setVisibility(View.VISIBLE);
            }

            if(EmptyUtils.isEmpty(mMedicalRecord.getSeePhone())){
                mLimitedTv.setText("仅自己");
                mSeeRange = "";
            }else{
                mLimitedTv.setText(mMedicalRecord.getSeePhone());
                mSeeRange = mPresenter.getLimitSeePhone(mMedicalRecord);
            }

            SpannableStringClickSpan clickSpan = new SpannableStringClickSpan(EditMedicalDetailActivity.this);
            clickSpan.setOnlickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.updateMedicalRecordInfo(mAddCaseInfo);
                }
            });
            mNoRecordTv.setText(SpannableStringUtils.getBuilder("没有诊疗记录，")
                    .append("跳过此步")
                    .setForegroundColor(getResources().getColor(R.color.color_main)).setProportion(1f)
                    .setClickSpan(clickSpan)
                    .create());
            mNoRecordTv.setMovementMethod(LinkMovementMethod.getInstance());
            mPresenter.getVisablePerson();
        }
    }

    private void getInputContent(){
         mDiagnoseTime = mDiagnoseTimeTv.getText().toString();
         mDiagnoseStage = mDiagnoseCountTv.getText().toString();
         mDiagnoseResult = mDiagnoseResultEdit.getText().toString();
         mDiagnoseClinic = mDiagnoseHospitalEdit.getText().toString();
         mDiagnoseDoctor = mDiagnoseDoctorEdit.getText().toString();
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        getInputContent();
        if(EmptyUtils.isNotEmpty(mAddCaseInfo)){
            mAddCaseInfo.setDiagnoseTime(DateUtil.dateToLong(mDiagnoseTime));
            mAddCaseInfo.setDiagnoseStage(Integer.parseInt(ChooseTypeUtil.getDiagnoseStageCode(mDiagnoseStage)));
            mAddCaseInfo.setDiagnoseResult(mDiagnoseResult);
            mAddCaseInfo.setDiagnoseClinic(mDiagnoseClinic);
            mAddCaseInfo.setDiagnoseDoctorName(mDiagnoseDoctor);
            mAddCaseInfo.setCaseId(mMedicalRecord.getCaseId());
            mAddCaseInfo.setSeePhone(mSeeRange);
        }
        if(mPresenter.checkInputContent(mDiagnoseResult,mDiagnoseClinic,mDiagnoseDoctor)){
            if(EmptyUtils.isNotEmpty(ChooseTypeUtil.filterUpdatePhotoList(mPhotoList))){
                mPresenter.updateUploadMedicalRecordInfo(mAddCaseInfo,mPhotoList);
            }else{
                mPresenter.updateMedicalRecordInfo(mAddCaseInfo);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case ImageSelectorActivity.REQUEST_CAMERA: {//拍照获取图片
                    String path = data.getStringExtra(TakePhotoActivity.RESULT_PHOTO_PATH);
                    mPhotoList.add(path);
                    mPhotoList = mPresenter.handlePhotoList(mPhotoList);
                    mAdapter.setNewData(mPhotoList);
                    break;
                }
                case ImageSelectorActivity.REQUEST_IMAGE: {//从图库中选择图片
                    ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    mPhotoList.addAll(images);
                    mPhotoList = mPresenter.handlePhotoList(mPhotoList);
                    mAdapter.setNewData(mPhotoList);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.mDiagnoseTimeTv, R.id.mDiagnoseCountTv, R.id.mLimitedLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDiagnoseTimeTv:
                ChooseDateDialog chooseTimeDialog = new ChooseDateDialog(this);
                chooseTimeDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mDiagnoseTimeTv.setText((String) obj[0]);
                    }
                });
                chooseTimeDialog.show();
                break;
            case R.id.mDiagnoseCountTv:
                ChooseSingleItemDialog dialog = new ChooseSingleItemDialog(this);
                dialog.setmContentStrList(ChooseTypeUtil.getDiagnoseStages());
                dialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mDiagnoseCountTv.setText((String) obj[0]);
                    }
                });
                dialog.show();
                break;
            case R.id.mLimitedLayout:
                ChooseVisablePermissionDialog mDialog = new ChooseVisablePermissionDialog(this);
                mDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        if(EmptyUtils.isNotEmpty(obj[0])){
                            mSeeRange = (String)obj[0];
                            mLimitedTv.setText((String)obj[1]);
                        }else{
                            mSeeRange = "";
                            mLimitedTv.setText("仅自己");
                        }

                    }
                });
                mDialog.show();
                break;
        }
    }

    @Override
    public void onDeleteSuccess() {
        mPhotoList.remove(deletePos);
        mPhotoList = mPresenter.handlePhotoList(mPhotoList);
        mAdapter.setNewData(mPhotoList);
    }

    @Override
    public void onAddOrUpdateMedicalRecordSuccess() {
        RouterActivityUtil.startActivity(EditMedicalDetailActivity.this, MainActivity.class,true);
        EventBus.getDefault().post(new UpdateUIEvent());
    }
}
