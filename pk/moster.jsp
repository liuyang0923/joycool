<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.pk.PKUserHSkillBean"%><%@ page import="net.joycool.wap.bean.pk.PKUserSkillBean"%><%@ page import="net.joycool.wap.action.pk.PKWorld"%><%@ page import="net.joycool.wap.action.pk.PKAction"%><%@ page import="net.joycool.wap.bean.pk.PKMonsterBean"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.pk.PKUserBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.pk.PKActBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.monster(request);
int index = StringUtil.toInt(request.getParameter("index"));
if(index<0){
	index = 0;
}
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
PKMonsterBean monster=(PKMonsterBean)request.getAttribute("monster");
PKUserBean pkUser = action.getPkUser();
String result =(String)request.getAttribute("result");
String url=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
url=("/pk/index.jsp");%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转回场景)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("monsterIsDeath")){
out.clearBuffer();
BaseAction.sendRedirect("/pk/index.jsp",response);
return;
}else if(result.equals("isDeath")){
out.clearBuffer();
BaseAction.sendRedirect("/pk/isDeath.jsp",response);
return;
}else{
List userHSkillList =pkUser.getUserSkillList(); 
PKActBean pkAct = (PKActBean)request.getAttribute("pkAct");
List log=pkAct.getLog();
url=("/pk/moster.jsp?index=" + index);
%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="150"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("/img/pk/monster/+"+monster.getId()+".gif")%>
<!--<img src="../img/pk/monster/<%=monster.getId()%>.gif" alt="loading"/><br/>-->
<%=monster.getName()%><a href="<%=url%>">刷新</a><br/>
体力：<%=monster.getPhysical()%>/<%=monster.getInitPhysical()%><br/>
----我的状态----<br/>
体力：<%=pkUser.getCurrentPhysical()%>/<%=pkUser.getPhysical()%><br/>
气力：<%=pkUser.getCurrentEnergy()%>/<%=pkUser.getEnergy()%><br/>
使用技能<br/>
<%if(userHSkillList.size()>0){
	PKUserHSkillBean userHSkill=null;
	PKUserSkillBean userSkill=null;
	for(int i=0;i<userHSkillList.size();i++){
		userHSkill=(PKUserHSkillBean)userHSkillList.get(i);
		//判断是否为被动技能
		if(userHSkill.getSkillType()==0)continue;
		userSkill=(PKUserSkillBean)PKWorld.getUserSkill().get(new Integer(userHSkill.getSkillId()));
		if(userSkill==null)continue;
		%>
	<a href="/pk/fight.jsp?index=<%=index %>&amp;isType=monster&amp;skillId=<%=userSkill.getId()%>"><%=userSkill.getName()%></a><br/>
	<%}
}
%>
<a href="/pk/pkUserBag.jsp">我的行囊</a><br/><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
====战斗记录====<br/>
<%
//场景内log
if(log!=null){
	String content=	pkAct.toString(log);%>
	<%=content%>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>