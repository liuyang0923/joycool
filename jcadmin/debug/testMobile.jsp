<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.action.chat.RoomRateAction,net.joycool.wap.framework.JoycoolSpecialUtil"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="net.joycool.wap.bean.home.HomePhotoBean" %><jsp:directive.page import="net.joycool.wap.bean.home.HomeUserBean"/>
<jsp:directive.page import="net.joycool.wap.cache.OsCacheUtil"/>
<jsp:directive.page import="net.joycool.wap.service.factory.ServiceFactory"/>
<%!
public void getMobile(HttpServletRequest request,
		HttpServletResponse response){
	try{
	HttpServletRequest httpRequest = (HttpServletRequest) request;
	HttpServletResponse httpResponse = (HttpServletResponse) response;
	
	HttpSession session = request.getSession();
	String mobile = (String) session.getAttribute("userMobile");
	
	String url = request.getRequestURL().toString();
	String qs = request.getQueryString();
	if (qs != null) {
		url += "?" + qs;
	}
	
//	 liuyi 2006-12-20 登录注册修改 start
	if (session.getAttribute("fetchMobileFromBottom") == null && mobile == null) {
		// 一些特殊页面
		if (session.getAttribute(Constants.LOGIN_USER_KEY) != null
				|| url.indexOf("fromlszx.jsp") != -1
				//|| (url.indexOf("jcadmin") != -1 && url.indexOf("register") == -1)
				|| url.indexOf("enter") != -1
				|| url.indexOf("friendlink") != -1
				|| url.startsWith("/wapIndex.jsp")
				|| url.startsWith("/Column.do?columnId=" + Constants.INDEX_ID)
				|| url.startsWith("/isNotMobile.jsp")
				|| url.equals("/")
				|| url.equals(BaseAction.URL_PREFIX)
				|| url.startsWith("/entry")
				|| url.startsWith("/friend")
				|| url.startsWith("/ebook/index.jsp")
				|| url
						.startsWith("/game/index.jsp")
				|| url.startsWith("/image/index.jsp")
				|| url.startsWith("/Column.do?columnId=3480")
				|| url.startsWith("/Column.do?columnId=2705")
				|| url.startsWith("/Column.do?columnId=3925")
				|| url.startsWith("/huangye/")) {
			// do noting
		}
		// 取手机号返回
		else if (SecurityUtil.needMobile(request)) {
			// mobile = "13811885620";
			// mobile = "";
			mobile = SecurityUtil.getPhone(httpRequest);
			int linkId = StringUtil.toInt(request.getParameter("linkId"));
			if (linkId == -1) {
				linkId = 0;
			}
			// liuyi 2006-11-07 体坛周报wap版返回 start
			int unionId = StringUtil.toInt(request.getParameter("unionId"));
			if (unionId == -1) {
				unionId = 0;
			}
			session.setAttribute("unionId", new Integer(unionId));
			// liuyi 2006-11-07 体坛周报wap版返回 end
			// 保存手机号
			if (mobile != null && !mobile.equals("")) {
				if (mobile.startsWith("86")) {
					mobile = mobile.substring(2);
				}
				// liuyi 2006-11-07 体坛周报wap版返回 start
				LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request)
						+ ":" + request.getRemoteAddr() + ":" + linkId
						+ ":" + url + ":" + unionId);
				// liuyi 2006-11-07 体坛周报wap版返回 end
				// liuyi 2006-09-19 书签功能 start
				session.setAttribute("UA", SecurityUtil.getUA(request));
				// liuyi 2006-09-19 书签功能 end
				// 自动注册，对于登陆的处理按四步走方案做
				// liuyi 2006-12-20 自动注册功能屏蔽 start
				if (mobile != null) {
					session.setAttribute("userMobile", mobile);
				}
				// liuyi 2006-12-26 注册流程修改 start
				if (mobile != null
						&& (mobile.startsWith("13") || mobile
								.startsWith("15"))) {
					String sql = "select id from user_info where mobile='"
							+ mobile + "'";
					int id = SqlUtil.getIntResult(sql,
							Constants.DBShortName);
					if (id < 1) {
						Util.updatePhoneNumber(httpRequest, mobile);
					}
				}
				// liuyi 2006-12-26 注册流程修改 end
				// liuyi 2006-12-20 自动注册功能屏蔽 end
			}
			session.setAttribute("fetchMobileFromBottom", "true");
		}
		// 方法为post的页面
		else if ("post".equalsIgnoreCase(httpRequest.getMethod())) {
			// do nothing
		}
		// 取手机号
		else if (SecurityUtil.getMobile != 0) {
			return;
		}
	}
	}catch(Exception e){
		e.printStackTrace();
	}
}

public String getFetchMobileImage(HttpServletRequest request,
		HttpServletResponse response) {
	
	HttpSession session = request.getSession();
	String mobile = (String) session.getAttribute("userMobile");
	String fetchMobileFromBottom = (String)session.getAttribute("fetchMobileFromBottom");
	
	StringBuffer wml = new StringBuffer();
	if((mobile==null || mobile.equals("")) && fetchMobileFromBottom==null){	
		session.setAttribute("fetchMobileFromBottom", "true");
		//getMobile(request, response);
		String url = request.getRequestURL().toString();
		String qs = request.getQueryString();
		if (qs != null) {
			url += "?" + qs;
		}
		
		int linkId = StringUtil.toInt((String) session
				.getAttribute("linkId"));
		if (linkId == -1) {
			linkId = 0;
		}
		
		String backUrl = null;
		if (url.indexOf("?") == -1) {
			backUrl = url + "?linkId=" + linkId;
		} else {
			backUrl = url + "&linkId=" + linkId;
		}
		backUrl = URLEncoder.encode(backUrl);
		wml.append("<img src=\""
				+ "http://wap.linktone.com:18000/ua/ub.ph?AN=joycool&amp;URL=" + backUrl + "\" />");
		wml.append("<br/>");
	}

	return wml.toString();
}

%>
<%
getMobile(request, response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="通吃岛">
<p align="left"><img src="img/logo.gif" alt="通吃岛"/><br/>
手机号：<%= session.getAttribute("userMobile") %><br/>
<%= BaseAction.getBottom(request, response) %><br/>
<%= getFetchMobileImage(request, response) %>
</p>
</card>
</wml>