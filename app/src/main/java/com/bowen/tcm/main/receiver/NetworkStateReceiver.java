package com.bowen.tcm.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

import com.bowen.commonlib.utils.LogUtil;
import com.bowen.tcm.common.event.NetworkConnectEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Describe:
 * Created by AwenZeng on 2018/8/27.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            getNetWorkState(context);
        } else {

            getNetWorkStateApi23(context);
        }
    }

    /**
     * api 23 之前使用此方法读取网络状态
     *
     * @param context context
     */
    @SuppressWarnings("AliDeprecation")
    private void getNetWorkState (Context context) {

        //获得ConnectivityManager对象
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connMgr == null) {
            return;
        }

        //获取ConnectivityManager对象对应的NetworkInfo对象
        //获取WIFI连接的信息
        NetworkInfo wifiNetworkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //获取移动数据连接的信息
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {//wifi和移动
            sendNetworkStateEvent(true);
        } else if(wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {//Wifi
            sendNetworkStateEvent(true);
        } else if(!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {//移动
            sendNetworkStateEvent(true);
        } else {
            sendNetworkStateEvent(false);
        }
    }

    /**
     * api 23 之后使用此方法读取网络状态
     *
     * @param context context
     */
    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    private void getNetWorkStateApi23 (Context context) {

        //API大于23时使用下面的方式进行网络监听

        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connMgr == null) {
            return;
        }
        //获取所有网络连接的信息
        Network[] networks = connMgr.getAllNetworks();
        boolean wifiConnect = false;
        boolean dataNetworkConnect = false;

        //通过循环将网络信息逐个取出来
        for(Network network : networks) {
            //获取ConnectivityManager对象对应的NetworkInfo对象
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            int type = networkInfo.getType();

            if(type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                wifiConnect = true;
            } else if(type == ConnectivityManager.TYPE_MOBILE &&
                    networkInfo.isConnected()) {
                dataNetworkConnect = true;
            }
        }

        if(wifiConnect) {
            if(dataNetworkConnect) {//wifi和移动
                sendNetworkStateEvent(true);
            } else {//wifi
                sendNetworkStateEvent(true);
            }
        } else {
            if(dataNetworkConnect) {//移动
                sendNetworkStateEvent(true);
            } else {//未连接
                sendNetworkStateEvent(false);
            }
        }
    }

    private void sendNetworkStateEvent(boolean isHaveNet){
        LogUtil.androidLog("网络连接状态："+isHaveNet);
        EventBus.getDefault().post(new NetworkConnectEvent(isHaveNet));
    }

}
