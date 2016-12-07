package net.joycool.wap.call;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bomb
 *
 */
public class CallParam {
	
	public String param;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public int unique;	// 一个独特的数字标记
	
	public CallParam(HttpServletRequest request, HttpServletResponse response,
			int unique) {
		this.request = request;
		this.response = response;
		this.unique = unique;
	}
	
	public CallParam(String param, HttpServletRequest request, HttpServletResponse response,
			int unique) {
		this.param = param;
		this.request = request;
		this.response = response;
		this.unique = unique;
	}

	public String getPageKey() {
		return "page" + unique;
	}
	
	public String getKey(String key) {
		return key + unique;
	}
	
	static String[] zeroStrings = new String[0];
	public String[] getParams() {
		if(param.length() == 0)
			return zeroStrings;
		String params[] = param.split(",");
		for(int i = 0;i < params.length;i++) {
			if(params[i].startsWith("$")) {
				params[i] = request.getParameter(params[i].substring(1));
			}
		}
		return params;
	}
	
	public String getParam() {
		if(param.startsWith("$")) {
			return request.getParameter(param.substring(1));
		} else {
			return param;
		}
	}
}
