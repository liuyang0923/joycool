<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightUserBean" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.pkOut(request);
int groupId = StringUtil.toInt((String) request.getAttribute("groupId"));
int bkId = StringUtil.toInt((String) request.getAttribute("bkId"));
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
Vector fightList = (Vector)request.getAttribute("fightList");
int count=0;
if(fightList!=null){
count = fightList.size();
}
WGameFightUserBean userBean =null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="挑战">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够庄家的乐币数
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamefight/index.jsp">返回房间</a><br/>
<%
} else if("success".equals(result)){
WGameFightBean fight =(WGameFightBean) request.getAttribute("fight");
%>
<%=StringUtil.toWml(fight.getContent())%><br/>
庄家:<%=StringUtil.toWml(fight.getLeftNickname())%><br/>
金额:<%=StringUtil.bigNumberFormat(fight.getWager())%><br/>
选择动作组：<br/><%if(count>0){
for(int i=0;i<count;i++){
userBean = (WGameFightUserBean)fightList.get(i);
%>
<anchor title="动作组">动作组<%=userBean.getGroupId()%>
    <go href="/wgamefight/chlStart.jsp" method="post">
    <postfield name="bkId" value="<%=bkId%>"/>
    <postfield name="groupId" value="<%=userBean.getGroupId()%>"/>
    </go>
    </anchor><br/>
    <%}%><%}%>
<%}%>
<br/>
<a href="/wgamefight/index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>