package com.bowen.tcm.inquiry.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.base.BaseViewHolder;
import com.bowen.commonlib.dialog.AffirmDialog;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.StringUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.Clinic;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 名医馆中rv的adapter
 * created by zhuzp at 2018/05/22
 */
public class FamousHospitalRvAdapter extends BaseQuickAdapter<Clinic> {
    private final Context mContext;
    @BindView(R.id.hospitalImg)
    ImageView hospitalImg;
    @BindView(R.id.recommendIcon)
    ImageView recommendIcon;
    @BindView(R.id.hospitalName)
    TextView hospitalName;
    @BindView(R.id.hospitalAddress)
    TextView hospitalAddress;
    @BindView(R.id.hospitalDistance)
    TextView hospitalDistance;
    @BindView(R.id.famousHospitalLogo)
    TextView famousHospitalLogo;
    public myItemClickListener mItemClickListener;
    private PermissionsModel mPermissionsModel;
    private String phone;

    public FamousHospitalRvAdapter(Context cxt) {
        super(cxt);
        mContext = cxt;
        mPermissionsModel = new PermissionsModel(mContext);
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_famous_hospital;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Clinic item, final int position) {
        ButterKnife.bind(this, helper.convertView);
        ImageLoaderUtil.getInstance().show(mContext, item.getHospitalImgUrl(), hospitalImg,R.drawable.img_bg);
        hospitalName.setText(item.getHospitalName());
        hospitalAddress.setText(item.getAddressStr());
        hospitalDistance.setText(item.getDistance()+"");
        hospitalDistance.setText("距您"+ StringUtil.showDistance(item.getDistance()));
        if(position==getData().size()){
            helper.convertView.findViewById(R.id.adapterFamousHospitalDivider).setVisibility(View.GONE);
        }
        if(TextUtils.equals("1", item.getRecommended())) {
            recommendIcon.setVisibility(View.VISIBLE);
            famousHospitalLogo.setVisibility(View.VISIBLE);
        }else{
            recommendIcon.setVisibility(View.GONE);
            famousHospitalLogo.setVisibility(View.GONE);
        }
        helper.convertView.findViewById(R.id.famousHospitalCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = item.getPhone();
                if(TextUtils.isEmpty(phone)){
                    return;
                }
                AffirmDialog affirmDialog = new AffirmDialog(mContext, "", "拨打电话 " + phone, "取消", "确定");
                affirmDialog.setAffirmDialogListenner(new AffirmDialog.AffirmDialogListenner() {
                    @Override
                    public void onCancle() {
                    }

                    @Override
                    public void onOK() {
                        mPermissionsModel.checkCallPhonePermission(new PermissionsModel.PermissionListener() {
                            @Override
                            public void onPermission(boolean isPermission) {
                                if (isPermission) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + phone);
                                    intent.setData(data);
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                    }
                });
                affirmDialog.show();
            }
        });
        helper.convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.onItemClick(position, (ImageView) helper.convertView.findViewById(R.id.hospitalImg));
                }
            }
        });
    }

    public interface myItemClickListener {
        void onItemClick(int position, ImageView imageView);
    }
    public void setOnMyItemClickListener(myItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
}
