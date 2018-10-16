package com.bowen.tcm.inquiry;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.base.BaseFragment;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.FindDoctorInfo;
import com.bowen.tcm.common.bean.network.FindDoctorItem;
import com.bowen.tcm.common.dialog.HPFindDoctorDialog;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.inquiry.activity.ClinicListActivity;
import com.bowen.tcm.inquiry.activity.DoctorSearchActivity;
import com.bowen.tcm.inquiry.activity.FamousChineseHospitalActivity;
import com.bowen.tcm.inquiry.activity.FindDoctorActivity;
import com.bowen.tcm.inquiry.adapter.HPFindDoctorDepartmentAdapter;
import com.bowen.tcm.inquiry.adapter.HPFindDoctorDiseaseAdapter;
import com.bowen.tcm.inquiry.model.DoctorModel;
import com.bowen.tcm.mine.activity.MyConsultActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class HPFindDoctorFragment extends BaseFragment {
    @BindView(R.id.consultIcon)
    ImageView consultIcon;
    @BindView(R.id.mMineConsultLayout)
    RelativeLayout mMineConsultLayout;
    @BindView(R.id.mFindDoctoIconImg)
    ImageView mFindDoctoIconImg;
    @BindView(R.id.mFindDoctorLayout)
    RelativeLayout mFindDoctorLayout;
    @BindView(R.id.mClinicMapIconImg)
    ImageView mClinicMapIconImg;
    @BindView(R.id.mClinicMapLayout)
    RelativeLayout mClinicMapLayout;
    @BindView(R.id.mClinicIconImg)
    ImageView mClinicIconImg;
    @BindView(R.id.mFindClinicLayout)
    RelativeLayout mFindClinicLayout;
    @BindView(R.id.mDiseaseMoreTv)
    TextView mDiseaseMoreTv;
    @BindView(R.id.mDiseaseRecyclerView)
    RecyclerView mDiseaseRecyclerView;
    @BindView(R.id.findServiceTv)
    TextView findServiceTv;
    @BindView(R.id.mFindServiceMoreTv)
    TextView mFindServiceMoreTv;
    @BindView(R.id.mFindServiceRecyclerView)
    RecyclerView mFindServiceRecyclerView;
    @BindView(R.id.mDepartmentMoreTv)
    TextView mDepartmentMoreTv;
    @BindView(R.id.mDepartmentRecyclerView)
    RecyclerView mDepartmentRecyclerView;
    @BindView(R.id.mSearchLayout)
    RelativeLayout mSearchLayout;
    @BindView(R.id.mMyConsultTv)
    TextView mMyConsultTv;
    @BindView(R.id.mArrowRightImg)
    ImageView mArrowRightImg;
    @BindView(R.id.mUser01Img)
    CircleImageView mUser01Img;
    @BindView(R.id.mUser02Img)
    CircleImageView mUser02Img;
    @BindView(R.id.mUser03Img)
    CircleImageView mUser03Img;
    @BindView(R.id.mUser04Img)
    CircleImageView mUser04Img;
    @BindView(R.id.mUser05Img)
    CircleImageView mUser05Img;
    @BindView(R.id.mChatUserListLayout)
    RelativeLayout mChatUserListLayout;
    @BindView(R.id.mNoticeRedImg)
    ImageView mNoticeRedImg;

    private DoctorModel mDoctorModel;
    private HPFindDoctorDiseaseAdapter mDiseaseAdapter;
    private HPFindDoctorDepartmentAdapter mDepartmentAdapter;
    private FindDoctorInfo mFindDoctorInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = mInflater.inflate(R.layout.fragment_homepage_find_doctor, null);
        ButterKnife.bind(this, mView);
        init();
    }

    private void init() {
        mDoctorModel = new DoctorModel(mActivity);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false);
        mDiseaseRecyclerView.setLayoutManager(mLayoutManager);
        mDiseaseAdapter = new HPFindDoctorDiseaseAdapter(mActivity);
        mDiseaseRecyclerView.setAdapter(mDiseaseAdapter);

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(mActivity, 4, GridLayoutManager.VERTICAL, false);
        mDepartmentRecyclerView.setLayoutManager(mLayoutManager1);
        mDepartmentAdapter = new HPFindDoctorDepartmentAdapter(mActivity);
        mDepartmentRecyclerView.setAdapter(mDepartmentAdapter);

        mDiseaseAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchField searchField = new SearchField();
                searchField.setDiseaseId(mDiseaseAdapter.getItem(position).getDiseaseId());
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, searchField);
                RouterActivityUtil.startActivity(mActivity, FindDoctorActivity.class, bundle);
            }
        });

        mDepartmentAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchField searchField = new SearchField();
                searchField.setDepartmentsId(mDepartmentAdapter.getItem(position).getDepartmentsId());
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, searchField);
                RouterActivityUtil.startActivity(mActivity, FindDoctorActivity.class, bundle);
            }
        });

        getData();
    }

    private void updateUI() {
        if (EmptyUtils.isNotEmpty(mFindDoctorInfo)) {
            if (LoginStatusUtil.getInstance().isLogin()) {
                mChatUserListLayout.setVisibility(View.VISIBLE);
            } else {
                mChatUserListLayout.setVisibility(View.GONE);
            }

            if (EmptyUtils.isNotEmpty(mFindDoctorInfo.getInquiryCount())) {
                mMyConsultTv.setText(String.format("我的咨询 （%s）", mFindDoctorInfo.getInquiryCount()));
            }

            ArrayList<FindDoctorItem> doctorItems = mFindDoctorInfo.getDiseaseList();
            if (doctorItems.size() <= 6) {
                mDiseaseAdapter.setNewData(doctorItems);
                mDiseaseMoreTv.setVisibility(View.VISIBLE);
            } else {
                mDiseaseAdapter.setNewData(doctorItems.subList(0, 6));
                mDiseaseMoreTv.setVisibility(View.VISIBLE);
            }

            ArrayList<FindDoctorItem> departments = mFindDoctorInfo.getHospitalDeptList();
            if (departments.size() <= 8) {
                mDepartmentMoreTv.setVisibility(View.GONE);
                mDepartmentAdapter.setNewData(departments);
            } else {
                mDepartmentMoreTv.setVisibility(View.VISIBLE);
                mDepartmentAdapter.setNewData(departments.subList(0, 8));
            }

            ArrayList<String> headImgList = mFindDoctorInfo.getHeadImgList();
            if (EmptyUtils.isNotEmpty(headImgList)) {
                mNoticeRedImg.setVisibility(View.VISIBLE);
                for (int i = 0; i < headImgList.size(); i++) {
                    String path = headImgList.get(i);
                    if (i < 5) {
                        getUserImg(i + 1).setVisibility(View.VISIBLE);
                        ImageLoaderUtil.getInstance().show(mActivity, path, getUserImg(i + 1), R.drawable.man);
                    }
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    getUserImg(i).setVisibility(View.GONE);
                }
                mNoticeRedImg.setVisibility(View.GONE);
            }
        }
    }

    private CircleImageView getUserImg(int pos){
        switch (pos+1) {
            case 1:
                return mUser01Img;
            case 2:
                return mUser02Img;
            case 3:
                return mUser03Img;
            case 4:
                return mUser04Img;
            case 5:
                return mUser05Img;
        }
        return null;
    }


    @OnClick({R.id.mSearchLayout, R.id.mMineConsultLayout, R.id.mFindDoctorLayout, R.id.mClinicMapLayout, R.id.mFindClinicLayout, R.id.mDiseaseMoreTv, R.id.mFindServiceMoreTv, R.id.mDepartmentMoreTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSearchLayout:
                Bundle bundle = new Bundle();
                bundle.putInt(RouterActivityUtil.FROM_TAG, DoctorSearchActivity.FROM_HOMEPAGE);
                RouterActivityUtil.startActivity(mActivity, DoctorSearchActivity.class, bundle);
                break;
            case R.id.mMineConsultLayout:
                if (((BaseActivity) mActivity).isLogin()) {
                    RouterActivityUtil.startActivity(mActivity, MyConsultActivity.class);
                }
                break;
            case R.id.mFindDoctorLayout:
                RouterActivityUtil.startActivity(mActivity, FindDoctorActivity.class);
                break;
            case R.id.mClinicMapLayout:
                RouterActivityUtil.startActivity(mActivity, ClinicListActivity.class);
                break;
            case R.id.mFindClinicLayout:
                RouterActivityUtil.startActivity(mActivity, FamousChineseHospitalActivity.class);
                break;
            case R.id.mDiseaseMoreTv:
                HPFindDoctorDialog hpFindDoctorDialog = new HPFindDoctorDialog(mActivity);
                hpFindDoctorDialog.setFromType(HPFindDoctorDialog.FROM_DISEASE);
                hpFindDoctorDialog.setFindDoctorItems(mFindDoctorInfo.getDiseaseList());
                hpFindDoctorDialog.show();
                break;
            case R.id.mFindServiceMoreTv:
                break;
            case R.id.mDepartmentMoreTv:
                HPFindDoctorDialog hpFindDoctorDialog1 = new HPFindDoctorDialog(mActivity);
                hpFindDoctorDialog1.setFromType(HPFindDoctorDialog.FROM_DEPARTMENT);
                hpFindDoctorDialog1.setFindDoctorItems(mFindDoctorInfo.getHospitalDeptList());
                hpFindDoctorDialog1.show();
                break;
        }
    }


    private void getData() {
        mDoctorModel.getHomeFindDoctorData(new HttpTaskCallBack<FindDoctorInfo>() {
            @Override
            public void onSuccess(HttpResult<FindDoctorInfo> result) {
                mFindDoctorInfo = result.getData();
                updateUI();
            }

            @Override
            public void onFail(HttpResult<FindDoctorInfo> result) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        getData();
    }

}
