package com.bowen.tcm.common.util;

import android.content.Context;


import com.bowen.tcm.BowenApplication;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.location.City;
import com.bowen.tcm.common.bean.location.County;
import com.bowen.tcm.common.bean.location.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 位置选择工具
 */
public class CityChooseUtil {
    private String AddressXML;		         //xml格式的中国省市区信息
	private List<Province> provinceList; //地址列表
	private List<City> cityList; //地址列表
	private List<County> countryList; //地址列表
	private Context mContext;
	private static CityChooseUtil mCityChooseUtil = null;
	private final int PROVINCE_TYPE=1;
	private final int CITY_TYPE=2;
	private final int AREA_TYPE=3;

	public CityChooseUtil() {
		super();
		AddressXML = getRawAddress().toString();
		try {
			analysisXML(AddressXML);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

    public static CityChooseUtil getInstance(){
        synchronized (CityChooseUtil.class){
            if(mCityChooseUtil == null){
                mCityChooseUtil = new CityChooseUtil();
            }
        }
        return mCityChooseUtil;
    }

    /**
	 * 获取地区raw里的地址xml内容
	 * */
	public StringBuffer getRawAddress(){
		InputStream in = BowenApplication.getBoWenAppContext().getResources().openRawResource(R.raw.address);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			br.close();
			isr.close();
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return sb;
	}
	

	/**
	 * 解析省市区xml，
	 * 采用的是pull解析，
	 * 为什么选择pull解析：因为pull解析简单浅显易懂！
	 * */
	public void analysisXML(String data) throws XmlPullParserException {
		try {
			Province provinceModel = null;
			City cityModel= null;
			County countyModel= null;
			List<City> cityList = null;
			List<County> countyList= null;
			InputStream xmlData = new ByteArrayInputStream(data.getBytes("UTF-8"));
			XmlPullParserFactory factory = null;
			factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser;
			parser = factory.newPullParser();
			parser.setInput(xmlData, "utf-8");
			String currentTag = null;
			String province;
			String city;
			String county;
			String code;
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				String typeName = parser.getName();
				
				if (type == XmlPullParser.START_TAG) {
					if("root".equals(typeName)){
						provinceList = new ArrayList<Province>();
						
					}else if("province".equals(typeName)){
						province = parser.getAttributeValue("","name");//获取标签里第一个属性,例如<city name="北京市" zipcode="1">中的name属性
						code = parser.getAttributeValue("","zipcode");
						provinceModel = new Province();
						provinceModel.setProvince(province);
						provinceModel.setCode(code);
						cityList = new ArrayList<City>();
					}else if("city".equals(typeName)){
						city = parser.getAttributeValue("","name");
						code = parser.getAttributeValue("","zipcode");
						cityModel = new City();
						cityModel.setCity(city);
						cityModel.setCode(code);
						countyList = new ArrayList<County>();
					}else if("district".equals(typeName)){
						county = parser.getAttributeValue("","name");
						code = parser.getAttributeValue("","zipcode");
						countyModel  = new County();
						countyModel.setCounty(county);
                        countyModel.setCode(code);
					}
					currentTag = typeName;
					
				} else if (type == XmlPullParser.END_TAG) {
					if("root".equals(typeName)){
					}else if("province".equals(typeName)){
						provinceModel.setCity_list(cityList);
						provinceList.add(provinceModel);
					}else if("city".equals(typeName)){
						cityModel.setCounty_list(countyList);
						cityList.add(cityModel);
					}else if("district".equals(typeName)){
						countyList.add(countyModel);
					}

				} else if (type == XmlPullParser.TEXT) {
					currentTag = null;
				}
				
					type = parser.next();
			}
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示省份信息
	 */
	public List<Province> getProvincesData() {
		provinceList=showLocationData(PROVINCE_TYPE, 0, 0);
		return provinceList;
	}

	/**
	 * 显示城市信息
	 * @param pPosition 要显示的省份的索引
	 */
	public List<City> getCitiesData(int pPosition) {
		cityList=showLocationData(CITY_TYPE, pPosition, 0);
		return cityList;

	}

	/**
	 * 显示地区信息
	 * @param pPosition 要显示的省份的索引
	 * @param cPosition 要显示的城市的索引
	 */
	public List<County> getCountyData(int pPosition, int cPosition) {
		countryList=showLocationData(AREA_TYPE,pPosition, cPosition);
		return countryList;
	}

	/**
	 * 根据调用类型显示相应的数据列表
	 * @param type 显示类型 1.省；2.市；3.县、区
	 */
	public List showLocationData(int type, int pPosition, int cPosition){
		if(type == 1){
			return provinceList;
		}else if(type == 2){
			cityList=provinceList.get(pPosition).getCity_list();
			return cityList;
		}else if(type ==3){
			countryList=provinceList.get(pPosition).getCity_list().get(cPosition).getCounty_list();
			return countryList;
		}
		return null;

	}
}
