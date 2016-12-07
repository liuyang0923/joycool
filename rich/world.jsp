<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);
int w = action.getParameterInt("w");
RichWorld world=action.worlds[w];
world.prepare();		// 查看的时候可能世界没有准备
RichUserBean richUser = action.getRichUser();
long now = action.now;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(richUser.getWorldId()==w){%>
<a href="start.jsp?w=<%=w%>">继续游戏</a><br/>
<%}%>
下一局将在<%=world.timeLeft(now)/60%>分钟之后开始<br/>
<%if(world.isFull()){%>本局人满<br/>
<%}else if(richUser.getWorldId()<0){%>
<a href="start.jsp?w=<%=w%>">进入开始游戏</a><br/>
<%}%>
<%if(!world.isFlag(0)){%>这是练习场，无法获得大富翁券<br/><%}%>
现有<%=world.getUserCount()%>人，最多<%=world.getMaxPlayer()%>人<br/>
<% String[] names = RichUserBean.roleNames;
int[] roleUser = world.roleUser;
for(int i=0;i<names.length;i++){ 
if(roleUser[i]!=0){ %>
<%=names[i]%>(<a href="/chat/post.jsp?toUserId=<%= roleUser[i]%>"><%=StringUtil.toWml(UserInfoUtil.getUser(roleUser[i]).getNickName())%></a>)<br/>
<%}}%>
本局开始:<%=DateUtil.formatTime(world.getStartTime())%><br/>
下局开始:<%=DateUtil.formatTime(world.getStartTime()+world.getInterval() * 1000)%><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>