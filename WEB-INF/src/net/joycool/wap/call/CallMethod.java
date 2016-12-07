package net.joycool.wap.call;

import java.lang.reflect.Method;
import java.util.regex.*;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.call.CallParam;

public class CallMethod{

	static ICacheMap methods = CacheManage.call;
	
    static Class[] callClasses = {CallParam.class};
    
    public static ClassLoader classLoader = CallMethod.class.getClassLoader();
    
    // 重置所有method，并创建新的classloader重新载入call class
    public static void reloadAll() {
    	classLoader = new CallClassLoader();
    	synchronized(methods){
    		methods.clear();
    	}
    }
    // 重置classloader为默认的
    public static void resetClassLoader() {
    	classLoader = CallMethod.class.getClassLoader();
    	synchronized(methods){
    		methods.clear();
    	}
    }
    public static void reloadClassLoader() {
    	classLoader = new CallClassLoader();
    }
    // 清空某个调用的缓存，一般情况下清空后需要调用reloadClassLoader()
    public static void clearMethod(String content) {
    	methods.srm(content);
    }
    
	Pattern pattern = Pattern.compile("(.+)\\.(.+)\\((.*)\\)");

	public String call(String content, CallParam callParam) {
		CallBean cb = null;
		synchronized(methods){
			cb = (CallBean)methods.get(content);
			if(cb == null) {
				Matcher m = pattern.matcher(content);
				if(m.find())
				{
					String className = m.group(1);
					String methodName = m.group(2);
					String param = m.group(3);
					
					try {
						Class cl = Class.forName("net.joycool.wap.call." + className, true, classLoader);
						Method me = cl.getMethod(methodName, callClasses);
						cb = new CallBean(me, param.trim());
						
					} catch (Exception e) {
						cb = new CallBean();
						System.out.println("CallMethod Error: no method found for " + content + "-" + callParam.unique);
						e.printStackTrace();
					}
				} else {
					cb = new CallBean(null, "");
				}
				methods.put(content, cb);
			}
		}
		
		if(cb.method == null) {
			if(cb.param == null)	// 不存在的函数
				return null;
			else					// 不是函数调用
				return content;
		}
		
		try {
			callParam.param = cb.param;
			Object[] callParams = {callParam};
			content = (String) cb.method.invoke(null, callParams);
		} catch (Exception e) {
			System.out.println("CallMethod Error: calling " + content);
			e.printStackTrace();
			return null;
		}
		
    	return content;
	}
}

