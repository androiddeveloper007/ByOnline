package com.bowen.tcm.common.bean.location;

/**
 * 区域
 */
public class County {
	private String county;

	private String code;

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "CountyModel [county=" + county + "]";
	}
	
	
}
