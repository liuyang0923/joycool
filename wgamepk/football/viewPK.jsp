<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FootBallAction action = new FootBallAction(request);
action.viewWGamePK(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBean pk = (WGamePKBean) request.getAttribute("pk");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌局">
<p align="center">赌局</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//已经被取消
if(pk == null){
%>
这局已被取消!<br/>
<%
} 
//坐庄中
else if(pk.getMark() == WGamePKBean.PK_MARK_BKING){
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(pk.getLeftUserId());
    
%>
庄家:<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%><br/>
状态:等待挑战<br/>
赌注:<%=pk.getWager()%>乐币<br/>
<%
}
//PK中
else if(pk.getMark() == WGamePKBean.PK_MARK_PKING){	
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(pk.getLeftUserId());
    UserStatusBean usRight=UserInfoUtil.getUserStatus(pk.getRightUserId());
%>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%>vs<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%><br/>
状态:等待<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%>应战<br/>
赌注:<%=pk.getWager()%>乐币<br/>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%><br/>
<%
}
//结束
else if(pk.getMark() == WGamePKBean.PK_MARK_END){	
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(pk.getLeftUserId());
    UserStatusBean usRight=UserInfoUtil.getUserStatus(pk.getRightUserId());
    
String a = pk.getLeftCardsStr();
String b = pk.getRightCardsStr();
%>
<%--
<%
//liq 2007.3.26增加胜利或者失败的图片赢家:
if(pk.getWinUserId() == pk.getLeftUserId()){%><%=ResultPicture.getPicture(2)%><%}
else if(pk.getWinUserId() == pk.getRightUserId()){%><%=ResultPicture.getPicture(1)%>
<%}else{%><%=ResultPicture.getPicture(0)%><%}
//liq 2007.3.26增加胜利或者失败的图片
%><br/>
--%>
根据射门规则射门方失败输掉两倍乐币<br/>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%>vs<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%><br/>
状态:本局已结束<br/>
赢家:<%if(pk.getWinUserId() == pk.getLeftUserId()){%><%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%><%}else if(pk.getWinUserId() == pk.getRightUserId()){%><%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%><%}else{%>平手<%}%><br/>
赌注:<%=pk.getWager()%>乐币<br/>
<%--<%if(pk.getWinUserId() == pk.getRightUserId() && pk.getFlag()==1){%>*赌神的骰子*生效，使用次数减一,您不用付输掉的钱！<br/><%}%>--%>
射门与扑救结果:<br/>
<%
	if(b != null){
%>
<img src="<%=("/wgamepk/football/img/" + a + "-" + b + ".gif")%>" alt="射门<%=action.getName(a)%>vs扑救<%=action.getName(b)%>"/><br/>
<%
    }
    else {
    	//action.incLoginUserGamePoint(pk.getWager());
%>
超时没应战.因对方超时而获胜只获得100乐币.<br/>
<%
	}
int enemyId;
if(loginUser.getId() == pk.getLeftUserId()){
	enemyId = pk.getRightUserId();
} else {
	enemyId = pk.getLeftUserId();
}
%>
<a href="/wgamepk/football/pkStart.jsp?userId=<%=enemyId%>">邀请对方再PK一局</a><br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamepk/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>