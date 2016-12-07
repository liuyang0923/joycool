<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BattleAction action = new BattleAction(request);
int act = action.getParameterInt("a");
if(act==4){
int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
if(hour<20||hour>21){%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
现在不是pvp时间,无法攻击玩家.<br/>
<a href="../help/pvp.jsp">阅读pvp说明</a><br/>
<a href="../map.jsp">返回场景</a>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml><%
return;}
}
action.combat();

int hideLog = action.getParameterIntS("hide");
if(hideLog==0){
action.removeAttribute2("hide_log");
}else if(hideLog==1){
action.setAttribute2("hide_log","");
}else{
if(action.getAttribute2("hide_log")==null)hideLog=0;else hideLog=1;
}

FarmUserBean farmUser = action.getUser();
MapNodeBean node = action.getUserNode();
BattleStatus bs = farmUser.getCurStat();
List targetList = farmUser.getTargetList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(farmUser.isAlive()){%>
<%if(targetList.size()>0){%>
<a href="ac.jsp">攻击</a>|
<a href="skills.jsp">技能</a>|
<a href="flee.jsp">逃跑</a>|
<%}else{%>
<a href="rest.jsp">休息</a>|
<%}%>
<a href="../user/items.jsp">物品</a>|
<%if(node.getDropCount()>0){%><a href="../drops.jsp">掉落</a>|<%}%>
<a href="cb.jsp">刷新</a><br/>
<%}else{%>
人物已经死亡!!!<br/>
<%}%>
<%
if(targetList.size()>0){%>
<%
for(int i = 0;i < targetList.size();i++){
Object obj = targetList.get(i);%>
<%if(i>0){%><a href="cb.jsp?a=2&amp;id=<%=i%>">↑</a><%}else{%>目标:<%}%>
<%if(obj instanceof CreatureBean){
CreatureBean creature = (CreatureBean)obj;
%>
<a href="../npc/cr.jsp?id=<%=creature.getGid()%>"><%=creature.getName()%></a><%if(creature.getTemplate().isFlagNoHp()){%>??<%}else{%><%=creature.getHp()%><%}%>(<%=creature.getHp()*100/creature.getHpMax()%>%)<br/>

<%}else if(obj instanceof FarmUserBean){
FarmUserBean user = (FarmUserBean)obj;
%>
<a href="../user/info.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a><%=user.getHp()%>(<%=user.hp*100/user.getCurStat().hp%>%)<br/>
<%}%>
<%}%>
<%}%>
==自身状态==<br/>
血:<%=farmUser.hp%>(<%=(farmUser.hp*100/bs.hp)%>%)<br/>
气力:<%=farmUser.mp%>(<%=(farmUser.mp*100/bs.mp)%>%)<br/>
体力:<%=farmUser.sp%>(<%=(farmUser.sp*100/bs.sp)%>%)<br/>
<%if(farmUser.log.size()>0){%>
==<a href="log.jsp">详细日志</a><%if(hideLog==0){
%>(<a href="cb.jsp?hide=1">隐藏</a>)<%}else{
%>(<a href="cb.jsp?hide=0">显示</a>)<%}%>==<br/>
<%if(hideLog==0){%><%=farmUser.log.getLogString(5)%><%}%>
<%}%>
<a href="../map.jsp">返回场景</a>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>