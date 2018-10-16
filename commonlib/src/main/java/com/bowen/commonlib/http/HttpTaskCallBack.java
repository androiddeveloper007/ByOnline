package com.bowen.commonlib.http;


/**
 * 调用接口的回调
 * @author junhez
 *
 */
public abstract class HttpTaskCallBack<T> {

	public abstract void onSuccess(HttpResult<T> result);
	
	public abstract void onFail(HttpResult<T> result);
	

}
