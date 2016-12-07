<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.guest.*"%><%@ page import="jc.guest.wgame.DiceDXAction"%><%
response.setHeader("Cache-Control","no-cache");
GuestHallAction action = new GuestHallAction(request);
GuestUserInfo guestUser = action.getGuestUser();
if (guestUser == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
DiceDXBean diceDXss = (DiceDXBean)session.getAttribute("dicedx");
if(session.getAttribute("playingDiceDX2") == null || diceDXss == null || diceDXss.getWager() > guestUser.getMoney()){
	BaseAction.sendRedirect("/guest/dicedx/start.jsp", response);
	return;
}
session.removeAttribute("playingDiceDX2");
DiceDXAction da = new DiceDXAction();
da.deal2(request,guestUser);
String result = (String) request.getAttribute("result");
DiceDXBean diceDX = (DiceDXBean) request.getAttribute("diceDX");
int[] diceDXs = (int[]) request.getAttribute("diceDXs");
int[] diceDXs1 = (int[]) request.getAttribute("diceDXs1");
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("dicedxWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="掷骰子">
<p align="left">
<%=BaseAction.getTop(request, response)%>
掷骰子<br/>
-------------------<br/>
您的骰子:<br/>
<img src="/wgamepk/dice/img/<%=diceDXs[0]%>.gif" alt="<%=diceDXs[0]%>"/><img src="/wgamepk/dice/img/<%=diceDXs[1]%>.gif" alt="<%=diceDXs[1]%>"/><img src="/wgamepk/dice/img/<%=diceDXs[2]%>.gif" alt="<%=diceDXs[2]%>"/><br/>
<%=girlName%>的骰子:<br/>
<img src="/wgamepk/dice/img/<%=diceDXs1[0]%>.gif" alt="<%=diceDXs1[0]%>"/><img src="/wgamepk/dice/img/<%=diceDXs1[1]%>.gif" alt="<%=diceDXs1[1]%>"/><img src="/wgamepk/dice/img/<%=diceDXs1[2]%>.gif" alt="<%=diceDXs1[2]%>"/><br/>
<%
//赢了
if("win".equals(result)){
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
您赢了<%=diceDX.getWager()%>个游币<br/>
您还有<%=guestUser.getMoney()%>个游币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%
} 
//输了
else if("lose".equals(result)){
session.removeAttribute("dicedxWins");
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=diceDX.getWager()%>个游币<br/>
您现在一共有<%=guestUser.getMoney()%>个游币<br/>
<%
}else{%>
您和系统打平了!
您还有<%=guestUser.getMoney()%>个游币!<br/>
<%}%>
<a href="start.jsp">继续挑战</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>