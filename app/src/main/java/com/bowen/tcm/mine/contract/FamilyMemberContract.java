package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.FamilyMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface FamilyMemberContract {

    interface View {
        void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list);
        void onGetFamilyMembersFail(ArrayList<FamilyMember> list);
        void onUpdateInfoSuccess();
    }

    interface Presenter{
        void getFamilyMembers();
        void getFamilyUserRelation();
        void updateFamilyMemberInfo(String familyId,String familyType, String familyNickname,String familyPhone, String sex,String birthday);
    }
}
