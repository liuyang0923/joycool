<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
GoBangAction action = new GoBangAction(request);
action.playing(request);
WGameHallBean gobang = (WGameHallBean) request.getAttribute("gobang");
GoBangDataBean data = (GoBangDataBean) request.getAttribute("data");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(gobang != null){    
	if(gobang.getLeftUserId() == loginUser.getId()){
		toUserId = gobang.getRightUserId();
	} else {
		toUserId = gobang.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
/**
 * 已删除。
 */
if(gobang == null){
%>
<card title="五子棋">
<p align="center">五子棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
该局已被取消<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
/**
 * 等待中。
 */
else if(gobang.getMark() == HallBean.GS_WAITING){
	String url = ("/wgamehall/gobang/playing.jsp?gameId=" + gobang.getId());
    String cancleUrl = ("/wgamehall/gobang/cancelInvitation.jsp?gameId=" + gobang.getId());
%>
<card title="五子棋" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">五子棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
    //还在等待中	
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
UserStatusBean usTemp=null;
usTemp=UserInfoUtil.getUserStatus(gobang.getLeftUserId());
%>
等待<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(gobang.getRightNickname())%>响应中,还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=cancleUrl%>">取消</a>|<a href="<%=chatUrl%>">发言</a><br/>
<a href="<%=viewUrl%>">刺探对方底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
//等待代码结束
/**
 * 游戏中
 */
else if(gobang.getMark() == HallBean.GS_PLAYING){
	String url = ("/wgamehall/gobang/playing.jsp?gameId=" + gobang.getId());
	String imgUrl = response.encodeURL("/wgamehall/gobang/getImage.jsp?gameId=" + gobang.getId());
	String sueForPeaceUrl = ("/wgamehall/gobang/sueForPeace.jsp?gameId=" + gobang.getId());
	String giveUpUrl = ("/wgamehall/gobang/giveUp.jsp?gameId=" + gobang.getId());
	String agreeToPeaceUrl = ("/wgamehall/gobang/agreeToPeace.jsp?gameId=" + gobang.getId());
	String denyPeaceUrl = ("/wgamehall/gobang/denyPeace.jsp?gameId=" + gobang.getId());
	String moveUrl = ("/wgamehall/gobang/move.jsp?gameId=" + gobang.getId());
	String active = (String) request.getAttribute("active");
	String result = (String) request.getAttribute("result");
	String tip = (String) request.getAttribute("tip");
	String canSueForPeace = (String) request.getAttribute("canSueForPeace");
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	//上一次是对方操作
	if("1".equals(active)){
%>
<card title="五子棋">
<p align="center">五子棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<img src="<%=imgUrl%>" alt="棋盘"/><br/>
<%
	    //对方求和
	    if("sueForPeace".equals(result)){
%>
<a href="<%=agreeToPeaceUrl%>">同意和局</a>|<a href="<%=denyPeaceUrl%>">继续游戏</a><br/>
<%
		} else {
%>
填写下子位置(如HH不分大小写):<br/>
<input type="text" name="position" maxlength="2" value="HH" format="2A" title="下子"/><br/>
<anchor title="确定">下子
    <go href="<%=moveUrl%>" method="post">
    <postfield name="position" value="$position"/>
    </go>
</anchor><br/>
<%
	      if("true".equals(canSueForPeace)){
%>
<a href="<%=sueForPeaceUrl%>">求和</a><br/>
<%
          }
%>
<a href="<%=giveUpUrl%>">认输</a><br/>
<%
        }
%>
您还有<%=leftSeconds%>秒决定.当前第<%=data.getMoveCount()%>手<br/>
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
    //上一次是己方操作
	else {
%>
<card title="五子棋" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">五子棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<img src="<%=imgUrl%>" alt="棋盘"/><br/>
等待对手响应中,还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
}
//游戏已经结束
else if(gobang.getMark() == HallBean.GS_END){
	String result = (String) request.getAttribute("result");
	int inviteUserId = ((Integer) request.getAttribute("inviteUserId")).intValue();
	//UserStatusBean us = action.getUserStatus();
	UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%>
<card title="五子棋">
<p align="center">五子棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String temp=null;
temp=StringUtil.toWml(gobang.getResult());
UserStatusBean leftUs=UserInfoUtil.getUserStatus(gobang.getLeftUserId());
UserStatusBean rightUs=UserInfoUtil.getUserStatus(gobang.getRightUserId());
if(gobang.getResult().indexOf(gobang.getLeftNickname())!=-1){
	if(leftUs!=null)
		temp=leftUs.getHatShow()+StringUtil.toWml(gobang.getResult());
}
else if(gobang.getResult().indexOf(gobang.getRightNickname())!=-1){
	if(rightUs!=null)
		temp=rightUs.getHatShow()+StringUtil.toWml(gobang.getResult());
}else{
	
}
	
	//赢了
	
    if("win".equals(result)){	
    	
    	
%>
<%=ResultPicture.getPicture(2)%>
<%=temp%>,您赢了5000(如果是对手超时赢500)个乐币.您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	}
    //输了
	else if("lose".equals(result)){
%>
<%=ResultPicture.getPicture(1)%>
<%=temp%>,您输了.您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} 
    //打平
	else {
%>
<%=ResultPicture.getPicture(0)%>
<%=temp%>,您和对手打平了.您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	}
%>
<a href="/wgamehall/gobang/invite.jsp?userId=<%=inviteUserId%>">继续</a>|
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/gobang/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>