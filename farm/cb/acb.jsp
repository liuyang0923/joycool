<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BattleAction action = new BattleAction(request);
action.attackCreature();
FarmUserBean farmUser = action.getUser();
MapNodeBean node = action.getUserNode();
BattleStatus bs = farmUser.getCurStat();
List targetList = farmUser.getTargetList();
String returnURL = "acb.jsp";
if(targetList.size()==0||farmUser.hp < bs.hp / 5)		// 血少于五分之一或者没有目标则暂停
	returnURL = "cb.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="战斗" ontimer="<%=response.encodeURL(returnURL)%>">
<timer value="<%=bs.attackInterval/100%>"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<%if(targetList.size()==0){%>目标死亡,自动攻击中止<br/><%}else if(farmUser.hp < bs.hp / 5){%>血不足,自动攻击中止<br/><%}else{%>
<%if(farmUser.isAlive()){%>
<a href="cb.jsp">停止</a>|
<a href="skills.jsp">技能</a>|
<a href="flee.jsp">逃跑</a>|
<a href="../user/items.jsp">物品</a><br/>
<%}else{%>
人物已经死亡!!!<br/>
<%}%>
<%
if(targetList.size()>0){%>
<%
for(int i = 0;i < targetList.size();i++){
Object obj = targetList.get(i);%>

<%if(obj instanceof CreatureBean){
CreatureBean creature = (CreatureBean)obj;
%>
<%=creature.getName()%><%if(creature.getTemplate().isFlagNoHp()){%>??<%}else{%><%=creature.getHp()%><%}%>(<%=creature.getHp()*100/creature.getHpMax()%>%)<br/>
<%}else if(obj instanceof FarmUserBean){
FarmUserBean user = (FarmUserBean)obj;
%>
<%=user.getNameWml()%><%=user.getHp()%>(<%=user.hp*100/user.getCurStat().hp%>%)<br/>
<%}%>
<%}%>
<%}%>
<%}%>
人物状态:<%=farmUser.hp%>/<%=farmUser.mp%>/<%=farmUser.sp%><br/>
<%if(farmUser.log.size()>0){%>
==<a href="log.jsp">详细日志</a>==<br/>
<%if(action.getAttribute2("hide_log")==null){%><%=farmUser.log.getLogString(5)%><%}%>
<%}%>
<a href="../map.jsp">返回场景</a>
</p>
</card>
</wml>