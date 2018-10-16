package com.bowen.commonlib.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 应用程序Activity管理类
 * @author AwenZeng
 * @date 2015-1-24 下午4:12:13
 */
public class StackUtils {

	private static Stack<Activity> activityStack;
	private static StackUtils stackUtils;
	private StackUtils() {
	}

	public static StackUtils getInstanse() {
		if (stackUtils == null) {
			synchronized (StackUtils.class) {
				if (stackUtils == null) {
					stackUtils = new StackUtils();
				}
			}
		}
		return stackUtils;
	}

	/**
	 * 获取堆栈内最后一个 Activity
	 * @return
	 */
	public Activity getLastActivty() {
		if (activityStack == null)
			return null;
		return currentActivity();
	}

	/**
	 * @param activity
	 * @throws
	 * @Description: TODO(添加Activity到堆栈)
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		if (activity != null) {
			activityStack.add(activity);
		}

	}

	/**
	 * @return
	 * @throws
	 * @Description: TODO(获取当前Activity（堆栈中最后一个压入 ）)
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if(activityStack != null){
			activity = activityStack.lastElement();
		}
		return activity;
	}

	/**
	 * @param cls
	 * @return
	 * @throws
	 * @Description: TODO(获取指定的Activity)
	 */
	public Activity getActivity(Class<?> cls) {
		if (activityStack != null)
			for (Activity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					return activity;
				}
			}
		return null;
	}

	/**
	 * @throws
	 * @Description: 结束当前Activity（堆栈中最后一个压入）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * @param activity
	 * @throws
	 * @Description: 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activityStack!=null&&activityStack.size()>0&&activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 移除
	 */
	public void removeActivity(Activity activity){
		activityStack.remove(activity);
	}

	/**
	 * @param class1
	 * @throws
	 * @Description: finish掉指定的Activity
	 */
	public void finishActivity(Class<?> class1) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(class1)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * @throws
	 * @Description: TODO(结束所有Activity)
	 */
	public void finishAllActivity() {
		for (int i = 0; i < activityStack.size(); i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * @throws
	 * @Description: TODO(退出应用程序)
	 */
	public void exitApp() {
		try {
			finishAllActivity();
			// 杀死应用进程
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
