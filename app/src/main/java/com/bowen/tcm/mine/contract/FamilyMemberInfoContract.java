package com.bowen.tcm.mine.contract;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface FamilyMemberInfoContract {

    interface View {
        void onUpdateInfoSuccess();
        void onUpdatePhotoSuccess(String picUrl);
        void onDeleteMemberSuccess();
    }

    interface Presenter{
        void updateFamilyMemberInfo(String familyId,String familyType, String familyNickname,String familyPhone, String sex,String birthday);
        void updatePhoto(String bizId,ArrayList<String> pics);
        void deleteFamilyMember(String familyId);
    }
}
