<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);
RichUserBean richUser = action.getRichUser();
action.index();
long now = System.currentTimeMillis();
int option = action.getParameterInt("o");
int flag = (option == 0) ? 3 : 0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<img src="img/logo.gif" alt="logo"/><br/>
<%=BaseAction.getTop(request, response)%>
<%if(richUser != null && richUser.getWorldId()>=0){%><a href="start.jsp">继续游戏</a><br/><%}%>
<%if(option==1){%><a href="index.jsp">比赛场</a><%}else{%>比赛场<%}%>|
<%if(option==0){%><a href="index.jsp?o=1">练习场</a><%}else{%>练习场<%}%><br/>
<%
int cur = 1;
for(int i=0;i<RichAction.worlds.length;i++){ RichWorld world=action.worlds[i]; if(world.getFlag() == flag){%>
<a href="world.jsp?w=<%=i%>">第<%=cur%>房间<%%>(<%=world.getUserCount()%>人/<%=world.maxPlayer%>)<%if(world.isFull()){%>(满)<%}%></a><br/>
每局<%=world.getInterval()/60%>分钟,剩<%=world.timeLeft(now)/60%>分钟<br/>
<%cur++;}}%>
<%if(richUser != null && richUser.getStatus()==RichUserBean.STATUS_PLAY){%>
<%--<a href="index.jsp?r=1">重新开始游戏</a><br/>--%>
<%}%><br/>
<a href="glog.jsp">大富翁游戏记录</a><br/>
<a href="change.jsp">大富翁购物超市</a><br/>
<a href="bigmap.jsp">查看全地图</a><br/>
<a href="/Column.do?columnId=9967">查看详细帮助</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>