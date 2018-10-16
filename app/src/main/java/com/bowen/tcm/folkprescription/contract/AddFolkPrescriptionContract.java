package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface AddFolkPrescriptionContract {

    interface View {
        void uploadSuccess(Object list);

        void uploadFail(Object list);

        void loadApplyCrowdSuccess(List<ShowApplyCrowd> list);
    }

    interface Presenter {

    }
}
