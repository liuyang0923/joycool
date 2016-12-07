<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.action.pk.PKWorld" %><%@ page import="net.joycool.wap.bean.pk.PKUserHSkillBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserSkillBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.userSkillInfo(request);
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
<%=request.getAttribute("tip") %>(3秒后跳转其它场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKUserHSkillBean userHSkill =(PKUserHSkillBean)request.getAttribute("pkUserHSkill");
PKUserSkillBean userSkill=(PKUserSkillBean)PKWorld.getUserSkill().get(new Integer(userHSkill.getSkillId()));
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=userSkill.toDetail1()%>
当前等级：<%=userHSkill.getRank()%><br/>
当前技能点：<%=userHSkill.getSkillKey()%><br/>
<%if(userHSkill.getSkillId()!=13){%>
<a href="/pk/delUserSkillAffirm.jsp?id=<%=userHSkill.getId()%>">删除技能</a><br/><br/>
<%}%>
<a href="/pk/userSkillList.jsp">返回主动技能</a><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>