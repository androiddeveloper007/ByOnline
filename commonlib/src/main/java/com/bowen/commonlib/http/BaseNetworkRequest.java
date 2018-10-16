package com.bowen.commonlib.http;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.dialog.LoadingDialog;
import com.bowen.commonlib.http.cache.DiskLruCacheHelper;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.FileUtil;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.MD5Util;
import com.bowen.commonlib.utils.NetworkRequestUtil;
import com.bowen.commonlib.utils.NetworkUtil;
import com.bowen.commonlib.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by AwenZeng on 2016/12/22.
 */

public class BaseNetworkRequest extends BaseHttp {
    public  SoftReference<Context> mContext;
    private Boolean isCache = false;
    private SoftReference<LoadingDialog> mLoadingDialog;
    private HttpRequestOverdueCallBack mOverdueCallBack;
    private DiskLruCacheHelper diskLruCacheHelper;

    public BaseNetworkRequest(Context context) {
        mContext = new SoftReference<>(context);
        mLoadingDialog = new SoftReference<>(new LoadingDialog(mContext.get()));
        try {
            diskLruCacheHelper = new DiskLruCacheHelper(CommonLibApplication.getCommonLibContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setmOverdueCallBack(HttpRequestOverdueCallBack mOverdueCallBack) {
        this.mOverdueCallBack = mOverdueCallBack;
    }

    @Override
    public void requestSRV(String api, HttpTaskCallBack mListener) {
        requestSRV(Method.POST, api, null, mListener);
    }

    public void requestSRV(String api, String loadingStr, HttpTaskCallBack mListener) {
        requestSRV(Method.POST, api, loadingStr, mListener);
    }

    @Override
    public void requestSRV(Method method, final String api, final String loadingStr, final HttpTaskCallBack mListener) {
        if (method == Method.POST) {
            reqParam.put("cmd", api);
            reqParam.put("sign", MD5Util.encode(requestParamSort()));
            FormBody.Builder builder = new FormBody.Builder();
            for (HashMap.Entry<String, String> entry : reqParam.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
            //无网络时获取缓存
            if (!NetworkUtil.isNetworkConnected()) {
                ToastUtil.getInstance().toast("网络异常，请检查后重试");
                if (isCache) {
                    String key = generateCacheKey();
                    String json = diskLruCacheHelper.getAsString(key);
                    if (json != null) {
                        HttpResult result = GsonUtil.fromJson(json, HttpResult.class);
                        mListener.onSuccess(result);
                    }
                }
                return;
            }
            LogUtil.json(GsonUtil.toJson(reqParam));
            RequestBody requestBody = builder.build();
            Subscription subscription = RetrofitHttp.getClient().requestPost(requestBody)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            if (!TextUtils.isEmpty(loadingStr)&&mLoadingDialog.get()!=null) {
                                mLoadingDialog.get().setContentStr(loadingStr);
                                mLoadingDialog.get().setRequestUrl(api);
                                mLoadingDialog.get().show();
                            }
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResult<Object>>() {
                        @Override
                        public void onCompleted() {
                            if(mLoadingDialog.get()!=null&&mLoadingDialog.get().isShowing()){
                                mLoadingDialog.get().dismiss();
                            }
                            NetworkRequestUtil.getInstance().remove(api);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(TAG, e.toString());
                            if(mLoadingDialog.get()!=null&&mLoadingDialog.get().isShowing()){
                                mLoadingDialog.get().dismiss();
                            }
                            NetworkRequestUtil.getInstance().remove(api);
                            if (mListener != null) {
                                HttpResult httpResult = HttpRequestException.handleException(e);
                                mListener.onFail(httpResult);
                                if (CommonLibApplication.DEBUG) {
                                    ToastUtil.getInstance().toast(httpResult.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onNext(HttpResult<Object> httpResult) {
                            LogUtil.json(GsonUtil.toJson(httpResult));
                            int code = httpResult.getCode();
                            if (code== HttpContants.HTTP_OK) {
                                if (isCache) {
                                    String cacheResponse = GsonUtil.toJson(httpResult);
                                    String key = generateCacheKey();
                                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(diskLruCacheHelper.getAsString(key))) {
                                        diskLruCacheHelper.remove(key);
                                    } else {
                                        diskLruCacheHelper.put(key, cacheResponse);
                                    }
                                }
                                try {
                                    if (mListener != null) {
                                        mListener.onSuccess(httpResult);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    LogUtil.e(TAG, e.toString());
                                }

                            } else if(code == HttpContants.HTTP_LOGIN_OVERDUE){
                                overdueCallBack();
                                if (mListener != null) {
                                    mListener.onFail(httpResult);
                                }
                            }else if(code == HttpContants.HTTP_LOGIN_INVALID){
                                ToastUtil.getInstance().toast(httpResult.getMsg());
                                if (mListener != null) {
                                    mListener.onFail(httpResult);
                                }
                            }else{
                                if (mListener != null) {
                                    mListener.onFail(httpResult);
                                }
                            }


                        }
                    });
            NetworkRequestUtil.getInstance().put(api, subscription);
        } else {
            reqParam.put("cmd", api);
            LogUtil.json(GsonUtil.toJson(reqParam));
            Subscription subscription = RetrofitHttp.getClient().requestGet(reqParam)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            if (!TextUtils.isEmpty(loadingStr)&&mLoadingDialog.get()!=null) {
                                mLoadingDialog.get().setContentStr(loadingStr);
                                mLoadingDialog.get().setRequestUrl(api);
                                mLoadingDialog.get().show();
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResult<Object>>() {
                        @Override
                        public void onCompleted() {
                            if(mLoadingDialog.get()!=null){
                                mLoadingDialog.get().dismiss();
                            }
                            NetworkRequestUtil.getInstance().remove(api);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(TAG, e.toString());
                            if(mLoadingDialog.get()!=null){
                                mLoadingDialog.get().dismiss();
                            }
                            NetworkRequestUtil.getInstance().remove(api);
                            if (mListener != null) {
                                HttpResult httpResult = HttpRequestException.handleException(e);
                                mListener.onFail(httpResult);
                                if (CommonLibApplication.DEBUG) {
                                    ToastUtil.getInstance().toast(httpResult.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onNext(HttpResult<Object> httpResult) {
                            LogUtil.json(GsonUtil.toJson(httpResult));
                            int code = httpResult.getCode();
                            if (code == HttpContants.HTTP_OK) {
                                ToastUtil.getInstance().toast(httpResult.getMsg());
                                if (mListener != null) {
                                    mListener.onSuccess(httpResult);
                                }
                            } else if(code == HttpContants.HTTP_LOGIN_OVERDUE) {
                                overdueCallBack();
                            } else {
                                if (mListener != null) {
                                    mListener.onFail(httpResult);
                                }
                            }
                        }
                    });
            NetworkRequestUtil.getInstance().put(api, subscription);
        }
    }

    @Override
    public void requestNewSRV(final String url, final HttpTaskCallBack mListener) {
        FormBody.Builder builder = new FormBody.Builder();
        for (HashMap.Entry<String, String> entry : reqParam.entrySet()) {
            builder.add(entry.getKey(), entry.getValue().toString());
        }
        LogUtil.d(TAG, builder.toString());
        LogUtil.json(GsonUtil.toJson(reqParam));
        Subscription subscription = RetrofitHttp.getUrlClient().requestGet(url, reqParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        if (mListener != null) {
                            HttpResult httpResult = HttpRequestException.handleException(e);
                            mListener.onFail(httpResult);
                            if (CommonLibApplication.DEBUG) {
                                ToastUtil.getInstance().toast(httpResult.getMsg());
                            }
                        }
                        if(mLoadingDialog.get()!=null){
                            mLoadingDialog.get().dismiss();
                        }
                        NetworkRequestUtil.getInstance().remove(url);
                    }

                    @Override
                    public void onNext(HttpResult<Object> httpResult) {
                        LogUtil.json(GsonUtil.toJson(httpResult));
                        int code = httpResult.getCode();
                        if (code == HttpContants.HTTP_OK) {
                            if (mListener != null) {
                                mListener.onSuccess(httpResult);
                            }

                        }else if(code == HttpContants.HTTP_LOGIN_OVERDUE) {
                            overdueCallBack();
                        } else {
                            ToastUtil.getInstance().toast(httpResult.getMsg());
                            if (mListener != null) {
                                mListener.onFail(httpResult);
                            }
                        }
                    }
                });
        NetworkRequestUtil.getInstance().put(url, subscription);
    }

    /**
     * 上传图片
     *
     * @param loadingStr
     * @param paths
     * @param mListener
     */
    public void uploadPhotos(String api, final String loadingStr, ArrayList<String> paths, final HttpTaskCallBack mListener) {
        List<MultipartBody.Part> imageList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(paths)) {
            for (int i = 0; i < paths.size(); i++) {
                LogUtil.d(TAG, paths.get(i) + "");
                if (EmptyUtils.isNotEmpty(paths.get(i))) {
                    File file = new File(paths.get(i));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file" + i, file.getName(), requestFile);
                    imageList.add(body);
                }
            }
        }
        final String url = HttpContants.REQUEST_URL + "api/tcm/app";
        reqParam.put("cmd", api);
        reqParam.put("sign", MD5Util.encode(requestParamSort()));
        String uploadUrl = appendParameter(url, reqParam);
        LogUtil.json(GsonUtil.toJson(reqParam));
        Subscription subscription = RetrofitHttp.getClient().uploadPhotos(uploadUrl, imageList)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!TextUtils.isEmpty(loadingStr)&&mLoadingDialog.get()!=null) {
                            mLoadingDialog.get().setContentStr(loadingStr);
                            mLoadingDialog.get().setRequestUrl(url);
                            mLoadingDialog.get().show();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<Object>>() {
                    @Override
                    public void onCompleted() {
                        if(mLoadingDialog.get()!=null){
                            mLoadingDialog.get().dismiss();
                        }
                        NetworkRequestUtil.getInstance().remove(url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        if (mListener != null) {
                            HttpResult httpResult = HttpRequestException.handleException(e);
                            mListener.onFail(httpResult);
                            if (CommonLibApplication.DEBUG) {
                                ToastUtil.getInstance().toast(httpResult.getMsg());
                            }
                        }
                        if(mLoadingDialog.get()!=null){
                            mLoadingDialog.get().dismiss();
                        }
                        NetworkRequestUtil.getInstance().remove(url);
                    }

                    @Override
                    public void onNext(HttpResult<Object> httpResult) {
                        LogUtil.json(GsonUtil.toJson(httpResult));
                        int code = httpResult.getCode();
                        if (code == HttpContants.HTTP_OK) {
                            if (mListener != null) {
                                mListener.onSuccess(httpResult);
                            }

                        } else if(code == HttpContants.HTTP_LOGIN_OVERDUE) {
                            overdueCallBack();
                        } else {
                            ToastUtil.getInstance().toast(httpResult.getMsg());
                            if (mListener != null) {
                                mListener.onFail(httpResult);
                            }
                        }
                    }
                });
        NetworkRequestUtil.getInstance().put(url, subscription);
    }


    /**
     * 下载文件
     *
     * @param url
     * @param loadingStr
     * @param mListener
     */
    public void downloadFile(final String url, final String loadingStr, final HttpTaskCallBack mListener) {
        LogUtil.show(url);
        Subscription subscription = RetrofitHttp.getUrlClient().downloadFileByUrl(url)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!TextUtils.isEmpty(loadingStr)&&mLoadingDialog.get()!=null) {
                            mLoadingDialog.get().setContentStr(loadingStr);
                            mLoadingDialog.get().setRequestUrl(url);
                            mLoadingDialog.get().show();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FileObserver(FileUtil.getSaveFileDir(CommonLibApplication.getCommonLibContext().getPackageName()), "download.apk") {
                    @Override
                    public void onSuccess(String filePath) {
                        LogUtil.show(filePath);
                        HttpResult httpResult = new HttpResult();
                        httpResult.setData(filePath);
                        if (mListener != null) {
                            mListener.onSuccess(httpResult);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        if(mLoadingDialog.get()!=null){
                            mLoadingDialog.get().dismiss();
                        }
                        NetworkRequestUtil.getInstance().remove(url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mListener != null) {
                            HttpResult httpResult = HttpRequestException.handleException(e);
                            mListener.onFail(httpResult);
                            if (CommonLibApplication.DEBUG) {
                                ToastUtil.getInstance().toast(httpResult.getMsg());
                            }
                        }
                        if(mLoadingDialog.get()!=null){
                            mLoadingDialog.get().dismiss();
                        }
                        NetworkRequestUtil.getInstance().remove(url);
                    }

                    @Override
                    public void progress(final long progress, final long total) {
                        ToastUtil.getInstance().toast("APK下载进度："+ ((float)progress/total)*100+"%");
                        LogUtil.androidLog("进度："+progress+"  总："+total+"    "+ (float)progress/total*100+"%");
                    }
                });
        NetworkRequestUtil.getInstance().put(url, subscription);
    }

    private void overdueCallBack(){
        if(mOverdueCallBack!=null){
            mOverdueCallBack.callback();
        }
    }


    private String requestParamSort(){
        Map<String,String> treeMap=new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return (lhs).compareTo(rhs);
            }
        });
        treeMap.putAll(reqParam);
        StringBuilder stringBuilder = new StringBuilder();
        for(String key:treeMap.keySet()){
            stringBuilder.append(key).append("=").append(treeMap.get(key)).append("#");
        }
        stringBuilder.append("signKey=boowtech.com");
        return stringBuilder.toString();
    }

    private String generateCacheKey(){
        StringBuilder builder = new StringBuilder();
        for (HashMap.Entry<String, String> entry : reqParam.entrySet()) {
            if(entry.getKey().equals("sign")||entry.getKey().equals("requestTime")){
                continue;
            }
            builder.append(entry.getKey()+entry.getValue().toString());
        }
        return builder.toString();
    }


    public Boolean getCache() {
        return isCache;
    }

    public void setCache(Boolean cache) {
        isCache = cache;
    }
}
