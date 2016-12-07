<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.runing();
PetUserBean petUser = action.getPetUser();
MatchRunBean matchrunbean = (MatchRunBean)request.getAttribute("matchrunbean");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="运动员列表">
<p align="left">
请选择打击的对手：<br/>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
int order = 0;
for (order = 0; order < action.RUN_PLAYNUMBER; order++)
	if ((playbean[order] != null)
			&& (playbean[order].getPetid() == petUser
					.getId()))
		break;
int pos = playbean[order].getPosition();
int avail = 0;//可以打的人数，如果0就redirect
for(int i=0;i<action.RUN_PLAYNUMBER;i++){
int dpos = playbean[i].getPosition() - pos;
%>
<%if(petUser.getId()==playbean[i].getPetid() ||dpos>60||dpos<-60){%>
<%=StringUtil.toWml(playbean[i].getName())%>(<%=playbean[i].getPosition()%>米)<br/>
<%}else{avail++;%>
<a href="/pet/matchact.jsp?id=<%=i%>&amp;task=4"><%=StringUtil.toWml(playbean[i].getName())%></a>(<%=playbean[i].getPosition()%>米)<br/>
<%}%>
<%}
if(avail==0){
out.clearBuffer();
response.sendRedirect(("/pet/matchact.jsp?id=-2&task=4"));
return;
}
%>
<a href="/pet/matchact.jsp?id=-2&amp;task=4">丢弃该道具</a><br/>
</p>
</card>

</wml>