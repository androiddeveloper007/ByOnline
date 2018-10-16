package com.bowen.tcm.folkprescription.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionContract;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionModel;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;

import java.util.List;

/**
 * Created by zzp on 2017/5/21.
 *偏方数据提供类
 */
public class FolkPrescriptionPresenter extends BasePresenter {

    private FolkPrescriptionModel folkPrescriptionModel;
    private FolkPrescriptionContract.View mView;

    public FolkPrescriptionPresenter(Context context, FolkPrescriptionContract.View view) {
        super(context);
        folkPrescriptionModel = new FolkPrescriptionModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 根据偏方名称,主治功能,用法用量搜索偏方
     */
    public void loadData(String searchInfo, int index, int pageSize) {
        folkPrescriptionModel.loadData(searchInfo, index, pageSize, new HttpTaskCallBack<FolkPrescriptionRecord>() {
            @Override
            public void onSuccess(HttpResult<FolkPrescriptionRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<FolkPrescriptionRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    /**
     * 根据适应人群,所属科室搜索偏方
     */
    public void getListByApplyDepartments(String hospitalDepartments, String applyCrowd, int index, int pageSize) {
        folkPrescriptionModel.getListByApplyDepartments(hospitalDepartments, applyCrowd, index, pageSize, new HttpTaskCallBack<FolkPrescriptionRecord>() {
            @Override
            public void onSuccess(HttpResult<FolkPrescriptionRecord> result) {
                mView.onLoadByApplyDepartmentsSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<FolkPrescriptionRecord> result) {
                mView.onLoadByApplyDepartmentsFail(result.getData());
            }
        });
    }

    /**
     * 加载侧滑菜单列表1
     */
    public void loadRightDrawerLists() {
        folkPrescriptionModel.loadRightDrawerLists(new HttpTaskCallBack<List<HospitalDepartments>>() {
            @Override
            public void onSuccess(HttpResult<List<HospitalDepartments>> result) {
                mView.loadRightDrawerListsSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<List<HospitalDepartments>> result) {
                showToast(result.getMsg());
            }
        });
    }

    /**
     * 加载侧滑菜单列表2
     *
     */
    public void loadRightDrawerLists1() {
        folkPrescriptionModel.loadRightDrawerLists1(new HttpTaskCallBack<List<ShowApplyCrowd>>() {
            @Override
            public void onSuccess(HttpResult<List<ShowApplyCrowd>> result) {
                mView.loadRightDrawerLists1Success(result.getData());
            }

            @Override
            public void onFail(HttpResult<List<ShowApplyCrowd>> result) {
                showToast(result.getMsg());
            }
        });
    }

    /***
     * 收藏偏方
     * @param isCollect true表示收藏， false表示取消收藏
     */
    public void folkPrescriptionCollectOrNot(final boolean isCollect, String prescriptionId) {
        folkPrescriptionModel.folkPrescriptionCollectOrNot(isCollect, prescriptionId, new HttpTaskCallBack<String>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                showToast(isCollect ? "成功加入偏方收藏":"已取消偏方收藏");
            }

            @Override
            public void onFail(HttpResult<String> result) {
                showToast(isCollect ? "加入偏方收藏失败":"取消偏方收藏失败");
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }

    public boolean hasNotLogin() {
        if (LoginStatusUtil.getInstance().isLogin()) {
            if (!UserInfo.getInstance().isBindPhone()) {
                RouterActivityUtil.startActivity((Activity) mContext, BindingPhoneActivity.class);
                return true;
            }
        } else {
            if (EmptyUtils.isNotEmpty(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""))) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(RouterActivityUtil.FROM_TAG, false);
                RouterActivityUtil.startActivity((Activity) mContext, QuickLoginActivity.class);
            } else {
                RouterActivityUtil.startActivity((Activity) mContext, LoginActivity.class);
            }
            return true;
        }
        return false;
    }
}
