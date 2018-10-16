package com.bowen.tcm.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awen.camera.view.TakePhotoActivity;
import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.commonlib.widget.InputEditText;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.dialog.ChooseContentDialog;
import com.bowen.tcm.common.dialog.ChooseImageDialog;
import com.bowen.tcm.common.dialog.ChooseYearMonthDialog;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.mine.contract.AddFamilyMemberContract;
import com.bowen.tcm.mine.presenter.AddFamilyMemberPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */

public class AddFamilyMemberActivity extends BaseActivity implements AddFamilyMemberContract.View{
    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mUserInfoTopLayout)
    LinearLayout mUserInfoTopLayout;
    @BindView(R.id.mNickNameEdit)
    InputEditText mNickNameEdit;
    @BindView(R.id.mRelationShipTv)
    TextView mRelationShipTv;
    @BindView(R.id.familyMemberSexBoy)
    TextView familyMemberSexBoy;
    @BindView(R.id.familyMemberSexGirl)
    TextView familyMemberSexGirl;
    @BindView(R.id.birthdayTv)
    TextView birthdayTv;
    private AddFamilyMemberPresenter mPresenter;
    private ArrayList<String> pics;
    private String mNickNameStr;
    private String mRelationShipStr;
    private boolean isChooseRelationShip = false;
    private String selectedBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_family_member);
        ButterKnife.bind(this);
        mPresenter= new AddFamilyMemberPresenter(this,this);
        initView();
    }

    private void initView() {
        setTitle("添加成员");
        getTitleBar().getRightTextButton().setText("添加");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        switchSelected(true);
        pics = new ArrayList<>();
    }

    private void switchSelected(boolean isBoySelected) {
        familyMemberSexBoy.setTextColor(Color.parseColor(isBoySelected ? "#ffffff":"#a4a4a4"));
        familyMemberSexBoy.setSelected(isBoySelected);
        familyMemberSexGirl.setTextColor(Color.parseColor(!isBoySelected ? "#ffffff":"#a4a4a4"));
        familyMemberSexGirl.setSelected(!isBoySelected);
    }

    private void getInputContent(){
        mNickNameStr = mNickNameEdit.getText().toString();
        mRelationShipStr = mRelationShipTv.getText().toString();
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        getInputContent();
        if(mPresenter.checkContent(mNickNameStr,isChooseRelationShip)){
            if(EmptyUtils.isEmpty(pics)){
                mPresenter.saveFamilyMember(ChooseTypeUtil.getFamilyRelationCode(mRelationShipStr),mNickNameStr, familyMemberSexBoy.isSelected() ? "1":"2", selectedBirthday);
            }else{
                mPresenter.saveFamilyMember(ChooseTypeUtil.getFamilyRelationCode(mRelationShipStr),mNickNameStr,pics, familyMemberSexBoy.isSelected() ? "1":"2", selectedBirthday);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case ImageSelectorActivity.REQUEST_CAMERA: {//拍照获取图片
                    pics.clear();
                    String path = data.getStringExtra(TakePhotoActivity.RESULT_PHOTO_PATH);
                    LogUtil.androidLog("图片地址：" + path);
                    pics.add(path);
                    ImageLoaderUtil.getInstance().show(this,path,mHeadPortraitImg);
                    break;
                }
                case ImageSelectorActivity.REQUEST_IMAGE: {//从图库中选择图片
                    pics.clear();
                    pics.addAll((ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT));
                    ImageLoaderUtil.getInstance().show(this,pics.get(0),mHeadPortraitImg);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.mHeadPortraitImg, R.id.mRelationShipTv, R.id.familyMemberSexBoy, R.id.familyMemberSexGirl,
    R.id.selectBirthdayLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mHeadPortraitImg:
                ChooseImageDialog chooseImageDialog = new ChooseImageDialog(AddFamilyMemberActivity.this);
                chooseImageDialog.setChoosePicCount(1);
                chooseImageDialog.show();
                break;
            case R.id.mRelationShipTv:
                ChooseContentDialog dialog = new ChooseContentDialog(AddFamilyMemberActivity.this);
                dialog.setTitleStr("家人关系");
                dialog.setList(ChooseTypeUtil.getFamilyUserRelations());
                dialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        isChooseRelationShip = true;
                        mRelationShipTv.setText((String)obj[0]);
                        mRelationShipTv.setTextColor(getResources().getColor(R.color.color_main_black));
                    }
                });
                dialog.show();
                break;
            case R.id.familyMemberSexBoy:
                switchSelected(true);
                break;
            case R.id.familyMemberSexGirl:
                switchSelected(false);
                break;
            case R.id.selectBirthdayLayout:
                ChooseYearMonthDialog chooseTimeDialog = new ChooseYearMonthDialog(this);
                chooseTimeDialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        birthdayTv.setText(obj[0]+"");
                        selectedBirthday = obj[0]+"-01";
                    }
                });
                chooseTimeDialog.show();
                break;
        }
    }

    @Override
    public void onSaveFamilyMemberSuccess() {
        onBackPressed();
    }

    @Override
    public void onUpdateInfoSuccess() {

    }
}
