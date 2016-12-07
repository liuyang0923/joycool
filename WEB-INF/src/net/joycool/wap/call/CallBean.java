/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.call;

import java.lang.reflect.Method;

/**
 * @author bomb
 *  
 */
class CallBean {
	public CallBean() {
		
	}
	public CallBean(Method method, String param) {
		this.method = method;
		this.param = param;
	}
	public Method method = null;
	public String param = null;
}
