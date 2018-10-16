package com.bowen.tcm.common.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.PopupWindow;

import com.bowen.commonlib.base.BasePopWindow;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.location.City;
import com.bowen.tcm.common.bean.location.County;
import com.bowen.tcm.common.bean.location.Province;
import com.bowen.tcm.common.dialog.adapter.CityAdapter;
import com.bowen.tcm.common.dialog.adapter.CountyAdapter;
import com.bowen.tcm.common.dialog.adapter.ProvinceAdapter;
import com.bowen.tcm.common.util.CityChooseUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:选择地址
 * Created by AwenZeng on 2018/7/2.
 */
public class ChooseAddressPopWindow extends BasePopWindow {

    @BindView(R.id.mProvinceRecyclerView)
    RecyclerView mProvinceRecyclerView;
    @BindView(R.id.mCityRecyclerView)
    RecyclerView mCityRecyclerView;
    @BindView(R.id.mCountyRecyclerView)
    RecyclerView mCountyRecyclerView;

    private ProvinceAdapter mProvinceAdapter;
    private CityAdapter mCityAdapter;
    private CountyAdapter mCountyAdapter;
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;

    private String mProviceCode;
    private String mCityCode;
    private String mCountyCode;
    private String mProvinceName;
    private String mCityName;
    private String mCountyName;

    private int provincePos;

    public ChooseAddressPopWindow(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mView = mInflater.inflate(R.layout.pop_window_choose_address, null);
        ButterKnife.bind(this, mView);
        mPopWindow = new PopupWindow(mView, ScreenSizeUtil.dp2px(360f), ScreenSizeUtil.dp2px(640f));
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);

        mProvinceAdapter = new ProvinceAdapter(mContext);
        mCityAdapter = new CityAdapter(mContext);
        mCountyAdapter = new CountyAdapter(mContext);

        mProvinceList = CityChooseUtil.getInstance().getProvincesData();
        mCityList = CityChooseUtil.getInstance().getCitiesData(0);
        mCountyList = CityChooseUtil.getInstance().getCountyData(0,0);

        LinearLayoutManager provinceLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        final LinearLayoutManager cityLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager countyLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mProvinceAdapter.setNewData(mProvinceList);
        mProvinceRecyclerView.setLayoutManager(provinceLayoutManager);
        mProvinceRecyclerView.setAdapter(mProvinceAdapter);

        mCityAdapter.setNewData(mCityList);
        mCityRecyclerView.setLayoutManager(cityLayoutManager);
        mCityRecyclerView.setAdapter(mCityAdapter);

        mCountyAdapter.setNewData(mCountyList);
        mCountyRecyclerView.setLayoutManager(countyLayoutManager);
        mCountyRecyclerView.setAdapter(mCountyAdapter);
        mProvinceAdapter.setSelectPos(0);
        mCityAdapter.setSelectPos(0);

        mProvinceAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                provincePos = position;
                mProviceCode = mProvinceAdapter.getItem(position).getCode();
                mProvinceName = mProvinceAdapter.getItem(position).getProvince();
                mProvinceAdapter.setSelectPos(position);
                mCityList = CityChooseUtil.getInstance().getCitiesData(position);
                mCityAdapter.setNewData(mCityList);
                mCountyList = CityChooseUtil.getInstance().getCountyData(provincePos,0);
                mCountyAdapter.setNewData(mCountyList);
                mCountyRecyclerView.scrollToPosition(0);
                mCityAdapter.setSelectPos(0);
                mCityName = mCityAdapter.getItem(0).getCity();
                mProvinceAdapter.notifyDataSetChanged();
                mCityAdapter.notifyDataSetChanged();
                mCountyAdapter.notifyDataSetChanged();
            }
        });
        mCityAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCityAdapter.setSelectPos(position);
                mCityCode = mCityAdapter.getItem(position).getCode();
                mCityName = mCityAdapter.getItem(position).getCity();
                mCountyList = CityChooseUtil.getInstance().getCountyData(provincePos,position);
                mCountyAdapter.setNewData(mCountyList);
                mCountyRecyclerView.scrollToPosition(0);
                mCityAdapter.notifyDataSetChanged();
                mCountyAdapter.notifyDataSetChanged();
            }
        });
        mCountyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCountyCode = mCountyAdapter.getItem(position).getCode();
                mCountyName = mCountyAdapter.getItem(position).getCounty();
                if(EmptyUtils.isNotEmpty(mListener)){
                    mListener.onDataCallBack(mProviceCode,mCityCode,mCountyCode);
                }
                disMissWindow();
            }
        });

        mView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    return true;
                }
                return false;
            }

        });
    }


    public void show(View v) {
        if (mPopWindow != null && !mPopWindow.isShowing()) {
            showAsDropDown(mPopWindow,v,0,0);
        }
    }

    public boolean isShowing() {
        if (!EmptyUtils.isEmpty(mPopWindow)) {
            return mPopWindow.isShowing();
        }
        return false;
    }

    public String getSelectedPositionStr(){
        if(EmptyUtils.isEmpty(mProvinceName)){
            mProvinceName = "";
        }

        if(EmptyUtils.isEmpty(mCityName)){
            mCityName = "";
        }

        if(EmptyUtils.isEmpty(mCountyName)){
            mCountyName = "";
        }
        return mProvinceName+mCityName+mCountyName;
    }



}