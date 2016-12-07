/*
 * Created on 2006-2-15
 *
 */
package net.joycool.wap.util;

import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * @author lbj
 * 
 */
public class LogUtil {
	public static int totalRedirect = 0;

	public static int totalBack = 0;
	
	// liuyi 2006-11-23 请求时间日志
	public static Logger moneyLogger = Logger.getLogger("money.Log");

	// liuyi 2006-11-23 请求时间日志
	public static Logger timeLogger = Logger.getLogger("time.Log");

	// liuyi 2006-10-20 http请求日志
	public static Logger accessLogger = Logger.getLogger("access.Log");

	// liuyi 2006-12-11 登录日志
	public static Logger loginLogger = Logger.getLogger("login.Log");

	// liuyi 2006-12-28 调试日志
	public static Logger debugLogger = Logger.getLogger("jcdebug.Log");
	public static Logger adminLogger = Logger.getLogger("admin.Log");

	public static Logger pvLogger = Logger.getLogger("pv.Log");

	public static Logger mobileLogger = Logger.getLogger("mobile.Log");

	public static Logger userLogger = Logger.getLogger("user.Log");

	public static Logger jumpoutLogger = Logger.getLogger("jumpout.Log");

	public static Logger onlineLogger = Logger.getLogger("online.Log");

	public static Logger operationLogger = Logger.getLogger("operation.Log");

	// liuyi 2006-10-20 http请求日志
	public static void logMoney(String log) {
		moneyLogger.error(log);
	}

	// liuyi 2006-10-20 http请求日志
	public static void logTime(String log) {
		timeLogger.error(log);
	}

	// liuyi 2006-10-20 http请求日志
	public static void logAccess(String log) {
	//	accessLogger.error(log);
	}

	// liuyi 2006-12-11 登录日志
	public static void logLogin(String log) {
		loginLogger.error(log);
	}

	// liuyi 2006-12-28 调试日志
	public static void logDebug(String log) {
		debugLogger.error(log);
	}

	public static void logPv(String log) {
		pvLogger.error(log);
	}

	public static void logMobile(String log) {
		mobileLogger.error(log);
	}

	public static void logOnline(String log) {
		onlineLogger.error(log);
	}

	public static void logUser(String log) {
		userLogger.error(log);
	}
	
	public static void logOperation(String log) {
		operationLogger.error(log);
	}
	public static void logAdmin(String log) {
		adminLogger.error(log);
	}

	public static void logJumpout(String log) {
		jumpoutLogger.error(log);
	}

	final static int MAX_USER_LOG = 100;
	static int logUser = 0;		// 特殊记录pv的用户
	static LinkedList logUserPv = new LinkedList();
	
	public static int getLogUser() {
		return logUser;
	}
	public static void setLogUser(int logUser) {
		LogUtil.logUser = logUser;
	}
	public synchronized static void logSingleUser(String logs) {
		logUserPv.addFirst(logs);
		if(logUserPv.size() > MAX_USER_LOG)
			logUserPv.removeLast();
	}
	public static LinkedList getSingleUserLog(int id) {
		LogUtil.logUser = id;
		return logUserPv;
	}
}
