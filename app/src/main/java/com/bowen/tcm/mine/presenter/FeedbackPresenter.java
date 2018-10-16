package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.main.model.DataUploadModel;
import com.bowen.tcm.mine.contract.FeedbackContract;
import com.bowen.tcm.mine.model.FeedBackModel;
import com.bowen.tcm.mine.model.FeedBackModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/16.
 */

public class FeedbackPresenter extends BasePresenter implements FeedbackContract.Presenter {

    private FeedBackModel mFeedbackModel;
    private DataUploadModel mDataUploadModel;
    private FeedbackContract.View mView;

    public FeedbackPresenter(Context mContext,FeedbackContract.View view) {
        super(mContext);
        mView = view;
        mFeedbackModel = new FeedBackModel(mContext);
        mDataUploadModel = new DataUploadModel(mContext);
    }

    @Override
    public void uploadFeedBackData(String content, List<String> pics) {
        mDataUploadModel.uploadFeedBackData(content, pics, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onFeedBackSuccess();
            }

            @Override
            public void onFail(HttpResult result) {
                ToastUtil.getInstance().showToastDialog("反馈提交失败，请重新提交");
            }
        });

    }

    @Override
    public void submitFeedBackData(String content) {
       mFeedbackModel.submitFeedbackData(content, new HttpTaskCallBack() {
           @Override
           public void onSuccess(HttpResult result) {
               mView.onFeedBackSuccess();
           }

           @Override
           public void onFail(HttpResult result) {
               ToastUtil.getInstance().showToastDialog("反馈提交失败，请重新提交");
           }
       });
    }


    public ArrayList<String> handleList(ArrayList<String> list){
        ArrayList<String> temp = new ArrayList<>();
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals("拍照")){
                iterator.remove();
            }
        }
        temp.addAll(list);
        temp.add("拍照");
        return temp;
    }
}
