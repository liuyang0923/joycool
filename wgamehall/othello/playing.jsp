<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.ChatDataAction"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
OthelloAction action = new OthelloAction(request);
action.playing(request);
WGameHallBean othello = (WGameHallBean) request.getAttribute("othello");
OthelloDataBean data = (OthelloDataBean) request.getAttribute("data");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(othello != null){    
	if(othello.getLeftUserId() == loginUser.getId()){
		toUserId = othello.getRightUserId();
	} else {
		toUserId = othello.getLeftUserId();
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
if(othello == null){
%>
<card title="黑白棋">
<p align="center">黑白棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
该局已结束,可能是对方拒绝您的邀请<br/>
<br/>
<a href="/wgamehall/othello/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
/**
 * 等待中。
 */
else if(othello.getMark() == HallBean.GS_WAITING){
	String url = ("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
    String cancleUrl = ("/wgamehall/othello/cancelInvitation.jsp?gameId=" + othello.getId());
%>
<card title="黑白棋" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">黑白棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
	String condition = (String) request.getAttribute("condition");	
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	UserStatusBean usTemp=null;
	usTemp=UserInfoUtil.getUserStatus(othello.getLeftUserId());
%>
等待<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(othello.getRightNickname())%>响应,还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=cancleUrl%>">取消</a>|<a href="<%=chatUrl%>">发言</a><br/>
<a href="<%=viewUrl%>">刺探她的底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/othello/index.jsp">返回上一级</a><br/>
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
else if(othello.getMark() == HallBean.GS_PLAYING){
	String url = ("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
	String imgUrl = response.encodeURL("/wgamehall/othello/getImage.jsp?gameId=" + othello.getId());
	String sueForPeaceUrl = ("/wgamehall/othello/sueForPeace.jsp?gameId=" + othello.getId());
	String giveUpUrl = ("/wgamehall/othello/giveUp.jsp?gameId=" + othello.getId());
	String agreeToPeaceUrl = ("/wgamehall/othello/agreeToPeace.jsp?gameId=" + othello.getId());
	String denyPeaceUrl = ("/wgamehall/othello/denyPeace.jsp?gameId=" + othello.getId());
	String moveUrl = ("/wgamehall/othello/move.jsp?gameId=" + othello.getId());
	String active = (String) request.getAttribute("active");
	String result = (String) request.getAttribute("result");
	String tip = (String) request.getAttribute("tip");
	String canSueForPeace = (String) request.getAttribute("canSueForPeace");
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	//上一次是对方操作
	if("1".equals(active)){
%>
<card title="黑白棋">
<p align="center">黑白棋</p>
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
您还有<%=leftSeconds%>秒决定<br/>
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/othello/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
    //上一次是己方操作
	else {
%>
<card title="黑白棋" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">黑白棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<img src="<%=imgUrl%>" alt="棋盘"/><br/>
等待对手响应中,还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/othello/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
}
//游戏已经结束
else if(othello.getMark() == HallBean.GS_END){
	String result = (String) request.getAttribute("result");
	int inviteUserId = ((Integer) request.getAttribute("inviteUserId")).intValue();
	//UserStatusBean us = action.getUserStatus();
	UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%>
<card title="黑白棋">
<p align="center">黑白棋</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String temp=null;
temp=StringUtil.toWml(othello.getResult());
UserStatusBean leftUs=UserInfoUtil.getUserStatus(othello.getLeftUserId());
UserStatusBean rightUs=UserInfoUtil.getUserStatus(othello.getRightUserId());
if(othello.getResult().indexOf(othello.getLeftNickname())!=-1){
	if(leftUs!=null)
	temp=leftUs.getHatShow()+StringUtil.toWml(othello.getResult());
}
else if(othello.getResult().indexOf(othello.getRightNickname())!=-1){
	if(rightUs!=null)
		temp=rightUs.getHatShow()+StringUtil.toWml(othello.getResult());
}else{

}
	//赢了
    if("win".equals(result)){
		//加入谣言
//	    ChatDataAction.addRumor("<a href=\"" + ("/wgamehall/index.jsp") + "\" title=\"go\">智力游戏</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamehall/othello/onlineList.jsp") + "\" title=\"go\">黑白棋</a>]赢了5000个乐币");
%>
<%=ResultPicture.getPicture(2)%>
<%=temp%>,您赢了5000个乐币.您现在还有<%=us.getGamePoint()%>个乐币<br/>
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
<a href="/wgamehall/othello/invite.jsp?userId=<%=inviteUserId%>">继续</a>|
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/othello/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>