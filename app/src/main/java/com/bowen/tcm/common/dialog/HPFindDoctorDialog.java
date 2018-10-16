package com.bowen.tcm.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.FindDoctorItem;
import com.bowen.tcm.common.dialog.adapter.FindDoctorDepartmentAdapter;
import com.bowen.tcm.common.dialog.adapter.FindDoctorDiseaseAdapter;
import com.bowen.tcm.inquiry.activity.FindDoctorActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/2.
 */

public class HPFindDoctorDialog extends BaseDialog {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mTitleTv)
    TextView mTitleTv;
    @BindView(R.id.mCancleTv)
    TextView mCancleTv;

    private int fromType;
    private FindDoctorDiseaseAdapter mDiseaseAdapter;
    private FindDoctorDepartmentAdapter mDepartmentAdapter;
    private ArrayList<FindDoctorItem> mFindDoctorItems;


    public static final int FROM_DISEASE = 0;//常见疾病
    public static final int FROM_DEPARTMENT = 1;//所有科室



    public HPFindDoctorDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
    }


    public HPFindDoctorDialog(Context context, int themeResId){
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hp_find_doctor);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager1);
        if(fromType == FROM_DISEASE){
            mTitleTv.setText("常见疾病");
            mDiseaseAdapter = new FindDoctorDiseaseAdapter(mContext);
            mRecyclerView.setAdapter(mDiseaseAdapter);
            mDiseaseAdapter.setNewData(mFindDoctorItems);
            mDiseaseAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position){
                    SearchField searchField = new SearchField();
                    searchField.setDiseaseId(mDiseaseAdapter.getItem(position).getDiseaseId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG, searchField);
                    RouterActivityUtil.startActivity((Activity) mContext, FindDoctorActivity.class, bundle);
                    dismiss();
                }
            });
        }else if(fromType == FROM_DEPARTMENT){
            mTitleTv.setText("所有科室");
            mDepartmentAdapter = new FindDoctorDepartmentAdapter(mContext);
            mRecyclerView.setAdapter(mDepartmentAdapter);
            mDepartmentAdapter.setNewData(mFindDoctorItems);
            mDepartmentAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SearchField searchField = new SearchField();
                    searchField.setDepartmentsId(mDepartmentAdapter.getItem(position).getDepartmentsId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG, searchField);
                    RouterActivityUtil.startActivity((Activity) mContext, FindDoctorActivity.class, bundle);
                    dismiss();
                }
            });
        }
        setCanceledOnTouchOutside(true);
    }


    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public void setFindDoctorItems(ArrayList<FindDoctorItem> mFindDoctorItems) {
        this.mFindDoctorItems = mFindDoctorItems;
    }

    @OnClick({R.id.mCancleTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCancleTv:
                dismiss();
                break;
        }
    }
}
