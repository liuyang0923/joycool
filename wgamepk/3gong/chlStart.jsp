<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.chlStart(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
Vector cardList = (Vector) request.getAttribute("cardList");
Vector cardList1 = (Vector) request.getAttribute("cardList1");
WGamePKBean gong3 = (WGamePKBean) request.getAttribute("gong3");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
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
<card title="挑战">
<p align="center">挑战</p>
<p align="left">
<%=action.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamepk/3gong/index.jsp">返回上级</a><br/>
<%
} else if("success".equals(result)){
	//得到挑战者的牌
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
<%--
<%
if(gong3.getWinUserId() == gong3.getLeftUserId()) {  //输了
%>
<%=ResultPicture.getPicture(1)%>
<%
}else if(gong3.getWinUserId() == gong3.getRightUserId()) { //赢了 %>
<%=ResultPicture.getPicture(2)%>
<%}else {  //平局%> 
<%=ResultPicture.getPicture(0)%>
<%}%>--%>

庄家的牌是:<br/>
<img src="<%=d%>" alt="<%=dd%>"/><img src="<%=e%>" alt="<%=ee%>"/><img src="<%=f%>" alt="<%=ff%>"/><br/>
您的牌是:<br/>
<img src="<%=a%>" alt="<%=aa%>"/><img src="<%=b%>" alt="<%=bb%>"/><img src="<%=c%>" alt="<%=cc%>"/><br/>
<%
	//赢了
    if(gong3.getWinUserId() == gong3.getLeftUserId()){
		//加入谣言
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgamepk/index.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/3gong/index.jsp") + "\" title=\"go\">三公</a>]输给" + gong3.getLeftNickname() + "*" + gong3.getWager() + "个乐币");
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(gong3.getLeftUserId());
%>
您的牌点数小,您输给<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(gong3.getLeftNickname())%>*<%=gong3.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else if(gong3.getWinUserId() == gong3.getRightUserId()){
	    //加入谣言
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgamepk/index.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/3gong/index.jsp") + "\" title=\"go\">三公</a>]赢了" + gong3.getLeftNickname() + "*" + gong3.getWager() + "个乐币");
%>
您的牌点数大,您赢了<%=StringUtil.toWml(gong3.getLeftNickname())%>*<%=gong3.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else {
		UserStatusBean usTemp=null;
		usTemp=UserInfoUtil.getUserStatus(gong3.getLeftUserId());
%>
您和庄家<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(gong3.getLeftNickname())%>打平了!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	}
}
if(chatUrl != null){
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<a href="/wgamepk/3gong/pkStart.jsp?userId=<%=gong3.getLeftUserId()%>">邀请对方再PK一局</a><br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>