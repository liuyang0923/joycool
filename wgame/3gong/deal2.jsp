<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wgame.CardBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Bean gong3ss = (Gong3Bean)session.getAttribute("gong3");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
if(session.getAttribute("playingGong3") == null || gong3ss == null || gong3ss.getWager() * 3 > us.getGamePoint()){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/3gong/start.jsp", response);
	return;
}
session.removeAttribute("playingGong3");
Gong3Action da = new Gong3Action();
da.deal2(request);
//UserStatusBean us = da.getUserStatus(request);
String result = (String) request.getAttribute("result");
Gong3Bean gong3 = (Gong3Bean) request.getAttribute("gong3");
Vector cardList = (Vector) request.getAttribute("cardList");
Vector cardList1 = (Vector) request.getAttribute("cardList1");

//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("gong3Wins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="三公">
<p align="left">
<%=BaseAction.getTop(request, response)%>
三公<br/>
-------------------<br/>
<%//得到挑战者的牌
	CardBean card1 = (CardBean) cardList.get(0);
    int aa=card1.getValue();
    String a =card1.getPicUrl();
    CardBean card2 = (CardBean) cardList.get(1);
	int bb=card2.getValue();
    String b =card2.getPicUrl();
    CardBean card3 = (CardBean) cardList.get(2);
	int cc=card3.getValue();
    String c =card3.getPicUrl();
	//得到庄家的牌
    CardBean card4 = (CardBean) cardList1.get(0);
    int dd=card4.getValue();
    String d =card4.getPicUrl();
    CardBean card5 = (CardBean) cardList1.get(1);
	int ee=card5.getValue();
    String e =card5.getPicUrl();
    CardBean card6 = (CardBean) cardList1.get(2);
	int ff=card6.getValue();
    String f =card6.getPicUrl();
	%>
<%=girlName%>的牌：<br/>
<img src="<%=d%>" alt="<%=dd%>"/><img src="<%=e%>" alt="<%=ee%>"/><img src="<%=f%>" alt="<%=ff%>"/><br/>
您的牌：<br/>
<img src="<%=a%>" alt="<%=aa%>"/><img src="<%=b%>" alt="<%=bb%>"/><img src="<%=c%>" alt="<%=cc%>"/><br/>
<%
//赢了
if("win".equals(result)){
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/3gong/start.jsp") + "\" title=\"go\">三公</a>]赢了" + gong3.getWager() + "个乐币");
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
您赢了<%=gong3.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%=loginUser.showImg(picList[picIndex])%>
<!--<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>-->
<%
} 
//输了
else if("lose".equals(result)){
session.removeAttribute("gong3Wins");
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/3gong/start.jsp") + "\" title=\"go\">三公</a>]输了" + gong3.getWager() + "个乐币");
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=gong3.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%
} else{%>
您和<%=girlName%>打平了!<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%}%>

<br/>
<a href="start.jsp">继续挑战</a><br/>
<%--mcq_2006-6-20_增加找真人PK链接_start--%>
<a href="/wgamepk/3gong/index.jsp">找真人PK</a><br/>
<%--mcq_2006-6-20_增加找真人PK链接_end--%>
<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>