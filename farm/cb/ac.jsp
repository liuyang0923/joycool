<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BattleAction action = new BattleAction(request);
action.attackCreature();
FarmUserBean farmUser = action.getUser();
BattleStatus bs = farmUser.getCurStat();
List targetList = farmUser.getTargetList();
int[] recentSkill = (int[])action.getAttribute2("recentSkill");
int skillId = action.getParameterInt("skill");
if(recentSkill == null) {
	recentSkill = new int[4];
	action.setAttribute2("recentSkill", recentSkill);
}
int iter1 = 0;
for(;iter1 < recentSkill.length-1;iter1++) if(skillId==recentSkill[iter1]) break;
for(;iter1 > 0;iter1--) recentSkill[iter1] = recentSkill[iter1-1];
recentSkill[0] = skillId;

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="战斗">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
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
<%}}%>
<%}%><%if(targetList.size()>0){if(recentSkill!=null)for(int i=0;i<recentSkill.length;i++){
int iid = recentSkill[i];
if(iid==0){%><a href="ac.jsp">攻击</a><%
}else{
FarmSkillBean bean = action.world.getSkill(iid);
if(bean==null)continue;
%><a href="ac.jsp?skill=<%=iid%>"><%=bean.getName()%></a>
<%}%>|<%}%><a href="skills.jsp">技能</a>|<%}%><a href="cb.jsp">返回</a><br/>
人物状态:<%=farmUser.hp%>/<%=farmUser.mp%>/<%=farmUser.sp%><br/>
===战斗日志===<br/>
<%if(action.getAttribute2("hide_log")==null){%><%=farmUser.log.getLogString(5)%><%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>