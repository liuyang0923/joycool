<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.pk.PKUserHSkillBean"%><%@ page import="net.joycool.wap.bean.pk.PKUserSkillBean"%><%@ page import="net.joycool.wap.action.pk.PKWorld"%><%@ page import="net.joycool.wap.action.pk.PKAction"%><%@ page import="net.joycool.wap.bean.pk.PKUserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.pk.PKActBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.player(request);
String result =(String)request.getAttribute("result");
String url=null ;
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
<%}else if(result.equals("pkUserIsDeath")){
out.clearBuffer();
BaseAction.sendRedirect("/pk/index.jsp",response);
return;
}else if(result.equals("isDeath")){
out.clearBuffer();
BaseAction.sendRedirect("/pk/isDeath.jsp",response);
return;
}else{
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
PKUserBean pkUser = loginUser.getPkUser();
PKUserBean player=(PKUserBean)request.getAttribute("player");
UserBean playerUser =UserInfoUtil.getUser(player.getUserId());
List userHSkillList =pkUser.getUserSkillList(); 
PKActBean pkAct = (PKActBean)request.getAttribute("pkAct");
List log=pkAct.getLog();
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">k
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(playerUser.getNickName())%>(玩家)<br/>
体力：<%=player.getCurrentPhysical()%>/<%=player.getPhysical()%><br/>
气力：<%=player.getCurrentEnergy()%>/<%=player.getEnergy()%><br/>
----我的状态----<br/>
体力：<%=pkUser.getCurrentPhysical()%>/<%=pkUser.getPhysical()%><br/>
气力：<%=pkUser.getCurrentEnergy()%>/<%=pkUser.getEnergy()%><br/>
----使用技能----<br/>
<%if(userHSkillList.size()>0){
	PKUserHSkillBean userHSkill=null;
	PKUserSkillBean userSkill=null;
	for(int i=0;i<userHSkillList.size();i++){
		userHSkill=(PKUserHSkillBean)userHSkillList.get(i);
		userSkill=(PKUserSkillBean)PKWorld.getUserSkill().get(new Integer(userHSkill.getSkillId()));
		if(userSkill==null)continue;
		%>
	<a href="/pk/fight.jsp?playerId=<%=player.getId() %>&amp;isType=person&amp;skillId=<%=userSkill.getId()%>"><%=userSkill.getName()%></a><br/>
	<%}
}
%><br/>
<a href="/chat/post.jsp?toUserId=<%=player.getUserId()%>">跟<%=playerUser.getGender() == 1? "他" : "她"%>说话</a><br/>
<a href="/pk/pkUserBag.jsp">我的行囊</a><br/>
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