package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.mine.contract.AddFamilyMemberContract;
import com.bowen.tcm.mine.model.AddFamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class AddFamilyMemberPresenter extends BasePresenter  implements AddFamilyMemberContract.Presenter {

    private AddFamilyMemberModel mAddFamilyMemberModel;
    private AddFamilyMemberContract.View mView;

    public AddFamilyMemberPresenter(Context mContext, AddFamilyMemberContract.View view) {
        super(mContext);
        mView = view;
        mAddFamilyMemberModel = new AddFamilyMemberModel(mContext);
    }

    public boolean checkContent(String nickNameStr, boolean isChooseRelation) {
        if (EmptyUtils.isEmpty(nickNameStr)) {
            ToastUtil.getInstance().showToastDialog("请输入家人昵称");
            return false;
        }
        if (!isChooseRelation) {
            ToastUtil.getInstance().showToastDialog("请选择关系");
            return false;
        }
        return true;
    }

    @Override
    public void saveFamilyMember(String mRelationShipStr, String mNickNameStr, ArrayList<String> pics, String sex, String birthday) {
        mAddFamilyMemberModel.saveFamilyMember(mRelationShipStr, mNickNameStr, sex, birthday, pics, new HttpTaskCallBack<List<FamilyMember>>() {
            @Override
            public void onSuccess(HttpResult<List<FamilyMember>> result) {
                mView.onSaveFamilyMemberSuccess();
            }

            @Override
            public void onFail(HttpResult<List<FamilyMember>> result) {

            }
        });
    }

    @Override
    public void saveFamilyMember(String mRelationShipStr, String mNickNameStr, String sex, String birthday) {
        mAddFamilyMemberModel.saveFamilyMember(mRelationShipStr, mNickNameStr, sex, birthday, new HttpTaskCallBack<List<FamilyMember>>() {
            @Override
            public void onSuccess(HttpResult<List<FamilyMember>> result) {
                mView.onSaveFamilyMemberSuccess();
            }

            @Override
            public void onFail(HttpResult<List<FamilyMember>> result) {

            }
        });
    }

    public void updateFamilyMemberInfo(String familyId,String familyType, String familyNickname,String familyPhone, String sex,String birthday) {
        mAddFamilyMemberModel.updateFamilyMember(familyId, familyType, familyNickname, familyPhone,sex, birthday, new HttpTaskCallBack() {
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
