package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog.BaseDialogListener;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.bean.SubmitOrderInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.dialog.ChooseMemberDialog;
import com.bowen.tcm.common.event.ChooseMedicalRecordEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.inquiry.model.PayModel;
import com.bowen.tcm.mine.activity.AddFamilyMemberActivity;
import com.bowen.tcm.mine.model.FamilyMemberModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:图文咨询详情页
 * Created by AwenZeng on 2018/7/12.
 */
public class ImgTextCosultDetailActivity extends BaseActivity {

    @BindView(R.id.mChooseMemberTipsTv)
    TextView mChooseMemberTipsTv;
    @BindView(R.id.mArrowRightImg)
    ImageView mArrowRightImg;
    @BindView(R.id.mMemberHeadPortraitImg)
    CircleImageView mMemberHeadPortraitImg;
    @BindView(R.id.mMemberNameTv)
    TextView mMemberNameTv;
    @BindView(R.id.mMemberAgeTv)
    TextView mMemberAgeTv;
    @BindView(R.id.mMemberInfoLayout)
    LinearLayout mMemberInfoLayout;
    @BindView(R.id.mChooseMemberLayout)
    RelativeLayout mChooseMemberLayout;
    @BindView(R.id.mAddFamilyMemberTv)
    TextView mAddFamilyMemberTv;
    @BindView(R.id.mLoadMedicalRecordTv)
    TextView mLoadMedicalRecordTv;
    @BindView(R.id.mDiseaseDesEdit)
    EditText mDiseaseDesEdit;

    private FamilyMemberModel mFamilyMemberModel;
    private PayModel mPayModel;
    private FamilyMember mChooseFamilyMember;
    private SubmitOrderInfo mSubmitOrderInfo;
    private String mDiseaseDes;
    private Doctor mDoctor;
    private MedicalRecord mMedicalRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_img_text_consult_detail);
        ButterKnife.bind(this);
        setTitle("图文咨询");
        init();
    }


    private void init(){
        mDoctor = (Doctor)RouterActivityUtil.getSerializable(this);
        mPayModel = new PayModel(this);
        mFamilyMemberModel = new FamilyMemberModel(this);
        mFamilyMemberModel.getFamilyUserRelationShip();
    }

    @OnClick({R.id.mChooseMemberLayout,R.id.mAddFamilyMemberTv, R.id.mLoadMedicalRecordTv,R.id.mNextBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChooseMemberLayout:
                ChooseMemberDialog chooseMemberDialog = new ChooseMemberDialog(this);
                chooseMemberDialog.show();
                chooseMemberDialog.setBaseDialogListener(new BaseDialogListener() {
                    @Override
                    public void onDataCallBack(Object... obj) {
                        mChooseFamilyMember = (FamilyMember)obj[0];
                        mMemberInfoLayout.setVisibility(View.VISIBLE);
                        ImageLoaderUtil.getInstance().show(ImgTextCosultDetailActivity.this,mChooseFamilyMember.getHeadSculptureUrl(),
                                mMemberHeadPortraitImg,R.drawable.man);
                        mMemberNameTv.setText(mChooseFamilyMember.getFamilyNickname());
                        if(EmptyUtils.isNotEmpty(mChooseFamilyMember.getAge())){
                            mMemberAgeTv.setText(mChooseFamilyMember.getAge()+"岁");
                        }else{
                            mMemberAgeTv.setText("未知");
                        }

                    }
                });
                break;
            case R.id.mAddFamilyMemberTv:
                RouterActivityUtil.startActivity(this, AddFamilyMemberActivity.class);
                break;
            case R.id.mLoadMedicalRecordTv:
                if(EmptyUtils.isNotEmpty(mChooseFamilyMember)){
                    Bundle bundle = new Bundle();
                    bundle.putString(RouterActivityUtil.FROM_TAG,mChooseFamilyMember.getFamilyId());
                    RouterActivityUtil.startActivity(this, LoadMedicalRecordActivity.class,bundle);
                }else{
                    ToastUtil.getInstance().showToastDialog("请先选择就诊人");
                }

                break;
            case R.id.mNextBtn:
                if(checkContent()){
                   addSubmitOrderInfo();
                   createOrder();
                }
                break;
        }
    }

    private boolean checkContent(){
        mDiseaseDes = mDiseaseDesEdit.getText().toString();
        if(EmptyUtils.isEmpty(mChooseFamilyMember)){
            ToastUtil.getInstance().showToastDialog("请选选择就诊人");
            return false;
        }

        if(EmptyUtils.isEmpty(mDiseaseDes)){
            ToastUtil.getInstance().showToastDialog("病症描述不可为空");
            return false;
        }
        return true;
    }


    private void addSubmitOrderInfo(){
        mSubmitOrderInfo = new SubmitOrderInfo();
        if(EmptyUtils.isNotEmpty(mDoctor)&&EmptyUtils.isNotEmpty(mChooseFamilyMember)){
            mSubmitOrderInfo.setDoctorId(mDoctor.getDoctorId());
            mSubmitOrderInfo.setPrice(mDoctor.getConsultFee());//==null ? "0":mDoctor.getConsultFee()
            mSubmitOrderInfo.setOrderType(Constants.TYPE_PRODUCT_CONSULT+"");
            mSubmitOrderInfo.setFamilyId(mChooseFamilyMember.getFamilyId());
            if(EmptyUtils.isNotEmpty(mMedicalRecord)){
                mSubmitOrderInfo.setCaseId(mMedicalRecord.getCaseId());
            }
            mSubmitOrderInfo.setIllDesc(mDiseaseDes);
        }
    }


    private void createOrder(){
        mPayModel.createPayOrder(mSubmitOrderInfo, new HttpTaskCallBack<PayOrderInfo>() {
            @Override
            public void onSuccess(HttpResult<PayOrderInfo> result) {
                if(result.getData().isPay()){
                    PayOrderInfo payOrderInfo = result.getData();
                    payOrderInfo.setPaySuccess(true);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG,payOrderInfo);
                    RouterActivityUtil.startActivity(ImgTextCosultDetailActivity.this, PayResultActivity.class,bundle,true);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RouterActivityUtil.FROM_TAG,result.getData());
                    RouterActivityUtil.startActivity(ImgTextCosultDetailActivity.this, PayDetaitActivity.class,bundle,true);
                }
            }

            @Override
            public void onFail(HttpResult<PayOrderInfo> result) {
               ToastUtil.getInstance().toast(result.getMsg());
            }
        });
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseMedicalRecordEvent event) {
        mMedicalRecord = (MedicalRecord)event.getData();
        mDiseaseDesEdit.setText(mMedicalRecord.getCaseDetails());
    }

}
