<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
action.playing(request);
WGameHallBean jinhua = (WGameHallBean) request.getAttribute("jinhua");
JinhuaDataBean data = (JinhuaDataBean) request.getAttribute("data");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);


int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(jinhua != null){    
	if(jinhua.getLeftUserId() == loginUser.getId()){
		toUserId = jinhua.getRightUserId();
	} else {
		toUserId = jinhua.getLeftUserId();
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
if(jinhua == null){
%>
<card title="砸金花">
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
该局已被取消<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<br/>
<a href="/wgamehall/jinhua/start.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
/**
 * 等待中。
 */
else if(jinhua.getMark() == HallBean.GS_WAITING){	
	String url = ("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
    String cancleUrl = ("/wgamehall/jinhua/cancelInvitation.jsp?gameId=" + jinhua.getId());	
%>
<card title="砸金花" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
	String condition = (String) request.getAttribute("condition");
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(jinhua.getLeftUserId());
%>
等待<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(jinhua.getRightNickname())%>响应,还有<%=leftSeconds%>秒超时<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=cancleUrl%>">取消</a>|<a href="<%=chatUrl%>">发言</a><br/>
<a href="<%=viewUrl%>">刺探对手底细</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/jinhua/start.jsp">返回上一级</a><br/>
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
else if(jinhua.getMark() == HallBean.GS_PLAYING){
	String url = ("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());		
	String active = (String) request.getAttribute("active");
	String result = (String) request.getAttribute("result");
	String tip = (String) request.getAttribute("tip");
	int leftSeconds = ((Integer) request.getAttribute("leftSeconds")).intValue();
	UserStatusBean usTemp=null;
	//轮到自己操作
	if("1".equals(active)){
%>
<card title="砸金花">
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(jinhua != null){    
	if(jinhua.getLeftUserId() == loginUser.getId()){
%>
<img src="/wgame/cardImg/<%=data.getL_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getL_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getL_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getL_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您已下注<%=data.getLeftStake()%>乐币<br/>
总赌注<%=data.getTotalStake()%>乐币<br/>
<%usTemp=UserInfoUtil.getUserStatus(jinhua.getLeftUserId());%>
<%if(data.getLeftStake() > 0) {%>

<%
if(usTemp.getGamePoint() > data.getLevel_stake()[data.getStakeLevel()] ) {
%>
<a href="/wgamehall/jinhua/addstake.jsp?gameId=<%=jinhua.getId()%>">下注（<%=data.getLevel_stake()[data.getStakeLevel()]%>乐币）</a><br/>
<%};%>
<%
if(usTemp.getGamePoint() > data.getLevel_stake()[data.getStakeLevel()]*2 ) {
%>
<a href="/wgamehall/jinhua/finish.jsp?gameId=<%=jinhua.getId()%>">开牌（<%=data.getLevel_stake()[data.getStakeLevel()]*2%>乐币）</a><br/>
<%};}else{%>



<%
if(usTemp.getGamePoint() > (data.getLevel_stake()[data.getStakeLevel()]+data.getLevel_bottom()[data.getStakeLevel()]) ) {
%>
<a href="/wgamehall/jinhua/addstake.jsp?gameId=<%=jinhua.getId()%>">下注（<%=(data.getLevel_stake()[data.getStakeLevel()]+data.getLevel_bottom()[data.getStakeLevel()])%>乐币）</a><br/>
<%};%>
<%
if(usTemp.getGamePoint() > data.getLevel_stake()[data.getStakeLevel()]*2 ) {
%>
<a href="/wgamehall/jinhua/finish.jsp?gameId=<%=jinhua.getId()%>">开牌（<%=(data.getLevel_stake()[data.getStakeLevel()]*2+data.getLevel_bottom()[data.getStakeLevel()])%>乐币）</a><br/>
<%};%>

<%}%>

<%} else {
%>
<img src="/wgame/cardImg/<%=data.getR_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getR_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getR_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getR_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您已下注<%=data.getRightStake()%>乐币<br/>
总赌注<%=data.getTotalStake()%>乐币<br/>
<%usTemp=UserInfoUtil.getUserStatus(jinhua.getRightUserId());%>
<%if(data.getRightStake() > 0) {%>

<%
if(usTemp.getGamePoint() > data.getLevel_stake()[data.getStakeLevel()] ) {
%>
<a href="/wgamehall/jinhua/addstake.jsp?gameId=<%=jinhua.getId()%>">下注（<%=data.getLevel_stake()[data.getStakeLevel()]%>乐币）</a><br/>
<%};%>
<%
if(usTemp.getGamePoint() > data.getLevel_stake()[data.getStakeLevel()]*2 ) {
%>
<a href="/wgamehall/jinhua/finish.jsp?gameId=<%=jinhua.getId()%>">开牌（<%=data.getLevel_stake()[data.getStakeLevel()]*2%>乐币）</a><br/>
<%};}else{%>



<%
if(usTemp.getGamePoint() > (data.getLevel_stake()[data.getStakeLevel()]+data.getLevel_bottom()[data.getStakeLevel()]) ) {
%>
<a href="/wgamehall/jinhua/addstake.jsp?gameId=<%=jinhua.getId()%>">下注（<%=(data.getLevel_stake()[data.getStakeLevel()]+data.getLevel_bottom()[data.getStakeLevel()])%>乐币）</a><br/>
<%};%>
<%
if(usTemp.getGamePoint() > data.getLevel_stake()[data.getStakeLevel()]*2 ) {
%>
<a href="/wgamehall/jinhua/finish.jsp?gameId=<%=jinhua.getId()%>">开牌（<%=(data.getLevel_stake()[data.getStakeLevel()]*2+data.getLevel_bottom()[data.getStakeLevel()])%>乐币）</a><br/>
<%};%>

<%}%>


<%	}
}
%>



<a href="/wgamehall/jinhua/giveUp.jsp?gameId=<%=jinhua.getId()%>">逃跑（输光已下乐币）</a><br/>
您还有<%=leftSeconds%>秒超时<br/>
<br/>
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">对手底细</a><br/>
你有<%=usTemp.getGamePoint()%>乐币,经验<%=usTemp.getPoint()%><br/>
<a href="/wgamehall/jinhua/start.jsp">返回砸金花首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
    //轮到对方操作
	else if("0".equals(active)){
%>
<card title="砸金花" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%
if(jinhua != null){    
	if(jinhua.getLeftUserId() == loginUser.getId()){
%>
<img src="/wgame/cardImg/<%=data.getL_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getL_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getL_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getL_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您已下注<%=data.getLeftStake()%>乐币<br/>
总赌注<%=data.getTotalStake()%>乐币<br/>
<%
usTemp=UserInfoUtil.getUserStatus(jinhua.getLeftUserId());
	} else {
%>
<img src="/wgame/cardImg/<%=data.getR_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getR_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getR_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getR_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您已下注<%=data.getRightStake()%>乐币<br/>
总赌注<%=data.getTotalStake()%>乐币<br/>
<%
usTemp=UserInfoUtil.getUserStatus(jinhua.getRightUserId());
	}
}
%>


请等待对手行动<br/>
还有<%=leftSeconds%>秒超时<br/>
<br/>
<a href="<%=url%>">刷新</a>|<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">刺探</a><br/>
你有<%=usTemp.getGamePoint()%>乐币,经验<%=usTemp.getPoint()%><br/>
<a href="/wgamehall/jinhua/start.jsp">返回砸金花首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
}
//游戏已经结束
else if(jinhua.getMark() == HallBean.GS_END){
	String result = (String) request.getAttribute("result");
	int inviteUserId = ((Integer) request.getAttribute("inviteUserId")).intValue();
	//UserStatusBean us = action.getUserStatus();
	UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%>
<card title="砸金花">
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String temp=null;
temp=StringUtil.toWml(jinhua.getResult());
UserStatusBean leftUs=UserInfoUtil.getUserStatus(jinhua.getLeftUserId());
UserStatusBean rightUs=UserInfoUtil.getUserStatus(jinhua.getRightUserId());
if(jinhua.getResult().indexOf(jinhua.getLeftNickname())!=-1){
	if(leftUs!=null)
		temp=leftUs.getHatShow()+StringUtil.toWml(jinhua.getResult());
}
else if(jinhua.getResult().indexOf(jinhua.getRightNickname())!=-1){
	if(rightUs!=null)
		temp=rightUs.getHatShow()+StringUtil.toWml(jinhua.getResult());
}else{
}
	//赢了
    if("win".equals(result)){		
%>
<%=ResultPicture.getPicture(2)%>
<%=temp%>,
您赢了

<%
if(jinhua.getWinUserId() == jinhua.getLeftUserId())
{%>
<%=data.getRightStake()%>乐币
<%}else{%>
<%=data.getLeftStake()%>乐币
<%}


%>
<br/>

<%
if(jinhua != null){    
	if(jinhua.getRightUserId() == loginUser.getId()){
%>
对方的牌：<br/>
<img src="/wgame/cardImg/<%=data.getL_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getL_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getL_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getL_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您的牌：<br/>
<img src="/wgame/cardImg/<%=data.getR_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getR_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getR_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getR_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
<%
	} else {
%>
对方的牌：<br/>
<img src="/wgame/cardImg/<%=data.getR_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getR_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getR_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getR_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您的牌：<br/>
<img src="/wgame/cardImg/<%=data.getL_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getL_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getL_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getL_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
<%
	}
}
%>
<%
	}
    //输了
	else if("lose".equals(result)){
%>
<%=ResultPicture.getPicture(1)%>
<%=temp%>,您输了

<%
if(jinhua.getWinUserId() == jinhua.getLeftUserId())
{%>
<%=data.getRightStake()%>乐币
<%}else{%>
<%=data.getLeftStake()%>乐币
<%}%>

<br/>
<%
if(jinhua != null){    
	if(jinhua.getRightUserId() == loginUser.getId()){
%>
对方的牌：<br/>
<img src="/wgame/cardImg/<%=data.getL_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getL_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getL_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getL_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您的牌：<br/>
<img src="/wgame/cardImg/<%=data.getR_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getR_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getR_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getR_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
<%
	} else {
%>
对方的牌：<br/>
<img src="/wgame/cardImg/<%=data.getR_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getR_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getR_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getR_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getR_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
您的牌：<br/>
<img src="/wgame/cardImg/<%=data.getL_1_R()%>_<%=data.getL_1_L()+1%>.gif" alt="<%=data.getL_1_R()%>-<%=data.getL_1_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_2_R()%>_<%=data.getL_2_L()+1%>.gif" alt="<%=data.getL_2_R()%>-<%=data.getL_2_L()+1%>"/>
<img src="/wgame/cardImg/<%=data.getL_3_R()%>_<%=data.getL_3_L()+1%>.gif" alt="<%=data.getL_3_R()%>-<%=data.getL_3_L()+1%>"/><br/>
<%
	}
}
%>
<%
	} else {
%>
<%=ResultPicture.getPicture(0)%>
<%=temp%>,您和对方打平了.<br/>
<%
    }
%>






你有<%=us.getGamePoint()%>乐币,经验<%=us.getPoint()%><br/>
<a href="/wgamehall/jinhua/invite.jsp?userId=<%=inviteUserId%>">继续</a>|
<a href="<%=chatUrl%>">发言</a>|<a href="<%=viewUrl%>">刺探</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/jinhua/start.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>