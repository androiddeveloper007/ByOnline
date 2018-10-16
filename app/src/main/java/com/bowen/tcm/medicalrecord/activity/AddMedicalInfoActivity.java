package com.bowen.tcm.medicalrecord.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.AddCaseInfo;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.dialog.ChooseContentDialog;
import com.bowen.tcm.common.dialog.ChooseDateDialog;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.mine.activity.SelfDefinitionActivity;
import com.bowen.tcm.medicalrecord.model.MedicalRecordModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/21.
 */

public class AddMedicalInfoActivity extends BaseActivity {
    @BindView(R.id.mDiseaseNameTv)
    TextView mDiseaseNameTv;
    @BindView(R.id.mDiseaseNameLayout)
    RelativeLayout mDiseaseNameLayout;
    @BindView(R.id.mDiseaseDesEdit)
    EditText mDiseaseDesEdit;
    @BindView(R.id.mContentCountTv)
    TextView mContentCountTv;
    @BindView(R.id.mDiseaseTimeTv)
    TextView mDiseaseTimeTv;
    @BindView(R.id.mDiseaseTimeLayout)
    RelativeLayout mDiseaseTimeLayout;

    private MedicalRecordModel mMedicalRecordModel;
    private String mCaseDetailStr;
    private String mCaseNameStr;
    private String mFallIllTimeStr;
    private String familyId;
    private String mCourseId;//病程ID
    private MedicalRecord mMedicalRecord;
    private boolean isChooseDiseaseName = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_medical_info);
        ButterKnife.bind(this);
        init();
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            familyId = bundle.getString(RouterActivityUtil.FROM_TAG);
            mMedicalRecord = (MedicalRecord) bundle.getSerializable(RouterActivityUtil.FROM_TAG1);
            if(EmptyUtils.isNotEmpty(mMedicalRecord)){
                mCourseId = mMedicalRecord.getCourseId();
                mDiseaseNameTv.setText(mMedicalRecord.getCaseName());
            }
        }

        mMedicalRecordModel = new MedicalRecordModel(this);
        mDiseaseTimeTv.setText(DateUtil.date2String(System.currentTimeMillis(),DateUtil.DEFAULT_FORMAT_DATE));

        if(EmptyUtils.isNotEmpty(mCourseId)){
            isChooseDiseaseName = true;
            setTitle("添加病程信息");
            mDiseaseNameLayout.setVisibility(View.GONE);
        }else{
            setTitle("添加病情信息");
            mDiseaseNameLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init(){
        mDiseaseDesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mContentCountTv.setText(mDiseaseDesEdit.getText().toString().length() + "/500");
            }
        });
    }

    private void getInputContent(){
        mCaseNameStr = mDiseaseNameTv.getText().toString();
        mCaseDetailStr = mDiseaseDesEdit.getText().toString();
        mFallIllTimeStr = mDiseaseTimeTv.getText().toString();
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        getInputContent();
        if(!isChooseDiseaseName){
            ToastUtil.getInstance().showToastDialog("请选择疾病名称");
            return;
        }
        if(EmptyUtils.isEmpty(mCaseDetailStr)){
            ToastUtil.getInstance().showToastDialog("请输入对疾病的描述");
            return;
        }

        AddCaseInfo addCaseInfo = new AddCaseInfo();
        addCaseInfo.setCaseDetails(mCaseDetailStr);
        addCaseInfo.setCaseName(mCaseNameStr);
        addCaseInfo.setFamilyId(familyId);
        addCaseInfo.setCourseId(mCourseId);
        if(EmptyUtils.isNotEmpty(mMedicalRecord)){
            addCaseInfo.setDiagnoseStage(Integer.parseInt(mMedicalRecord.getDoctorStage()));
        }
        addCaseInfo.setIllTime(DateUtil.dateToLong(mFallIllTimeStr));

        Bundle bundle = new Bundle();
        bundle.putSerializable(RouterActivityUtil.FROM_TAG,addCaseInfo);
        RouterActivityUtil.startActivity(this,AddMedicalDetailActivity.class,bundle);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case SelfDefinitionActivity.REQUEST_CODE:
                    String content = data.getStringExtra(SelfDefinitionActivity.REQUEST_CONTENT);
                    isChooseDiseaseName = true;
                    mDiseaseNameTv.setText(content);
                    mDiseaseNameTv.setTextColor(getResources().getColor(R.color.color_main_black));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.mDiseaseNameLayout, R.id.mDiseaseTimeLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDiseaseNameLayout:
                ChooseContentDialog chooseDiseaseNameDialog = new ChooseContentDialog(this);
                chooseDiseaseNameDialog.setSelectStr(mDiseaseNameTv.getText().toString());
                chooseDiseaseNameDialog.setList(ChooseTypeUtil.getDiseaseNames());
                chooseDiseaseNameDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        isChooseDiseaseName = true;
                        mDiseaseNameTv.setText((String)obj[0]);
                        mDiseaseNameTv.setTextColor(getResources().getColor(R.color.color_main_black));
                    }
                });
                chooseDiseaseNameDialog.show();
                break;
            case R.id.mDiseaseTimeLayout:
                ChooseDateDialog chooseTimeDialog = new ChooseDateDialog(this);
                chooseTimeDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mDiseaseTimeTv.setText((String) obj[0]);
                    }
                });
                chooseTimeDialog.show();
                break;
        }
    }
}
