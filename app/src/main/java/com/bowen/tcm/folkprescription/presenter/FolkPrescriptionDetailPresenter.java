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
import com.bowen.tcm.common.bean.DoctorPrescriptionComment;
import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionContract;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionDetailContract;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionDetailModel;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionModel;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;

import java.util.List;

/**
 * Created by zzp on 2017/5/21.
 *偏方详情页，根据偏方id获取偏方分享的一些数据，数据提供类
 */
public class FolkPrescriptionDetailPresenter extends BasePresenter {

    private FolkPrescriptionDetailModel folkPrescriptionDetailModel;
    private FolkPrescriptionModel folkPrescriptionModel;
    private FolkPrescriptionDetailContract.View mView;

    public FolkPrescriptionDetailPresenter(Context context, FolkPrescriptionDetailContract.View view) {
        super(context);
        folkPrescriptionDetailModel = new FolkPrescriptionDetailModel(context);
        folkPrescriptionModel = new FolkPrescriptionModel(context);
        mContext = context;
        mView = view;
    }

    /***
     * 根据偏方名称,主治功能,用法用量搜索偏方
     * @param id
     */
    public void loadData(String id) {
        folkPrescriptionDetailModel.loadData(id, new HttpTaskCallBack<InfoFolkPrescription>() {
            @Override
            public void onSuccess(HttpResult<InfoFolkPrescription> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<InfoFolkPrescription> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
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

    /***
     * 偏方模块 / 分页获取医生偏方评论
     * @param pageNo
     * @param pageSize
     * @param prescriptionId
     */
    public void getDoctorPrescriptionCommentList(int pageNo, int pageSize, String prescriptionId) {
        folkPrescriptionDetailModel.getDoctorPrescriptionCommentList(pageNo, pageSize, prescriptionId, new HttpTaskCallBack<PrescriptionDoctorCommentRecord>() {
            @Override
            public void onSuccess(HttpResult<PrescriptionDoctorCommentRecord> result) {
                mView.getDoctorCommentSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<PrescriptionDoctorCommentRecord> result) {
                showToast(result.getMsg());
                mView.getDoctorCommentFail(result.getData());
            }
        });
    }

    /***
     * 偏方模块 / 分页获取用户偏方评论
     * @param pageNo
     * @param pageSize
     * @param prescriptionId
     */
    public void getUserPrescriptionCommentList(int pageNo, int pageSize, String prescriptionId) {
        folkPrescriptionDetailModel.getUserPrescriptionCommentList(pageNo, pageSize, prescriptionId, new HttpTaskCallBack<PrescriptionUserCommentRecord>() {
            @Override
            public void onSuccess(HttpResult<PrescriptionUserCommentRecord> result) {
                mView.getUserCommentSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<PrescriptionUserCommentRecord> result) {
                showToast(result.getMsg());
                mView.getUserCommentFail(result.getData());
            }
        });
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
                RouterActivityUtil.startActivity((Activity) mContext, QuickLoginActivity.class,bundle);
            } else {
                RouterActivityUtil.startActivity((Activity) mContext, LoginActivity.class);
            }
            return true;
        }
        return false;
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}
