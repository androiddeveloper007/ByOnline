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
import com.awen.contact.model.ContactsInfo;
import com.awen.contact.view.ContactSelectorActivity;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.dialog.ChooseContentDialog;
import com.bowen.tcm.common.dialog.ChooseImageDialog;
import com.bowen.tcm.common.dialog.ChooseYearMonthDialog;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.mine.contract.FamilyMemberInfoContract;
import com.bowen.tcm.mine.presenter.FamilyMemberInfoPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/2.
 */

public class FamilyMemberInfoActivity extends BaseActivity implements FamilyMemberInfoContract.View {

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mUserInfoTopLayout)
    LinearLayout mUserInfoTopLayout;
    @BindView(R.id.mNameTv)
    TextView mNameTv;
    @BindView(R.id.mRelationShipTv)
    TextView mRelationShipTv;
    @BindView(R.id.mRelationShipLayout)
    RelativeLayout mRelationShipLayout;
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

    private FamilyMember mFamilyMember;
    private FamilyMemberInfoPresenter mPresenter;
    private String phone;
    private String nickName;
    private String relation;
    private String selectedBirthday;
    ChooseYearMonthDialog chooseTimeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_family_member_info);
        ButterKnife.bind(this);
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("删除");
        mPresenter = new FamilyMemberInfoPresenter(this, this);
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            mFamilyMember = (FamilyMember) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
        }
        updateUI();
    }

    private void updateUI() {
        if (EmptyUtils.isNotEmpty(mFamilyMember)) {
            ImageLoaderUtil.getInstance().show(this, mFamilyMember.getHeadSculptureUrl(), mHeadPortraitImg, R.drawable.man);
            setTitle(mFamilyMember.getFamilyNickname());
            mNameTv.setText(mFamilyMember.getFamilyNickname());
            mRelationShipTv.setText(mFamilyMember.getFamilyTypeTxt());
            if (EmptyUtils.isNotEmpty(mFamilyMember.getFamilyPhone())) {
                mPhoneNumTv.setText(mFamilyMember.getFamilyPhone());
                mPhoneNumLayout.setVisibility(View.VISIBLE);
            } else {
                mPhoneNumLayout.setVisibility(View.GONE);
            }
            if (EmptyUtils.isNotEmpty(mFamilyMember.getBirthday())) {
                String str = DateUtil.date2String(mFamilyMember.getBirthday(), DateUtil.DEFAULT_FORMAT_DATE);
                birthdayTv.setText(str.substring(0, str.length() - 3));
            }
            //本人时隐藏删除按钮
            if (mFamilyMember.getFamilyType().equals("0")) {
                mPhoneNumTv.setCompoundDrawables(null, null, null, null);
                mRelationShipTv.setCompoundDrawables(null, null, null, null);
                mPhoneNumLayout.setEnabled(false);
                mRelationShipLayout.setEnabled(false);
                getTitleBar().getRightTextButton().setVisibility(View.GONE);
            }
            switchSelected(TextUtils.equals("1", mFamilyMember.getSex()));
        }
    }

    private void switchSelected(boolean isBoySelected) {
        familyMemberSexBoy.setTextColor(Color.parseColor(isBoySelected ? "#ffffff" : "#a4a4a4"));
        familyMemberSexBoy.setSelected(isBoySelected);
        familyMemberSexGirl.setTextColor(Color.parseColor(!isBoySelected ? "#ffffff" : "#a4a4a4"));
        familyMemberSexGirl.setSelected(!isBoySelected);
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        AffirmDialog affirmDialog = new AffirmDialog(this, "您确认要删除该家庭成员吗？相应的病历档案也会同步删除");
        affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
            @Override
            public void onCancle() {

            }

            @Override
            public void onOK() {
                mPresenter.deleteFamilyMember(mFamilyMember.getFamilyId());
            }
        });
        affirmDialog.show();


    }

    @OnClick({R.id.mHeadPortraitImg, R.id.mRelationShipLayout, R.id.mPhoneNumLayout, R.id.mNameTv
            , R.id.familyMemberSexBoy, R.id.familyMemberSexGirl, R.id.selectBirthdayLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mHeadPortraitImg:
                ChooseImageDialog chooseImageDialog = new ChooseImageDialog(FamilyMemberInfoActivity.this);
                chooseImageDialog.setChoosePicCount(1);
                chooseImageDialog.show();
                break;
            case R.id.mNameTv:
                Bundle bundle = new Bundle();
                bundle.putInt(RouterActivityUtil.FROM_TAG, SelfDefinitionActivity.TYPE_FROM_FAMILY_MEMBER);
                bundle.putString(RouterActivityUtil.FROM_TAG1, mFamilyMember.getFamilyNickname());
                RouterActivityUtil.startActivityResult(this, SelfDefinitionActivity.class, SelfDefinitionActivity.REQUEST_CODE, bundle);
                break;
            case R.id.mPhoneNumLayout:
                chooseContacts();
                break;
            case R.id.familyMemberSexBoy:
                switchSelected(true);
                mPresenter.updateFamilyMemberInfo(mFamilyMember.getFamilyId(), null,
                        null, "1", null, null);
                break;
            case R.id.familyMemberSexGirl:
                switchSelected(false);
                mPresenter.updateFamilyMemberInfo(mFamilyMember.getFamilyId(), null,
                        null, "2", null, null);
                break;
            case R.id.selectBirthdayLayout:
                if(chooseTimeDialog==null){
                    if (mFamilyMember != null && mFamilyMember.getBirthday() != 0) {
                        String dateStr = DateUtil.date2String(mFamilyMember.getBirthday(), DateUtil.DEFAULT_FORMAT_DATE);
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
                            mPresenter.updateFamilyMemberInfo(mFamilyMember.getFamilyId(), null,
                                    null, null, null, selectedBirthday);
                        }
                    });
                }
                chooseTimeDialog.show();
                break;
            case R.id.mRelationShipLayout:
                ChooseContentDialog dialog;
                if (mFamilyMember != null && mFamilyMember.getFamilyTypeTxt().length() > 0) {
                    dialog = new ChooseContentDialog(FamilyMemberInfoActivity.this, mFamilyMember.getFamilyTypeTxt());
                } else {
                    dialog = new ChooseContentDialog(FamilyMemberInfoActivity.this);
                }
                dialog.setTitleStr("家人关系");
                dialog.setList(ChooseTypeUtil.getFamilyUserRelations());
                dialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        relation = (String) obj[0];
                        mPresenter.updateFamilyMemberInfo(mFamilyMember.getFamilyId(), ChooseTypeUtil.getFamilyRelationCode(relation),
                                null, null, null, null);
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case ImageSelectorActivity.REQUEST_CAMERA: {//拍照获取图片
                    String path = data.getStringExtra(TakePhotoActivity.RESULT_PHOTO_PATH);
                    ArrayList<String> pics = new ArrayList<>();
                    pics.add(path);
                    mPresenter.updatePhoto(mFamilyMember.getFamilyId(), pics);
                    break;
                }
                case ImageSelectorActivity.REQUEST_IMAGE: {//从图库中选择图片
                    ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    mPresenter.updatePhoto(mFamilyMember.getFamilyId(), images);
                    break;
                }
                case ContactSelectorActivity.REQUEST_CONTACT:
                    ArrayList<ContactsInfo> contactsInfos = (ArrayList<ContactsInfo>) data.getSerializableExtra(ContactSelectorActivity.REQUEST_OUTPUT);
                    phone = contactsInfos.get(0).getPhone();
                    mPresenter.updateFamilyMemberInfo(mFamilyMember.getFamilyId(), null, null, phone, null, null);
                    break;
                case SelfDefinitionActivity.REQUEST_CODE:
                    nickName = data.getStringExtra(SelfDefinitionActivity.REQUEST_CONTENT);
                    mPresenter.updateFamilyMemberInfo(mFamilyMember.getFamilyId(), null, nickName, null, null, null);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void chooseContacts() {
        PermissionsModel permissionsModel = new PermissionsModel(this);
        permissionsModel.checkContactsPermission(new PermissionsModel.PermissionListener() {
            @Override
            public void onPermission(boolean isPermission) {
                if (isPermission) {
                    Intent intent = new Intent(FamilyMemberInfoActivity.this, ContactSelectorActivity.class);
                    startActivityForResult(intent, ContactSelectorActivity.REQUEST_CONTACT);
                }
            }
        });
    }

    @Override
    public void onUpdateInfoSuccess() {
        if (EmptyUtils.isNotEmpty(nickName)) {
            mNameTv.setText(nickName);
            UserInfo.getInstance().setUserNickname(nickName);
        }
        if (EmptyUtils.isNotEmpty(phone)) {
            mPhoneNumTv.setText(phone);
        }
        if (EmptyUtils.isNotEmpty(relation)) {
            mRelationShipTv.setText(relation);
            mRelationShipTv.setTextColor(getResources().getColor(R.color.color_main_black));
        }
    }


    @Override
    public void onUpdatePhotoSuccess(String picUrl) {
        ImageLoaderUtil.getInstance().show(FamilyMemberInfoActivity.this, picUrl, mHeadPortraitImg);
        if (!TextUtils.isEmpty(picUrl))
            UserInfo.getInstance().setPicUrl(picUrl);
    }

    @Override
    public void onDeleteMemberSuccess() {
        ToastUtil.getInstance().toast("删除成功");
        finish();
    }

}
