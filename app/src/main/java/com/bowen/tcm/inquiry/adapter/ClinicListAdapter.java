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
import com.bowen.commonlib.model.PermissionsModel.PermissionListener;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.StringUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.util.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class ClinicListAdapter extends BaseQuickAdapter<Clinic> {

    @BindView(R.id.mClinicNameTv)
    TextView mClinicNameTv;
    @BindView(R.id.mDialImg)
    ImageView mDialImg;
    @BindView(R.id.mClinicAddressTv)
    TextView mClinicAddressTv;
    @BindView(R.id.mClinicDistanceTv)
    TextView mClinicDistanceTv;

    private PermissionsModel mPermissionsModel;

    public ClinicListAdapter(Context cxt) {
        super(cxt);
        mPermissionsModel = new PermissionsModel(mContext.get());
    }

    @Override
    protected int setItemLayout() {
        return R.layout.adapter_clinic_list;
    }

    @Override
    protected void convert(BaseViewHolder helper, Clinic item, int position) {
        ButterKnife.bind(this, helper.getConvertView());
        mClinicNameTv.setText(item.getHospitalName());
        mClinicAddressTv.setText(item.getAddressStr());
        mClinicDistanceTv.setText(String.format("距您%s", StringUtil.showDistance(item.getDistance())));
        if (EmptyUtils.isNotEmpty(item.getPhone())) {
            mDialImg.setVisibility(View.VISIBLE);
            mDialImg.setTag(item.getPhone());
        } else {
            mDialImg.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.mDialImg)
    public void onViewClicked(final View view) {
        final String phone = (String) view.getTag();
        AffirmDialog affirmDialog = new AffirmDialog(mContext.get(), "", "拨打电话 " + phone, "取消", "确定");
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
                            mContext.get().startActivity(intent);
                        }
                    }
                });
            }
        });
        affirmDialog.show();
    }
}
