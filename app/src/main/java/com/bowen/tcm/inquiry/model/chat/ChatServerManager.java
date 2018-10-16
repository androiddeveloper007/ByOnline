package com.bowen.tcm.inquiry.model.chat;

import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.NetworkUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.BowenApplication;
import com.bowen.tcm.common.bean.network.MedicalRecord;
import com.bowen.tcm.common.event.ChatServerLoginSuccessEvent;
import com.bowen.tcm.common.event.LoginOutEvent;
import com.bowen.tcm.common.event.NetworkConnectEvent;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 聊天服务器连接
 */
public class ChatServerManager {

    public static int SERVER_PORT = 5222;// 端口

    public static String SERVER_HOST = HttpContants.CHAT_SERVER_URL;// 服务器地址

    public static String SERVER_NAME = HttpContants.CHAT_SERVER_URL;// 域

    private static XMPPConnection connection;

    private static FileTransferManager fileManager;

    private static MultiUserChat multiUserChat;

    private static int connectCount = 0;
    private static int CONNECT_COUNT_MAX = 5;

    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAILED  = 2;


    public interface  ChatServerLoginListener{
        void backLoginSucessStatus(boolean isSuccess);
    }

    /**
     * 打开连接
     */
    private static void openConnection() {
        try {
            if (null == connection || !connection.isAuthenticated()) {
                XMPPConnection.DEBUG_ENABLED = BowenApplication.DEBUG;
                //配置文件  参数（服务地地址，端口号，域）
                ConnectionConfiguration conConfig = new ConnectionConfiguration(SERVER_HOST, SERVER_PORT, SERVER_NAME);
                //设置断网重连 默认为true
                conConfig.setReconnectionAllowed(true);
                //设置登录状态 true-为在线
                conConfig.setSendPresence(true);
                //设置不需要SAS验证
                conConfig.setSASLAuthenticationEnabled(true);
                //开启连接
                connection = new XMPPConnection(conConfig);
                connection.connect();
                connection.addConnectionListener(new ConnectionListener() {
                    @Override
                    public void connectionClosed() {
                        LogUtil.androidLog("聊天服务器已关闭");
                        if(BowenApplication.DEBUG){
                            ToastUtil.getInstance().toast("聊天服务器已关闭");
                        }
                    }

                    @Override
                    public void connectionClosedOnError(Exception e) {
                        LogUtil.androidLog("聊天服务器连接关闭异常");
                        startReconnectLogin();
                    }

                    @Override
                    public void reconnectingIn(int i) {
                        LogUtil.androidLog("聊天服务器重连"+i);
                        if(BowenApplication.DEBUG){
                            ToastUtil.getInstance().toast("聊天服务器重连"+i);
                        }
                    }

                    @Override
                    public void reconnectionSuccessful() {
                        LogUtil.androidLog("聊天服务器连接成功");
                        connectCount = 0;
                    }

                    @Override
                    public void reconnectionFailed(Exception e){
                        LogUtil.androidLog("聊天服务器连接失败");
                        if(BowenApplication.DEBUG){
                            ToastUtil.getInstance().toast("聊天服务器链接失败");
                        }
                        startReconnectLogin();
                    }
                });
                //添加额外配置信息
                configureConnection();
            }
        } catch (XMPPException e) {

        }
    }

    public static void startReconnectLogin(){

        if(connectCount < CONNECT_COUNT_MAX){
            if (NetworkUtil.isNetworkConnected()) {
                closeConnection();
                ChatServerManager.login(null);
                connectCount ++;
                LogUtil.androidLog("开始重连聊天服务器"+connectCount);
            }else{
                ToastUtil.getInstance().toast("网络异常，请检查后重试");
            }
        }
    }


    /**
     * 获取连接
     *
     * @return
     */
    public static XMPPConnection getConnection() {
        if (connection == null) {
            try{
                openConnection();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 登录聊天Sever
     */
    public static void login(final ChatServerLoginListener mListener){
        rx.Observable.just("").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                try {
                    LogUtil.androidLog("聊天服务器登录中...");
                    getConnection().login(UserInfo.getInstance().getUserId(), "000000");
                    Presence presence = new Presence(Presence.Type.available);// 连接服务器成功，更改在线状态
                    getConnection().sendPacket(presence);
                    return LOGIN_SUCCESS;
                } catch (Exception e) {
                    closeConnection();
                    return LOGIN_FAILED;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer code) {
                        if (code == LOGIN_SUCCESS) {
                            if(EmptyUtils.isNotEmpty(mListener)){
                                mListener.backLoginSucessStatus(true);
                            }
                            LogUtil.androidLog("聊天服务器登录成功");
                            UserInfo.getInstance().setChatServerLoginSuccess(true);
                            EventBus.getDefault().post(new ChatServerLoginSuccessEvent());
                            connectCount = 0;
                            if(BowenApplication.DEBUG){
                                ToastUtil.getInstance().toast("聊天服务器登录成功");
                            }

                        } else if (code == LOGIN_FAILED) {
                            if(EmptyUtils.isNotEmpty(mListener)){
                                mListener.backLoginSucessStatus(false);
                            }
                            LogUtil.androidLog("聊天服务器登录失败");
                            UserInfo.getInstance().setChatServerLoginSuccess(false);
                            startReconnectLogin();
                            if(BowenApplication.DEBUG){
                                ToastUtil.getInstance().toast("聊天服务器登录失败！");
                            }
                        }
                    }
                });
    }

    /**
     * 文件监听
     */
    public static FileTransferManager getFileTransferManager() {
        if (fileManager == null) {
            ServiceDiscoveryManager sdManager = ServiceDiscoveryManager
                    .getInstanceFor(connection);
            if (sdManager == null) {
                sdManager = new ServiceDiscoveryManager(connection);
            }
            sdManager.addFeature("http://jabber.org/protocol/disco#info");
            sdManager.addFeature("jabber:iq:privacy");
            FileTransferNegotiator.setServiceEnabled(connection, true);
            fileManager = new FileTransferManager(connection);
        }
        return fileManager;
    }

    /**
     * 加入会议室
     *
     * @param user      昵称
     * @param password  会议室密码
     * @param roomsName 会议室名
     */
    public static void joinMultiUserChat(String user, String password, String roomsName) {
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = new MultiUserChat(connection, roomsName
                    + "@conference." + connection.getServiceName());
            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);
            muc.join(user, password, history, SmackConfiguration.getPacketReplyTimeout());
            multiUserChat = muc;
            ToastUtil.getInstance().toast("会议室加入成功........");
        } catch (XMPPException e) {
            e.printStackTrace();
            ToastUtil.getInstance().toast("会议室加入失败........");
            multiUserChat = null;
        }
    }

    /**
     * @param user     用户名
     * @param roomName 房间名
     * @param password 密码
     * @return
     */
    public static boolean createRoom(String user, String roomName,
                                     String password) {
        if (getConnection() == null)
            return false;

        MultiUserChat muc = null;
        try {
            // 创建一个MultiUserChat
            muc = new MultiUserChat(getConnection(), roomName + "@conference."
                    + getConnection().getServiceName());
            // 创建聊天室
            muc.create(roomName);
            // 获得聊天室的配置表单
            Form form = muc.getConfigurationForm();
            // 根据原始表单创建一个要提交的新表单。
            Form submitForm = form.createAnswerForm();
            // 向要提交的表单添加默认答复
            for (Iterator<FormField> fields = form.getFields(); fields
                    .hasNext(); ) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType())
                        && field.getVariable() != null) {
                    // 设置默认值作为答复
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            // 设置聊天室的新拥有者
            List<String> owners = new ArrayList<>();
            owners.add(getConnection().getUser());// 用户JID
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            // 房间仅对成员开放
            submitForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            if (!password.equals("")) {
                // 进入是否需要密码
                submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
                        true);
                // 设置进入密码
                submitForm.setAnswer("muc#roomconfig_roomsecret", password);
            }
            // 能够发现占有者真实 JID 的角色
            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            submitForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            muc.sendConfigurationForm(submitForm);
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static MultiUserChat getMultiUserChat() {
        return multiUserChat;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }


    /**
     * ASmack在/META-INF缺少一个smack.providers 文件，配置文件
     * 不然会出现 空指针异常或者是ClassCastExceptions
     */
    private static void configureConnection() {
        ProviderManager pm = ProviderManager.getInstance();
        pm.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());
        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",
                new RosterExchangeProvider());
        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event",
                new MessageEventProvider());
        // Chat State
        pm.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());
        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());
        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());
        pm.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
                new BytestreamsProvider());
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.SessionExpiredError());
    }

}
