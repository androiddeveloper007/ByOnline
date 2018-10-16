package com.bowen.commonlib.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;


/**
 * Activity跳转路由
 * Created by AwenZeng on 2016/12/14.
 */

public class RouterActivityUtil {


    public static String FROM_TAG = "fromTag";
    public static String FROM_TAG1 = "fromTag1";

//    public static Bundle  getBundle(){
//        return new Bundle();
//    }
//
//    public static Bundle putInt(String key,int value){
//        Bundle bundle = getBundle();
//        bundle.putInt(key,value);
//        return bundle;
//    }
//
//    public static Bundle putString(String key,String value){
//        Bundle bundle = getBundle();
//        bundle.putString(key,value);
//        return bundle;
//    }
//
//    public static Bundle putString(String key,Serializable value){
//        Bundle bundle = getBundle();
//        bundle.putSerializable(key,value);
//        return bundle;
//    }

    public static void  startActivity(Activity mActivity, Class<?> cls){
        Intent intent = new Intent(mActivity,cls);
        mActivity.startActivity(intent);
    }

    public static void  startActivity(Activity mActivity, Class<?> cls,Bundle bundle){
        Intent intent = new Intent(mActivity,cls);
        intent.putExtra(FROM_TAG,bundle);
        mActivity.startActivity(intent);
    }


    public static void  startActivity(Activity mActivity, Class<?> cls,boolean isFinish){
        Intent intent = new Intent(mActivity,cls);
        mActivity.startActivity(intent);
        if(isFinish){
            mActivity.finish();
        }
    }
    public static void  startActivity(Activity mActivity, Class<?> cls,Bundle bundle,boolean isFinish){
        Intent intent = new Intent(mActivity,cls);
        intent.putExtra(FROM_TAG,bundle);
        mActivity.startActivity(intent);
        if(isFinish){
            mActivity.finish();
        }
    }

    public static void startActivityResult(Activity mActivity, Class<?> cls,int requestCode){
        Intent intent = new Intent(mActivity,cls);
        mActivity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityResult(Activity mActivity, Class<?> cls,int requestCode,Bundle bundle){
        Intent intent = new Intent(mActivity,cls);
        intent.putExtra(FROM_TAG,bundle);
        mActivity.startActivityForResult(intent, requestCode);
    }


    public static Bundle getBundle(Activity activity){
        return activity.getIntent().getBundleExtra(FROM_TAG);
    }

    public static String getString(Activity activity){
        Bundle bundle = activity.getIntent().getBundleExtra(FROM_TAG);
        if(bundle!=null){
            return bundle.getString(RouterActivityUtil.FROM_TAG);
        }
        return "";
    }


    public static int getInt(Activity activity){
        Bundle bundle = activity.getIntent().getBundleExtra(FROM_TAG);
        if(bundle!=null){
            return bundle.getInt(RouterActivityUtil.FROM_TAG);
        }
        return 0;
    }

    public static boolean getBoolean(Activity activity){
        Bundle bundle = activity.getIntent().getBundleExtra(FROM_TAG);
        if(bundle!=null){
            return bundle.getBoolean(RouterActivityUtil.FROM_TAG);
        }
        return false;
    }

    public static Object getSerializable(Activity activity){
        Bundle bundle = activity.getIntent().getBundleExtra(FROM_TAG);
        if(bundle!=null){
            return bundle.getSerializable(RouterActivityUtil.FROM_TAG);
        }
        return null;
    }
}
