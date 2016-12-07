<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction"%>
<%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request);
int uid = action.getParameterInt("uid");
UserBean user = UserInfoUtil.getUser(uid);
if (user == null){
	out.clearBuffer();
	response.sendRedirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
感谢您邀请了用户id<%=user.getId()%>,您获得5000万乐币的奖励,在您的好友列表中能找到他,快去关心一下他吧!<br/>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">查看<%=user.getId()%>的资料</a><br/>
<a href="index.jsp">返回邀请首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>