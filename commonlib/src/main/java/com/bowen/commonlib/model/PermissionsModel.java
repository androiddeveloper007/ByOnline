package com.bowen.commonlib.model;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * 权限检测模块
 * Created by AwenZeng on 2017/3/3.
 */

public class PermissionsModel extends BaseModel {
    private RxPermissions rxPermissions;
    private String packageName = "";

    public interface PermissionListener {
        void onPermission(boolean isPermission);
    }


    public PermissionsModel(Context mContext) {
        super(mContext);
        rxPermissions = new RxPermissions((Activity) mContext);
        packageName = mContext.getPackageName();
    }

    public void checkCallPhonePermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线拨打电话权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkSendMessagePermission(final PermissionListener listener) {
        rxPermissions.request(permission.SEND_SMS).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线发送短信权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkCameraPermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线访问相机权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkContactsPermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线访问通讯录权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkWriteSDCardPermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启“博闻在线”访问存储空间的权限，便于保存数据。");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkReadSDCardPermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线数据读取权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkLocationPermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线定位权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    public void checkReadPhoneStatePermission(final PermissionListener listener) {
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    showAlert("请到设置->开启博闻在线电话状态权限");
                }
                if (listener != null) {
                    listener.onPermission(aBoolean);
                }
            }
        });
    }

    private void showAlert(String content) {
        AffirmDialog affirmDialog = new AffirmDialog(mContext, "温馨提示", content, "取消", "设置");
        affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
            @Override
            public void onCancle() {
            }

            @Override
            public void onOK() {
                gotoPermissionSetting();
            }
        });
        affirmDialog.show();
    }

    /**
     * 跳转到权限设置界面
     */
    private void gotoPermissionSetting() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivity(intent);
    }


}
