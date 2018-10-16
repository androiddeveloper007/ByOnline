package com.bowen.commonlib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.R;



/**
 * Toast工具累
 * Created by AwenZeng on 2016/12/2.
 */

public class ToastUtil {

    private Toast toast;
    private Toast toastDialog;
//    private ToastDialog toastDialog;

    private static ToastUtil instance;

    public static ToastUtil getInstance(){
        if(instance == null){
            synchronized (ToastUtil.class){
                if(instance == null){
                    instance = new ToastUtil();
                }
            }
        }
        return instance;
    }


    public void toast(String content){
        if(!TextUtils.isEmpty(content)){
            showShort(content);
        }
    }

    public void showToastDialog(String message) {
        LayoutInflater layoutInflater = (LayoutInflater) CommonLibApplication.getCommonLibContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_toast,null);
        TextView mContentTv = (TextView)view.findViewById(R.id.mContentTv);
        mContentTv.setText(message);
        if(toastDialog == null){
            toastDialog = new Toast(CommonLibApplication.getCommonLibContext());
            toastDialog.setGravity(Gravity.CENTER, 0, 0);
            toastDialog.setDuration(Toast.LENGTH_SHORT);
            toastDialog.setView(view);
        }else{
            toastDialog.setGravity(Gravity.CENTER, 0, 0);
            toastDialog.setDuration(Toast.LENGTH_SHORT);
            toastDialog.setView(view);
        }
        toastDialog.show();
    }

    /**
     * 短时间显示Toast
     * @param message
     */
    public void showShort(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(CommonLibApplication.getCommonLibContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public void showLong(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(CommonLibApplication.getCommonLibContext(), message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public void show(int message, int duration) {
        if (null == toast) {
            toast = Toast.makeText(CommonLibApplication.getCommonLibContext(), message, duration);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /** Hide the toast, if any. */
    public void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    public void closeToastDialog() {
        if(toastDialog!=null){
            toastDialog.cancel();
        }
    }

    public void closeAll() {
        if (null != toast) {
            toast.cancel();
        }
        if(toastDialog!=null){
            toastDialog.cancel();
        }
    }
}
