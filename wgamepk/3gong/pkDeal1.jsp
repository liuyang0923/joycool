<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.pkDeal1(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean gong3 = (WGamePKBean) request.getAttribute("gong3");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
UserBean enemy = (UserBean) request.getAttribute("enemy");
Vector cardList = (Vector) request.getAttribute("cardList");
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(gong3 != null){    
	if(gong3.getLeftUserId() == loginUser.getId()){
		toUserId = gong3.getRightUserId();
	} else {
		toUserId = gong3.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="强行PK">
<p align="center">强行PK</p>
<p align="left">
<%=action.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<anchor><prev/>返回下注</anchor><br/>
<%
} else if("success".equals(result)){
	CardBean card1 = (CardBean) cardList.get(0);
    int aa=card1.getValue();
    String a =card1.getPicUrl();
    CardBean card2 = (CardBean) cardList.get(1);
	int bb=card2.getValue();
    String b =card2.getPicUrl();
    CardBean card3 = (CardBean) cardList.get(2);
	int cc=card3.getValue();
    String c =card3.getPicUrl();
%>
强行PK成功!<br/>
您下注<%=gong3.getWager()%>个乐币!<br/>
根据三公规则暂时扣除你赌注5倍的乐币，将根据赌博结果返还。<br/>
您的牌是:<br/>
<img src="<%=a%>" alt="<%=aa%>"/><img src="<%=b%>" alt="<%=bb%>"/><img src="<%=c%>" alt="<%=cc%>"/><br/>
请等待<%if(enemy.getUs2()!=null){%><%=enemy.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(enemy.getNickName())%>应战<br/>
<%
}
if(chatUrl != null){
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<%
}
%>
<br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgamepk/index.jsp">返回通吃岛</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>