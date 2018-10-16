package com.bowen.tcm.folkprescription.activity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.DiseaseInfo;
import com.bowen.tcm.common.dialog.MultiChooseContentDialog;
import com.bowen.tcm.common.event.AddFitDiseaseEvent;
import com.bowen.tcm.common.event.UploadPrescriptionSuccessEvent;
import com.bowen.tcm.folkprescription.adapter.AddPrescriptionFitDiseaseRvAdapter;
import com.bowen.tcm.folkprescription.contract.AddFolkPrescriptionContract;
import com.bowen.tcm.folkprescription.presenter.AddFolkPrescriptionPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:添加偏方
 * Created by zzp on 2018/6/25.
 */

public class AddFolkPrescriptionActivity extends BaseActivity implements AddFolkPrescriptionContract.View{

    @BindView(R.id.fitDiseaseRv)
    RecyclerView fitDiseaseRv;
    @BindView(R.id.addFolkPrescriptionName)
    EditText addFolkPrescriptionName;
    @BindView(R.id.addFolkPrescriptionUse)
    EditText addFolkPrescriptionUse;
//    @BindView(R.id.clearFolkPrescriptionNameImg)
//    ImageView clearFolkPrescriptionNameImg;
    @BindView(R.id.applyCrowdName)
    TextView applyCrowdName;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    private Context mContext;
    private AddPrescriptionFitDiseaseRvAdapter mAdapter;
    private AddFolkPrescriptionPresenter mPresenter;
//    private boolean isPhoneNumFocus = false;
    private ArrayList<ShowApplyCrowd> applyCrowdList;
    private String selectedCrowdStr;
    private boolean isFromMyPrescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_add_folk_prescription);
        ButterKnife.bind(this);
        setTitle("上传偏方");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("完成");
        mContext = this;
        mPresenter = new AddFolkPrescriptionPresenter(this, this);
        mPresenter.loadApplyCrowd();
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if(EmptyUtils.isNotEmpty(bundle)){
            isFromMyPrescriptionFragment = bundle.getBoolean(RouterActivityUtil.FROM_TAG,false);
        }
        initView();
    }

    private void initView() {
        List<DiseaseInfo> list;
        list = new ArrayList<>();
        mAdapter = new AddPrescriptionFitDiseaseRvAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        fitDiseaseRv.setLayoutManager(layoutManager);
        fitDiseaseRv.setAdapter(mAdapter);
        mAdapter.setNewData(list);
        /*addFolkPrescriptionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String temp = addFolkPrescriptionName.getText().toString();
                if (!isPhoneNumFocus || TextUtils.isEmpty(temp)) {
                    clearFolkPrescriptionNameImg.setVisibility(View.GONE);
                } else if (isPhoneNumFocus && !TextUtils.isEmpty(temp)) {
                    clearFolkPrescriptionNameImg.setVisibility(View.VISIBLE);
                }
            }
        });
        addFolkPrescriptionName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isPhoneNumFocus = hasFocus;
                String temp = addFolkPrescriptionName.getText().toString();
                if (!isPhoneNumFocus || TextUtils.isEmpty(temp)) {
                    clearFolkPrescriptionNameImg.setVisibility(View.GONE);
                } else if (isPhoneNumFocus && !TextUtils.isEmpty(temp)) {
                    clearFolkPrescriptionNameImg.setVisibility(View.VISIBLE);
                }
            }
        });*/
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        checkParamsEmpty();
    }

    private void checkParamsEmpty() {
        if(addFolkPrescriptionName.getText().length()==0){
            ToastUtil.getInstance().showToastDialog("请填写偏方名称");
            return;
        }
        if(addFolkPrescriptionUse.getText().length()==0){
            ToastUtil.getInstance().showToastDialog("请填写用法用量");
            return;
        }
        if(selectedCrowdStr!=null && selectedCrowdStr.length()==0){
            ToastUtil.getInstance().showToastDialog("请选择适用人群");
            return;
        }
        if(mAdapter.getSelectedDisease().length()==0){
            ToastUtil.getInstance().showToastDialog("请选择病症");
            return;
        }
        mPresenter.uploadPrescription(addFolkPrescriptionName.getText().toString(), addFolkPrescriptionUse.getText().toString(),
                selectedCrowdStr, mAdapter.getSelectedDisease());
    }

    private boolean checkParamsChanged() {
        if(addFolkPrescriptionName.getText().length()>0){
            return true;
        }
        if(addFolkPrescriptionUse.getText().length()>0){
            return true;
        }
        if(selectedCrowdStr!=null && selectedCrowdStr.length()>0){
            return true;
        }
        if(mAdapter.getSelectedDisease().length()>0){
            return true;
        }
        return false;
    }

    @OnClick({R.id.addFolkPrescriptionTrialCrowd, R.id.fitDiseaseEdit,R.id.contentLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addFolkPrescriptionTrialCrowd:
                if(applyCrowdList!=null && applyCrowdList.size()>0){
                    final MultiChooseContentDialog dialog = new MultiChooseContentDialog(mContext);
                    dialog.setList(applyCrowdList);
                    dialog.setBaseDialogListener(new BaseDialog.BaseDialogListener() {
                        @Override
                        public void onDataCallBack(Object... obj) {
                            applyCrowdName.setText((String)obj[0]);
                            applyCrowdName.setTextColor(getResources().getColor(R.color.color_main_black));
                        }
                    });
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            selectedCrowdStr = dialog.getSelectedId();
                        }
                    });
                } else {
                    ToastUtil.getInstance().showToastDialog("适应人群加载失败");
                }
                break;
            case R.id.fitDiseaseEdit:
                RouterActivityUtil.startActivity((Activity) mContext, FitDiseaseSelectActivity.class);
                break;
            case R.id.contentLayout:
                contentLayout.requestFocus();
                if(ApplicationUtils.isKeyboardOpen()){
                    ApplicationUtils.closeKeyboard(contentLayout);
                }
                break;
        }
    }

    @Override
    public void uploadSuccess(Object list) {
        EventBus.getDefault().post(new UploadPrescriptionSuccessEvent());
        finish();
    }

    @Override
    public void uploadFail(Object list) {

    }

    @Override
    public void loadApplyCrowdSuccess(List<ShowApplyCrowd> list) {
        applyCrowdList = (ArrayList<ShowApplyCrowd>) list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddFitDiseaseEvent event) {
        //对集合中重复的元素进行过滤
        List<DiseaseInfo> list = event.getList();
        List<DiseaseInfo> listNow = mAdapter.getData();
        if (list != null && list.size() > 0) {
            for (DiseaseInfo bean : listNow) {
                Iterator<DiseaseInfo> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (TextUtils.equals(bean.getDiseaseName(), iterator.next().getDiseaseName())) {
                        iterator.remove();
                    }
                }
            }
            if (list.size() > 0) mAdapter.addData(list);//添加新增的病症item
        }
    }

    @Override
    public void onBackPressed() {
        if(checkParamsChanged()){
            final AffirmDialog dialog = new AffirmDialog(AddFolkPrescriptionActivity.this, "提醒",
                    "数据未保存，是否保存?", "不了", "保存");
            dialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                @Override
                public void onCancle() {
                    finish();
                }
                @Override
                public void onOK() {
                    checkParamsEmpty();
                }
            });
            dialog.show();
        }else{
            super.onBackPressed();
        }
    }
}
