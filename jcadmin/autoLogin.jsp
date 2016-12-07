<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
%><%
int userId = StringUtil.toInt(request.getParameter("id"));
String mobile = request.getParameter("mobile");
UserBean user = UserInfoUtil.getUser(userId);
if(user!=null && user.getMobile().equals(mobile)){
	session.setAttribute("userMobile", mobile);
	session.setAttribute("allowVisit", "true");
	JoycoolSessionListener.updateOnlineUser(request, user);	
	
    //	 liuyi 2006-12-20 登录注册修改 start
	mobile = (String) request.getSession().getAttribute(
			"userMobile");
	if (mobile != null) {
		if (user.getMobile() == null
				|| !user.getMobile().startsWith("13")
				|| !user.getMobile().startsWith("15")) {
			UserInfoUtil
					.updateUser("mobile = '" + mobile + "'", "id = "
							+ user.getId(), user.getId() + "");
			user.setMobile(mobile);
			OsCacheUtil.flushGroup(OsCacheUtil.USER_ID_GROUP, mobile);
		}
	}
	// liuyi 2006-12-20 登录注册修改 end
}
//response.sendRedirect("/");
BaseAction.sendRedirect(null, response);
%>
