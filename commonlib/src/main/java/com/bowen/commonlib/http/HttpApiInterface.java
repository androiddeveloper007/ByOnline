package com.bowen.commonlib.http;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by AwenZeng on 2016/12/14.
 */

public interface HttpApiInterface {


    @GET("api/tcm/app")
     Observable<HttpResult<Object>> requestGet(@QueryMap HashMap<String, String> map);//传入的参数为RequestBody

    @POST("api/tcm/app")
    Observable<HttpResult<Object>> requestPost(@Body RequestBody body);//传入的参数为RequestBody

    /**
     * 改变URL地址Get
     */
    @GET()
    Observable<HttpResult<Object>> requestGet(@Url String url, @QueryMap HashMap<String, String> map);//传入的参数为RequestBody

    /**
     * 改变URL地址POST
     */
    @POST()
    Observable<HttpResult<Object>> requestPost(@Url String url, @Body RequestBody body);//传入的参数为RequestBody
    /**
     * 多个文件上传
     */
    @Multipart
    @POST()
    Observable<HttpResult<Object>> uploadPhotos(@Url String url, @Part List<MultipartBody.Part> files);
    /**
     * 单个文件上传
     */
    @Multipart
    @POST()
    Observable<HttpResult<Object>> uploadFile(@Url String url, @Part MultipartBody.Part file);
    /**
     * 下载文件
     * @param fileName
     * @return
     */
    @Streaming
    @GET("{fileName}")
    Observable<ResponseBody> downloadFile(@Path("fileName") String fileName);

    /**
     * 下载文件
     */
    @Streaming
    @GET()
    Observable<ResponseBody> downloadFileByUrl(@Url String url);
}
