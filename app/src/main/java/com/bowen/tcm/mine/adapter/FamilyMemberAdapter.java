package com.bowen.tcm.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.contact.view.ContactSelectorActivity;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.FamilyMember;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/26.
 */

public class FamilyMemberAdapter extends BaseQuickAdapter<FamilyMember> {

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mNameTv)
    TextView mNameTv;
    @BindView(R.id.mRelationShipTv)
    TextView mRelationShipTv;
    @BindView(R.id.mRecentMedicalTimeTv)
    TextView mRecentMedicalTimeTv;
    @BindView(R.id.mUserInfoLayout)
    RelativeLayout mUserInfoLayout;
    @BindView(R.id.mLinkTv)
    TextView mLinkTv;
    @BindView(R.id.mLinkImg)
    ImageView mLinkImg;
    @BindView(R.id.mBottomLime)
    View mBottomLime;

    private OnLinkListener mLinkListener;

    public void setmLinkListener(OnLinkListener mLinkListener) {
        this.mLinkListener = mLinkListener;
    }

    public FamilyMemberAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_family_member;
    }

    @Override
    protected void convert(BaseViewHolder helper, final FamilyMember item, int position) {
        ButterKnife.bind(this,helper.convertView);

        if(EmptyUtils.isEmpty(item)){
            return;
        }

        mNameTv.setText(item.getFamilyNickname());
        mRelationShipTv.setText(item.getFamilyTypeTxt());
        if(EmptyUtils.isNotEmpty(item.getHeadSculptureUrl())){
            ImageLoaderUtil.getInstance().show(mContext.get(),item.getHeadSculptureUrl(),mHeadPortraitImg,R.drawable.man);
        }else{
            ImageLoaderUtil.getInstance().show(mContext.get(),"",mHeadPortraitImg,R.drawable.man);
        }

        if(EmptyUtils.isNotEmpty(item.getRecentUploadTime())){
            mRecentMedicalTimeTv.setText("最近病历："+ DateUtil.date2String(Long.parseLong(item.getRecentUploadTime()), DateUtil.DEFAULT_FORMAT_DATE));
        }else{
            mRecentMedicalTimeTv.setText("暂无病历");
        }
        if(item.getFamilyType().equals("0")){
            mLinkTv.setVisibility(View.GONE);
            mLinkImg.setVisibility(View.GONE);
        }else{
            if(EmptyUtils.isNotEmpty(item.getFamilyPhone())){
                mLinkTv.setVisibility(View.VISIBLE);
                mLinkImg.setVisibility(View.GONE);
            }else{
                mLinkTv.setVisibility(View.GONE);
                mLinkImg.setVisibility(View.VISIBLE);
            }
        }
        mLinkImg.setTag(item);
        if(getData().size() - 1 == position){
            mBottomLime.setVisibility(View.GONE);
        }else{
            mBottomLime.setVisibility(View.VISIBLE);
        }
        mLinkImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(item.getFamilyId());
            }
        });
    }

    private void showDialog(final String familyId){
        AffirmDialog dialog = new AffirmDialog(mContext.get(),"温馨提示",
                "关联家人的手机号码，就可以看到家人 的上传的病历，时刻关爱家人健康","不了","关联");
        dialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
            @Override
            public void onCancle() {

            }

            @Override
            public void onOK() {
                chooseContacts();
                if(EmptyUtils.isNotEmpty(mLinkListener)){
                    mLinkListener.onLinkFamilyMember(familyId);
                }
            }
        });
        dialog.show();
    }

    public void chooseContacts() {
        PermissionsModel permissionsModel = new PermissionsModel(mContext.get());
        permissionsModel.checkContactsPermission(new PermissionsModel.PermissionListener() {
            @Override
            public void onPermission(boolean isPermission) {
                if (isPermission) {
                    Intent intent = new Intent(mContext.get(), ContactSelectorActivity.class);
                    ((Activity)mContext.get()).startActivityForResult(intent, ContactSelectorActivity.REQUEST_CONTACT);
                }
            }
        });
    }
}
