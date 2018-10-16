package com.bowen.tcm.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.mine.presenter.FamilyMemberInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/21.
 */

public class SelfDefinitionActivity extends BaseActivity{
    @BindView(R.id.mInputEdit)
    EditText mInputEdit;

    private int mFromType;
    private String mFromContentStr;
    private String mTitleStr;
    private String mEditTipsStr;
    private String mRightBtnStr;

    private FamilyMemberInfoPresenter mFamilyMemberInfoPresenter;

    public static final int TYPE_FROM_USERINFO = 0;//个人信息
    public static final int TYPE_FROM_FAMILY_MEMBER = 1;//家庭成员信息
    public static final int TYPE_FROM_DISEASE_NAME = 2;//疾病名称

    public final static String REQUEST_CONTENT = "content";
    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_self_definition);
        ButterKnife.bind(this);

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            mFromType = bundle.getInt(RouterActivityUtil.FROM_TAG);
            mFromContentStr = bundle.getString(RouterActivityUtil.FROM_TAG1);
        }

        switch (mFromType){
            case TYPE_FROM_USERINFO:
            case TYPE_FROM_FAMILY_MEMBER:
                mTitleStr = "昵称";
                mRightBtnStr = "保存";
                mEditTipsStr = "请输入您的昵称";
                break;
            case TYPE_FROM_DISEASE_NAME:
                mTitleStr = "疾病名称";
                mRightBtnStr = "确认";
                mEditTipsStr = "请输入疾病名称";
                break;
        }

        setTitle(mTitleStr);
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText(mRightBtnStr);
        mInputEdit.setHint(mEditTipsStr);
        mInputEdit.setText(mFromContentStr);
        mInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mInputEdit.getText().toString();
                switch (mFromType){
                    case TYPE_FROM_FAMILY_MEMBER:
                    case TYPE_FROM_USERINFO:{
                        if(content.length()>5){
                            ToastUtil.getInstance().showToastDialog("昵称不能超过5个字符");
                            mInputEdit.setText(content.substring(0,5));
                            mInputEdit.setSelection(5);
                            return;
                        }
                        break;
                    }
                    case TYPE_FROM_DISEASE_NAME:
                        break;

                }
            }
        });
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        String inputContent = mInputEdit.getText().toString();
        if(EmptyUtils.isEmpty(inputContent)){
            ToastUtil.getInstance().toast(mEditTipsStr);
            return;
        }
        setResult(RESULT_OK, new Intent().putExtra(REQUEST_CONTENT, inputContent));
        finish();
    }
}
