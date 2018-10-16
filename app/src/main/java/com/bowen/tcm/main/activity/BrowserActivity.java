package com.bowen.tcm.main.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.NetworkUtil;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.ShareData;
import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.NewsTop;
import com.bowen.tcm.common.dialog.ShareDialog;
import cn.sharesdk.framework.Platform;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内部浏览器
 * Created by AwenZeng on 2017/1/6.
 */

public class BrowserActivity extends BaseActivity {
    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;


    private final String TAG = BrowserActivity.class.getSimpleName();
    private String mUrl = "";
    private News mNews;
    private NewsTop mNewsTop;
    public static final String URL = "url";
    public static final String DATA = "data";
    private int fromTag = FROM_NOMAL;
    public static final int FROM_NOMAL = 0;//正常情况
    public static final int FROM_NEWS_DETAIL_TOP = 1;//推荐顶部栏
    public static final int FROM_NEWS_DETAIL = 2;   //新闻详情


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);
        setTitle("");
        init();
    }


    private void init() {
        Bundle bundle = RouterActivityUtil.getBundle(this);
        if (bundle != null) {
            fromTag = bundle.getInt(RouterActivityUtil.FROM_TAG, FROM_NOMAL);
            mUrl = bundle.getString(URL);
            if (fromTag == FROM_NEWS_DETAIL) {
                mNews = (News) bundle.getSerializable(DATA);
            }else if(fromTag == FROM_NEWS_DETAIL_TOP){
                mNewsTop = (NewsTop) bundle.getSerializable(DATA);
            }
        }
        if(fromTag == FROM_NEWS_DETAIL||fromTag == FROM_NEWS_DETAIL_TOP){
            getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
            getTitleBar().getRightTextButton().setText("分享");
        }


        WebSettings settings = mWebView.getSettings();
        if (NetworkUtil.isNetworkConnected()) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //防止跳转到浏览器
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        //恶意程序可以利用它们实现远程代码执行
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        mWebView.requestFocusFromTouch();
        mWebView.loadUrl(mUrl);
        mWebView.reload();
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        if (fromTag == FROM_NEWS_DETAIL) {
            if(EmptyUtils.isNotEmpty(mNews)){
                ShareData shareData = new ShareData();
                shareData.setShareType(Platform.SHARE_WEBPAGE);
                shareData.setContent(mNews.getShareContent());
                shareData.setLinkUrl(mNews.getShareUrl());
                shareData.setImgUrl(mNews.getFileLink());
                shareData.setTitile(mNews.getNewsTitle());
                ShareDialog.getBuilder().shareData(shareData).build(this).show();
            }
        }else if(fromTag == FROM_NEWS_DETAIL_TOP){
            if(EmptyUtils.isNotEmpty(mNewsTop)){
                ShareData shareData = new ShareData();
                shareData.setShareType(Platform.SHARE_WEBPAGE);
                shareData.setContent(mNewsTop.getShareContent());
                shareData.setLinkUrl(mNewsTop.getShareUrl());
                shareData.setImgUrl(mNewsTop.getFileLink());
                shareData.setTitile(mNewsTop.getNewsTitle());
                ShareDialog.getBuilder().shareData(shareData).build(this).show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearHistory();
        mWebView.removeAllViews();
        mWebView.destroy();
        super.onDestroy();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.d(TAG, "======== onLoadUrl   =======");
            LogUtil.d(TAG, "Url=" + url);
            switch (fromTag) {
                case FROM_NOMAL://正常
                    view.loadUrl(url);
                    break;

            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setProgress(0);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtil.d(TAG, "======== onPageFinished   =======");
            LogUtil.d(TAG, "finish url->" + (TextUtils.isEmpty(url) ? "" : url));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            LogUtil.d(TAG, "Website Title= " + title);
            setTitle(title);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

