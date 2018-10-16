package com.bowen.tcm.remind.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.SpannableStringUtils;
import com.bowen.commonlib.widget.SwitchButton;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.DrugVolume;
import com.bowen.tcm.common.bean.network.Alarm;
import com.bowen.tcm.common.util.ChooseTypeUtil;
import com.bowen.tcm.common.widget.SwipeMenuLayout;
import com.bowen.tcm.remind.presenter.AlarmRemindPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/27.
 */

public class AlarmRemindAdapter extends BaseQuickAdapter<Alarm> {

    @BindView(R.id.mTimeTv)
    TextView mTimeTv;
    @BindView(R.id.mAlarmSwitchBtn)
    SwitchButton mAlarmSwitchBtn;
    @BindView(R.id.mRepeateRateTipsTv)
    TextView mRepeateRateTipsTv;
    @BindView(R.id.mRemindWayTipsTv)
    TextView mRemindWayTipsTv;
    @BindView(R.id.mRemindPersonTv)
    TextView mRemindPersonTv;
    @BindView(R.id.mRemindMedicineDesTv)
    TextView mRemindMedicineDesTv;
    @BindView(R.id.contentLayout)
    RelativeLayout contentLayout;
    @BindView(R.id.mDeleteTv)
    TextView mDeleteTv;
    @BindView(R.id.mSwipeMenuLayout)
    SwipeMenuLayout mSwipeMenuLayout;

    public interface DeleteListener{
       void onDeleteListener(Alarm alarm);
    }

    private DeleteListener mDeleteListener;

    private AlarmRemindPresenter mPresenter;

    private OnRecyclerViewItemClickListener mItemOnClickListener;

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;


    public void setItemOnClickListener(OnRecyclerViewItemClickListener mItemOnClickListener) {
        this.mItemOnClickListener = mItemOnClickListener;
    }

    public void setmOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public void setmDeleteListen(DeleteListener mDeleteListener) {
        this.mDeleteListener = mDeleteListener;
    }

    public AlarmRemindAdapter(Context cxt,AlarmRemindPresenter presenter) {
        super(cxt);
        this.mPresenter = presenter;
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_alarm_remind;
    }


    @Override
    protected void convert(BaseViewHolder helper, final Alarm item, int position) {
        ButterKnife.bind(this, helper.convertView);
        mDeleteTv.setTag(item);
        mSwipeMenuLayout.setTag(position);
        mTimeTv.setText(DateUtil.date2String(item.getRemindTime(), DateUtil.DEFAULT_FORMAT_HOUR));
        mRepeateRateTipsTv.setText("从" + DateUtil.date2String(item.getRemindDate(), DateUtil.DEFAULT_FORMAT_DATE) + "起  "
                + ChooseTypeUtil.getRemindPeriodStr(Integer.parseInt(item.getDrugCycle())));
        boolean isHaveRing = EmptyUtils.isNotEmpty(item.getRingtone());
        if (isHaveRing && item.isIsShockBool()) {
            mRemindWayTipsTv.setText("铃声 + 震动");
        } else if (isHaveRing && !item.isIsShockBool()) {
            mRemindWayTipsTv.setText("铃声");
        } else if (!isHaveRing && item.isIsShockBool()) {
            mRemindWayTipsTv.setText("震动");
        } else {
            mRemindWayTipsTv.setText("无");
        }
        SpannableStringUtils.Builder builder = SpannableStringUtils.getBuilder("提醒");
        builder.setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray));
        String familyType = item.getRemindFamilyType();
        if(EmptyUtils.isNotEmpty(familyType)&&Integer.parseInt(familyType)== 0){//0:为本人
            builder.append("自己");
            builder.setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray));
        }else{
            builder.append(item.getRemindNickname());
            builder.setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_gray));
        }

        mRemindPersonTv.setText(builder.create());
        SpannableStringUtils.Builder builder1 = SpannableStringUtils.getBuilder("");
        for (DrugVolume drugVolume : item.getEmrMedicineList()) {
            builder1.append(drugVolume.getDrugName())
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_main_black))
                    .append(" " + drugVolume.getDosage()+"  ")
                    .setForegroundColor(mContext.get().getResources().getColor(R.color.color_red));
        }
        mRemindMedicineDesTv.setText(builder1.create());
        mAlarmSwitchBtn.setTag(item);
        mAlarmSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mOnCheckedChangeListener!=null){
                    mOnCheckedChangeListener.onCheckedChanged(buttonView,isChecked);
                }
            }
        });
        mAlarmSwitchBtn.setChecked(item.isStatusBool());
        mSwipeMenuLayout.smoothToCloseMenu();
    }

    @OnClick({R.id.mDeleteTv, R.id.mSwipeMenuLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mDeleteTv:
                if (mSwipeMenuLayout.isMenuOpen()) {
                    mSwipeMenuLayout.smoothToCloseMenu();
                }
                if(EmptyUtils.isNotEmpty(mDeleteListener)){
                    mDeleteListener.onDeleteListener((Alarm) view.getTag());
                }
                break;
            case R.id.mSwipeMenuLayout:
                if (mSwipeMenuLayout.isMenuOpen()) {
                    mSwipeMenuLayout.smoothToCloseMenu();
                }
                notifyDataSetChanged();
                if(mItemOnClickListener!=null){
                    mItemOnClickListener.onItemClick(view,(int)view.getTag());
                }
                break;
        }
    }
}
