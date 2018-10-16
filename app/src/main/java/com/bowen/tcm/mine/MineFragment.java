package com.bowen.tcm.mine;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseFragment;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;
import com.bowen.tcm.mine.activity.FamilyMemberActivity;
import com.bowen.tcm.mine.activity.FeedBackActivity;
import com.bowen.tcm.mine.activity.MyConsultActivity;
import com.bowen.tcm.mine.activity.MyOrderActivity;
import com.bowen.tcm.mine.activity.MyOutpatientAppointmentActivity;
import com.bowen.tcm.mine.activity.MyPrescriptionActivity;
import com.bowen.tcm.mine.activity.MySubscribeActivity;
import com.bowen.tcm.mine.activity.SafeSettingActivity;
import com.bowen.tcm.mine.activity.UserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/23.
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mUserNameTv)
    TextView mUserNameTv;
    @BindView(R.id.mUserInfoTopLayout)
    LinearLayout mUserInfoTopLayout;
    @BindView(R.id.mFamilyLayout)
    RelativeLayout mFamilyLayout;
    @BindView(R.id.mInviteLayout)
    RelativeLayout mInviteLayout;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, mView);
    }

    private void updateUI() {
        if (LoginStatusUtil.getInstance().isLogin()) {
            mUserNameTv.setText(UserInfo.getInstance().getUserNickname());
            mUserNameTv.setCompoundDrawables(null, null, null, null);
            ImageLoaderUtil.getInstance().show(mActivity, UserInfo.getInstance().getPicUrl(), mHeadPortraitImg, R.drawable.man);
        } else {
            mUserNameTv.setText("去登录或注册");
            Drawable drawable = getResources().getDrawable(R.drawable.arrow_right);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mUserNameTv.setCompoundDrawables(null, null, drawable, null);
            ImageLoaderUtil.getInstance().show(mActivity, "", mHeadPortraitImg, R.drawable.man);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @OnClick({R.id.mUserInfoTopLayout, R.id.mFamilyLayout, R.id.mOrderLayout, R.id.mCommentionLayout,
            R.id.mDepartmentLayout, R.id.mInterestLayout, R.id.mFolkPrescriptionLayout, R.id.mFeedbackLayout, R.id.mSafeSettingLayout})
    public void onViewClicked(View view) {
        if (hasNotLogin()) return;
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.mUserInfoTopLayout:
                RouterActivityUtil.startActivity(mActivity, UserInfoActivity.class);
                break;
            case R.id.mFamilyLayout:
                bundle.putInt(RouterActivityUtil.FROM_TAG, FamilyMemberActivity.FROM_FAMILY_MEMBER);
                RouterActivityUtil.startActivity(mActivity, FamilyMemberActivity.class, bundle);
                break;
            case R.id.mOrderLayout:
                RouterActivityUtil.startActivity(mActivity, MyOrderActivity.class);
                break;
            case R.id.mCommentionLayout:
                RouterActivityUtil.startActivity(mActivity, MyConsultActivity.class);
                break;
            case R.id.mDepartmentLayout:
                RouterActivityUtil.startActivity(mActivity, MyOutpatientAppointmentActivity.class);
                break;
            case R.id.mInterestLayout:
                RouterActivityUtil.startActivity(mActivity, MySubscribeActivity.class);
                break;
            case R.id.mFolkPrescriptionLayout:
                RouterActivityUtil.startActivity(mActivity, MyPrescriptionActivity.class);
                break;
            case R.id.mFeedbackLayout:
                RouterActivityUtil.startActivity(mActivity, FeedBackActivity.class);
                break;
            case R.id.mSafeSettingLayout:
                RouterActivityUtil.startActivity(mActivity, SafeSettingActivity.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean hasNotLogin() {
        if (LoginStatusUtil.getInstance().isLogin()) {
            if (!UserInfo.getInstance().isBindPhone()) {
                RouterActivityUtil.startActivity(mActivity, BindingPhoneActivity.class);
                return true;
            }
        } else {
            if (EmptyUtils.isNotEmpty(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""))) {
                RouterActivityUtil.startActivity(mActivity, QuickLoginActivity.class);
            } else {
                RouterActivityUtil.startActivity(mActivity, LoginActivity.class);
            }
            return true;
        }
        return false;
    }
}