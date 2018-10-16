package com.bowen.tcm.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.model.SwitchFragmentModel;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.StackUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.FamilyMember;
import com.bowen.tcm.common.event.ChatServerLoginSuccessEvent;
import com.bowen.tcm.common.event.ChooseFamilyMemberEvent;
import com.bowen.tcm.common.event.LoginEvent;
import com.bowen.tcm.common.event.NetworkConnectEvent;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.healthcare.fragment.HealthFragment;
import com.bowen.tcm.homepage.HomePageFragment;
import com.bowen.tcm.inquiry.HPFindDoctorFragment;
import com.bowen.tcm.inquiry.model.chat.ChatModel;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager;
import com.bowen.tcm.main.model.AppModel;
import com.bowen.tcm.mine.MineFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.mHomeImg)
    ImageView mHomeImg;
    @BindView(R.id.mHomeTv)
    TextView mHomeTv;
    @BindView(R.id.mMineImg)
    ImageView mMineImg;
    @BindView(R.id.mMineTv)
    TextView mMineTv;
    @BindView(R.id.mHomeLayout)
    LinearLayout mHomeLayout;
    @BindView(R.id.mRecordImg)
    ImageView mRecordImg;
    @BindView(R.id.mRecordTv)
    TextView mRecordTv;
    @BindView(R.id.mRecordLayout)
    LinearLayout mRecordLayout;
    @BindView(R.id.mHealthCareImg)
    ImageView mHealthCareImg;
    @BindView(R.id.mHealthCareTv)
    TextView mHealthCareTv;
    @BindView(R.id.mHealthCareLayout)
    LinearLayout mHealthCareLayout;
    @BindView(R.id.mMineLayout)
    LinearLayout mMineLayout;
    @BindView(R.id.act_main_bottom_layout)
    LinearLayout actMainBottomLayout;
    @BindView(R.id.mFragmentContent)
    FrameLayout mFragmentContent;
    private HomePageFragment mHomePageFragment;
    private HPFindDoctorFragment mFindDoctorFrament;
    private HealthFragment mHealthFragment;
    private MineFragment mMineFragment;
    private SwitchFragmentModel switchFragmentModel;
    private AppModel mAppModel;
    private Fragment baseFragment;
    private int showTab;
    private String familyId;
    private long lastTime = 0; //记录第一次点击的时间
    public static final int KEY_HOME        = 100;
    public static final int KEY_RECORD      = 101;
    public static final int KEY_HEALTH_CARE = 102;
    public static final int KEY_MINE        = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        switchFragmentModel = new SwitchFragmentModel(this);
        mAppModel = new AppModel(this);
        mAppModel.getAppConfig();
        mAppModel.getColumnsList();

        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (bundle != null) {
            showTab = bundle.getInt(RouterActivityUtil.FROM_TAG,KEY_HOME);
            chooseFragment(showTab);
        }else{
            chooseFragment(KEY_HOME);
        }
    }



    @Override
    protected boolean enableSlidingClose() {
        return false;
    }

    /**
     * SingleTask方式启动调用
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getBundleExtra(RouterActivityUtil.FROM_TAG);
        if (bundle != null) {
            showTab = bundle.getInt(RouterActivityUtil.FROM_TAG);
            chooseFragment(showTab);
        }
    }

    public void chooseFragment(int type) {
        showTab = type;
        switch (type) {
            case KEY_HOME:
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                }
                mHomePageFragment.setFamilyId(familyId);
                baseFragment = mHomePageFragment;
                selectItem(true, false, false,false);
                break;
            case KEY_RECORD:
                if(mFindDoctorFrament == null){
                    mFindDoctorFrament = new HPFindDoctorFragment();
                }
                baseFragment = mFindDoctorFrament;
                selectItem(false, true, false,false);
                break;
            case KEY_HEALTH_CARE:
                if (mHealthFragment == null) {
                    mHealthFragment = new HealthFragment();
                }
                baseFragment = mHealthFragment;
                selectItem(false, false, true,false);
                break;
            case KEY_MINE:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                }
                baseFragment = mMineFragment;
                selectItem(false, false, false,true);
                break;
        }
        switchFragmentModel.add(baseFragment, R.id.mFragmentContent);
    }

    /**
     * 按钮显示
     * @param isShowHome
     * @param isShowRecord
     * @param isShowHealthCare
     * @param isShowMine
     */
    private void selectItem(boolean isShowHome, boolean isShowRecord, boolean isShowHealthCare,boolean isShowMine) {
        mHomeImg.setSelected(isShowHome);
        mHomeTv.setSelected(isShowHome);
        mRecordImg.setSelected(isShowRecord);
        mRecordTv.setSelected(isShowRecord);
        mHealthCareImg.setSelected(isShowHealthCare);
        mHealthCareTv.setSelected(isShowHealthCare);
        mMineImg.setSelected(isShowMine);
        mMineTv.setSelected(isShowMine);
    }

    @OnClick({R.id.mHomeLayout, R.id.mRecordLayout,R.id.mHealthCareLayout, R.id.mMineLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mHomeLayout:
                chooseFragment(KEY_HOME);
                break;
            case R.id.mRecordLayout:
                chooseFragment(KEY_RECORD);
                break;
            case R.id.mHealthCareLayout:
                chooseFragment(KEY_HEALTH_CARE);
                break;
            case R.id.mMineLayout:
                chooseFragment(KEY_MINE);
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseFamilyMemberEvent event) {
        FamilyMember familyMember = (FamilyMember) event.getData();
        if (EmptyUtils.isNotEmpty(familyMember)) {
            familyId = familyMember.getFamilyId();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        //如果检测到本地有关注实体类集合，就请求后台设置
        if(DataCacheUtil.getInstance().getBoolean(DataCacheUtil.CHOOSE_INTEREST_SUCCESS, false)
                && !DataCacheUtil.getInstance().getBoolean(DataCacheUtil.UPLOAD_INTEREST_SUCCESS, false)) {
            mAppModel.submitChooseColumns();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChatServerLoginSuccessEvent event) {
        ChatModel chatModel = new ChatModel(this);
        chatModel.addChatListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NetworkConnectEvent event) {
        if((Boolean) event.getData()){
            ChatServerManager.startReconnectLogin();
            mAppModel.getAppConfig();
            mAppModel.getColumnsList();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - lastTime) > 2000) {
            ToastUtil.getInstance().toast("再按一次退出");
            lastTime = System.currentTimeMillis();
        } else {
            UserInfo.getInstance().setChatServerLoginSuccess(false);
            ChatServerManager.closeConnection();
            StackUtils.getInstanse().exitApp();
        }
    }
}
