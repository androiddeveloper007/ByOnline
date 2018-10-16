package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.mine.contract.UserInfoContract;
import com.bowen.tcm.mine.model.FamilyMemberModel;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class UserInfoPresenter extends BasePresenter {

    private FamilyMemberModel mFamilyMemberModel;
    private UserInfoContract.View mView;

    public UserInfoPresenter(Context mContext, UserInfoContract.View mView) {
        super(mContext);
        mFamilyMemberModel = new FamilyMemberModel(mContext);
        this.mView = mView;
    }

    public void getFamilyMembers() {
        mFamilyMemberModel.getFamilyMembers(new HttpTaskCallBack<ArrayList<FamilyMember>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<FamilyMember>> result) {
                mView.onGetFamilyMembersSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ArrayList<FamilyMember>> result) {
                mView.onGetFamilyMembersFail(result.getData());
            }
        });
    }

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
