package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.network.DiseaseInfo;
import com.bowen.tcm.common.bean.network.DiseaseInfoRecord;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface FitDiseaseSelectContract {

    interface View {
        void onLoadSuccess(DiseaseInfoRecord list);

        void onLoadFail(DiseaseInfoRecord list);
    }

    interface Presenter {

    }
}
