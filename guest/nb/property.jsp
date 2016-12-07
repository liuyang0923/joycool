<%@page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%response.setHeader("Cache-Control", "no-cache");%>
<%@page import="net.joycool.wap.bean.*,jc.guest.battle.*,java.util.LinkedList,net.joycool.wap.util.*"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request);%>
<%Stored stored=(Stored)session.getAttribute("stored");
String index=request.getParameter("index");
if (index == null){
	response.sendRedirect("game.jsp");
	return;
}
NameProperty np;
if(index.equals("1")){
	np=stored.getNp1();
}else{
	np=stored.getNp2();
}
%>
<wml>
<card title="名字属性"><p>
<%=BaseAction.getTop(request,response)%>
名字:<%=StringUtil.toWml(np.getName())%><br/>
血量:<%=np.getHp()%><br/>
攻击:<%=np.getAttack()%><br/>
防御:<%=np.getDefense()%><br/>
速度:<%=np.getSpeed()%><br/>
幸运:<%=np.getLuck()%><br/>
命中:<%=np.getImpact()%><br/>
<a href="battle.jsp">返回战斗</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>