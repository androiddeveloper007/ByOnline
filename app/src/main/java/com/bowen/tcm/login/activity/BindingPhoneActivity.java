package com.bowen.tcm.login.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.login.model.RegistModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/10.
 */

public class BindingPhoneActivity extends BaseActivity {
    @BindView(R.id.mBackLayout)
    LinearLayout mBackLayout;
    @BindView(R.id.mPhoneNumEdit)
    EditText mPhoneNumEdit;
    @BindView(R.id.mImmediateBindingTv)
    TextView mImmediateBindingTv;

    private RegistModel mRegistModel;
    private String mPhoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_binding_phone);
        ButterKnife.bind(this);
        mRegistModel = new RegistModel(this);
    }

    @OnClick({R.id.mBackLayout, R.id.mImmediateBindingTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBackLayout:
                onBackPressed();
                break;
            case R.id.mImmediateBindingTv:
                mPhoneNum = mPhoneNumEdit.getText().toString();
                if(EmptyUtils.isEmpty(mPhoneNum)){
                    ToastUtil.getInstance().showToastDialog("请输入手机号");
                    return;
                }
                checkPhoneAccount();
                break;
        }
    }


    private void checkPhoneAccount(){
        mRegistModel.checkAccount(mPhoneNum, Constants.TYPE_CHECK_ACCOUNT_PHONE, new HttpTaskCallBack<Boolean>() {
            @Override
            public void onSuccess(HttpResult<Boolean> result) {
                if(!result.getData()){
                    Bundle bundle = new Bundle();
                    bundle.putString(RouterActivityUtil.FROM_TAG,mPhoneNum);
                    RouterActivityUtil.startActivity(BindingPhoneActivity.this,VerifiPhoneActivity.class,bundle);
                }else{
                    ToastUtil.getInstance().showToastDialog("账号已注册");
                }
            }

            @Override
            public void onFail(HttpResult<Boolean> result) {

            }
        });
    }
}
