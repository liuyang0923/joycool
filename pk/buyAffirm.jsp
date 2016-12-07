<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKEquipBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.pk.PKMedicineBean" %><%@ page import="net.joycool.wap.bean.pk.PKProtoBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserSkillBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBSkillBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.buyAffirm(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKProtoBean proto =(PKProtoBean)request.getAttribute("proto");
int type = StringUtil.toInt((String)request.getAttribute("type"));
String npcId=(String)request.getAttribute("npcId");
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//装备
if(type==PKAction.PK_EQUIP){
	PKEquipBean equip = (PKEquipBean)proto;
%>
	<%=equip.toDetail()%>
	<anchor title="确定">购买
	  <go href="buyResult.jsp" method="post">
	    <postfield name="type" value="<%=type%>"/>
	    <postfield name="npcId" value="<%=npcId%>"/>
	    <postfield name="id" value="<%=equip.getId()%>"/>
	  </go>
	</anchor> 
	| <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回</a><br/>
<%}//物品
else if(type==PKAction.PK_MEDICINE){
	PKMedicineBean medicine = (PKMedicineBean)proto;
%>
	<%=medicine.toDetail()%>
	
	<anchor title="确定">购买1个
	  <go href="buyResult.jsp" method="post">
	    <postfield name="type" value="<%=type%>"/>
	    <postfield name="npcId" value="<%=npcId%>"/>
	    <postfield name="count" value="1"/>
	    <postfield name="id" value="<%=medicine.getId()%>"/>
	  </go>
	</anchor> <br/>
	<anchor title="确定">购买5个
	  <go href="buyResult.jsp" method="post">
	    <postfield name="type" value="<%=type%>"/>
	    <postfield name="npcId" value="<%=npcId%>"/>
	    <postfield name="count" value="5"/>
	    <postfield name="id" value="<%=medicine.getId()%>"/>
	  </go>
	</anchor> <br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回</a><br/>
<%}//主动技能
else if(type==PKAction.PK_USER_SKILL){
	PKUserSkillBean userSkill = (PKUserSkillBean)proto;
%>
	<%=userSkill.toDetail()%>
	<anchor title="确定">购买
	  <go href="buyResult.jsp" method="post">
	    <postfield name="type" value="<%=type%>"/>
	    <postfield name="npcId" value="<%=npcId%>"/>
	    <postfield name="id" value="<%=userSkill.getId()%>"/>
	  </go>
	</anchor> 
	| <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回</a><br/>
<%}//被动技能
else if(type==PKAction.PK_USER_BSKILL){
	PKUserBSkillBean userBSkill = (PKUserBSkillBean)proto;
%>
	<%=userBSkill.toDetail()%>
	<anchor title="确定">购买
	  <go href="buyResult.jsp" method="post">
	    <postfield name="type" value="<%=type%>"/>
	    <postfield name="npcId" value="<%=npcId%>"/>
	    <postfield name="id" value="<%=userBSkill.getId()%>"/>
	  </go>
	</anchor> 
	| <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回</a><br/>
<%}%>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>