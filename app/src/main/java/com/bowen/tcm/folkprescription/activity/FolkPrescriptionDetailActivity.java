package com.bowen.tcm.folkprescription.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.UserPrescriptionComment;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;
import com.bowen.tcm.common.dialog.ShareDialog;
import com.bowen.tcm.common.event.FolkPrescriptionCollectEvent;
import com.bowen.tcm.folkprescription.adapter.UserCommentPrescriptionRecyclerAdapter;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionDetailContract;
import com.bowen.tcm.folkprescription.presenter.FolkPrescriptionDetailPresenter;
import com.bowen.tcm.inquiry.activity.FindDoctorActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * Describe:偏方详情
 * Created by zzp on 2018/3/23.
 */
public class FolkPrescriptionDetailActivity extends BaseActivity implements FolkPrescriptionDetailContract.View {
    @BindView(R.id.tv_folk_detail_fit_people)
    TextView tv_folk_detail_fit_people;
    @BindView(R.id.tv_folk_detail_apply_disease)
    TextView tv_folk_detail_apply_disease;
    @BindView(R.id.tv_folk_detail_usage_dosage)
    TextView tv_folk_detail_usage_dosage;
    @BindView(R.id.FolkDetailFrom)
    TextView FolkDetailFrom;
    //    @BindView(R.id.prescriptionCommentCount)
//    TextView prescriptionCommentCount;
    @BindView(R.id.folkPrescriptionListCollect)
    ImageView folkPrescriptionListCollect;
    @BindView(R.id.consultDoctorLayout)
    LinearLayout consultDoctorLayout;
    private FolkPrescription folkPrescription;
    private FolkPrescriptionDetailPresenter mPresenter;
    private InfoFolkPrescription infoFolkPrescription;
    private Context mContext;
    private boolean isCollect;
    private static final String CLICK_POSITION = "clickPosition";
    private int clickPosition;//从列表页传过来的位置
    //    private int page = 1;//页码
//    private final int pageSize = 10;
//    private CircleImageView doctorImg;
//    private TextView doctorName;
//    private TextView doctorLevel;
//    private TextView commentTime;
//    private TextView doctorComment;
//    private TextView hospitalName;
//    private RecyclerView userCommentPrescriptionRv;
//    private TextView doctorCommentAll;
//    private TextView bottomAllComment;
    private final static String PrescriptionDoctorCommentRecord = "PrescriptionDoctorCommentRecord";
    private final static String PrescriptionUserCommentRecord = "PrescriptionUserCommentRecord";
    private final static String PrescriptionId = "PrescriptionId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_folk_prescription_detail);
        ButterKnife.bind(this);
        mContext = this;
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("分享");
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (EmptyUtils.isNotEmpty(bundle)) {
            folkPrescription = (FolkPrescription) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
            setTitle(folkPrescription.getPrescriptionName());
            tv_folk_detail_fit_people.setText(folkPrescription.getApplyCrowdStr());
            tv_folk_detail_apply_disease.setText(folkPrescription.getApplyDisease());
            tv_folk_detail_usage_dosage.setText(folkPrescription.getUsageDosage());
            if(folkPrescription.isFromMine()){
                folkPrescriptionListCollect.setVisibility(View.GONE);
                if(!TextUtils.equals("1", folkPrescription.getAuditStatus())){
                    getTitleBar().getRightTextButton().setVisibility(View.GONE);
                    consultDoctorLayout.setVisibility(View.GONE);
                }
            }
            clickPosition = bundle.getInt(CLICK_POSITION);
            mPresenter = new FolkPrescriptionDetailPresenter(this, this);
            mPresenter.loadData(folkPrescription.getPrescriptionId());
        }
    }

//    public void getUserCommentData() {
//        mPresenter.getUserPrescriptionCommentList(page, pageSize, folkPrescription.getPrescriptionId());
//    }
//
//    public void getDoctorCommentData() {
//        mPresenter.getDoctorPrescriptionCommentList(page, pageSize, folkPrescription.getPrescriptionId());
//    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        ShareData shareData = new ShareData();
        shareData.setShareType(Platform.SHARE_WEBPAGE);
        shareData.setContent(infoFolkPrescription.getShareContent());
        shareData.setLinkUrl(infoFolkPrescription.getShareUrl().replace("\\", ""));
        shareData.setTitile(infoFolkPrescription.getPrescriptionName());
        ShareDialog.getBuilder().shareData(shareData).build(this).show();
    }

    @Override
    public void onLoadSuccess(InfoFolkPrescription info) {
        infoFolkPrescription = info;
        updateUI(info);
//        getDoctorCommentData();
    }

    private void updateUI(InfoFolkPrescription info) {
        String sourceTypeStr;//0:用户 1:医生 2:后台
        if(TextUtils.equals("0", info.getPreSourceType())){
            sourceTypeStr = "用户";
        }else if(TextUtils.equals("1",info.getPreSourceType())){
            sourceTypeStr = "医生";
        }else{
            sourceTypeStr = "";
        }
        if(EmptyUtils.isNotEmpty(info.getPrescriptionSource())){
            FolkDetailFrom.setText(SpannableStringUtils.getBuilder("来自 ")
                    .setForegroundColor(getResources().getColor(R.color.color_main_black))
                    .append(sourceTypeStr)
                    .setForegroundColor(getResources().getColor(R.color.color_main_black))
                    .append(info.getPrescriptionSource())
                    .setForegroundColor(getResources().getColor(R.color.color_main))
                    .create());
        }
        isCollect = TextUtils.equals("1", info.getIsCollect());
        folkPrescriptionListCollect.setSelected(isCollect);
    }

//    private void inflateDoctorCommentEmpty() {
//        findViewById(R.id.viewStubDoctorCommentEmpty).setVisibility(View.VISIBLE);
//    }
//
//    private void inflateUserCommentEmpty() {
//        findViewById(R.id.viewStubUserCommentEmpty).setVisibility(View.VISIBLE);
//    }
//
//    private void inflateUserComment() {
//        findViewById(R.id.viewStubUserComment).setVisibility(View.VISIBLE);
//        bottomAllComment = findViewById(R.id.bottomAllComment);
//        userCommentPrescriptionRv = findViewById(R.id.userCommentPrescriptionRv);
//    }
//
//    private void inflateDoctorComment() {
//        findViewById(R.id.viewStubDoctorComment).setVisibility(View.VISIBLE);
//        doctorCommentAll = findViewById(R.id.doctorCommentAll);
//        doctorImg = findViewById(R.id.doctorImg);
//        doctorName = findViewById(R.id.doctorName);
//        doctorLevel = findViewById(R.id.doctorLevel);
//        commentTime = findViewById(R.id.commentTime);
//        hospitalName = findViewById(R.id.hospitalName);
//        doctorComment = findViewById(R.id.doctorComment);
//    }

    @Override
    public void onLoadFail(InfoFolkPrescription info) {

    }

    @Override
    public void getDoctorCommentSuccess(final PrescriptionDoctorCommentRecord list) {
        if (list != null && list.getPrescriptionCommentList().size() > 0) {
//            inflateDoctorComment();
            //填入医生评论的数据（第一条）
//            Glide.with(mContext).load(list.getPrescriptionCommentList().get(0).getHeadImgUrl()).transform(new GlideCircleTransform(mContext)).into(doctorImg);
//            doctorName.setText(list.getPrescriptionCommentList().get(0).getName());
//            doctorLevel.setText(list.getPrescriptionCommentList().get(0).getPosition());
//            commentTime.setText(list.getPrescriptionCommentList().get(0).getCreateTime());
//            hospitalName.setText(list.getPrescriptionCommentList().get(0).getHostipal());
//            doctorComment.setText(list.getPrescriptionCommentList().get(0).getComment());
//            doctorCommentAll.setText("全部点评（"+list.getPage().getTotalCount()+"）");
//            doctorCommentAll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(PrescriptionDoctorCommentRecord,list);
//                    bundle.putString(PrescriptionId, infoFolkPrescription.getPrescriptionId());
//                    RouterActivityUtil.startActivity((Activity) mContext, DoctorCommentActivity.class);
//                }
//            });
        } else if (list != null && list.getPrescriptionCommentList().size() == 0) {
            //显示无对应数据的视图
//            inflateDoctorCommentEmpty();
        }
//        getUserCommentData();
    }

    @Override
    public void getDoctorCommentFail(PrescriptionDoctorCommentRecord list) {
//        inflateDoctorCommentEmpty();
//        getUserCommentData();
    }

    @Override
    public void getUserCommentSuccess(final PrescriptionUserCommentRecord list) {
        if (list != null && list.getPrescriptionCommentList().size() > 0) {
//            inflateUserComment();
            UserCommentPrescriptionRecyclerAdapter mAdapter = new UserCommentPrescriptionRecyclerAdapter(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            userCommentPrescriptionRv.setLayoutManager(layoutManager);
//            userCommentPrescriptionRv.setAdapter(mAdapter);
//            userCommentPrescriptionRv.setNestedScrollingEnabled(false);
            //取前三条，如果有的话，没有则取全部
            List<UserPrescriptionComment> showUserCommentList = new ArrayList<>();
            if (list.getPrescriptionCommentList().size() < 3) {
                showUserCommentList = list.getPrescriptionCommentList();
            } else {
                showUserCommentList.add(list.getPrescriptionCommentList().get(0));
                showUserCommentList.add(list.getPrescriptionCommentList().get(1));
                showUserCommentList.add(list.getPrescriptionCommentList().get(2));
            }
            mAdapter.setNewData(showUserCommentList);
//            prescriptionCommentCount.setText("（"+list.getPage().getTotalCount()+"）");
            findViewById(R.id.userCommentAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PrescriptionUserCommentRecord, list);
                    bundle.putString(PrescriptionId, infoFolkPrescription.getPrescriptionId());
                    RouterActivityUtil.startActivity((Activity) mContext, UserCommentActivity.class, bundle);
                }
            });
//            bottomAllComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(PrescriptionUserCommentRecord,list);
//                    bundle.putString(PrescriptionId, infoFolkPrescription.getPrescriptionId());
//                    RouterActivityUtil.startActivity((Activity) mContext, UserCommentActivity.class, bundle);
//                }
//            });
        }
//        else if(list!=null && list.getPrescriptionCommentList().size()==0) {
//            inflateUserCommentEmpty();
//        }
    }

    @Override
    public void getUserCommentFail(PrescriptionUserCommentRecord list) {
//        inflateUserCommentEmpty();
    }

    @OnClick({R.id.consultDoctorLayout,R.id.folkPrescriptionListCollect})// , R.id.userCommentLayout
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.consultDoctorLayout:
                if(!mPresenter.hasNotLogin()){
                    Bundle bundle = new Bundle();
                    bundle.putString(RouterActivityUtil.FROM_TAG, folkPrescription.getPrescriptionId());
                    RouterActivityUtil.startActivity(FolkPrescriptionDetailActivity.this,
                            FindDoctorActivity.class, bundle);
                }
                break;
//            case R.id.userCommentLayout:
//                if(!mPresenter.hasNotLogin()){
//                    Bundle bundle = new Bundle();
//                    bundle.putString(PrescriptionId, infoFolkPrescription.getPrescriptionId());
//                    RouterActivityUtil.startActivity((Activity) mContext, UserCommentEditActivity.class, bundle);
//                }
//                break;
            case R.id.folkPrescriptionListCollect:
                if (!mPresenter.hasNotLogin()) {
                    isCollect = !isCollect;
                    folkPrescriptionListCollect.setSelected(isCollect);
                    mPresenter.folkPrescriptionCollectOrNot(isCollect, folkPrescription.getPrescriptionId());
                    //将收藏状态传到列表页
                    EventBus.getDefault().post(new FolkPrescriptionCollectEvent(clickPosition));
                }
                break;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(RefreshPrescriptionUserCommentEvent event) {
//        getUserCommentData();
//    }
}