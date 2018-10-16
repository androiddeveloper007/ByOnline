package com.bowen.tcm.medicalrecord.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.HomeMedicalRecord;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.event.ChooseFamilyMemberEvent;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.event.LoginOutEvent;
import com.bowen.tcm.common.event.ThirdLoginEvent;
import com.bowen.tcm.common.event.UpdateUIEvent;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.common.widget.ScrollRecyclerView;
import com.bowen.tcm.common.widget.ScrollRecyclerView.OnScrollListener;
import com.bowen.tcm.login.activity.BindingPhoneActivity;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.activity.QuickLoginActivity;
import com.bowen.tcm.medicalrecord.adapter.MedicalRecordAdapter;
import com.bowen.tcm.medicalrecord.contract.HomeMedicalRecordContract;
import com.bowen.tcm.medicalrecord.presenter.HomeMedicalRecordPresenter;
import com.bowen.tcm.mine.activity.FamilyMemberActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:
 * Created by AwenZeng on 2018/8/18.
 */
public class MedicalRecordActivity extends BaseActivity implements OnScrollListener, RequestLoadMoreListener, HomeMedicalRecordContract.View {

    @BindView(R.id.mRecyclerView)
    ScrollRecyclerView mRecyclerView;
    @BindView(R.id.mTitleBackImg)
    ImageView mTitleBackImg;
    @BindView(R.id.mTitleHeadPortraitImg)
    CircleImageView mTitleHeadPortraitImg;
    @BindView(R.id.mTitleNameTv)
    TextView mTitleNameTv;
    @BindView(R.id.mTitleLayout)
    RelativeLayout mTitleLayout;

    @BindView(R.id.mTopBgImg)
    ImageView mTopBgImg;

    @BindView(R.id.mAddMedicalRecordImg)
    ImageView mAddMedicalRecordImg;

    @BindView(R.id.mNoRecordViewStup)
    ViewStub mNoRecordViewStup;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private int scrollPos;
    private TextView mNoRecordTv;
    private TextView mHealthTipsTv;
    private CircleImageView mHeadPortraitImg;
    private TextView mNameTv;
    private TextView mChangerUserBtn;
    private LinearLayout mNoRecordLayout;

    private String familyId;
    private String mShowFamilyNickNameStr;
    private String mShowFamilyPicUrl;
    private int page = 1;//页码
    private boolean isMore = false;
    private boolean isLoadMore = false;

    private MedicalRecordAdapter mAdapter;
    private HomeMedicalRecordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_medical_record);
        ButterKnife.bind(this);
        init();
    }


    private void init() {

        View headView = this.getLayoutInflater().inflate(R.layout.headview_medical_record, null);

        mNameTv = (TextView) headView.findViewById(R.id.mNameTv);
        mHealthTipsTv = (TextView) headView.findViewById(R.id.mHealthTipsTv);
        mHeadPortraitImg = (CircleImageView) headView.findViewById(R.id.mHeadPortraitImg);
        mNameTv.setOnClickListener(this);
        mChangerUserBtn = (TextView) headView.findViewById(R.id.mChangerUserBtn);
        mChangerUserBtn.setOnClickListener(this);

        mNoRecordViewStup.inflate();
        mNoRecordTv = (TextView) findViewById(R.id.mNoRecordTv);
        mNoRecordLayout = (LinearLayout) findViewById(R.id.mNoRecordLayout);
        mNoRecordTv.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MedicalRecordAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(headView);
        mAdapter.setOnLoadMoreListener(this);

        mTitleLayout.getBackground().setAlpha(0);
        mRecyclerView.setScrolListener(this);

        mPresenter = new HomeMedicalRecordPresenter(this, this);


        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, mAdapter.getItem(position));
                RouterActivityUtil.startActivity(MedicalRecordActivity.this, MedicalDetailActivity.class, bundle);
            }
        });
        mPtrFrameLayout.setRefreshViewConfig(R.drawable.pull_refreshptr_rotate_arrow,
                getResources().getDrawable(R.drawable.pull_refresh_progress_bar),
                getResources().getColor(R.color.color_white));

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                if (LoginStatusUtil.getInstance().isLogin() && UserInfo.getInstance().isBindPhone()) {
                    getData(false);
                }
            }
        });
        mShowFamilyNickNameStr = UserInfo.getInstance().getUserNickname();
        mShowFamilyPicUrl = UserInfo.getInstance().getPicUrl();
        updateUI();
        //只有注册过的账号，才拉取数据，没有注册过的第三方账号不拉数据
        if (UserInfo.getInstance().isBindPhone()) {
            getData(false);
        }
    }


    private void updateUI() {
        if (LoginStatusUtil.getInstance().isLogin()){
            if (EmptyUtils.isEmpty(familyId)) {
                familyId = UserInfo.getInstance().getFamilyId();
            }
            mPtrFrameLayout.setEnabled(true);
            mNameTv.setText(mShowFamilyNickNameStr);
            mTitleNameTv.setText(mShowFamilyNickNameStr);
            mNameTv.setCompoundDrawables(null, null, null, null);
            mChangerUserBtn.setVisibility(View.VISIBLE);
            ImageLoaderUtil.getInstance().show(this, mShowFamilyPicUrl, mHeadPortraitImg, R.drawable.man);
            ImageLoaderUtil.getInstance().show(this, mShowFamilyPicUrl, mTitleHeadPortraitImg, R.drawable.man);
            if (EmptyUtils.isNotEmpty(mAdapter.getData()) && mAdapter.getData().size() > 0) {
                mAddMedicalRecordImg.setVisibility(View.VISIBLE);
            } else {
                mAddMedicalRecordImg.setVisibility(View.GONE);
            }
        }
        //健康提示语
        if (EmptyUtils.isNotEmpty(AppConfigInfo.getInstance().getWarmWishes())) {
            mHealthTipsTv.setText(AppConfigInfo.getInstance().getWarmWishes());
        }
    }

    private void getData(final boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getMedicalRecordList(page, 10, familyId);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.mChangerUserBtn:
                if (UserInfo.getInstance().isBindPhone()) {
                    bundle.putInt(RouterActivityUtil.FROM_TAG, FamilyMemberActivity.FROM_HOMEPAGE_MEDICAL_RECORD);
                    RouterActivityUtil.startActivity(MedicalRecordActivity.this, FamilyMemberActivity.class, bundle);
                } else {
                    RouterActivityUtil.startActivity(MedicalRecordActivity.this, BindingPhoneActivity.class);
                }
                break;
            case R.id.mNoRecordTv:

                if (isLogin()) {
                    if (EmptyUtils.isEmpty(familyId)) {
                        familyId = UserInfo.getInstance().getFamilyId();
                    }
                    bundle.putString(RouterActivityUtil.FROM_TAG, familyId);
                    RouterActivityUtil.startActivity(MedicalRecordActivity.this, AddMedicalInfoActivity.class, bundle);
                }
                break;
            case R.id.mNameTv:
                if (!LoginStatusUtil.getInstance().isLogin()) {
                    if (EmptyUtils.isNotEmpty(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""))) {
                        RouterActivityUtil.startActivity(MedicalRecordActivity.this, QuickLoginActivity.class);
                    } else {
                        RouterActivityUtil.startActivity(MedicalRecordActivity.this, LoginActivity.class);
                    }
                }
                break;
        }
    }

    @OnClick({R.id.mTitleBackImg, R.id.mBackImg, R.id.mChangeAccountTv, R.id.mAddMedicalRecordImg})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.mTitleBackImg:
            case R.id.mBackImg:
                onBackPressed();
                break;
            case R.id.mChangeAccountTv:
                bundle.putInt(RouterActivityUtil.FROM_TAG, FamilyMemberActivity.FROM_HOMEPAGE_MEDICAL_RECORD);
                RouterActivityUtil.startActivity(MedicalRecordActivity.this, FamilyMemberActivity.class, bundle);
                break;
            case R.id.mAddMedicalRecordImg:
                if (LoginStatusUtil.getInstance().isLogin()) {
                    if (UserInfo.getInstance().isBindPhone()) {
                        if (EmptyUtils.isEmpty(familyId)) {
                            familyId = UserInfo.getInstance().getFamilyId();
                        }
                        bundle.putString(RouterActivityUtil.FROM_TAG, familyId);
                        RouterActivityUtil.startActivity(MedicalRecordActivity.this, AddMedicalInfoActivity.class, bundle);
                    } else {
                        RouterActivityUtil.startActivity(MedicalRecordActivity.this, BindingPhoneActivity.class);
                    }

                } else {
                    if (EmptyUtils.isNotEmpty(DataCacheUtil.getInstance().getString(DataCacheUtil.LOGIN_TOKEN, ""))) {
                        RouterActivityUtil.startActivity(MedicalRecordActivity.this, QuickLoginActivity.class);
                    } else {
                        RouterActivityUtil.startActivity(MedicalRecordActivity.this, LoginActivity.class);
                    }
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        onScroll(scrollPos);
    }

    @Override
    public void onScroll(int scrollY) {
        scrollPos = scrollY;
        if (scrollY < ScreenSizeUtil.dp2px(5)) {
            mTitleLayout.getBackground().setAlpha(255);
            mTitleBackImg.getBackground().setAlpha(255);
            mTitleHeadPortraitImg.setAlpha(255);
            mTopBgImg.setVisibility(View.VISIBLE);
            mTitleLayout.setVisibility(View.INVISIBLE);
        } else if (scrollY >= ScreenSizeUtil.dp2px(5) && scrollY < ScreenSizeUtil.dp2px(60)) {
        } else {
            mTitleLayout.getBackground().setAlpha(255);
            mTitleBackImg.getBackground().setAlpha(255);
            mTitleHeadPortraitImg.setAlpha(255);
            mTopBgImg.setVisibility(View.INVISIBLE);
            mTitleLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mTitleLayout.getBackground().setAlpha(255);
        mTitleBackImg.getBackground().setAlpha(255);
        mTitleHeadPortraitImg.setAlpha(255);
    }

    @Override
    public void onGetMedicalRecordSuccess(HomeMedicalRecord homeMedicalRecord) {
        mPtrFrameLayout.refreshComplete();
        Page pageBean = homeMedicalRecord.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(homeMedicalRecord.getEmrCaseInfoList(), true);
        } else {
            mAdapter.setNewData(homeMedicalRecord.getEmrCaseInfoList());
        }

        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
            mAdapter.addFooterView(footView);
        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
            mPtrFrameLayout.setEnabled(false);
            mNoRecordLayout.setVisibility(View.VISIBLE);
        } else {
            mPtrFrameLayout.setEnabled(true);
            mNoRecordLayout.setVisibility(View.GONE);
        }
        updateUI();
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onGetMedicalRecordFail(HomeMedicalRecord homeMedicalRecord) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseFamilyMemberEvent event) {
        FamilyMember familyMember = (FamilyMember) event.getData();
        if (EmptyUtils.isNotEmpty(familyMember)) {
            familyId = familyMember.getFamilyId();
            mShowFamilyNickNameStr = familyMember.getFamilyNickname();
            mShowFamilyPicUrl = familyMember.getHeadSculptureUrl();
            //只有注册过的账号，才拉取数据，没有注册过的第三方账号不拉数据
            if (UserInfo.getInstance().isBindPhone()) {
                getData(false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        familyId = UserInfo.getInstance().getFamilyId();
        mShowFamilyNickNameStr = UserInfo.getInstance().getUserNickname();
        mShowFamilyPicUrl = UserInfo.getInstance().getPicUrl();
        mPresenter.getDiagnoseStage();
        mPresenter.getDiseaseNameCategory();
        //只有注册过的账号，才拉取数据，没有注册过的第三方账号不拉数据
        if (UserInfo.getInstance().isBindPhone()) {
            getData(false);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ThirdLoginEvent event) {
        mShowFamilyNickNameStr = UserInfo.getInstance().getUserNickname();
        mShowFamilyPicUrl = UserInfo.getInstance().getPicUrl();
        updateUI();
        mAdapter.setNewData(new ArrayList<MedicalRecord>());
        mAdapter.addFooterView(null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginOutEvent event) {
        updateUI();
        mAdapter.setNewData(new ArrayList<MedicalRecord>());
        mAdapter.addFooterView(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUIEvent event) {
        updateUI();
        if (UserInfo.getInstance().isBindPhone()) {
            getData(false);
        }
    }

}
