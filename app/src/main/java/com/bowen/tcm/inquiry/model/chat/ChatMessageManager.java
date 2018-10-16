package com.bowen.tcm.inquiry.model.chat;

import com.bowen.commonlib.utils.BitmapUtil;
import com.bowen.commonlib.utils.DateUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.FileUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.tcm.BowenApplication;
import com.bowen.tcm.common.database.entity.ChatMessage;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.Base64Utils;
import com.bowen.tcm.inquiry.adapter.ChatAdapter;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Describe:及时通信发送与接收
 * Created by AwenZeng on 2018/7/19.
 */
public class ChatMessageManager {
    private static ChatMessageManager mChatMessageManager = null;

    private ChatManager mChatManager;

    private final static String Folder = "BoWen";

    private final static String ALBUM_PATH = FileUtil.getAppFileSaveRootDri() + File.separator + Folder + File.separator;

    public interface ChatMessageListener{
        void onRecieveMessage(ChatMessage msg);
    }



    public static ChatMessageManager getInstance() {
        synchronized (ChatMessageManager.class) {
            if (mChatMessageManager == null) {
                mChatMessageManager = new ChatMessageManager();
            }
        }
        return mChatMessageManager;
    }

    public ChatMessageManager() {
        if (ChatServerManager.getConnection().isConnected()) {
            mChatManager = ChatServerManager.getConnection().getChatManager();
        }
    }

    public void addChatListener(final ChatMessageListener listener) {
        if (EmptyUtils.isEmpty(mChatManager)||EmptyUtils.isNotEmpty(mChatManager.getChatListeners())) {
            return;
        }
        mChatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean able) {
                chat.addMessageListener(new MessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        if (message.getBody().length() > 0){
                            String[] from = message.getFrom().split("@", 2);
                            String[] temp = message.getBody().split("&", 3);
                            ChatMessage msg = new ChatMessage();
                            msg.setType(ChatAdapter.TYPE_CHAT_RECEIVE);
                            msg.setUserId(from[0] + UserInfo.getInstance().getUserId());
                            msg.setDate(temp[0]);
                            if(EmptyUtils.isNotEmpty(message.getSubject())&&message.getSubject().equals("picture")){
                                msg.setContent("图片信息");
                                msg.setImgPath(Base64Utils.Base64ToImage(temp[2], FileUtil.getSavePicPath("Bowen").getPath()));
                            }else{
                                if(temp[2].contains(Constants.CHAT_OVER_TAG)){
                                    msg.setType(ChatAdapter.TYPE_CHAT_OVER);
                                }
                                msg.setContent(temp[2]);
                            }
                            if (EmptyUtils.isNotEmpty(listener)) {
                                listener.onRecieveMessage(msg);
                            }
                        }
                    }
                });
            }
        });
    }

    public void addFileListener(final ChatMessageListener listener) {
        FileTransferManager manager = ChatServerManager.getFileTransferManager();
        manager.addFileTransferListener(new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest request) {
                Observable.just(request).map(new Func1<FileTransferRequest, ChatMessage>() {
                    @Override
                    public ChatMessage call(FileTransferRequest fileTransferRequest) {
                        IncomingFileTransfer transfer = fileTransferRequest.accept();
                        String fileName = transfer.getFileName();
                        File sdCardDir = new File(ALBUM_PATH);
                        if (!sdCardDir.exists()) {
                            sdCardDir.mkdir();
                        }
                        String save_path = ALBUM_PATH + fileName;
                        File file = new File(save_path);
                        try {
                            transfer.recieveFile(file); //接收文件
                            while (!transfer.isDone()) {
                                if (transfer.getStatus().equals(FileTransfer.Status.error)) {
                                    LogUtil.show("ERROR!!! " + transfer.getError());
                                } else {
                                    LogUtil.show(transfer.getStatus().toString());
                                    LogUtil.show(transfer.getProgress() + "");
                                }
                            }
                            if (transfer.isDone()) {
                                String[] temp = fileTransferRequest.getRequestor().split("@", 2);
                                ChatMessage msg = new ChatMessage();
                                msg.setType(ChatAdapter.TYPE_CHAT_RECEIVE);
                                msg.setUserId(temp[0] + UserInfo.getInstance().getUserId());
                                msg.setDate(DateUtil.date2String(System.currentTimeMillis(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND));
                                msg.setImgPath(save_path);
                                return msg;
                            }
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ChatMessage>() {
                            @Override
                            public void call(ChatMessage message) {
                                if (EmptyUtils.isNotEmpty(listener)) {
                                    listener.onRecieveMessage(message);
                                }
                            }
                        });
            }
        });
    }

    public void sendMessage(final Chat chat,final String toUserId,final String orderNo,String content,final ChatMessageListener listener) {
        Observable.just(content).map(new Func1<String, ChatMessage>() {
            @Override
            public ChatMessage call(String content) {
                try {
                    //发送消息
                    Message msg = new Message();
                    String time = DateUtil.date2String(System.currentTimeMillis(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND);
                    msg.setBody(time+"&"+orderNo+"&"+content);
                    chat.sendMessage(msg);
                    //显示聊天消息
                    ChatMessage message = new ChatMessage();
                    message.setUserId(toUserId + UserInfo.getInstance().getUserId());
                    message.setType(ChatAdapter.TYPE_CHAT_SEND);
                    message.setDate(time);
                    if(content.contains(Constants.CHAT_OVER_TAG)){
                        message.setType(ChatAdapter.TYPE_CHAT_OVER);
                    }
                    message.setContent(content);
                    return message;
                } catch (XMPPException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChatMessage>() {
                    @Override
                    public void call(ChatMessage msg) {
                        if (EmptyUtils.isNotEmpty(listener)) {
                            listener.onRecieveMessage(msg);
                        }
                    }
                });
    }

    public void sendImgMessage(final String toUserId,String imgPath,final ChatMessageListener listener) {
        Observable.just(imgPath).map(new Func1<String, String>() {
            @Override
            public String call(String path) {

                String imgPath = path;
                String toId = toUserId + "@"+ChatServerManager.SERVER_HOST+"/Smack";

                FileTransferManager fileTransferManager = ChatServerManager.getFileTransferManager();
                File filetosend = new File(imgPath);
                if (filetosend.exists() == false) {
                    return "";
                }
                OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(toId);// 创建一个输出文件传输对象
                try {
                    transfer.sendFile(filetosend, "recv img");
                    while (!transfer.isDone()) {
                        if (transfer.getStatus().equals(FileTransfer.Status.error)) {
                            LogUtil.show("ERROR!!! " + transfer.getError());
                        } else {
                            LogUtil.show(transfer.getStatus().toString());
                            LogUtil.show(transfer.getProgress() + "");
                        }
                    }
                    if (transfer.isDone()) {
                        return imgPath;
                    }
                } catch (XMPPException e1) {
                    e1.printStackTrace();
                    return "";
                }
                return "";
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        ChatMessage msg = new ChatMessage();
                        msg.setUserId(toUserId + UserInfo.getInstance().getUserId());
                        msg.setType(ChatAdapter.TYPE_CHAT_SEND);
                        msg.setDate(DateUtil.date2String(System.currentTimeMillis(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND));
                        msg.setImgPath(path);
                        if (EmptyUtils.isNotEmpty(listener)) {
                            listener.onRecieveMessage(msg);
                        }
                    }
                });
    }

    public void sendImgMessage(final Chat chat, final String toUserId, final String orderNo, final String imgPath, final ChatMessageListener listener) {
        Observable.just(imgPath).map(new Func1<String, ChatMessage>() {
            @Override
            public ChatMessage call(String imgPath) {
                try {
                    //发送消息
                    Message msg = new Message();
                    String time = DateUtil.date2String(System.currentTimeMillis(), DateUtil.DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND);
                    if(imgPath.contains("http")){
                        msg.setBody(time+"&"+orderNo+"&"+Base64Utils.ImageToBase64ByOnline(imgPath));
                    }else{
                        String tempPath = FileUtil.getSavePicPath("compress").getPath();
                        msg.setBody(time+"&"+orderNo+"&"+Base64Utils.ImageToBase64ByLocal(compressImage(imgPath,tempPath)));
                    }
                    msg.setSubject("picture");
                    chat.sendMessage(msg);
                    //显示聊天消息
                    ChatMessage message = new ChatMessage();
                    message.setUserId(toUserId + UserInfo.getInstance().getUserId());
                    message.setType(ChatAdapter.TYPE_CHAT_SEND);
                    message.setDate(time);
                    message.setImgPath(imgPath);
                    return message;
                } catch (XMPPException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChatMessage>() {
                    @Override
                    public void call(ChatMessage msg) {
                        if (EmptyUtils.isNotEmpty(listener)) {
                            listener.onRecieveMessage(msg);
                        }
                    }
                });
    }

    private String compressImage(String fromPath, String outPath) {
        if (FileUtil.getFileSize(new File(fromPath)) > 1 * 1024 * 1024) {//大于1M就进行压缩
            try {
                BitmapUtil.compressByResolution(fromPath, outPath, Constants.DEFAULT_PHOTO_WIDTH, Constants.DEFAULT_PHOTO_HEIGHT, false);
                if (BowenApplication.DEBUG) {
                    LogUtil.show("图片压缩大小：" + FileUtil.getFileSize(new File(outPath)) / 1024 + "k");
                }
                outPath = compressImage(outPath, outPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            outPath = fromPath;
        }
        return outPath;
    }
}
