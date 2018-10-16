package com.bowen.tcm.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bowen.commonlib.utils.ApplicationUtils;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.tcm.BowenApplication;
import com.bowen.tcm.common.bean.ShareData;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ShareUtil {
    private static ShareUtil mShareUtil = null;
    private ShareUtil(){}
    public static ShareUtil getInstance(){
          synchronized (ShareUtil.class){
             if(mShareUtil == null){
                mShareUtil = new ShareUtil();
             }
          }
          return mShareUtil;
    }

    public void shareWeiBo(ShareData shareData){
        if(EmptyUtils.isEmpty(shareData)){
            return;
        }
        Platform.ShareParams sp = new Platform.ShareParams();
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        sp.setText(shareData.getContent()+shareData.getLinkUrl());
        sp.setImageUrl(shareData.getImgUrl());
        if(shareData.getShareType() == Platform.SHARE_IMAGE){
            sp.setImagePath(shareData.getImgUrl());
        }
        platform.share(sp);
    }

    public void shareQQ(ShareData shareData){
        if(EmptyUtils.isEmpty(shareData)){
            return;
        }
        Platform.ShareParams sp = new Platform.ShareParams();
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        switch (shareData.getShareType()){
            case Platform.SHARE_IMAGE:
                sp.setImagePath(shareData.getImgUrl());
                break;
            case Platform.SHARE_TEXT:
                sp.setText(shareData.getContent());
                break;
            case Platform.SHARE_WEBPAGE:
                sp.setTitle(shareData.getTitile());
                sp.setText(shareData.getContent());
                sp.setTitleUrl(shareData.getLinkUrl());
                if(!ApplicationUtils.isHavaApp(BowenApplication.getBoWenAppContext(),"com.tencent.mobileqq")){
                    sp.setImageUrl(shareData.getImgUrl());
                }
                break;
        }
        sp.setShareType(shareData.getShareType());
        platform.share(sp);
    }


    public void shareWechat(ShareData shareData){
        if(EmptyUtils.isEmpty(shareData)){
            return;
        }
        Platform.ShareParams sp = new Platform.ShareParams();
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        sp.setTitle(shareData.getTitile());
        sp.setUrl(shareData.getLinkUrl());
        sp.setText(shareData.getContent());
        if(shareData.getShareType() == Platform.SHARE_IMAGE){
            sp.setImagePath(shareData.getImgUrl());
        }
        sp.setImageUrl(shareData.getImgUrl());
        sp.setShareType(shareData.getShareType());
        platform.share(sp);
    }

    public void shareWechatMoments(ShareData shareData){
        if(EmptyUtils.isEmpty(shareData)){
            return;
        }
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        Platform.ShareParams sp = new  Platform.ShareParams();
        sp.setTitle(shareData.getTitile());
        sp.setUrl(shareData.getLinkUrl());
        sp.setText(shareData.getContent());
        if(shareData.getShareType() == Platform.SHARE_IMAGE){
            sp.setImagePath(shareData.getImgUrl());
        }
        sp.setImageUrl(shareData.getImgUrl());
        sp.setShareType(shareData.getShareType());
        platform.share(sp);
    }

    public void dialPhone(Context context,ShareData shareData){
        if(EmptyUtils.isEmpty(shareData)){
            return;
        }
        Intent intent = new Intent();

        if(EmptyUtils.isNotEmpty(shareData.getPhoneNum())){
            intent.setAction(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + shareData.getPhoneNum());
            intent.setData(data);
        }else{
            intent.setAction(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + shareData.getPhoneNum());
            intent.setData(data);
        }
        context.startActivity(intent);

    }

    public void sendMessage(Context context,ShareData shareData){
        if(EmptyUtils.isEmpty(shareData)){
            return;
        }
        if(EmptyUtils.isNotEmpty(shareData.getPhoneNum())){
            //7.发送短信
            Uri uriMessage = Uri.parse("smsto:"+shareData.getPhoneNum());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uriMessage);
            intent.putExtra("sms_body", shareData.getContent());
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("sms_body", shareData.getContent());
            intent.setType("vnd.android-dir/mms-sms");
            context.startActivity(intent);
        }
    }



}
