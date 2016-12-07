<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.FriendRingBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%
response.setHeader("Cache-Control","no-cache");
int drinkId=StringUtil.toInt(request.getParameter("drinkId"));
int toId=StringUtil.toInt(request.getParameter("toId"));
int friendBeanId=StringUtil.toInt(request.getParameter("beanId"));
//结义处理
FriendAction action=new FriendAction(request);
action.jy();
String tip = (String)request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="结义提示" ontimer="<%=response.encodeURL("/user/ViewUserInfo.do?userId="+toId)%>">
<timer value="30"/>
<p align="left" >
<%=BaseAction.getTop(request, response)%>
<%if(tip!=null){%>
<%= StringUtil.toWml(tip) %><br/>
<%}
else
{
	IFriendService friendService = ServiceFactory.createFriendService();
	FriendDrinkBean drink = friendService.getFriendDrink(drinkId);
	UserBean toUser = UserInfoUtil.getUser(toId);
%>
您已经用<%=drink.getName()%>向<%=StringUtil.toWml(toUser.getNickName())%>提出结义请求，成功还是失败，请静候回音吧！<br/>
<%
}
%>
(3秒钟跳转回主页面)<br/>
<a href="<%=("/friend/friendCenter.jsp") %>">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>