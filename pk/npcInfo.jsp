<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKNpcBean" %><%@ page import="net.joycool.wap.bean.pk.PKEquipBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.pk.PKMedicineBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserSkillBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBSkillBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.npcInfo(request);
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
PKNpcBean pkNpc =(PKNpcBean)request.getAttribute("pkNpc");
int type = StringUtil.toInt((String)request.getAttribute("type"));
String npcId=(String)request.getAttribute("npcId");
String  equip = pkNpc.getEquip();
String  medicine = pkNpc.getMedicine();
String  skill = pkNpc.getSkill();
String  bSkill = pkNpc.getBSkill();
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//装备
if(type==PKAction.PK_EQUIP){%>
	<%=pkNpc.getName()%>出售物品列表：<br/>
<%
	if(equip!=null && equip.length()>1){ 
	String[] things =equip.split(",") ;
	PKEquipBean pkEquip = null;
	for(int i=0;i<things.length;i++){
		String id = things[i];
		pkEquip=(PKEquipBean)action.getProto(type,StringUtil.toInt(id));
		if(pkEquip==null)continue;
		%>
		<%=i+1%>.
		<a href="/pk/buyAffirm.jsp?npcId=<%=npcId%>&amp;type=<%=type%>&amp;id=<%=pkEquip.getId()%>"><%=pkEquip.getName()%></a><br/>
		<%}
	}else{%>
	该<%=PKAction.NPCNAME%>没有物品售卖！<br/>
	<%}%>
	<%--
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=1">物品售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=3">主动技能售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=4">被动技能售卖列表</a><br/>
	--%>
	<%
}//物品
else if(type==PKAction.PK_MEDICINE){%>
	<%=pkNpc.getName()%>出售物品列表：<br/>
<%
	if(medicine!=null && medicine.length()>1){
		String[] things =medicine.split(",") ;
		PKMedicineBean pkMedicine = null;
		for(int i=0;i<things.length;i++){
			String id = things[i];
			pkMedicine=(PKMedicineBean)action.getProto(type,StringUtil.toInt(id));
			if(pkMedicine==null)continue;
			%>
		<%=i+1%>.
		<a href="/pk/buyAffirm.jsp?npcId=<%=npcId%>&amp;type=<%=type%>&amp;id=<%=pkMedicine.getId()%>"><%=pkMedicine.getName()%></a><br/>
		<%}
	}else{%>
	该<%=PKAction.NPCNAME%>没有物品售卖！<br/>
	<%}%>
	<%--
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=0">装备售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=3">主动技能售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=4">被动技能售卖列表</a><br/>
	--%>
	<%
}//主动技能
else if(type==PKAction.PK_USER_SKILL){%>
	<%=pkNpc.getName()%>出售物品列表：<br/>
<%
	if(skill!=null && skill.length()>1){
		String[] things =skill.split(",") ;
		PKUserSkillBean pkUserSkill = null;
		for(int i=0;i<things.length;i++){
			String id = things[i];
			pkUserSkill=(PKUserSkillBean)action.getProto(type,StringUtil.toInt(id));
			if(pkUserSkill==null)continue;
			%>
		<%=i+1%>.
		<a href="/pk/buyAffirm.jsp?npcId=<%=npcId%>&amp;type=<%=type%>&amp;id=<%=pkUserSkill.getId()%>"><%=pkUserSkill.getName()%></a><br/>
		<%}
	}else{%>
	该<%=PKAction.NPCNAME%>没有物品售卖！<br/>
	<%}%>
	<%--
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=0">装备售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=1">物品售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=4">被动技能售卖列表</a><br/>
	--%>
	<%
}//被动技能
else if(type==PKAction.PK_USER_BSKILL){%>
	<%=pkNpc.getName()%>出售物品列表：<br/>
<%
	if(bSkill!=null && bSkill.length()>1){
		String[] things =bSkill.split(",") ;
		PKUserBSkillBean pkUserBSkill = null;
		for(int i=0;i<things.length;i++){
			String id = things[i];
			pkUserBSkill=(PKUserBSkillBean)action.getProto(type,StringUtil.toInt(id));
			if(pkUserBSkill==null)continue;
			%>
		<%=i+1%>.
		<a href="/pk/buyAffirm.jsp?npcId=<%=npcId%>&amp;type=<%=type%>&amp;id=<%=pkUserBSkill.getId()%>"><%=pkUserBSkill.getName()%></a><br/>
		<%}
	}else{%>
	该<%=PKAction.NPCNAME%>没有物品售卖！<br/>
	<%}%>
	<%--
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=0">装备售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=1">物品售卖列表</a><br/>
	<a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=3">主动技能售卖列表</a><br/>
	--%>
<%}%>
<a href="/pk/pkSale.jsp">物品收购</a><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>