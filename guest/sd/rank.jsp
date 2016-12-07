<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="jc.guest.*,jc.guest.sd.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.Base2Action"%>
<% 	
	response.setHeader("Cache-Control","no-cache");
	ShuDuAction action = new ShuDuAction(request,response);
	GuestUserInfo guestUser = action.getGuestUser();
	int uid = 0;
	if(guestUser != null) {
		uid = guestUser.getId();
	}
	int lvl = action.getParameterInt("lvl");
	if(lvl <= 0 || lvl > 4){
		lvl = 4;
	}
	ShuDuUser shuUser = ShuDuAction.service.getShuDuUser("uid="+uid);
	List list = action.getUserRank(lvl);
 %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="数独"><p>
<%=BaseAction.getTop(request, response)%>
<%
if (lvl == 1) {
	%><a href="rank.jsp?lvl=4">终极</a>|<a href="rank.jsp?lvl=3">困难</a>|<a href="rank.jsp?lvl=2">中等</a>|简单<br/><%
} else if (lvl == 2) {
	%><a href="rank.jsp?lvl=4">终极</a>|<a href="rank.jsp?lvl=3">困难</a>|中等|<a href="rank.jsp?lvl=1">简单</a><br/><%
} else if (lvl == 3) {
	%><a href="rank.jsp?lvl=4">终极</a>|困难|<a href="rank.jsp?lvl=2">中等</a>|<a href="rank.jsp?lvl=1">简单</a><br/><%
} else if (lvl == 4) {
	%>终极|<a href="rank.jsp?lvl=3">困难</a>|<a href="rank.jsp?lvl=2">中等</a>|<a href="rank.jsp?lvl=1">简单</a><br/><%
} 
 %>
我:<%
if(shuUser != null) {
	%>完成次数<%=shuUser.getUseLvlValue(lvl)%><%
} else {
	%>还没有您的记录<%
}
%><br/><% 
if(list != null && list.size() > 0){
	%>排名|昵称|完成次数<br/><%
	PagingBean paging = new PagingBean(action,list.size(),10,"p");
	for (int i = paging.getStartIndex();i < paging.getEndIndex(); i++) {
		ShuDuUser tempUser = (ShuDuUser) list.get(i);
		int tempUid = tempUser.getUid(); 
		GuestUserInfo tempGuest = action.getGuestUser(tempUid);
		int score = tempUser.getUseLvlValue(lvl);
  		%>第<%=i+1%>名&#160;<a href="/guest/info.jsp?uid=<%=tempUid%>"><%=tempGuest.getUserNameWml()%></a>|<%=score%><br/><%
	}
  	%><%=paging.shuzifenye("rank.jsp?jcfr=1&amp;lvl="+lvl,true,"|",response)%><%
} else {
	%>暂无排名<br/><%
}
 %><a href="index.jsp">返回数独</a><br/><%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>