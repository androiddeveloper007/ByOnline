package com.bowen.tcm.mine.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.camera.view.TakePhotoActivity;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.dialog.ChooseImageDialog;
import com.bowen.tcm.common.dialog.ChooseYearMonthDialog;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.main.model.DataUploadModel;
import com.bowen.tcm.mine.contract.UserInfoContract;
import com.bowen.tcm.mine.model.UserInfoModel;
import com.bowen.tcm.mine.presenter.UserInfoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class UserInfoActivity extends BaseActivity implements UserInfoContract.View{

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mUserInfoTopLayout)
    LinearLayout mUserInfoTopLayout;
    @BindView(R.id.mNameTv)
    TextView mNameTv;
    @BindView(R.id.mPhoneNumTv)
    TextView mPhoneNumTv;
    @BindView(R.id.mPhoneNumLayout)
    RelativeLayout mPhoneNumLayout;
    @BindView(R.id.familyMemberSexBoy)
    TextView familyMemberSexBoy;
    @BindView(R.id.familyMemberSexGirl)
    TextView familyMemberSexGirl;
    @BindView(R.id.birthdayTv)
    TextView birthdayTv;
    private String selectedBirthday;
    private DataUploadModel mPhotoUploadModel;
    private UserInfoModel mUserInfoModel;
    private UserInfoPresenter mPresenter;
    private FamilyMember member;
    ChooseYearMonthDialog chooseTimeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setTitle("个人信息");
        mPhotoUploadModel = new DataUploadModel(this);
        mUserInfoModel = new UserInfoModel(this);
        mPresenter = new UserInfoPresenter(this,this);
        mPresenter.getFamilyMembers();
    }

    private void updateUI() {
        ImageLoaderUtil.getInstance().show(this, UserInfo.getInstance().getPicUrl(), mHeadPortraitImg, R.drawable.man);
        mNameTv.setText(UserInfo.getInstance().getUserNickname());
        mPhoneNumTv.setText(UserInfo.getInstance().getUserMobile());
        if(member!=null){
            switchSelected(TextUtils.equals(Constants.SEX_BOY, member.getSex()));
            String str = DateUtil.date2String(member.getBirthday(), DateUtil.DEFAULT_FORMAT_DATE);
            birthdayTv.setText(str.substring(0, str.length() - 3));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case ImageSelectorActivity.REQUEST_CAMERA: {//拍照获取图片
                    String path = data.getStringExtra(TakePhotoActivity.RESULT_PHOTO_PATH);
                    LogUtil.androidLog("图片地址：" + path);
                    ArrayList<String> pics = new ArrayList<>();
                    pics.add(path);
                    uploadPhoto(pics);
                    break;
                }
                case ImageSelectorActivity.REQUEST_IMAGE: {//从图库中选择图片
                    ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    uploadPhoto(images);
                    break;
                }

                case SelfDefinitionActivity.REQUEST_CODE: {
                    String content = data.getStringExtra(SelfDefinitionActivity.REQUEST_CONTENT);
                    updateUserInfo(content);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.mHeadPortraitImg, R.id.mNameTv, R.id.familyMemberSexBoy, R.id.familyMemberSexGirl, R.id.selectBirthdayLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mHeadPortraitImg:
                ChooseImageDialog chooseImageDialog = new ChooseImageDialog(UserInfoActivity.this);
                chooseImageDialog.setChoosePicCount(1);
                chooseImageDialog.show();
                break;
            case R.id.mNameTv:
                Bundle bundle = new Bundle();
                bundle.putInt(RouterActivityUtil.FROM_TAG, SelfDefinitionActivity.TYPE_FROM_USERINFO);
                bundle.putString(RouterActivityUtil.FROM_TAG1, UserInfo.getInstance().getUserNickname());
                RouterActivityUtil.startActivityResult(this, SelfDefinitionActivity.class, SelfDefinitionActivity.REQUEST_CODE, bundle);
                break;
            case R.id.familyMemberSexBoy:
                switchSelected(true);
                mPresenter.updateFamilyMemberInfo(UserInfo.getInstance().getFamilyId(),null,null,"1",null,null);
                break;
            case R.id.familyMemberSexGirl:
                switchSelected(false);
                mPresenter.updateFamilyMemberInfo(UserInfo.getInstance().getFamilyId(),null,null,"2",null,null);
                break;
            case R.id.selectBirthdayLayout:
                if(chooseTimeDialog==null){
                    if (member != null && member.getBirthday() != 0) {
                        String dateStr = DateUtil.date2String(member.getBirthday(), DateUtil.DEFAULT_FORMAT_DATE);
                        chooseTimeDialog = new ChooseYearMonthDialog(this, Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5, 7)));
                    } else {
                        chooseTimeDialog = new ChooseYearMonthDialog(this);
                    }
                    chooseTimeDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                        @Override
                        public void onDataCallBack(Object... obj) {
                            selectedBirthday = obj[0] + "";
                            birthdayTv.setText(selectedBirthday);
                            selectedBirthday = obj[0] + "-01";
                            mPresenter.updateFamilyMemberInfo(member.getFamilyId(), null,
                                    null, null, null, selectedBirthday);
                        }
                    });
                }
                chooseTimeDialog.show();
                break;
        }
    }

    private void switchSelected(boolean isBoySelected) {
        familyMemberSexBoy.setTextColor(Color.parseColor(isBoySelected ? "#ffffff":"#253231"));
        familyMemberSexBoy.setSelected(isBoySelected);
        familyMemberSexGirl.setTextColor(Color.parseColor(!isBoySelected ? "#ffffff":"#253231"));
        familyMemberSexGirl.setSelected(!isBoySelected);
    }

    private void uploadPhoto(ArrayList<String> pics) {
        mPhotoUploadModel.uploadCompressPhoto(UserInfo.getInstance().getFamilyId(), Constants.TYPE_UPLOAD_PHOTO_FAMILY_MEMBER, pics, new HttpTaskCallBack<List<PhotoFile>>() {
            @Override
            public void onSuccess(HttpResult<List<PhotoFile>> result) {
                UserInfo.getInstance().setPicUrl(result.getData().get(0).getFileLink());
                updateUI();
            }
            @Override
            public void onFail(HttpResult<List<PhotoFile>> result) {

            }
        });
    }

    private void updateUserInfo(final String name) {
        mUserInfoModel.updateUserNick(name, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                UserInfo.getInstance().setUserNickname(name);
                updateUI();
            }
            @Override
            public void onFail(HttpResult result) {

            }
        });
    }

    @Override
    public void onUpdateInfoSuccess() {

    }

    @Override
    public void onGetFamilyMembersSuccess(ArrayList<FamilyMember> list) {
        if(EmptyUtils.isNotEmpty(list) && list.size()>0){
            member = list.get(0);//本人
            updateUI();
        }
    }

    @Override
    public void onGetFamilyMembersFail(ArrayList<FamilyMember> list) {

    }
}
