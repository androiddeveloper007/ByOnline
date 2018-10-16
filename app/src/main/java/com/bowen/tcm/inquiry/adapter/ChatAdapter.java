package com.bowen.tcm.inquiry.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.dialog.AffirmDialog.AffirmDialogListenner;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.database.entity.ChatMessage;
import com.bowen.tcm.common.dialog.ShowBigPicDialog;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.inquiry.activity.ImgTextCosultDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/18.
 */
public class ChatAdapter extends BaseQuickAdapter<ChatMessage> {

    @BindView(R.id.mChatTimeTv)
    TextView mChatTimeTv;
    @BindView(R.id.mOtherHeadPortraitImg)
    CircleImageView mOtherHeadPortraitImg;
    @BindView(R.id.mOtherChatContentTv)
    TextView mOtherChatContentTv;
    @BindView(R.id.mOtherChatImg)
    ImageView mOtherChatImg;
    @BindView(R.id.mOtherChatLayout)
    RelativeLayout mOtherChatLayout;
    @BindView(R.id.mSelfHeadPortraitImg)
    CircleImageView mSelfHeadPortraitImg;
    @BindView(R.id.mSelfChatContentTv)
    TextView mSelfChatContentTv;
    @BindView(R.id.mSelfChatImg)
    ImageView mSelfChatImg;
    @BindView(R.id.mSelfChatLayout)
    RelativeLayout mSelfChatLayout;

    private String receiveUserPicUrl;

    public static final int TYPE_CHAT_SEND = 0;//发送
    public static final int TYPE_CHAT_RECEIVE = 1;//接收
    public static final int TYPE_CHAT_OVER = 2;//结束

    private View.OnClickListener mListener;

    public void setListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    public ChatAdapter(Context cxt) {
        super(cxt);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_chat_item;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item, int position) {
        ButterKnife.bind(this, helper.convertView);
        switch (item.getType()) {
            case TYPE_CHAT_SEND:
                mChatTimeTv.setText(item.getDate());
                mOtherChatLayout.setVisibility(View.GONE);
                mSelfChatLayout.setVisibility(View.VISIBLE);
                ImageLoaderUtil.getInstance().show(mContext.get(), UserInfo.getInstance().getPicUrl(), mSelfHeadPortraitImg, R.drawable.man);
                if (EmptyUtils.isNotEmpty(item.getImgPath())) {
                    mSelfChatContentTv.setVisibility(View.GONE);
                    mSelfChatImg.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.getInstance().show(mContext.get(), item.getImgPath(), mSelfChatImg, R.drawable.img_bg);
                    mSelfChatLayout.setTag(item.getImgPath());
                } else {
                    mSelfChatContentTv.setVisibility(View.VISIBLE);
                    mSelfChatImg.setVisibility(View.GONE);
                    if(item.getContent().contains("BOWEN")){
                        String[] temp = item.getContent().split("BOWEN");
                        mSelfChatContentTv.setVisibility(View.GONE);
                        mSelfChatImg.setVisibility(View.VISIBLE);
                        ImageLoaderUtil.getInstance().show(mContext.get(),temp[1], mSelfChatImg, R.drawable.img_bg);
                    }else{
                        mSelfChatContentTv.setText(item.getContent());
                    }
                }
                break;
            case TYPE_CHAT_RECEIVE:
                mChatTimeTv.setText(item.getDate());
                mOtherChatLayout.setVisibility(View.VISIBLE);
                mSelfChatLayout.setVisibility(View.GONE);
                ImageLoaderUtil.getInstance().show(mContext.get(), receiveUserPicUrl, mOtherHeadPortraitImg, R.drawable.man);
                if (EmptyUtils.isNotEmpty(item.getImgPath())) {
                    mOtherChatContentTv.setVisibility(View.GONE);
                    mOtherChatImg.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.getInstance().show(mContext.get(), item.getImgPath(), mOtherChatImg, R.drawable.img_bg);
                    mOtherChatLayout.setTag(item.getImgPath());
                } else {
                    mOtherChatContentTv.setVisibility(View.VISIBLE);
                    mOtherChatImg.setVisibility(View.GONE);
                    mOtherChatContentTv.setText(item.getContent());
                }
                break;
            case TYPE_CHAT_OVER:
                mOtherChatLayout.setVisibility(View.GONE);
                mSelfChatLayout.setVisibility(View.GONE);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if(EmptyUtils.isNotEmpty(mListener)){
                            mListener.onClick(widget);
                        }
                    }
                };
                mChatTimeTv.setText(SpannableStringUtils.getBuilder("本次服务已结束，可再次")
                        .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray))
                        .append("发起咨询")
                        .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main))
                        .setClickSpan(clickableSpan)
                        .create());
                mChatTimeTv.setMovementMethod(LinkMovementMethod.getInstance());
                break;
        }

    }

    public void setReceiveUserPicUrl(String receiveUserPicUrl) {
        this.receiveUserPicUrl = receiveUserPicUrl;
    }

    @OnClick({R.id.mOtherChatLayout, R.id.mSelfChatLayout})
    public void onViewClicked(View view) {
        String path = (String)view.getTag();
        if(EmptyUtils.isEmpty(path))
            return;
        List<PhotoFile> list = new ArrayList<>();
        PhotoFile photoFile = new PhotoFile();
        switch (view.getId()) {
            case R.id.mOtherChatLayout:
                photoFile.setFileLink(path);
                list.add(photoFile);
                ShowBigPicDialog showBigPicDialog1 = new ShowBigPicDialog(mContext.get(),null,1,list);
                showBigPicDialog1.show();
                break;
            case R.id.mSelfChatLayout:
                photoFile.setFileLink(path);
                list.add(photoFile);
                ShowBigPicDialog showBigPicDialog = new ShowBigPicDialog(mContext.get(),null,1,list);
                showBigPicDialog.show();
                break;
        }
    }
}
