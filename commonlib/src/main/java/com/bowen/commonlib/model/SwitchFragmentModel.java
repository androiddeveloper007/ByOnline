package com.bowen.commonlib.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bowen.commonlib.base.BaseLibActivity;
import com.bowen.commonlib.base.BaseLibFragment;


/**
 * Created by AwenZeng on 2017/1/20.
 */

public class SwitchFragmentModel {
    private Context mContext;
    private Fragment currentFragment;

    public SwitchFragmentModel(Context mContext) {
        this.mContext = mContext;
    }

    public void replace(BaseLibFragment fragment, int id){
        FragmentManager fragmentManager = ((BaseLibActivity)mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
    public void replace(FragmentTransaction fragmentTransaction, BaseLibFragment fragment, int id){
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }


    public void add(Fragment fragment, int id){
        FragmentManager fragmentManager = ((BaseLibActivity)mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragment.isAdded()){
            addOrShowFragment(fragmentTransaction,fragment,id);
        }else{
            if(currentFragment!=null&&currentFragment.isAdded()){
                fragmentTransaction.hide(currentFragment).add(id, fragment).commit();
            }else{
                fragmentTransaction.add(id, fragment).commit();
            }
            currentFragment = fragment;
        }
    }

    public void add(FragmentTransaction transaction, BaseLibFragment fragment, int id){
        if(fragment.isAdded()){
            addOrShowFragment(transaction,fragment,id);
        }else{
            if(currentFragment!=null&&currentFragment.isAdded()){
                transaction.hide(currentFragment).add(id, fragment).commit();
            }else{
                transaction.add(id, fragment).commit();
            }
            currentFragment = fragment;
        }
    }
    /**
     * 添加或者显示 fragment
     *
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment, int id) {
        if(currentFragment == fragment)
            return;
        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(id, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment.setUserVisibleHint(false);
        currentFragment =  fragment;
        currentFragment.setUserVisibleHint(true);
    }
}
