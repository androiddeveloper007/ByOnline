package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.mine.contract.FamilyMemberInfoContract;
import com.bowen.tcm.mine.model.FamilyMemberModel;
import com.bowen.tcm.main.model.DataUploadModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/3.
 */

public class    FamilyMemberInfoPresenter extends BasePresenter implements FamilyMemberInfoContract.Presenter{
    private FamilyMemberModel mFamilyMemberModel;
    private DataUploadModel mPhotoUploadModel;
    private FamilyMemberInfoContract.View mView;

    public FamilyMemberInfoPresenter(Context mContext,FamilyMemberInfoContract.View view) {
        super(mContext);
        mView = view;
        mFamilyMemberModel = new FamilyMemberModel(mContext);
        mPhotoUploadModel = new DataUploadModel(mContext);
    }

    @Override
    public void updatePhoto(String bizId, ArrayList<String> pics) {
        mPhotoUploadModel.uploadCompressPhoto(bizId, Constants.TYPE_UPLOAD_PHOTO_FAMILY_MEMBER, pics, new HttpTaskCallBack<List<PhotoFile>>() {
            @Override
            public void onSuccess(HttpResult<List<PhotoFile>> result) {
                mView.onUpdatePhotoSuccess(result.getData().get(0).getFileLink());
            }

            @Override
            public void onFail(HttpResult<List<PhotoFile>> result) {

            }
        });
    }

    @Override
    public void deleteFamilyMember(String familyId) {
        mFamilyMemberModel.deleteFamilyMember(familyId, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onDeleteMemberSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

    @Override
    public void updateFamilyMemberInfo(String familyId,String familyType, String familyNickname, String sex, String familyPhone, String birthday) {
        mFamilyMemberModel.updateFamilyMember(familyId, familyType, familyNickname, familyPhone, sex, birthday, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onUpdateInfoSuccess();
            }

            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

}
