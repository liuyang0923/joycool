<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(loginUser==null){%><img src="img/logo.gif" alt="logo"/><br/><%}else{%><%=loginUser.showImg("img/logo.gif")%><%}%>
乐酷浮生记<br/>
年纪轻轻就决定出来闯荡？从做小生意开始吧！<br/>
挣钱后可别忘了尽快去邮局还离家时欠村长的高利贷哦，利息每天10％!<br/>
出门的艰辛与机遇是并存的，未知的经历等待着您。开始体验这痛并快乐的旅程吧!<br/>
<a href="/Column.do?columnId=9056">查看游戏说明</a><br/>
<a href="/Column.do?columnId=9080">查看攻略心得</a><br/>
~~~~~~~~~~~~~~<br/>
<a href="/fs/index.jsp">继续游戏</a>(如果之前的游戏没有结束，点击可以继续)<br/><br/>
切换游戏模式：（换游戏模式会丢失之前的游戏进度！）<br/>
提示：1、新版3种游戏时间：25、30、40天，在“选择游戏模式”里随你挑，每种都有独立排行榜。<br/>
2、只要游戏里时间超过3天都会自动保存，无论掉线、退出网络、还是选择休息、玩别的，都可以随时随地继续游戏。<br/>
3、游戏结束时，赚到的钱会按比例兑换成乐币哦！<br/>
<a href="/fs/start.jsp?type=0">标准模式(40天)</a>-
<a href="/fs/top.jsp?type=0">排行榜</a><br/>
<a href="/fs/start.jsp?type=1">短篇模式(30天)</a>-
<a href="/fs/top.jsp?type=1">排行榜</a><br/>
<a href="/fs/start.jsp?type=2">快速模式(25天)</a>-
<a href="/fs/top.jsp?type=2">排行榜</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>