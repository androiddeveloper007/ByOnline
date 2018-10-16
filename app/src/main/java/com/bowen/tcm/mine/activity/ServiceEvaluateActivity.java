package com.bowen.tcm.mine.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.ActionTitleBar;
import com.bowen.gallery.utils.ScreenUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.common.bean.network.VoListBean;
import com.bowen.tcm.common.event.CancelOrDeleteOrderSuccessEvent;
import com.bowen.tcm.common.widget.GridLeft2RightDecoration;
import com.bowen.tcm.common.widget.GridOffsetsItemDecoration;
import com.bowen.tcm.folkprescription.adapter.FolkPrescriptionSearchRvAdapter;
import com.bowen.tcm.mine.adapter.ServiceEvaluateRvAdapter;
import com.bowen.tcm.mine.contract.ServiceEvaluateContract;
import com.bowen.tcm.mine.presenter.ServiceEvaluatePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:服务评价
 * Created by AwenZeng on 2018/7/5.
 */
public class ServiceEvaluateActivity extends BaseActivity implements ServiceEvaluateContract.View{
    @BindView(R.id.mTitleBar)
    ActionTitleBar mTitleBar;
    @BindView(R.id.mServiceEvaluageRatingBar)
    RatingBar mServiceEvaluageRatingBar;
    @BindView(R.id.mDoctorAtitudeRatingBar)
    RatingBar mDoctorAtitudeRatingBar;
    @BindView(R.id.mDoctorReplyRateRatingBar)
    RatingBar mDoctorReplyRateRatingBar;
    @BindView(R.id.mServiceLevelRatingBar)
    RatingBar mServiceLevelRatingBar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mEvaluateContentEdit)
    EditText mEvaluateContentEdit;
    @BindView(R.id.mSubmitBtn)
    TextView mSubmitBtn;
    private ServiceEvaluatePresenter mPresenter;
    private ServiceEvaluateRvAdapter mAdapter;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_service_evaluate);
        ButterKnife.bind(this);
        setTitle("服务评价");
        mPresenter = new ServiceEvaluatePresenter(this, this);
        mPresenter.userEvaluateVoList();
        orderId = RouterActivityUtil.getString(this);
    }

    private void initRecyclerView(List<VoListBean> list) {
        mAdapter = new ServiceEvaluateRvAdapter(this, list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);//, GridLayoutManager.VERTICAL, false
        mRecyclerView.setLayoutManager(layoutManager);
        GridLeft2RightDecoration offsetsItemDecoration = new GridLeft2RightDecoration();
        mRecyclerView.addItemDecoration(offsetsItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ServiceEvaluateRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, String str) {
                mEvaluateContentEdit.setText(str);
            }
        });
    }

    @OnClick(R.id.mSubmitBtn)
    public void onViewClicked() {
        mPresenter.saveTraOrderById(orderId, mServiceEvaluageRatingBar.getRating(),
                mDoctorAtitudeRatingBar.getRating(), mDoctorReplyRateRatingBar.getRating(),
                mServiceLevelRatingBar.getRating(),
                mEvaluateContentEdit.getText().toString());
    }

    @Override
    public void onLoadSuccess(List<VoListBean> list) {
        initRecyclerView(list);
    }

    @Override
    public void onLoadFail(List<VoListBean> list) {

    }

    @Override
    public void saveEvaluateSucc() {
        EventBus.getDefault().post(new CancelOrDeleteOrderSuccessEvent());//返回订单页面时，刷新订单页
        finish();
    }

    @Override
    public void saveEvaluateFail() {

    }

    @Override
    public void onBackPressed() {
        final AffirmDialog dialog = new AffirmDialog(this, "提醒",
                "评价一下医生的服务再离开吧。", "不了", "继续评价");
        dialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
            @Override
            public void onCancle() {
                finish();
            }
            @Override
            public void onOK() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
