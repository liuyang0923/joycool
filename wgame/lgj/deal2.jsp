<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
LgjBean lgjss = (LgjBean) session.getAttribute("lgj");
if(session.getAttribute("playingLgj") == null || lgjss == null || lgjss.getWager() > us.getGamePoint()){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/lgj/start.jsp", response);
	return;
}
session.removeAttribute("playingLgj");
LgjAction action = new LgjAction();
action.deal2(request);
//UserStatusBean us = action.getUserStatus(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
String systemAction = (String) request.getAttribute("systemAction");
LgjBean lgj = (LgjBean) request.getAttribute("lgj");
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("lgjWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="老虎杠子鸡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老虎杠子鸡<br/>
-------------------<br/>
<img src="img/<%=lgj.getAction()%>.gif" alt="<%=lgj.getAction()%>"/>&lt;-&gt;<img src="img/<%=systemAction%>.gif" alt="<%=systemAction%>"/><br/>
<%
//打平
if("draw".equals(result)){
%>
平手。您还有<%=us.getGamePoint()%>个乐币<br/>
<%
}
//赢了
else if("win".equals(result)){
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/lgj/start.jsp") + "\" title=\"go\">剪刀石头布</a>]赢了" + lgj.getWager() + "个乐币");
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
您赢了<%=lgj.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%=loginUser.showImg(picList[picIndex])%>
<!--<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>-->
<%
} 
//输了
else {
    session.removeAttribute("lgjWins");
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/lgj/start.jsp") + "\" title=\"go\">剪刀石头布</a>]输了" + lgj.getWager() + "个乐币");
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=lgj.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续PK</a><br/>
<%--mcq_2006-6-20_增加找真人PK链接_start--%>
<a href="/wgamepk/lgj/index.jsp">找真人PK</a><br/>
<%--mcq_2006-6-20_增加找真人PK链接_end--%>
<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>