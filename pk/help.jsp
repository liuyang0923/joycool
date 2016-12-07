<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action=new PKAction(request);
UserBean loginUser = action.getLoginUser();
if(loginUser!=null)
	action.help();
UserBean user = (UserBean)request.getAttribute("user");
PKUserBean pkUser = (PKUserBean)request.getAttribute("pkUser");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<img src="../img/pk/help.gif" alt="logo"/><br/>
<%=BaseAction.getTop(request, response)%>
打怪、升级、练功、杀人，欢迎进入乐酷自己的江湖！<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<%if(pkUser!=null && pkUser.getOldKCount()>0){%>
昨日杀人王：  
<%=StringUtil.toWml(user.getNickName())%>
<%=pkUser.getOldKCount()%>条人命<br/>
<%}%>
<a href="/pk/pkTop.jsp">查看侠客秘境杀人榜</a><br/>
[必读]<a href="/pk/readme.jsp">详细游戏指南</a><br/>
<a href="help/placeIntro.jsp">地点介绍</a>:地点相当于游戏地图，是敌人出没、大家战斗的地方，越往后敌人越强。入口可以去任何地点。<br/>
<a href="help/roleIntro.jsp">个人属性</a>:每个用户拥有一个游戏角色，各种属性进入"我的状态"就可以查看。<br/>
<a href="help/shopIntro.jsp">商店介绍</a>:商人出售和回收游戏中的各种武器、装备、道具。<br/>
<a href="help/skillIntro.jsp">武功介绍</a>:外功在战斗中使用发挥作用，内功需要修炼，一直发挥作用。<br/>
<a href="help/equipIntro.jsp">装备介绍</a>:装备分武器和防具、佩饰三类，提升属性，可以购买和打怪获得。<br/>
<a href="help/bagIntro.jsp">行囊介绍</a>:每人拥有一个容量为15的行囊，装备、药品、任务道具都在里面。<br/>
<a href="help/missionIntro.jsp">任务介绍</a>:任务堂里的"乐酷老油条"提供不同的任务，完成之后在他那里提交任务，能获得任务奖励。<br/>
<a href="help/actionIntro.jsp">战斗介绍</a>:战斗时需要先选择怪物或其他人，然后选择要使用的主动技能，就可以攻击目标了。<br/>
<a href="help/homicideIntro.jsp">关于杀人</a>:系统每天会开放一定时间的人人战斗，可以进行对战!<br/>
====游戏动态====<br/>
1.匪首孙笑尔已经被多次击败。<br/>
2.目前大家疯狂练功中。<br/>
3.每天晚8-10点开放人人对战，血流成河。<br/>
~~~~~~~~~~~~~~<br/>
<a href="index.jsp">马上开始游戏</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>