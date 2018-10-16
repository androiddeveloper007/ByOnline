package com.bowen.tcm.remind.model;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.FileUtil;

import java.io.IOException;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/14.
 */

public class ClockAlarmModel extends BaseModel {

    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    public ClockAlarmModel(Context mContext) {
        super(mContext);
        mMediaPlayer = new MediaPlayer();
    }

    /**
     * 开始播放铃声
     */
    public void startPlaySound(String soundUri) {
        Uri alamUri = null;
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
        try {
            if (EmptyUtils.isEmpty(soundUri)||!soundUri.contains("content://media/internal/audio/media")) {//如果未自定义铃声，则调用系统默认的铃声
                alamUri = getSystemDefultRingtoneUri();
            } else {
                alamUri = Uri.parse(soundUri);
            }
            mMediaPlayer.setDataSource(mContext, alamUri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 开始播放铃声
//     */
//    public void startPlaySound(String soundUri){
//        if(mMediaPlayer != null){
//            mMediaPlayer.reset();
//        }
//        try {
//            if (EmptyUtils.isEmpty(soundUri)||!isMediaFile(Uri.parse(soundUri))) {//如果未自定义铃声，则调用系统默认的铃声
//                mMediaPlayer = MediaPlayer.create(mContext,  getSystemDefultRingtoneUri());
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//                mMediaPlayer.start();
//                mMediaPlayer.setLooping(true);
//            }else{
//                mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.setDataSource(mContext, Uri.parse(soundUri));
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//                mMediaPlayer.setLooping(true);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 检查Media文件是否存在
     *
     * @param uri
     * @return
     */
    public static boolean isMediaFile(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_ALARM);
    }

    public void stopPlaySound() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    /**
     * 开始震动
     * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
     */
    public void startVibrator() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(new long[]{100, 10, 100, 600}, 0);
    }


    public void stopVibrator() {
        if (mVibrator != null) {
            mVibrator.cancel();
        }
    }
}
