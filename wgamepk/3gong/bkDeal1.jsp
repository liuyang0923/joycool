<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.bkDeal1(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean gong3 = (WGamePKBean) request.getAttribute("gong3");
Vector cardList = (Vector) request.getAttribute("cardList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="坐庄">
<p align="center">坐庄</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamepk/3gong/bkStart.jsp">返回下注</a><br/>
<%
} else if("success".equals(result)){
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/3gong/index.jsp") + "\" title=\"go\">三公</a>]坐庄" + gong3.getWager() + "个乐币");
	CardBean card1 = (CardBean) cardList.get(0);
    int aa=card1.getValue();
    String a =card1.getPicUrl();
    CardBean card2 = (CardBean) cardList.get(1);
	int bb=card2.getValue();
    String b =card2.getPicUrl();
    CardBean card3 = (CardBean) cardList.get(2);
	int cc=card3.getValue();
    String c = card3.getPicUrl();
%>
坐庄成功!<br/>
您下注<%=gong3.getWager()%>个乐币<br/>
根据三公规则暂时扣除你赌注5倍的乐币，将根据赌博结果返还。<br/>
<%--!您的牌是:<br/>
<img src="<%=a%>" alt="<%=aa%>"/><img src="<%=b%>" alt="<%=bb%>"/><img src="<%=c%>" alt="<%=cc%>"/><br/>!--%>
请等待其他用户挑战<br/>
<%
}
%>
<br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>