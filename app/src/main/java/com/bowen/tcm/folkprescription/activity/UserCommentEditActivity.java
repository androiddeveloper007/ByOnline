package com.bowen.tcm.folkprescription.activity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;
import com.bowen.tcm.common.event.RefreshPrescriptionUserCommentEvent;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionDetailModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:用户评价,编辑页面
 * Created by zzp on 2018/6/25.
 */

public class UserCommentEditActivity extends BaseActivity {

    @BindView(R.id.userCommentEditText)
    EditText userCommentEditText;
    private FolkPrescriptionDetailModel mModel;
    private final static String PrescriptionId = "PrescriptionId";
    private String prescriptionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_user_comment_edit);
        ButterKnife.bind(this);
        mModel = new FolkPrescriptionDetailModel(this);
        setTitle("评论");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("提交");
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            prescriptionId = bundle.getString(PrescriptionId);
        }
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        if(userCommentEditText.getText().length()>0){
            mModel.addPrescriptionComment(prescriptionId, userCommentEditText.getText().toString(), new HttpTaskCallBack<Object>() {

                @Override
                public void onSuccess(HttpResult<Object> result) {
                    //TODO 刷新上一页面中用户评论列表
                    EventBus.getDefault().post(new RefreshPrescriptionUserCommentEvent());
                    finish();
                }

                @Override
                public void onFail(HttpResult<Object> result) {
                    ToastUtil.getInstance().showToastDialog(result.getMsg());
                }
            });
        }else{
            ToastUtil.getInstance().showToastDialog("请填写评论！");
        }
    }

}
