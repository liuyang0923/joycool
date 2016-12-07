<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
action.viewInvitation(request);
String result = (String) request.getAttribute("result");
WGameHallBean jinhua = (WGameHallBean) request.getAttribute("jinhua");
JinhuaDataBean data = (JinhuaDataBean) request.getAttribute("data");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="查看玩家邀请">
<p align="center">查看玩家邀请</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//失败
if("failure".equals(result)){
%>
对方已经取消邀请<br/>
<%
}
//应邀或者取消
else if(jinhua != null){
	UserStatusBean us=null;
	us=UserInfoUtil.getUserStatus(jinhua.getLeftUserId());
%>
<%if(us!=null){%><%=us.getHatShow()%><%}%><%=StringUtil.toWml(jinhua.getLeftNickname())%>想跟您玩砸金花(<%=data.getLevel_bottom()[data.getStakeLevel()]%>底<%=data.getLevel_stake()[data.getStakeLevel()]%>赌注/局),他现在有<%=enemyUs.getGamePoint()%>个乐币.<br/>
<a href="/wgamehall/jinhua/acceptInvitation.jsp?gameId=<%=jinhua.getId()%>">接受邀请</a>|<a href="/wgamehall/jinhua/denyInvitation.jsp?gameId=<%=jinhua.getId()%>">拒绝邀请</a><br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/jinhua/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>