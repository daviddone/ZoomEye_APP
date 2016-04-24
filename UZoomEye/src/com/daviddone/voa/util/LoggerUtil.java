package com.daviddone.voa.util;

import android.util.Log;

/** 
* @ClassName: LoggerUtil 
* @Description: 本类作用 
* @author tangdongqing
* @date 2016-4-23 下午11:55:12 
*  
*/
public class LoggerUtil {
	//logLevel设置为7，不输出日志,6为error
	public static int logLevel = 7;
	
	public static void i(String tag, String msg) {
		if (logLevel <= Log.INFO)
			android.util.Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (logLevel <= Log.ERROR)
			android.util.Log.e(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (logLevel <= Log.DEBUG)
			android.util.Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (logLevel <= Log.VERBOSE)
			android.util.Log.v(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (logLevel <= Log.WARN)
			android.util.Log.w(tag, msg);
	}
}
