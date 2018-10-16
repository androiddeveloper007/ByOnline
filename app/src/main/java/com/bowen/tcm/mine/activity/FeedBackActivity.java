package com.bowen.tcm.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.awen.camera.view.TakePhotoActivity;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.dialog.ChooseImageDialog;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.mine.adapter.PhotoAdapter;
import com.bowen.tcm.mine.contract.FeedbackContract;
import com.bowen.tcm.mine.presenter.FeedbackPresenter;
import com.bowen.tcm.medicalrecord.adapter.MedicalRecordPhotoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */

public class FeedBackActivity extends BaseActivity implements FeedbackContract.View{
    @BindView(R.id.mContentEdit)
    EditText mContentEdit;
    @BindView(R.id.mContentCountTv)
    TextView mContentCountTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<String> feedbackPics;
    private PhotoAdapter mAdapter;
    private FeedbackPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        setTitle("吐槽我们");
        getTitleBar().getRightTextButton().setText("发送");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);

        init();

    }

    private void init(){
        mPresenter = new FeedbackPresenter(this,this);
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mContentCountTv.setText(mContentEdit.getText().toString().length() + "/500");
            }
        });
        feedbackPics = new ArrayList<>();
        feedbackPics.add("拍照");
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new PhotoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(feedbackPics);


        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mAdapter.getItem(position).equals("拍照")){
                    if(feedbackPics!=null&&feedbackPics.size()<4){
                        ChooseImageDialog chooseImageDialog = new ChooseImageDialog(FeedBackActivity.this);
                        chooseImageDialog.setChoosePicCount(4-feedbackPics.size());
                        chooseImageDialog.show();
                    }else{
                        ToastUtil.getInstance().showToastDialog("最多只能上传3张图片哦！！");
                    }
                }else{
                    ImageView showImg = (ImageView) view.findViewById(R.id.mPhotoImg);
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(FeedBackActivity.this, showImg,
                            position + 1, ChooseTypeUtil.getShowBigPhotoList(feedbackPics));
                    showBigPicDialog.show();
                }
            }
        });

        mAdapter.setmListener(new MedicalRecordPhotoAdapter.DeletePhotoListener() {
            @Override
            public void onDelete(View view) {
                int pos = (int)view.getTag();
                feedbackPics.remove(pos);
                feedbackPics = mPresenter.handleList(feedbackPics);
                mAdapter.setNewData(feedbackPics);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case ImageSelectorActivity.REQUEST_CAMERA: {//拍照获取图片
                    String path = data.getStringExtra(TakePhotoActivity.RESULT_PHOTO_PATH);
                    feedbackPics.add(path);
                    feedbackPics = mPresenter.handleList(feedbackPics);
                    mAdapter.setNewData(feedbackPics);
                    LogUtil.androidLog("图片地址：" + path);
                    break;
                }
                case ImageSelectorActivity.REQUEST_IMAGE: {//从图库中选择图片
                    ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    feedbackPics.addAll(images);
                    feedbackPics = mPresenter.handleList(feedbackPics);
                    mAdapter.setNewData(feedbackPics);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        String feedbackContent = mContentEdit.getText().toString();

        if(EmptyUtils.isEmpty(feedbackContent)){
            ToastUtil.getInstance().showToastDialog("请输入反馈内容");
            return;
        }

        if(EmptyUtils.isNotEmpty(feedbackPics)&&feedbackPics.size()>1){
            mPresenter.uploadFeedBackData(feedbackContent,feedbackPics);
        }else{
            mPresenter.submitFeedBackData(feedbackContent);
        }
    }


    @Override
    public void onFeedBackSuccess() {
        ToastUtil.getInstance().toast("提交成功");
        finish();
    }

}
