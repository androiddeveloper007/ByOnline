package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */

public class AddMedicineDialog extends BaseDialog {
    @BindView(R.id.mMedicineNameEdit)
    EditText mMedicineNameEdit;
    @BindView(R.id.mVolmeEdit)
    EditText mVolmeEdit;
    @BindView(R.id.mCancleTv)
    TextView mCancleTv;
    @BindView(R.id.mOkTv)
    TextView mOkTv;

    private String nameStr;
    private String volmeStr;


    public AddMedicineDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_medicine);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.CENTER;
        ButterKnife.bind(this);
    }

    private boolean checkInputContent(){
        nameStr = mMedicineNameEdit.getText().toString();
        volmeStr = mVolmeEdit.getText().toString();
        if(EmptyUtils.isEmpty(nameStr)){
            ToastUtil.getInstance().showToastDialog("请输入药品名称");
            return false;
        }
        if(EmptyUtils.isEmpty(volmeStr)){
            ToastUtil.getInstance().showToastDialog("请输入药品用量");
            return false;
        }
        return true;
    }

    @OnClick({R.id.mCancleTv, R.id.mOkTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCancleTv:
                dismiss();
                break;
            case R.id.mOkTv:
                if(checkInputContent()){
                    if (mListener != null) {
                        mListener.onDataCallBack(nameStr,volmeStr);
                    }
                    dismiss();
                }
                break;
        }
    }
}
