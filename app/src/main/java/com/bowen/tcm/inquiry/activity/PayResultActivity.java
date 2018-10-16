package com.bowen.tcm.inquiry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.inquiry.model.chat.ChatModel;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager.ChatServerLoginListener;
import com.bowen.tcm.mine.activity.MyOrderActivity;
import com.bowen.tcm.mine.activity.MyOrderDetailActivity;
import com.bowen.tcm.mine.activity.MyOutpatientAppointmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bowen.tcm.common.model.Constants.TYPE_PRODUCT_CONSULT;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/3.
 */
public class PayResultActivity extends BaseActivity {
    @BindView(R.id.mPayResultImg)
    ImageView mPayResultImg;
    @BindView(R.id.mPayResultTv)
    TextView mPayResultTv;
    @BindView(R.id.mImmediateConsultTv)
    TextView mImmediateConsultTv;

    private PayOrderInfo mPayOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_pay_result);
        ButterKnife.bind(this);
        setTitle("支付结果");
        mPayOrderInfo = (PayOrderInfo) RouterActivityUtil.getSerializable(this);
        if (mPayOrderInfo.isPaySuccess()) {
            mPayResultImg.setBackgroundResource(R.drawable.pay_result_success);
            mPayResultTv.setText("支付成功");
            if(mPayOrderInfo.getOrderType() == TYPE_PRODUCT_CONSULT){
                mImmediateConsultTv.setText("立即咨询");
                loginChatServer();
            }else{
                mImmediateConsultTv.setText("查看详情");
            }
        } else {
            mPayResultImg.setBackgroundResource(R.drawable.pay_result_failed);
            mImmediateConsultTv.setText("重新支付");
            mPayResultTv.setText("支付失败");
        }
    }

    @OnClick({R.id.mImmediateConsultTv, R.id.mCheckOrderTv})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.mImmediateConsultTv:
                if(mPayOrderInfo.isPaySuccess()){
                    if(mPayOrderInfo.getOrderType() == TYPE_PRODUCT_CONSULT){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(RouterActivityUtil.FROM_TAG,mPayOrderInfo);
                        RouterActivityUtil.startActivity(this, ChatActivity.class,bundle,true);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString(RouterActivityUtil.FROM_TAG,  mPayOrderInfo.getAppointmentOrderId());
                        RouterActivityUtil.startActivity(this, MyOrderDetailActivity.class, bundle,true);
                    }
                }else{
                    finish();
                }
                break;
            case R.id.mCheckOrderTv:
                if(mPayOrderInfo.getOrderType() == TYPE_PRODUCT_CONSULT){
                    RouterActivityUtil.startActivity(this, MyOrderActivity.class,true);
                }else{
                    RouterActivityUtil.startActivity(this, MyOutpatientAppointmentActivity.class,true);
                }

                break;
        }
    }

    private void loginChatServer() {
        if(!UserInfo.getInstance().isChatServerLoginSuccess()){
            ChatServerManager.login(new ChatServerLoginListener(){
                @Override
                public void backLoginSucessStatus(boolean isSuccess) {
                    if (isSuccess) {
                        ChatModel chatModel = new ChatModel(PayResultActivity.this);
                        chatModel.addChatListener();
                        chatModel.addFileListener();
                    }
                }
            });
        }
    }
}
