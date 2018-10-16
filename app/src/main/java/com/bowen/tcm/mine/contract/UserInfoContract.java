package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.FamilyMember;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface UserInfoContract {

    interface View {
        void onUpdateInfoSuccess();
        void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list);
        void onGetFamilyMembersFail(ArrayList<FamilyMember> list);
    }

    interface Presenter{

    }
}
