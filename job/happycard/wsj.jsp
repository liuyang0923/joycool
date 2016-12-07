<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.OsCacheUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="春节礼物">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%

UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
if (OsCacheUtil.get(loginUser.getId()+"","candyGroup",3600*24) != null)
{%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
return;
}
int num = 500000;
UserInfoUtil.updateUserStatus("game_point=game_point+" + num,
				"user_id=" + loginUser.getId(), loginUser.getId(),
				UserCashAction.PRESENT, "获春节红包" + num + "乐币");
String info = "获春节红包" + num + "乐币";	
OsCacheUtil.put(loginUser.getId()+"",info,"candyGroup");
	
%>
<br/>
迎新春乐酷给您送红包，50万乐币送给您！
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>