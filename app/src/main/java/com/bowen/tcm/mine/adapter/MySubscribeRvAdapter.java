package com.bowen.tcm.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Doctor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的关注中rv的adapter
 * created by zhuzp at 2018/05/22
 */
public class MySubscribeRvAdapter extends BaseQuickAdapter<Doctor> {

    @BindView(R.id.mHeadPortraitImg)
    CircleImageView mHeadPortraitImg;
    @BindView(R.id.mySubscribeDoctorName)
    TextView mySubscribeDoctorName;
    @BindView(R.id.mySubscribeDepartment)
    TextView mySubscribeDepartment;
    @BindView(R.id.mySubscribeLevel)
    TextView mySubscribeLevel;
    @BindView(R.id.mySubscribeGoodAt)
    TextView mySubscribeGoodAt;
    @BindView(R.id.mySubscribeRateOfPraise)
    ProgressBar mySubscribeRateOfPraise;
    @BindView(R.id.mySubscribeRateOfReply)
    ProgressBar mySubscribeRateOfReply;
    @BindView(R.id.mDocotorAvatarLayout)
    RelativeLayout mDocotorAvatarLayout;
    @BindView(R.id.mySubscribeHospital)
    TextView mySubscribeHospital;
    @BindView(R.id.isRecommend)
    ImageView isRecommend;
    @BindView(R.id.rateOfPraiseText)
    TextView rateOfPraiseText;
    @BindView(R.id.rateOfReplyText)
    TextView rateOfReplyText;
    @BindView(R.id.mySubscribeCancel)
    TextView mySubscribeCancel;
    @BindView(R.id.mySubscribePresent)
    TextView mySubscribePresent;
    @BindView(R.id.mySubscribeConsult)
    TextView mySubscribeConsult;

    private final Context mContext;
    public MySubscribeCancelClick mSubscribeCancelClick;
    public MySubscribePresentClick mSubscribePresentClick;
    public MySubscribeConsultClick mSubscribeConsultClick;


    public MySubscribeRvAdapter(Context cxt) {
        super(cxt);
        mContext = cxt;
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_my_subscribe;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Doctor item, final int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext, item.getFileLink(), mHeadPortraitImg, R.drawable.man);
        mySubscribeDoctorName.setText(item.getName());
        mySubscribeDepartment.setText(item.getHospitalDepartments());
        mySubscribeLevel.setText(item.getPositionStr());
        mySubscribeHospital.setText(item.getHospital());

        mySubscribeGoodAt.setText("擅长治疗：" + item.getDiseases());

        int praiseRate = (int) (item.getReversionRate() * 100);
        mySubscribeRateOfPraise.setProgress(praiseRate);
        rateOfPraiseText.setText(praiseRate + "%");

        int replyRate = (int) (item.getReversionRate() * 100);
        mySubscribeRateOfReply.setProgress(replyRate);
        rateOfReplyText.setText(replyRate + "%");

        if (item.getRecommended() == 1) {//为1:为名医
            isRecommend.setVisibility(View.VISIBLE);
        } else {
            isRecommend.setVisibility(View.GONE);
        }

        mySubscribeCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSubscribeCancelClick != null) {
                    mSubscribeCancelClick.mySubscribeCancel(position);
                }
            }
        });
        mySubscribeConsult.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSubscribeConsultClick != null) {
                    mSubscribeConsultClick.mySubscribeConsult(position);
                }
            }
        });
    }

    public interface MySubscribeCancelClick {
        void mySubscribeCancel(int pos);
    }

    public interface MySubscribePresentClick {
        void mySubscribePresent(int pos);
    }

    public interface MySubscribeConsultClick {
        void mySubscribeConsult(int pos);
    }

    public void setOnSubscribeCancelListener(MySubscribeCancelClick listener) {
        mSubscribeCancelClick = listener;
    }

    public void setOnSubscribePresentListener(MySubscribePresentClick listener) {
        mSubscribePresentClick = listener;
    }

    public void setOnSubscribeConsultListener(MySubscribeConsultClick listener) {
        mSubscribeConsultClick = listener;
    }
}
