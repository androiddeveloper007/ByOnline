package com.bowen.tcm.inquiry.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.EmrDoctorListBean;
import com.bowen.tcm.common.widget.GlideCircleTransform;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 名医馆详情中的医生列表rv的adapter
 * created by zhuzp at 2018/05/22
 */
public class HospitalDetailDoctorRvAdapter extends BaseQuickAdapter<Doctor> {

    private final Context mContext;
    @BindView(R.id.doctorImg)
    CircleImageView doctorImg;
    @BindView(R.id.doctorName)
    TextView doctorName;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.doctorLevel)
    TextView doctorLevel;
    @BindView(R.id.doctorGoodAt)
    TextView doctorGoodAt;
    @BindView(R.id.hospitalDetailConsult)
    TextView hospitalDetailConsult;
    @BindView(R.id.divider_folk_prescription)
    View dividerFolkPrescription;
    public MySubscribeConsultClick mSubscribeConsultClick;

    public HospitalDetailDoctorRvAdapter(Context cxt) {
        super(cxt);
        mContext = cxt;
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_hospital_detail_doctor;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Doctor item, final int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext,item.getHeadImgUrl(),doctorImg,R.drawable.man,true);
        doctorName.setText(item.getName());
        department.setText(item.getHospitalDepartments());
        doctorLevel.setText(item.getPositionStr());
        doctorGoodAt.setText("擅长治疗："+item.getDiseases());
        hospitalDetailConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSubscribeConsultClick!=null){
                    mSubscribeConsultClick.mySubscribeConsult(position);
                }
            }
        });
        if(position==getData().size()){
            dividerFolkPrescription.setVisibility(View.GONE);
        }
    }

    public interface MySubscribeConsultClick{
        void mySubscribeConsult(int pos);
    }
    public void setOnSubscribeConsultListener (MySubscribeConsultClick listener) {
        mSubscribeConsultClick = listener;
    }
}
