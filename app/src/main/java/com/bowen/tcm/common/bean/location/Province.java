package com.bowen.tcm.common.bean.location;

import java.util.List;

/**
 * 省份
 */
public class Province {
	private String province;
	private String code;
	private List<City> city_list;
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<City> getCity_list() {
		return city_list;
	}

	public void setCity_list(List<City> city_list) {
		this.city_list = city_list;
	}

	@Override
	public String toString() {
		return "ProvinceModel [province=" + province + ", city_list="
				+ city_list + "]";
	}
	
	
	
	

	
}
