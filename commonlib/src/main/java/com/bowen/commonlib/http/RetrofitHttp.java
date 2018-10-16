package com.bowen.commonlib.http;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.http.ssl.SslContextFactory;
import com.bowen.commonlib.http.ssl.SslX509TrustManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AwenZeng on 2016/12/14.
 */

public class RetrofitHttp {

    private static HttpApiInterface apiInterface;

    private static HttpApiInterface apiUrlInterface;

    public static HttpApiInterface getClient() {

        if (apiInterface == null) {
            synchronized (RetrofitHttp.class) {
                if (apiInterface == null) {
                    OkHttpClient client = getOkHttpClient();
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(HttpContants.REQUEST_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                    apiInterface = retrofit.create(HttpApiInterface.class);
                }
            }
        }
        return apiInterface;
    }


    public static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocketFactory();
        interceptor.setLevel(CommonLibApplication.DEBUG?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory,new SslX509TrustManager())
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        return client;
    }

    public static HttpApiInterface getUrlClient() {
        if (apiUrlInterface == null) {
            synchronized (RetrofitHttp.class) {
                if (apiUrlInterface == null) {
                    OkHttpClient client = getOkHttpUrlClient();
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(HttpContants.REQUEST_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                    apiUrlInterface = retrofit.create(HttpApiInterface.class);
                }
            }
        }
        return apiUrlInterface;
    }

    public static OkHttpClient getOkHttpUrlClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocketFactory();
        interceptor.setLevel(CommonLibApplication.DEBUG?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory,new SslX509TrustManager())
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response orginalResponse = chain.proceed(chain.request());
                        return orginalResponse.newBuilder()
                                .body(new ProgressResponseBody(orginalResponse.body()))
                                .build();
                    }
                })
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        return client;
    }


}
