package com.bowen.tcm.inquiry.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CollapsedTextView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseSupportActivity;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.EmrHospitalBean;
import com.bowen.tcm.common.bean.network.HospitalDetailInfo;
import com.bowen.tcm.common.bean.network.Page;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.dialog.ShareDialog;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.widget.AppBarStateChangeListener;
import com.bowen.tcm.inquiry.adapter.HospitalDetailDoctorRvAdapter;
import com.bowen.tcm.inquiry.contract.HospitalDetailContract;
import com.bowen.tcm.inquiry.presenter.HospitalDetailPresenter;
import com.bowen.tcm.mine.activity.MySubscribeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * Describe:医馆详情
 * Created by zhuzhipeng on 2018/7/3.
 */
public class HospitalDetailActivity extends BaseSupportActivity implements HospitalDetailContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    TextView hospitalName;
    TextView hospitalAddress;
    CollapsedTextView hospitalIntroduce;
    ImageView hospitalDetailImg;
    RecyclerView hospitalDetailDoctorRv;
    private HospitalDetailDoctorRvAdapter mAdapter;
    private HospitalDetailPresenter mPresenter;
    private int page = 1;//页码
    private final int pageSize = 10;
    private boolean isMore = false;
    private boolean isLoadMore = false;
    private View emptyView;
    private String hospitalId;
    private View headView;
    private EmrHospitalBean mHospital;
    private Clinic hospitalBean;
    private Activity mActivity;
    private PermissionsModel mPermissionsModel;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_hospital_detail);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
    }

    private void initView() {
        if(getIntent()!=null && getIntent().getSerializableExtra("hospitalBean")!=null){
            hospitalBean = (Clinic) getIntent().getSerializableExtra("hospitalBean");
            hospitalId = hospitalBean.getHospitalId();
        }
        initHeaderView();
        hospitalDetailImg = getView(R.id.hospitalDetailImg);
        TextView toolbarTitle = getView(R.id.toolbarTitle);
        toolbarTitle.setText(hospitalBean.getHospitalName());
        ImageLoaderUtil.getInstance().show(this, hospitalBean.getHospitalImgUrl(), hospitalDetailImg,R.drawable.img_bg);
        final AppBarLayout appbar = getView(R.id.appbar);
        final ButtonBarLayout playButton = getView(R.id.playButton);
        final Toolbar toolbar = getView(R.id.toolbar);
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    playButton.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    playButton.setVisibility(View.VISIBLE);
                }else {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
        hospitalDetailDoctorRv = getView(R.id.hospitalDetailDoctorRv);
        mPresenter = new HospitalDetailPresenter(this, this);
        mAdapter = new HospitalDetailDoctorRvAdapter(this);
        mAdapter.addHeaderView(headView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        hospitalDetailDoctorRv.setLayoutManager(layoutManager);
        hospitalDetailDoctorRv.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isLogin()){
                    Bundle bundle = new Bundle();
                    bundle.putString(RouterActivityUtil.FROM_TAG, mAdapter.getItem(position).getDoctorId());
                    bundle.putInt(RouterActivityUtil.FROM_TAG1,mAdapter.getItem(position).getRecommended());
                    RouterActivityUtil.startActivity(mActivity, DoctorDetailActivity.class, bundle);
                }
            }
        });
        mAdapter.setOnSubscribeConsultListener(new HospitalDetailDoctorRvAdapter.MySubscribeConsultClick() {
            @Override
            public void mySubscribeConsult(int pos) {
                if(isLogin()){
                    mPresenter.checkImmediateConsult(mAdapter.getItem(pos-1).getDoctorId());
                }
            }
        });
        getData(false);
    }

    private void initHeaderView() {
        headView = getLayoutInflater().inflate(R.layout.header_hospital_detail, null);
        hospitalName = headView.findViewById(R.id.hospitalName);
        hospitalAddress = headView.findViewById(R.id.hospitalAddress);
        hospitalIntroduce = headView.findViewById(R.id.hospitalIntroduce);
        if(hospitalBean!=null){
            if(!TextUtils.isEmpty(hospitalBean.getHospitalName())){
                hospitalName.setText(hospitalBean.getHospitalName());
            }
            if(!TextUtils.isEmpty(hospitalBean.getAddressStr())){
                hospitalAddress.setText(hospitalBean.getAddressStr());
            }
        }
        mPermissionsModel = new PermissionsModel(mActivity);
        headView.findViewById(R.id.hospitalCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = hospitalBean.getPhone();
                if(TextUtils.isEmpty(phone)){
                    return;
                };
                AffirmDialog affirmDialog = new AffirmDialog(mActivity, "", "拨打电话 " + phone, "取消", "确定");
                affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                    @Override
                    public void onCancle() {
                    }
                    @Override
                    public void onOK() {
                        mPermissionsModel.checkCallPhonePermission(new PermissionsModel.PermissionListener() {
                            @Override
                            public void onPermission(boolean isPermission) {
                                if (isPermission) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + phone);
                                    intent.setData(data);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });
                affirmDialog.show();
            }
        });
    }

    public void getData(boolean isLoad) {
        isMore = isLoad;
        if (isMore && isLoadMore) {
            return;
        }
        if (isMore) {
            isLoadMore = true;
        } else {
            page = 1;
        }
        mPresenter.getHospitalDetail(hospitalId, page, pageSize);
    }

    @Override
    public void onLoadSuccess(HospitalDetailInfo info) {
        if (page == 1) {
            hospitalIntroduce.setText(info.getEmrHospital().getIntroduction());
            mHospital = info.getEmrHospital();
            ImageLoaderUtil.getInstance().show(this, mHospital.getHospitalImgUrl(), hospitalDetailImg);
        }
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.empty_view_no_doctor,
                    (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
        Page pageBean = info.getPage();
        if (isMore) {
            mAdapter.notifyDataChangedAfterLoadMore(info.getEmrDoctorList(), true);
        } else {
            mAdapter.setNewData(info.getEmrDoctorList());
        }
        mAdapter.openLoadMore(pageBean.getPageSize(), pageBean.getPageNo() < pageBean.getTotalPages());
//        if (pageBean.getTotalCount() != 0 && mAdapter.getData().size() == pageBean.getTotalCount()) {
//            View footView = getLayoutInflater().inflate(R.layout.view_no_more, null);
//            mAdapter.addFooterView(footView);
//        }
        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
        }
        page = pageBean.getNextPage();
        isLoadMore = false;
    }

    @Override
    public void onLoadFail(HospitalDetailInfo info) {
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.viewstub_no_folk_prescription, (ViewGroup)
                    findViewById(android.R.id.content), false);
            mAdapter.setEmptyView(true, emptyView);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getData(true);
    }

    @OnClick({R.id.backIcon0, R.id.shareIcon0, R.id.backIcon1, R.id.shareIcon1,R.id.hospitalDetailImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIcon1:
                onBackPressed();
                break;
            case R.id.shareIcon1:
                shareHospital();
                break;
            case R.id.backIcon0:
                onBackPressed();
                break;
            case R.id.shareIcon0:
                shareHospital();
                break;
            case R.id.hospitalDetailImg:
                if(EmptyUtils.isNotEmpty(hospitalBean)){
                    ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(mActivity,
                            null,1,mPresenter.transfromPhotos(hospitalBean.getHospitalImgUrl()));
                    showBigPicDialog.show();
                }
                break;
        }
    }

    @Override
    public void onCheckImmediateConsultSuccess(Doctor doctor) {
        if(!doctor.isAsk()){
            if(doctor.isShowConsult()){
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, doctor);
                RouterActivityUtil.startActivity(HospitalDetailActivity.this, ImgTextCosultDetailActivity.class,bundle);
            }else{
                ToastUtil.getInstance().showToastDialog("该医生暂不提供图文咨询服务");
            }
        }else{
            Bundle bundle = new Bundle();
            PayOrderInfo payOrderInfo = new PayOrderInfo();
            payOrderInfo.setEmrDoctor(doctor);
            payOrderInfo.setOrderId(doctor.getOrderId());
            bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo);
            RouterActivityUtil.startActivity(HospitalDetailActivity.this, ChatActivity.class,bundle);
        }
    }

    private void shareHospital() {
        if (mHospital != null) {
            ShareData shareData = new ShareData();
            shareData.setShareType(Platform.SHARE_WEBPAGE);
            shareData.setContent(mHospital.getIntroduction());
            shareData.setLinkUrl(mHospital.getShareUrl().replace("\\", ""));
            shareData.setTitile(mHospital.getHospitalName());
            ShareDialog.getBuilder().shareData(shareData).build(this).show();
        } else {
            ToastUtil.getInstance().showToastDialog("获取数据失败");
        }
    }
}