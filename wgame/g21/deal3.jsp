<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
G21Action ga = new G21Action();
ga.deal3(request);
//UserStatusBean us = ga.getUserStatus(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
String userHasBlackJack = (String) request.getAttribute("userHasBlackJack");
G21Bean g21 = (G21Bean) request.getAttribute("g21");
if(g21==null){
	response.sendRedirect("start.jsp");
	return;
}
int count, i;
CardBean card = null;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("g21Wins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="二十一点">
<p align="left">
<%=BaseAction.getTop(request, response)%>
二十一点<br/>
-------------------<br/>
<%=girlName%>的牌:<br/>
<%
//显示系统牌
count = g21.getSystemCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) g21.getSystemCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
您的牌:<br/>
<%
//显示系统牌
count = g21.getUserCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) g21.getUserCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
<%
//打平
if("draw".equals(result)){
%>
您和<%=girl.getName()%>打平了!<%=tip%><br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%
} 
//赢了
else if("userWin".equals(result)){
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/g21/start.jsp") + "\" title=\"go\">二十一点</a>]赢了" + g21.getWager() + "个乐币");
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
<%=tip%><br/>
您赢了<%=("true".equals(userHasBlackJack)? 2 * g21.getWager() : g21.getWager())%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%=loginUser.showImg(picList[picIndex])%>
<!--<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>-->
<%
}
//输了
else {
session.removeAttribute("g21Wins");
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/g21/start.jsp") + "\" title=\"go\">二十一点</a>]输了" + g21.getWager() + "个乐币");
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
<%=tip%><br/>
您输了<%=g21.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续玩</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>