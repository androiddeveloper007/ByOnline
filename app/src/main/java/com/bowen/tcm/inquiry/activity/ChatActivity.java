package com.bowen.tcm.inquiry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.awen.camera.view.TakePhotoActivity;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.dialog.AffirmDialog.AffirmDialogListenner;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.model.PermissionsModel.PermissionListener;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.gallery.view.ImageSelectorActivity;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.ChatStatusInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.database.entity.ChatMessage;
import com.bowen.tcm.common.event.ChatMessageEvent;
import com.bowen.tcm.common.event.ChooseMedicalRecordEvent;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.SoftKeyBoardListenUtil;
import com.bowen.tcm.common.util.SoftKeyBoardListenUtil.OnSoftKeyBoardChangeListener;
import com.bowen.tcm.inquiry.adapter.ChatAdapter;
import com.bowen.tcm.inquiry.contract.ChatContract;
import com.bowen.tcm.inquiry.presenter.ChatPresenter;

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
 * Describe:聊天界面
 * Created by AwenZeng on 2018/7/5.
 */
public class ChatActivity extends BaseActivity implements ChatContract.View {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mChatContentEdit)
    EditText mChatContentEdit;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.mCloseTalkImg)
    ImageView mCloseTalkImg;

    private ChatAdapter mAdapter;
    private ArrayList<ChatMessage> mChatList;

    private ChatPresenter mPresenter;
    private PayOrderInfo mPayOrderInfo;
    private String toUserID;
    private String orderId;
    private String appointmentId;
    private Doctor mToDoctor;
    private PermissionsModel mPermissionsModel;
    private ChatStatusInfo mChatStatusInfo;
    private boolean isConsultOver = false;//咨询是否结束
    private boolean isCanAppiontment = false;//是否可以预约
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        getTitleBar().getTitleView().setVisibility(View.GONE);
        init();
    }


    private void init() {
        mPayOrderInfo = (PayOrderInfo) RouterActivityUtil.getSerializable(this);
        mToDoctor = mPayOrderInfo.getEmrDoctor();
        orderId = mPayOrderInfo.getOrderId();
        appointmentId = mPayOrderInfo.getAppointmentOrderId();
        if(EmptyUtils.isEmpty(orderId)){//订单ID为空时，预约ID
            orderId = appointmentId;
        }
        setTitle(mToDoctor.getName());
        mChatList = new ArrayList<>();
        mPermissionsModel = new PermissionsModel(this);
        toUserID = mToDoctor.getDoctorId();
        mPresenter = new ChatPresenter(this, this, toUserID, orderId);
        UserInfo.getInstance().setStartChat(true);

        mAdapter = new ChatAdapter(this);
        mAdapter.setReceiveUserPicUrl(mToDoctor.getHeadImgUrl());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(mChatList);
        mRecyclerView.smoothScrollToPosition(mChatList.size());

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getChatDataFromDB();
            }
        });
        mAdapter.setListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConsultOver) {
                    showConsultAgainDialog();
                }
            }
        });

        //输入法控制
        SoftKeyBoardListenUtil.setListener(this, new OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mChatList.size());
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });


        //数据库的拉取
        int size = mPresenter.getDataSize(toUserID);//所有记录
        index = size / 10;
        if(size%10 == 0){//如果被整除，直接拉取下面10条数据
            index--;
        }
        getChatDataFromDB();

        mPresenter.getChatStatusInfo(toUserID, orderId);
        mPresenter.clearChatStatus(toUserID);
        mPermissionsModel.checkWriteSDCardPermission(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getBundleExtra(RouterActivityUtil.FROM_TAG);
        if(bundle!=null){
            mPayOrderInfo =  (PayOrderInfo) bundle.getSerializable(RouterActivityUtil.FROM_TAG);
        }
        isConsultOver = false;
        orderId = mPayOrderInfo.getOrderId();
        mPresenter.getChatStatusInfo(toUserID, orderId);
        mCloseTalkImg.setImageBitmap(null);
        mCloseTalkImg.setBackgroundResource(R.drawable.chat_close_talk);
    }


    private void getChatDataFromDB() {
        mPresenter.queryChatData(toUserID+UserInfo.getInstance().getUserId(), index, 10);
    }


    private void sendMessage() {
        final String content = mChatContentEdit.getText().toString();
        if (EmptyUtils.isNotEmpty(content)) {
            mPresenter.sendMessage(content);
            mChatContentEdit.setText("");
        } else {
            ToastUtil.getInstance().toast("发送消息为空");
        }
    }

    private void sendImgMessage(String imgPath) {
        if (EmptyUtils.isNotEmpty(imgPath)) {
            mPresenter.sendImgMessage(imgPath);
        } else {
            ToastUtil.getInstance().toast("图片路径不存在");
        }
    }

    private void addChatMessage(ChatMessage message) {
        mChatList.add(message);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mChatList.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case ImageSelectorActivity.REQUEST_CAMERA: {//拍照获取图片
                    String path = data.getStringExtra(TakePhotoActivity.RESULT_PHOTO_PATH);
                    LogUtil.androidLog("图片地址：" + path);
                    sendImgMessage(path);
                    break;
                }
                case ImageSelectorActivity.REQUEST_IMAGE: {//从图库中选择图片
                    ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    for(String path:images){
                        sendImgMessage(path);
                    }
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onSendMessage(ChatMessage message) {
        String content = message.getContent();
        if(EmptyUtils.isNotEmpty(content)&&content.contains(Constants.CHAT_OVER_TAG)){
            message.setType(ChatAdapter.TYPE_CHAT_OVER);
        }
        addChatMessage(message);
    }

    @Override
    public void getChatDataBaseSuccess(ArrayList<ChatMessage> list) {
        mPtrFrameLayout.refreshComplete();
        if (EmptyUtils.isEmpty(mChatList)) {
            mChatList = list;
            mAdapter.setNewData(mChatList);
            mRecyclerView.smoothScrollToPosition(mChatList.size());
            index--;
        } else if(EmptyUtils.isNotEmpty(list)&&index >= 0) {
                mChatList.addAll(0, list);
                mAdapter.notifyDataSetChanged();
                index--;
        }
    }

    @Override
    public void getChatDataBaseFailed() {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onGetDoctorDetailSuccess(DoctorDetail doctorDetail) {
        if(EmptyUtils.isNotEmpty(doctorDetail)) {
            mToDoctor = doctorDetail.getEmrDoctor();
        }
    }

    @Override
    public void onCloseChatConsultSuccess() {
        isConsultOver = true;
        ScreenSizeUtil.setImageViewGray(mCloseTalkImg);
        mPresenter.sendMessage(Constants.CHAT_OVER_TAG);
    }

    @Override
    public void getChatStatusInfoSuccess(ChatStatusInfo info) {
        isConsultOver = info.isServiceEnd();
        isCanAppiontment = info.isShowAppointment();
        mChatStatusInfo = info;
        if(isConsultOver){
            ScreenSizeUtil.setImageViewGray(mCloseTalkImg);
        }
        if(info.getIsFirstConsult() == 1){//第一次咨询
            StringBuilder builder = new StringBuilder();
            builder.append("患者：").append(info.getFamilyNickname());
            if(EmptyUtils.isNotEmpty(info.getAge())){
                builder.append(",年龄").append((int)Float.parseFloat(info.getAge())).append("岁");
            }
            builder.append("\n描述：").append(info.getIllDesc());
            mPresenter.sendMessage(builder.toString());
        }
    }

    @OnClick({R.id.mSendBtn, R.id.mGalleryImg, R.id.mTakePhotoImg, R.id.mMedicalRecordImg, R.id.mReservationImg, R.id.mCloseTalkImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSendBtn:
                if (!isCanChat()) {
                    return;
                }
                sendMessage();
                break;
            case R.id.mGalleryImg:
                if (!isCanChat()) {
                    return;
                }
                mPermissionsModel.checkReadSDCardPermission(new PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission) {
                            ImageSelectorActivity.start(ChatActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE,
                                    false, true, false);
                        }
                    }
                });
                break;
            case R.id.mTakePhotoImg:
                if (!isCanChat()) {
                    return;
                }
                mPermissionsModel.checkCameraPermission(new PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission) {
                            Intent intent = new Intent(ChatActivity.this, TakePhotoActivity.class);
                            startActivityForResult(intent, ImageSelectorActivity.REQUEST_CAMERA);
                        }
                    }
                });
                break;
            case R.id.mMedicalRecordImg:
                if (!isCanChat()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(RouterActivityUtil.FROM_TAG, UserInfo.getInstance().getFamilyId());
                RouterActivityUtil.startActivity(this, LoadMedicalRecordActivity.class, bundle);
                break;
            case R.id.mReservationImg:
                if(isCanAppiontment){
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable(RouterActivityUtil.FROM_TAG, mToDoctor);
                    RouterActivityUtil.startActivity(this, OutpatientAppointmentActivity.class, bundle1);
                }else{
                    ToastUtil.getInstance().showToastDialog("该医生暂未提供门诊预约服务");
                }

                break;
            case R.id.mCloseTalkImg:
                if(!isConsultOver){
                    showIsCloseConsultDialog();
                }
                break;
        }
    }

    /**
     * 是否还可以聊天
     *
     * @return
     */
    private boolean isCanChat() {
        if (isConsultOver) {
            showConsultAgainDialog();
            return false;
        }
        return true;
    }

    /**
     * 是否再次咨询
     */
    private void showConsultAgainDialog() {
        AffirmDialog affirmDialog = new AffirmDialog(this, "本次服务已结束，是否再次发起咨询？", "不用了", "再次咨询");
        affirmDialog.setAffirmDialogListenner(new AffirmDialogListenner() {
            @Override
            public void onCancle() {
            }
            @Override
            public void onOK() {
                mPresenter.checkImmediateConsult(mToDoctor.getDoctorId());
            }
        });
        affirmDialog.show();
    }

    /**
     * 是否结束咨询
     */
    private void showIsCloseConsultDialog() {
        if (isConsultOver) {
            return;
        }
        AffirmDialog affirmDialog = new AffirmDialog(ChatActivity.this, "确定要结束本次咨询服务吗？");
        affirmDialog.setAffirmDialogListenner(new AffirmDialogListenner() {
            @Override
            public void onCancle() {

            }
            @Override
            public void onOK() {
                mPresenter.closeChatConsult(orderId);
            }
        });
        affirmDialog.show();
    }

    @Override
    public void onCheckImmediateConsultSuccess(Doctor doctor) {
        if(!doctor.isAsk()){
            if(doctor.isShowConsult()){
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouterActivityUtil.FROM_TAG, doctor);
                RouterActivityUtil.startActivity(ChatActivity.this, ImgTextCosultDetailActivity.class,bundle);
            }else{
                ToastUtil.getInstance().showToastDialog("该医生暂不提供图文咨询服务");
            }
        }else{
            isConsultOver = false;
            orderId = doctor.getOrderId();
            mPresenter.getChatStatusInfo(toUserID, orderId);
            mCloseTalkImg.setImageBitmap(null);
            mCloseTalkImg.setBackgroundResource(R.drawable.chat_close_talk);
            ToastUtil.getInstance().showToastDialog("您已重新下单，可重新咨询");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChatMessageEvent message) {
        ChatMessage chatMessage = (ChatMessage) message.getData();
        if (chatMessage.getUserId().contains(toUserID)) {
            addChatMessage(chatMessage);
        } else {
            mPresenter.showNotification(chatMessage.getContent());
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseMedicalRecordEvent event) {
        MedicalRecord medicalRecord = (MedicalRecord) event.getData();
        StringBuilder builder = new StringBuilder();
        builder.append("患者：").append(mChatStatusInfo.getFamilyNickname());
        if(EmptyUtils.isNotEmpty(mChatStatusInfo.getAge())){
            builder.append(",年龄").append((int)Float.parseFloat(mChatStatusInfo.getAge())).append("岁");
        }
        builder.append("\n描述：").append(medicalRecord.getCaseDetails());
        mPresenter.sendMessage(builder.toString());
        for(PhotoFile file:medicalRecord.getComFileInfoList()){
            mPresenter.sendImgMessage(file.getFileLink());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.getInstance().setStartChat(false);
        if (EmptyUtils.isNotEmpty(mChatList)) {
            mChatList.clear();
        }
    }

}
