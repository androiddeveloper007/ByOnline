package com.bowen.tcm.mine.contract;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public interface AddFamilyMemberContract {

    interface View {
        void onSaveFamilyMemberSuccess();
        void onUpdateInfoSuccess();
    }

    interface Presenter{
       void saveFamilyMember(String mRelationShipStr, String mNickNameStr, ArrayList<String> pics, String sex, String birthday);
       void saveFamilyMember(String mRelationShipStr, String mNickNameStr, String sex, String birthday);
    }
}
