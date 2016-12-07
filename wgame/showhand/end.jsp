<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingShowHand") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/showhand/start.jsp", response);
	return;
}
session.removeAttribute("playingShowHand");
ShowHandAction action = new ShowHandAction();
action.end(request);
String result = (String) request.getAttribute("result");
//UserStatusBean us = action.getUserStatus(request);
ShowHandBean sh = (ShowHandBean) request.getAttribute("showhand");
int count, i;
CardBean card = null;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("showhandWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="梭哈">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
-------------------<br/>
<%=girlName%>的牌:<br/>
<%
//显示系统牌
count = sh.getSystemCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getSystemCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
您的牌:<br/>
<%
//显示用户牌
count = sh.getUserCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getUserCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
<%
//赢了
if("win".equals(result)){
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/showhand/start.jsp") + "\" title=\"go\">梭哈</a>]赢了" + sh.getTotalWager() + "个乐币");
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
您赢了<%=sh.getTotalWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%=loginUser.showImg(picList[picIndex])%>
<!--<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>-->
<%
} 
//输了
else {
session.removeAttribute("showhandWins");
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/showhand/start.jsp") + "\" title=\"go\">梭哈</a>]输了" + sh.getTotalWager() + "个乐币");
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=sh.getTotalWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续梭哈</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>