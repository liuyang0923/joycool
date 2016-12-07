<%@page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%response.setHeader("Cache-Control", "no-cache");%>
<%@page import="jc.guest.*,net.joycool.wap.bean.*,jc.guest.battle.*,java.util.LinkedList,net.joycool.wap.util.*"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request);
GuestHallAction action2 = new GuestHallAction(request,response);
GuestUserInfo guestUser = action2.getGuestUser();
%>
<wml><card title="名字大作战"><p>
<%=BaseAction.getTop(request,response)%>
在下面2个方框里填入要比拼的2个名字,看看谁的名字厉害!(最多可输入10个字)<br/>
<%//取错误信息
if(session.getAttribute("stored")!=null){
	Stored stored=(Stored)session.getAttribute("stored");
if(!stored.isError()){%>
<%=stored.getErrormessage()%><br/>
<%}}session.removeAttribute("stored");%>
<input type="text" name="username1" format="M,A,a,N" />
vs
<input type="text" name="username2" format="M,A,a,N" />
<br/>
<anchor title="确定">
确定
<go href="battle.jsp" method="post">
<postfield name="username1" value="$username1" />
<postfield name="username2" value="$username2" />
<postfield name="first" value="0" />
</go>
</anchor><%=guestUser != null ? "需要花费1游币" : "" %><br/>
<a href="top.jsp?winlose=0">昨日挑战榜</a><br/>
最近战斗记录:<br/>
<%LinkedList list = GamepageAction.nplistLink;
PagingBean paging = new PagingBean(action,list.size(),10,"p");
int	pageindex = paging.getStartIndex();			
int ki=0;
for (int i =pageindex; i <paging.getEndIndex(); i++) {				
	Namebean nb = (Namebean)list.get(i);
	if (nb != null){
		String username1=nb.getUsername1();
		String username2=nb.getUsername2();
		%><%=i+1%>.<a  href="battle.jsp?k=<%=i%>&amp;first=0"><%=StringUtil.toWml(username1)%>vs<%=StringUtil.toWml(username2)%></a><br/><%
   }
}%>
<%=paging.shuzifenye("game.jsp",false,"|",response)%>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>