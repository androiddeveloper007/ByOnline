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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/21.
 */

public class EditMedicalInfoActivity extends BaseActivity {
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

    private MedicalRecord mMedicalRecord;
    private String mCaseDetailStr;
    private String mCaseNameStr;
    private String mFallIllTimeStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_medical_info);
        ButterKnife.bind(this);
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            mMedicalRecord = (MedicalRecord) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
        }
        setTitle("编辑病情信息");
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
        updateUI();
    }

    private void updateUI(){
        if(EmptyUtils.isNotEmpty(mMedicalRecord)){
            mDiseaseNameTv.setText(mMedicalRecord.getCaseName());
            mDiseaseNameTv.setTextColor(getResources().getColor(R.color.color_main_black));
            mDiseaseTimeTv.setText(DateUtil.date2String(mMedicalRecord.getIllTime(),DateUtil.DEFAULT_FORMAT_DATE));
            mDiseaseDesEdit.setText(mMedicalRecord.getCaseDetails());
        }
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
        if(EmptyUtils.isEmpty(mCaseDetailStr)){
            ToastUtil.getInstance().showToastDialog("请输入对疾病的描述");
            return;
        }

        AddCaseInfo addCaseInfo = new AddCaseInfo();
        addCaseInfo.setCaseDetails(mCaseDetailStr);
        addCaseInfo.setCaseName(mCaseNameStr);
        addCaseInfo.setCaseId(mMedicalRecord.getCaseId());
        addCaseInfo.setIllTime(DateUtil.dateToLong(mFallIllTimeStr));

        Bundle bundle = new Bundle();
        bundle.putSerializable(RouterActivityUtil.FROM_TAG,addCaseInfo);
        bundle.putSerializable(RouterActivityUtil.FROM_TAG1,mMedicalRecord);
        RouterActivityUtil.startActivity(this,EditMedicalDetailActivity.class,bundle);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case SelfDefinitionActivity.REQUEST_CODE:
                    String content = data.getStringExtra(SelfDefinitionActivity.REQUEST_CONTENT);
                    mDiseaseNameTv.setText(content);
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
                chooseDiseaseNameDialog.setList(ChooseTypeUtil.getDiseaseNames());
                chooseDiseaseNameDialog.setSelectStr(mDiseaseNameTv.getText().toString());
                chooseDiseaseNameDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
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
