<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightUserBean" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.bkStart(request);
long money = StringUtil.toLong((String) request.getAttribute("money"));
int groupId = StringUtil.toInt((String) request.getAttribute("groupId"));
Vector fightList = (Vector)request.getAttribute("fightList");
int count=0;
if(fightList!=null){
count = fightList.size();
}
WGameFightUserBean userBean =null;
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷街霸" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够10个乐币
if("notEnoughMoney".equals(result)){
%>
<%=tip%><br/>
<%
} else if("hasBk".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamefight/index.jsp">我的游戏</a><br/>
<%
} else if("notGroup".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamefight/index.jsp">我的游戏</a><br/>
<%
} else if("continue".equals(result)){
%>


下注(至少100个乐币您还有<%=money%>个乐币)<br/>
主题(15字以下)<br/>
<input type="text" name="content" maxlength="15" value="KO"/><br/>
金额<br/>
<input type="text" name="wager" format="*N" maxlength="10" value="100"/><br/>
选择动作组：<br/><%if(count>0){
for(int i=0;i<count;i++){
userBean = (WGameFightUserBean)fightList.get(i);
%>
<anchor title="动作组">动作组<%=userBean.getGroupId()%>
    <go href="/wgamefight/bkDeal.jsp" method="post">
    <postfield name="wager" value="$wager"/>
    <postfield name="content" value="$content"/>
    <postfield name="groupId" value="<%=userBean.getGroupId()%>"/>
    </go>
    </anchor><br/>
    <%}%><%}%>
    <%}%>
<a href="/wgamefight/index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛赌坊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>