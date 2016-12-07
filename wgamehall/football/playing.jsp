<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
FootballAction action = new FootballAction(request);
action.playing(request);
WGameHallBean football = (WGameHallBean) request.getAttribute("football");
FootballDataBean data = (FootballDataBean) request.getAttribute("data");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(football != null){    
	if(football.getLeftUserId() == loginUser.getId()){
		toUserId = football.getRightUserId();
	} else {
		toUserId = football.getLeftUserId();
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
if(football == null){
%>
<card title="点球决战">
<p align="center">点球决战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
该局已被取消<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
/**
 * 等待中。
 */
else if(football.getMark() == HallBean.GS_WAITING){	
	String url = ("/wgamehall/football/playing.jsp?gameId=" + football.getId());
    String cancleUrl = ("/wgamehall/football/cancelInvitation.jsp?gameId=" + football.getId());	
%>
<card title="点球决战" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">点球决战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
	String condition = (String) request.getAttribute("condition");
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(football.getLeftUserId());
%>
等待<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(football.getRightNickname())%>响应,还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=cancleUrl%>">取消</a>|<a href="<%=chatUrl%>">发言</a><br/>
<a href="<%=viewUrl%>">刺探对手底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
//等待代码结束
/**
 * 游戏中
 */
else if(football.getMark() == HallBean.GS_PLAYING){
	String url = ("/wgamehall/football/playing.jsp?gameId=" + football.getId());	
	String giveUpUrl = ("/wgamehall/football/giveUp.jsp?gameId=" + football.getId());	
	String kickUrl = ("/wgamehall/football/kick.jsp?gameId=" + football.getId());
	String saveUrl = ("/wgamehall/football/save.jsp?gameId=" + football.getId());
	String active = (String) request.getAttribute("active");
	String result = (String) request.getAttribute("result");
	String tip = (String) request.getAttribute("tip");
	int currentRound = ((Integer) request.getAttribute("currentRound")).intValue();
	String currentSit = (String) request.getAttribute("currentSit");
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	//轮到自己操作
	if("1".equals(active)){
%>
<card title="点球决战">
<p align="center">点球决战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
	    //射门
	    if("kick".equals(result)){
			//显示上次扑救
			if(data.getLastKick() != null){
%>
<img src="img/<%=data.getLastKick()%>-<%=data.getLastSave()%>.gif" alt="<%=data.getLastKick()%>-<%=data.getLastSave()%>"/><br/>
<%
			}
%>
<%=tip%><br/>
<img src="img/football.gif" alt="射门"/><br/>
<a href="<%=(kickUrl + "&amp;position=l")%>">左</a>|<a href="<%=(kickUrl + "&amp;position=m")%>">中</a>|<a href="<%=(kickUrl + "&amp;position=r")%>">右</a>|<a href="<%=giveUpUrl%>">认输</a><br/>
<%
		}
        //守门
        else if("save".equals(result)){
			//显示上次扑救
			if(data.getHistoryKick() != null){
%>
<img src="img/<%=data.getHistoryKick()%>-<%=data.getHistorySave()%>.gif" alt="<%=data.getHistoryKick()%>-<%=data.getHistorySave()%>"/><br/>
<%
			}
%>
<%=tip%><br/>
<img src="img/shemen.gif" alt="守门"/><br/>
<a href="<%=(saveUrl + "&amp;position=l")%>">左</a>|<a href="<%=(saveUrl + "&amp;position=m")%>">中</a>|<a href="<%=(saveUrl + "&amp;position=r")%>">右</a>|<a href="<%=giveUpUrl%>">认输</a><br/>
<%
		}
%>
您还有<%=leftSeconds%>秒决定.当前第<%=currentRound%>轮.比分<%=currentSit%>:<br/>
<%
	    int[] leftResults = data.getLeftResults();
        int[] rightResults = data.getRightResults();
		int i;
		for(i = 0; i < leftResults.length; i ++){
%>
<%if(leftResults[i] == 0){%>O<%}else if(leftResults[i] == 1){%>V<%}else if(leftResults[i] == 2){%>X<%}%>
<%
		}
%>
<br/>
<%
	   for(i = 0; i < rightResults.length; i ++){
%>
<%if(rightResults[i] == 0){%>O<%}else if(rightResults[i] == 1){%>V<%}else if(rightResults[i] == 2){%>X<%}%>
<%
		}
%>
<br/>
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
    //轮到对方操作
	else {
%>
<card title="点球决战" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">点球决战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
	    //轮到对方扑救
        if("save".equals(result)){
%>
<%=tip%><br/>
<img src="img/<%=data.getLastKick()%>.gif" alt="<%=data.getLastKick()%>"/><br/>
<%
		}
        //轮到对方射门
		else if("kick".equals(result)){
			//显示上次扑救
			if(data.getLastKick() != null){
%>
<img src="img/<%=data.getLastKick()%>-<%=data.getLastSave()%>.gif" alt="<%=data.getLastKick()%>-<%=data.getLastSave()%>"/><br/>
<%
			}
%>
<%=tip%><br/>
<img src="img/shemen.gif" alt="射门"/><br/>
<%
		}
%>
当前第<%=currentRound%>轮,比分<%=currentSit%>:<br/>
<%
	    int[] leftResults = data.getLeftResults();
        int[] rightResults = data.getRightResults();
		int i;
		for(i = 0; i < leftResults.length; i ++){
%>
<%if(leftResults[i] == 0){%>O<%}else if(leftResults[i] == 1){%>V<%}else if(leftResults[i] == 2){%>X<%}%>
<%
		}
%>
<br/>
<%
	   for(i = 0; i < rightResults.length; i ++){
%>
<%if(rightResults[i] == 0){%>O<%}else if(rightResults[i] == 1){%>V<%}else if(rightResults[i] == 2){%>X<%}%>
<%
		}
%>
<br/>
还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">刺探</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
}
//游戏已经结束
else if(football.getMark() == HallBean.GS_END){
	String result = (String) request.getAttribute("result");
	int inviteUserId = ((Integer) request.getAttribute("inviteUserId")).intValue();
	//UserStatusBean us = action.getUserStatus();
	UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%>
<card title="点球决战">
<p align="center">点球决战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String temp=null;
temp=StringUtil.toWml(football.getResult());
UserStatusBean leftUs=UserInfoUtil.getUserStatus(football.getLeftUserId());
UserStatusBean rightUs=UserInfoUtil.getUserStatus(football.getRightUserId());
if(football.getResult().indexOf(football.getLeftNickname())!=-1){
	if(leftUs!=null)
		temp=leftUs.getHatShow()+StringUtil.toWml(football.getResult());
}
else if(football.getResult().indexOf(football.getRightNickname())!=-1){
	if(rightUs!=null)
		temp=rightUs.getHatShow()+StringUtil.toWml(football.getResult());
}else{
}
	//赢了
    if("win".equals(result)){		
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
	} else {
%>
<%=ResultPicture.getPicture(0)%>
<%=temp%>,您和对方打平了.您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
    }
%>
<a href="/wgamehall/football/invite.jsp?userId=<%=inviteUserId%>">继续</a>|
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">刺探</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>