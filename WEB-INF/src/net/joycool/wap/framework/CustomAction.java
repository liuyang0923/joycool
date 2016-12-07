package net.joycool.wap.framework;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.StringUtil;

public class CustomAction {
	public static String defaultResult = "failure";
	public static String defaultTip = "参数错误";
	
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	
	protected HttpSession session = null;
	
	protected String tip = null;
	protected String result = null;
	
	public CustomAction(){}
	
	public CustomAction(HttpServletRequest request) {
		this.request = request;
		session = request.getSession();
	}

	public CustomAction(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		session = request.getSession();
	}
	
	public void doTip() {
		doTip(null, null);
	}
	
	public void doTip(String tip) {
		doTip(null, tip);
	}

	public void doTip(String result, String tip) {
		if (result == null) {
			result = defaultResult;
		}
		if (tip == null) {
			tip = defaultTip;
		}
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
	}
	
	public UserBean getLoginUser() {
		return (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	}
	
	public void tip(String result, String tip) {
		this.result = result;
		this.tip = tip;
	}
	
	public void tip(String result) {
		this.result = result;
	}
	
	public String getTip() {
		if(tip == null)
			return defaultTip;
		else
			return tip;
	}
	
	public boolean isResult(String result) {
		return this.result != null && this.result.equals(result);
	}
	
	public String getResult() {
		if(result == null)
			return defaultResult;
		else
			return result;
	}
	
	public void sendRedirect(String url, HttpServletResponse response) throws IOException {
		response.sendRedirect((url));
	}
	
	public int getAttributeInt(String name) {
		try {
			return ((Integer)request.getAttribute(name)).intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public float getAttributeFloat(String name) {
		try {
			return ((Float)request.getAttribute(name)).floatValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public long getAttributeLong(String name) {
		try {
			return ((Long)request.getAttribute(name)).longValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public void setAttribute(String name, int value) {
		request.setAttribute(name, Integer.valueOf(value));
	}
	
	public void setAttribute(String name, long value) {
		request.setAttribute(name, Long.valueOf(value));
	}
	
	public void setAttribute(String name, float value) {
		request.setAttribute(name, Float.valueOf(value));
	}
	
	public void setAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}
	
	// session 操作
	public void setAttribute2(String name, int value) {
		session.setAttribute(name, Integer.valueOf(value));
	}
	public void setAttribute2(String name, long value) {
		session.setAttribute(name, Long.valueOf(value));
	}
	public void setAttribute2(String name, float value) {
		session.setAttribute(name, Float.valueOf(value));
	}
	public void setAttribute2(String name, Object value) {
		session.setAttribute(name, value);
	}
	public Object getAttribute2(String name) {
		return session.getAttribute(name);
	}
	public void removeAttribute2(String name) {
		session.removeAttribute(name);
	}
	public int getAttribute2Int(String name) {
		try {
			return ((Integer)session.getAttribute(name)).intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	public float getAttribute2Float(String name) {
		try {
			return ((Float)session.getAttribute(name)).floatValue();
		} catch (Exception e) {
			return 0;
		}
	}
	public long getAttribute2Long(String name) {
		try {
			return ((Long)session.getAttribute(name)).longValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	public int getParameterInt(String name) {
		try {
			return Integer.parseInt(request.getParameter(name).trim());
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	public List getParameterIntList(String name) {
		List list = new ArrayList();
		String[] p = request.getParameterValues(name);
		if(p != null) {
			if(p.length == 1 && p[0].indexOf(';') != -1) {
				p = p[0].split(";");
				for(int i = 0;i < p.length;i++) {
					int v = StringUtil.toInt(p[i]);
					if(v >= 0)
						list.add(Integer.valueOf(v));
				}
			} else {
				for(int i = 0;i < p.length;i++) {
					int v = StringUtil.toInt(p[i]);
					if(v >= 0)
						list.add(Integer.valueOf(v));
				}
			}
		}
		return list;
	}
	
	public int getParameterIntS(String name) {
		try {
			return Integer.parseInt(request.getParameter(name));
		} catch (Exception e) {
			return -1;
		}
	}
	
	public long getParameterLong(String name) {
		try {
			return Long.parseLong(request.getParameter(name));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public float getParameterFloat(String name) {
		try {
			return Float.parseFloat(request.getParameter(name));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public String getParameterString(String name) {
		return StringUtil.removeCtrlAsc(request.getParameter(name));
	}
	
	public String getParameterNoEnter(String name) {
		return StringUtil.noEnter(request.getParameter(name));
	}
	
	public String getParameterNoCtrl(String name) {
		return StringUtil.removeCtrlAsc(request.getParameter(name));
	}
	
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 

	public Date getParameterDate(String name) {
		try {
			return sdf.parse(request.getParameter(name));
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isMethodGet() {
		return request.getMethod().equalsIgnoreCase("get");
	}
	
	// 只判断，不设置冷却
	public boolean ifCooldown(String param, long cd) {
		param = "cd-" + param;
		try {
			Long t = (Long)session.getAttribute(param);
			long t2 = System.currentTimeMillis();
			if(t == null || t2 > t.longValue()) {
				return true;
			} else {
				return false;
			}
			
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * 判断是否已经冷却
	 * @return
	 */
	public boolean isCooldown(String param, long cd) {
		param = "cd-" + param;
		try {
			Long t = (Long)session.getAttribute(param);
			long t2 = System.currentTimeMillis();
			if(t == null || t2 > t.longValue()) {
				session.setAttribute(param, Long.valueOf(t2 + cd));
				return true;
			} else {
				return false;
			}
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public void setCooldown(String param, long cd) {
		param = "cd-" + param;
		session.setAttribute(param, Long.valueOf(System.currentTimeMillis() + cd));
	}
	
	public boolean hasParam(String param) {
		return request.getParameter(param) != null;
	}
	
	public void innerRedirect(String url, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	// 带所有参数
	public static void innerRedirect(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(url).forward(request, response);
	}
	// 如果要使用以下两个跳转函数，那么构造的时候必须带response
	public void innerRedirect(String url) throws ServletException, IOException {
		request.getRequestDispatcher(url).forward(request, response);
	}
	public void redirect(String url) throws IOException {
		response.sendRedirect((url));
	}
	
	public boolean isLogin() {
		return getLoginUser() != null;
	}
	
	public String getURI() {
		return StringUtil.getURI(request);
	}
	
	// 根据一堆int，每个是位数，获得二进制标志的结果
	public static int getFlag(List list) {
		int flag = 0;
		for(int i = 0;i < list.size();i++) {
			flag |= ( 1 << ((Integer)list.get(i)).intValue());
		}
		return flag;
	}
	public int getParameterFlag(String name) {
		List list = getParameterIntList(name);
		return getFlag(list);
	}
	// 判断url带的sid是否等于用户的session id，如果相等，表示访问者为用户本人
	public boolean isURLSession() {
		return session.getId().equals(request.getParameter("sid"));
	}
	public String getURLSession() {
		return "sid=" + session.getId();
	}
	
	public String encodeURL(String url) {
		return response.encodeURL(url);
	}

	// 是否支持xhtml, wap2.0
	public boolean isWap20() {
		return SecurityUtil.isWap20(request);
	}
}
