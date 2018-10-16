package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.mine.contract.FamilyMemberContract;
import com.bowen.tcm.mine.model.FamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class FamilyMemberPresenter extends BasePresenter  implements FamilyMemberContract.Presenter {

    private FamilyMemberModel mFamilyMemberModel;
    private FamilyMemberContract.View mView;

    public FamilyMemberPresenter(Context mContext,FamilyMemberContract.View view) {
        super(mContext);
        mView = view;
        mFamilyMemberModel = new FamilyMemberModel(mContext);
    }

    @Override
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

    @Override
    public void getFamilyUserRelation() {
        mFamilyMemberModel.getFamilyUserRelationShip();
    }

    @Override
    public void updateFamilyMemberInfo(String familyId, String familyType, String familyNickname, String familyPhone, String sex, String birthday) {
        mFamilyMemberModel.updateFamilyMember(familyId, familyType, familyNickname, familyPhone, sex, birthday, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onUpdateInfoSuccess();
            }

            @Override
            public void onFail(HttpResult result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }
}
