package com.bowen.commonlib.http;


import android.util.Log;


import com.bowen.commonlib.event.FileLoadEvent;
import com.bowen.commonlib.model.RxBus;
import com.bowen.commonlib.utils.FileUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by AwenZeng on 2017/2/23.
 */

public abstract class FileObserver implements Observer<ResponseBody> {

    private String destFileDir;
    private String destFileName;

    public FileObserver(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();
    }

    public abstract void onSuccess(String filePath);

    public abstract void progress(long progress, long total);

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(final ResponseBody responseBody) {
        Observable.just(responseBody)
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        File file = null;
                        try {
                            file = saveFile(responseBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return file;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file.getName().endsWith(".zip"))
                            unzipFile(file);
                        else
                            onSuccess(file.getPath());
                    }
                });
    }

    public File saveFile(ResponseBody responseBody) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            unsubscribe();
            return file;
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e("saveFile", e.getMessage());
            }
        }
    }

    private void subscribeLoadProgress() {
        Subscription subscription = RxBus.getInstance().doSubscribe(FileLoadEvent.class, new Action1<FileLoadEvent>() {
            @Override
            public void call(FileLoadEvent fileLoadEvent) {
                progress(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtil.e("", throwable.getMessage());
            }
        });
        RxBus.getInstance().addSubscription(this, subscription);
    }

    private void unsubscribe() {
        RxBus.getInstance().unSubscribe(this);
    }

    private void unzipFile(final File file) {
        try {
            ZipUtil.unzipFolder(file, destFileDir, new ZipUtil.IUnzipCallback() {
                @Override
                public void completed() {
                    onSuccess(file.getPath());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
