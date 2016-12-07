<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.viewWGamePK(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBean pk = (WGamePKBean) request.getAttribute("pk");
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
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
	UserStatusBean usLeft=null;
    usLeft=UserInfoUtil.getUserStatus(pk.getLeftUserId());
%>
庄家:<%if(usLeft!=null){%><%=usLeft.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%><br/>
状态:等待挑战<br/>
赌注:<%=pk.getWager()%>乐币<br/>
<%
}
//PK中
else if(pk.getMark() == WGamePKBean.PK_MARK_PKING){	
	UserStatusBean usLeft=null;
	
    usLeft=UserInfoUtil.getUserStatus(pk.getLeftUserId());
    UserStatusBean usRight=UserInfoUtil.getUserStatus(pk.getRightUserId());
%>
<%if(usLeft!=null){%><%=usLeft.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%>
vs<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%><br/>
状态:等待<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%>应战<br/>
赌注:<%=pk.getWager()%>乐币<br/>
<%if(usLeft!=null){%><%=usLeft.getHatShow()%><%}%><%=pk.getLeftNickname()%><br/>
<%
}
//结束
else if(pk.getMark() == WGamePKBean.PK_MARK_END){	
	UserStatusBean usLeft=null;
    usLeft=UserInfoUtil.getUserStatus(pk.getLeftUserId());
    UserStatusBean usRight=UserInfoUtil.getUserStatus(pk.getRightUserId());
Object[] leftGong3s = pk.getLeftGong3s().toArray();
Object[] rightGong3s = null;
%>
<%--<%
//liq 2007.3.26增加胜利或者失败的图片赢家:
if(pk.getWinUserId() == pk.getLeftUserId()){%><%=ResultPicture.getPicture(2)%><%}
else if(pk.getWinUserId() == pk.getRightUserId()){%><%=ResultPicture.getPicture(1)%>
<%}else{%><%=ResultPicture.getPicture(0)%><%}
//liq 2007.3.26增加胜利或者失败的图片
%><br/>--%>


<%if(usLeft!=null){%><%=usLeft.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%>vs<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%><br/>
状态:本局已结束<br/>
赢家:<%if(pk.getWinUserId() == pk.getLeftUserId()){%><%if(usLeft!=null){%><%=usLeft.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%><%}
else if(pk.getWinUserId() == pk.getRightUserId()){%><%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%><%}else{%>平手<%}%><br/>
赌注:<%=pk.getWager()%>乐币<br/>
<%if(usLeft!=null){%><%=usLeft.getHatShow()%><%}%><%=StringUtil.toWml(pk.getLeftNickname())%>的牌:<br/>
<img src="<%=((CardBean)leftGong3s[0]).getPicUrl()%>" alt="<%=((CardBean)leftGong3s[0]).getValue()%>"/><img src="<%=((CardBean)leftGong3s[1]).getPicUrl()%>" alt="<%=((CardBean)leftGong3s[1]).getValue()%>"/><img src="<%=((CardBean)leftGong3s[2]).getPicUrl()%>" alt="<%=((CardBean)leftGong3s[2]).getValue()%>"/><br/>
<%if(usRight!=null){%><%=usRight.getHatShow()%><%}%><%=StringUtil.toWml(pk.getRightNickname())%>的牌:<br/>
<%
	if(pk.getRightGong3s() != null){
	    rightGong3s = pk.getRightGong3s().toArray();
%>
<img src="<%=((CardBean)rightGong3s[0]).getPicUrl()%>" alt="<%=((CardBean)rightGong3s[0]).getValue()%>"/><img src="<%=((CardBean)rightGong3s[1]).getPicUrl()%>" alt="<%=((CardBean)rightGong3s[1]).getValue()%>"/><img src="<%=((CardBean)rightGong3s[2]).getPicUrl()%>" alt="<%=((CardBean)rightGong3s[2]).getValue()%>"/><br/>
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
<a href="/wgamepk/3gong/pkStart.jsp?userId=<%=enemyId%>">邀请对方再PK一局</a><br/>
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