package com.bowen.tcm.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.event.LocationEvent;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseFragment;
import com.bowen.tcm.common.bean.network.BannerInfo;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.HomePageInfo;
import com.bowen.tcm.common.bean.network.Tips;
import com.bowen.tcm.common.dialog.SmallTipsDialog;
import com.bowen.tcm.common.event.ColumnEvent;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.util.LocationUtil;
import com.bowen.tcm.common.widget.AutoPlayRecyclerView;
import com.bowen.tcm.common.widget.FixConflictRecyclerView;
import com.bowen.tcm.common.widget.ScrollRecyclerView;
import com.bowen.tcm.folkprescription.activity.FolkPrescriptionActivity;
import com.bowen.tcm.homepage.adapter.FamousClinicRecommendAdapter;
import com.bowen.tcm.homepage.adapter.FamousDoctorRecommendAdapter;
import com.bowen.tcm.homepage.adapter.HomePageAdapter;
import com.bowen.tcm.homepage.adapter.TopBannerAdapter;
import com.bowen.tcm.homepage.adapter.TopBannerAdapter.OnBannerItemClickListener;
import com.bowen.tcm.homepage.contract.HomePageFgContract;
import com.bowen.tcm.homepage.presenter.HomePageFgPresenter;
import com.bowen.tcm.inquiry.activity.ClinicListActivity;
import com.bowen.tcm.inquiry.activity.DoctorDetailActivity;
import com.bowen.tcm.inquiry.activity.DoctorSearchActivity;
import com.bowen.tcm.inquiry.activity.FamousChineseHospitalActivity;
import com.bowen.tcm.inquiry.activity.FindDoctorActivity;
import com.bowen.tcm.inquiry.activity.HospitalDetailActivity;
import com.bowen.tcm.main.activity.BrowserActivity;
import com.bowen.tcm.main.activity.MessageActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Describe:首页
 * Created by AwenZeng on 2018/5/24.
 */
public class
HomePageFragment extends BaseFragment implements HomePageFgContract.View {
    @BindView(R.id.mBannerRecyclerView)
    AutoPlayRecyclerView mBannerRecyclerView;
    @BindView(R.id.mFindDoctorIconImg)
    ImageView mFindDoctorIconImg;
    @BindView(R.id.mFindDoctorTv)
    TextView mFindDoctorTv;
    @BindView(R.id.mFindDoctorLayout)
    CardView mFindDoctorLayout;
    @BindView(R.id.mFindClinicIconImg)
    ImageView mFindClinicIconImg;
    @BindView(R.id.mFindClinicTv)
    TextView mFindClinicTv;
    @BindView(R.id.mFindClinicLayout)
    CardView mFindClinicLayout;
    @BindView(R.id.mFamousDoctorMoreTv)
    TextView mFamousDoctorMoreTv;
    @BindView(R.id.findFamousTipslayout)
    LinearLayout findFamousTipslayout;
    @BindView(R.id.mFamousDoctorRecyclerView)
    RecyclerView mFamousDoctorRecyclerView;
    @BindView(R.id.mMoreFamousClinicTv)
    TextView mMoreFamousClinicTv;
    @BindView(R.id.mFamousClinicRecyclerView)
    RecyclerView mFamousClinicRecyclerView;
    @BindView(R.id.mFindFolkPresciptionIconImg)
    ImageView mFindFolkPresciptionIconImg;
    @BindView(R.id.mFindFolkPresciptionLayout)
    RelativeLayout mFindFolkPresciptionLayout;
    @BindView(R.id.mTipsBgImg)
    ImageView mTipsBgImg;
    @BindView(R.id.mTipsContentTv)
    TextView mTipsContentTv;
    @BindView(R.id.mTipsAuthorTv)
    TextView mTipsAuthorTv;
    @BindView(R.id.mSmallTipsLayout)
    RelativeLayout mSmallTipsLayout;

    private ImageView mMessageImg;
    private TextView mMessageCountTv;
    private FrameLayout mSearchLayout;
    private FixConflictRecyclerView mRecyclerView;
    private PtrClassicFrameLayout mPtrFrameLayout;

    private String familyId;
    private LocationEvent mLoctionEvent;
    private ArrayList<BannerInfo> mBannerList;
    private ArrayList<Doctor> mFamousDoctorList;
    private ArrayList<Clinic> mClinicList;
    private Tips mSmallTips;
    private HomePageAdapter mAdapter;
    private TopBannerAdapter mBannerAdapter;
    private FamousDoctorRecommendAdapter mFamousDoctorRecommendAdapter;
    private FamousClinicRecommendAdapter mFamousClinicRecommendAdapter;
    private HomePageFgPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_homepage, null);
        init();
    }

    private void init() {
        mSearchLayout = getView(R.id.mSearchLayout);
        mMessageImg = getView(R.id.mMessageImg);
        mMessageCountTv = getView(R.id.mMessageCountTv);
        mRecyclerView = getView(R.id.mRecyclerView);
        mPtrFrameLayout = getView(R.id.mPtrFrameLayout);
        mMessageImg.setOnClickListener(this);
        mMessageCountTv.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);

        mPresenter = new HomePageFgPresenter(mActivity, this);
        mAdapter = new HomePageAdapter();

        View headView = mInflater.inflate(R.layout.headview_homepage, null);
        ButterKnife.bind(this, headView);

        mAdapter.addHeaderView(headView);

        mSmallTipsLayout.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.getHomePageInfo(mLoctionEvent.getLongitude(), mLoctionEvent.getLatitude());
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        mBannerRecyclerView.setLayoutManager(mLayoutManager);
        mBannerRecyclerView.setAutoPlaying(true);
        new PagerSnapHelper().attachToRecyclerView(mBannerRecyclerView);
        mBannerAdapter = new TopBannerAdapter(mActivity);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        mFamousDoctorRecyclerView.setLayoutManager(mLayoutManager1);
        mFamousDoctorRecommendAdapter = new FamousDoctorRecommendAdapter(mActivity);
        mFamousDoctorRecyclerView.setAdapter(mFamousDoctorRecommendAdapter);
        new PagerSnapHelper().attachToRecyclerView(mFamousDoctorRecyclerView);
        mFamousDoctorRecommendAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG, mFamousDoctorRecommendAdapter.getItem(position).getDoctorId());
                bundle.putInt(RouterActivityUtil.FROM_TAG1, mFamousDoctorRecommendAdapter.getItem(position).getRecommended());
                RouterActivityUtil.startActivity(mActivity, DoctorDetailActivity.class, bundle);
            }
        });


        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        mFamousClinicRecyclerView.setLayoutManager(mLayoutManager2);
        mFamousClinicRecommendAdapter = new FamousClinicRecommendAdapter(mActivity);
        mFamousClinicRecyclerView.setAdapter(mFamousClinicRecommendAdapter);
        new PagerSnapHelper().attachToRecyclerView(mFamousClinicRecyclerView);
        mFamousClinicRecommendAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, HospitalDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("hospitalBean", mFamousClinicRecommendAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        LocationUtil.getInstance().startLocation();
        updateUI();
    }

    /**
     * 开始刷新数据
     */
    private void startRereshData() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();
            }
        }, 100);
    }

    private void updateUI() {
        Tips item = AppConfigInfo.getInstance().getSmallTips();
        if (EmptyUtils.isNotEmpty(item)) {
            mTipsContentTv.setText(item.getTips());
            mTipsAuthorTv.setText(item.getTipsWriter() + "(作)");
            ImageLoaderUtil.getInstance().show(mActivity, item.getFileLink(), mTipsBgImg, R.drawable.small_tips_default_bg);
        }

        if (EmptyUtils.isNotEmpty(mBannerList)) {
            mBannerAdapter.setBannerList(mBannerList);
            mBannerRecyclerView.setCurrentIndex(1);
            mBannerRecyclerView.setAutoPlaying(true);
            mBannerRecyclerView.setAdapter(mBannerAdapter);
            mBannerAdapter.setOnBannerItemClickListener(new OnBannerItemClickListener() {
                @Override
                public void onItemClick(BannerInfo bannerInfo) {
                    String url = bannerInfo.getLinkUrl();
                    if (EmptyUtils.isNotEmpty(url)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(RouterActivityUtil.FROM_TAG, BrowserActivity.FROM_NOMAL);
                        bundle.putString(BrowserActivity.URL, url);
                        RouterActivityUtil.startActivity(mActivity, BrowserActivity.class, bundle);
                    }
                }
            });
        } else {
            ArrayList<BannerInfo> banners = new ArrayList<>();
            banners.add(new BannerInfo());
            mBannerAdapter.setBannerList(banners);
            mBannerRecyclerView.setAdapter(mBannerAdapter);
            mBannerRecyclerView.setAutoPlaying(false);
        }

        if (EmptyUtils.isNotEmpty(mClinicList)) {
            mFamousClinicRecommendAdapter.setNewData(mClinicList);
        }

        if (EmptyUtils.isNotEmpty(mFamousDoctorList)) {
            mFamousDoctorRecommendAdapter.setNewData(mFamousDoctorList);
        }
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(LoginStatusUtil.getInstance().isLogin()){
//            mPresenter.getMessageCount();
//        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mSearchLayout:
                Bundle bundle = new Bundle();
                bundle.putInt(RouterActivityUtil.FROM_TAG, DoctorSearchActivity.FROM_HOMEPAGE);
                RouterActivityUtil.startActivity(mActivity, DoctorSearchActivity.class, bundle);
                break;
            case R.id.mMessageImg:
            case R.id.mMessageCountTv:
                if (!isLogin()) {
                    return;
                }
                RouterActivityUtil.startActivity(mActivity, MessageActivity.class);
                break;
            case R.id.mSmallTipsLayout:
                new SmallTipsDialog(mActivity, AppConfigInfo.getInstance().getSmallTips()).show();
                break;
        }
    }


    @OnClick({R.id.mFindFolkPresciptionLayout, R.id.mFindClinicLayout, R.id.mFindDoctorLayout, R.id.mMoreFamousClinicTv, R.id.mFamousDoctorMoreTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mFindFolkPresciptionLayout:
                RouterActivityUtil.startActivity(mActivity, FolkPrescriptionActivity.class);
                break;
            case R.id.mMoreFamousClinicTv:
                RouterActivityUtil.startActivity(mActivity, FamousChineseHospitalActivity.class);
                break;
            case R.id.mFindClinicLayout:
                RouterActivityUtil.startActivity(mActivity, ClinicListActivity.class);
                break;
            case R.id.mFamousDoctorMoreTv:
            case R.id.mFindDoctorLayout:
                RouterActivityUtil.startActivity(mActivity, FindDoctorActivity.class);
                break;
        }
    }


    @Override
    public void onGetHomePageInfoSuccess(HomePageInfo info) {
        mPtrFrameLayout.refreshComplete();
        mBannerList = info.getBannerlist();
        mFamousDoctorList = info.getDoctorList();
        mClinicList = info.getHospitalList();
        updateUI();
        mAdapter.setNewData(info.getHomeNewsList());
    }

    @Override
    public void onGetHomePageInfoFailed() {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onGetMessageCountSuccess(int msgCount) {
        if (msgCount > 0) {
            mMessageCountTv.setVisibility(View.VISIBLE);
            mMessageCountTv.setText(msgCount);
        } else {
            mMessageCountTv.setVisibility(View.GONE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        mPresenter.getDiagnoseStage();
        mPresenter.getDiseaseNameCategory();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ColumnEvent event) {
        mAdapter.setNewData(mPresenter.updateNewsColumns(mAdapter.getData()));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationEvent event) {
        mLoctionEvent = event;
        AppConfigInfo.getInstance().setLocationEvent(event);
        startRereshData();
    }

}
