package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.model.PermissionsModel;
import com.bowen.commonlib.utils.BitmapUtil;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.Tips;
import com.bowen.tcm.common.util.ChooseTypeUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * 分享框（构造者模式）
 */
public class SmallTipsDialog extends BaseDialog {
    @BindView(R.id.mTipsBgImg)
    CircleImageView mTipsBgImg;
    @BindView(R.id.mTipsMonthTv)
    TextView mTipsMonthTv;
    @BindView(R.id.mTipsDayTv)
    TextView mTipsDayTv;
    @BindView(R.id.mTipsWeekTv)
    TextView mTipsWeekTv;
    @BindView(R.id.mTipsContentTv)
    TextView mTipsContentTv;
    @BindView(R.id.mTipsAuthorTv)
    TextView mTipsAuthorTv;
    @BindView(R.id.mTipsQRcodeImg)
    ImageView mTipsQRcodeImg;
    @BindView(R.id.mShareSmallTipsLayout)
    RelativeLayout mShareSmallTipsLayout;

    private Tips mTips;
    private Bitmap mShareBitmap;
    private String mSharePath;

    public SmallTipsDialog(Context context, Tips tips) {
        super(context, R.style.dialog_transparent_style);
        setTranslucentStatus();
        mTips = tips;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_small_tips);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().height = ScreenSizeUtil.getScreenHeight();
        getWindow().getAttributes().gravity = Gravity.CENTER;
        ButterKnife.bind(this);
        mTipsMonthTv.setText(ChooseTypeUtil.getMonthStr(DateUtil.getNowMonth()));
        mTipsDayTv.setText(DateUtil.getNowDay()+"");
        mTipsWeekTv.setText(ChooseTypeUtil.getWeekStr(DateUtil.getNowWeek()));
        mTipsContentTv.setText(mTips.getTips());
        mTipsAuthorTv.setText(mTips.getTipsWriter() + "(作)");
        ImageLoaderUtil.getInstance().show(mContext, mTips.getFileLink(), mTipsBgImg, R.drawable.small_tips_big_bg);
        ImageLoaderUtil.getInstance().show(mContext, mTips.getAppPicUrl(), mTipsQRcodeImg, R.drawable.img_bg);
    }

    @OnClick({R.id.mCloseImg, R.id.mShareBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCloseImg:
                dismiss();
                break;
            case R.id.mShareBtn:
                mShareSmallTipsLayout.setDrawingCacheEnabled(true);
                mShareBitmap = mShareSmallTipsLayout.getDrawingCache();
                mShareBitmap = mShareBitmap.createBitmap(mShareBitmap); // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
                mShareSmallTipsLayout.setDrawingCacheEnabled(false);
                PermissionsModel permissionsModel = new PermissionsModel(mContext);
                permissionsModel.checkWriteSDCardPermission(new PermissionsModel.PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if (isPermission) {
                            shareTips();
                        }
                    }
                });
                break;
        }

    }


    private void shareTips() {
        try {
            mSharePath = BitmapUtil.saveBitmap(mShareBitmap);
            LogUtil.androidLog("分享图片：" + mSharePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShareData shareData = new ShareData();
        shareData.setShareType(Platform.SHARE_IMAGE);
        shareData.setImgUrl(mSharePath);
        ShareDialog.getBuilder().shareData(shareData).build(mContext).show();
    }

}