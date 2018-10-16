package com.bowen.tcm.inquiry.model;

import android.content.Context;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.FindClinicParam;
import com.bowen.tcm.common.bean.network.Clinic;
import com.bowen.tcm.common.bean.network.CliniclInfo;
import com.bowen.tcm.common.bean.network.PhotoFile;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class ClinicModel extends BaseModel {

    public ClinicModel(Context mContext) {
        super(mContext);
    }
    /**
     * 获取医馆列表相关数据
     * @param mListener
     */
    public void getAllClinicListOrMap(FindClinicParam param,int pageNo,int pageSize,final HttpTaskCallBack<CliniclInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("provinceCode", param.getProvinceCode());
        networkRequest.setParam("cityCode",param.getCityCode());
        networkRequest.setParam("areaCode", param.getAreaCode());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.setParam("longitude", param.getLongitude());
        networkRequest.setParam("latitude", param.getLatitude());
        networkRequest.requestSRV(HttpContants.CMD_GET_ALL_CLINIC_LISTMAP, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<CliniclInfo> result = new HttpResult<CliniclInfo>();
                result.copy(httpResult);
                CliniclInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), CliniclInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<CliniclInfo> result = new HttpResult<CliniclInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取医馆列表相关数据
     * @param mListener
     */
    public void getClinicMapData(double longitude,double latitude,String areaCode,final HttpTaskCallBack<CliniclInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("longitude", longitude);
        networkRequest.setParam("latitude", latitude);
        networkRequest.setParam("areaCode", areaCode);
        networkRequest.requestSRV(HttpContants.CMD_GET_CLINIC_MAP_DATA, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<CliniclInfo> result = new HttpResult<CliniclInfo>();
                result.copy(httpResult);
                CliniclInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), CliniclInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<CliniclInfo> result = new HttpResult<CliniclInfo>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 获取map医馆详情
     * @param mListener
     */
    public void getMapClinicDetail(String clinicId,final HttpTaskCallBack<Clinic> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("hospitalId", clinicId);
        networkRequest.requestSRV(HttpContants.CMD_GET_MAP_CLINIC_DETAIL, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Clinic> result = new HttpResult<Clinic>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Clinic clinic = GsonUtil.fromJson(jsonObject.get("mapHospital").toString(), Clinic.class);;
                    if (clinic != null) {
                        result.setData(clinic);
                    }
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Clinic> result = new HttpResult<Clinic>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 获取地图中选中的医馆
     * @param clinics
     * @param marker
     * @return
     */
    public Clinic getChooseClinic(ArrayList<Clinic> clinics, Marker marker){
        LatLng latLng = marker.getPosition();
        if(EmptyUtils.isNotEmpty(clinics)){
            for(Clinic clinic:clinics){
                if(latLng.longitude == clinic.getLongitudeValue()&&latLng.latitude == clinic.getLatitudeValue()){
                    return clinic;
                }
            }
        }
        return null;
    }

    /**
     * 转换医馆相册
     * @param photos
     * @return
     */
    public List<PhotoFile> transfromPhotos(ArrayList<String> photos){
        ArrayList<PhotoFile> photoFiles = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(photos)){
            for(String path:photos){
                PhotoFile photoFile = new PhotoFile();
                photoFile.setFileLink(path);
                photoFiles.add(photoFile);
            }
            return photoFiles;
        }
        return photoFiles;
    }

}
