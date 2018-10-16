package com.bowen.tcm.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.utils.SpannableStringUtils.Builder;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.DrugVolume;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.Message;
import com.bowen.tcm.common.dialog.ShareDialog;
import com.bowen.tcm.common.model.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/3.
 */

public class MessageAdapter extends BaseQuickAdapter<Message> {

    @BindView(R.id.mTimeTv)
    TextView mTimeTv;
    @BindView(R.id.mIgnoreBtnTv)
    TextView mIgnoreBtnTv;
    @BindView(R.id.mFinishBtnTv)
    TextView mFinishBtnTv;
    @BindView(R.id.mRemindBtnTv)
    TextView mRemindBtnTv;
    @BindView(R.id.mMsgTitileTv)
    TextView mMsgTitileTv;
    @BindView(R.id.mMsgContent01Tv)
    TextView mMsgContent01Tv;
    @BindView(R.id.mMsgContent02Tv)
    TextView mMsgContent02Tv;
    @BindView(R.id.mRemindFunctionLayout)
    LinearLayout mRemindFunctionLayout;
    @BindView(R.id.mLineView)
    View mLineView;


    private UpdateMessageStatusListener mUpdateMessageStatusListener;

    public interface UpdateMessageStatusListener {
        void onUpdateMessageStatus(String msgId, int status);
    }

    public MessageAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_message;
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mTimeTv.setText(DateUtil.date2String(item.getMsgHandDate(), DateUtil.DEFAULT_FORMAT_DAYTIME));
        mMsgTitileTv.setText(item.getMsgTypeStr());
        if (item.getMsgType() == Constants.MESSAGE_TYPE_ALARM_REMIND) {
            mRemindFunctionLayout.setVisibility(View.VISIBLE);
            mMsgContent01Tv.setVisibility(View.VISIBLE);
            mLineView.setVisibility(View.VISIBLE);
            showRemindMessage(item);
        } else {
            mRemindFunctionLayout.setVisibility(View.GONE);
            mMsgContent01Tv.setVisibility(View.GONE);
            mLineView.setVisibility(View.GONE);
            mMsgContent02Tv.setText(item.getMsgContent());
        }
    }


    private void showRemindMessage(Message item) {
        Builder builder = SpannableStringUtils.getBuilder("提醒");
        builder.setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray));
        String familyType = item.getRemindFamilyType();
        if (EmptyUtils.isNotEmpty(familyType) && Integer.parseInt(familyType) == 0) {//0:为本人
            builder.append("自己");
            builder.setForegroundColor(mContext.get().getResources().getColor(R.color.color_red));
        } else {
            if (EmptyUtils.isNotEmpty(item.getRemindNickname())) {
                builder.append(item.getRemindNickname());
                builder.setForegroundColor(mContext.get().getResources().getColor(R.color.color_red));
            }
        }
        builder.append("记得在").setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black))
                .append(DateUtil.date2String(item.getRemindTime(), DateUtil.DEFAULT_FORMAT_HOUR))
                .setForegroundColor(mContext.get().getResources().getColor(R.color.color_red));

        mMsgContent01Tv.setText(builder.create());
        if (EmptyUtils.isNotEmpty(item.getEmrMedicineList())) {
            Builder builder1 = SpannableStringUtils.getBuilder("服用")
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black));
            List<DrugVolume> drugVolumes = item.getEmrMedicineList();
            for (int i = 0; i < drugVolumes.size(); i++) {
                builder1.append(drugVolumes.get(i).getDrugName()).setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black));
                builder1.append(drugVolumes.get(i).getDosage()).setForegroundColor(mContext.get().getResources().getColor(R.color.color_red));
                if (i != drugVolumes.size() - 1) {
                    builder1.append("、").setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black));
                }
            }
            mMsgContent02Tv.setText(builder1.create());
        }
        mIgnoreBtnTv.setTag(item);
        mFinishBtnTv.setTag(item);
        mRemindBtnTv.setTag(item);
        showMsgStatusUI(Integer.parseInt(item.getMsgStatus()));
    }


    private void showMsgStatusUI(int status) {
        switch (status) {
            case Constants.MESSAGE_STATUS_NOT_HANDLE:
                mIgnoreBtnTv.setText("忽略");
                mIgnoreBtnTv.setVisibility(View.VISIBLE);
                mFinishBtnTv.setVisibility(View.VISIBLE);
                mRemindBtnTv.setVisibility(View.VISIBLE);
                break;
            case Constants.MESSAGE_STATUS_IGNORE:
                mIgnoreBtnTv.setText("已忽略");
                mIgnoreBtnTv.setVisibility(View.VISIBLE);
                mFinishBtnTv.setVisibility(View.GONE);
                mRemindBtnTv.setVisibility(View.GONE);
                break;
            case Constants.MESSAGE_STATUS_FINISH:
                mIgnoreBtnTv.setText("已提醒");
                mIgnoreBtnTv.setVisibility(View.VISIBLE);
                mFinishBtnTv.setVisibility(View.GONE);
                mRemindBtnTv.setVisibility(View.GONE);
                break;
        }
    }

    public void setmUpdateMessageStatusListener(UpdateMessageStatusListener mUpdateMessageStatusListener) {
        this.mUpdateMessageStatusListener = mUpdateMessageStatusListener;
    }

    @OnClick({R.id.mIgnoreBtnTv, R.id.mFinishBtnTv, R.id.mRemindBtnTv})
    public void onViewClicked(View view) {
        Message message = (Message) view.getTag();
        switch (view.getId()) {
            case R.id.mIgnoreBtnTv:
                if (EmptyUtils.isNotEmpty(mUpdateMessageStatusListener)) {
                    mUpdateMessageStatusListener.onUpdateMessageStatus(message.getMsgId(), Constants.MESSAGE_STATUS_IGNORE);
                }
                break;
            case R.id.mFinishBtnTv:
                if (EmptyUtils.isNotEmpty(mUpdateMessageStatusListener)) {
                    mUpdateMessageStatusListener.onUpdateMessageStatus(message.getMsgId(), Constants.MESSAGE_STATUS_FINISH);
                }
                break;
            case R.id.mRemindBtnTv:
                ShareData shareData = new ShareData();
                shareData.setTitile("用药提醒");
                shareData.setPhoneNum(message.getRemindPhone());
                shareData.setShareType(Platform.SHARE_TEXT);
                StringBuilder builder = new StringBuilder(DateUtil.date2String(message.getRemindTime(), DateUtil.DEFAULT_FORMAT_HOUR));
                builder.append("了，记得按时吃药哦！\n");
                builder.append("这会儿，该吃这些药了：\n");
                for (DrugVolume drugVolume : message.getEmrMedicineList()) {
                    builder.append(drugVolume.getDrugName());
                    builder.append(drugVolume.getDosage() + "\n");
                }
                builder.append("\n" + "按时吃药，生病才能快快好~");
                shareData.setContent(builder.toString());
                ShareDialog.getBuilder().shareDialogType(ShareDialog.TYPE_SHARE_DIALOG_REMIND).shareData(shareData).build(mContext.get()).show();
                break;
        }
    }

}
