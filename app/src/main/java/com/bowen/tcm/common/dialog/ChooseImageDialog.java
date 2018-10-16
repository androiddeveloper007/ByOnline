package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.awen.camera.view.TakePhotoActivity;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AwenZeng on 2016/12/13.
 */

public class ChooseImageDialog extends BaseDialog {

    @BindView(R.id.cameraBtn)
    TextView cameraBtn;
    @BindView(R.id.imageBtn)
    TextView imageBtn;
    @BindView(R.id.cancelBtn)
    TextView cancelBtn;

    private BaseActivity mActivity;
    private int choosePicCount;
    private static final int CHOOSE_PICTURE_MAX = 9;

    public ChooseImageDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
        mActivity = (BaseActivity) context;
        choosePicCount = CHOOSE_PICTURE_MAX;
    }

    public ChooseImageDialog(Context context, int themeResId) {
        super(context, themeResId);
        mActivity = (BaseActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_image);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        cameraBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        PermissionsModel permissionsModel = new PermissionsModel(mActivity);
        switch (v.getId()) {
            case R.id.cameraBtn:
                permissionsModel.checkCameraPermission(new PermissionsModel.PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission) {
                            Intent intent = new Intent(mActivity, TakePhotoActivity.class);
                            mActivity.startActivityForResult(intent, ImageSelectorActivity.REQUEST_CAMERA);
                        }
                    }
                });
                break;
            case R.id.imageBtn:
                permissionsModel.checkReadSDCardPermission(new PermissionsModel.PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission) {
                            ImageSelectorActivity.start(mActivity, choosePicCount, ImageSelectorActivity.MODE_MULTIPLE,
                                    false, true, false);
                        }
                    }
                });

                break;
            case R.id.cancelBtn:
                break;
        }
        dismiss();

    }

    public int getChoosePicCount() {
        return choosePicCount;
    }

    public void setChoosePicCount(int choosePicCount) {
        if(choosePicCount>0){
            this.choosePicCount = choosePicCount;
        }else{
            this.choosePicCount = 0;
        }
    }
}
