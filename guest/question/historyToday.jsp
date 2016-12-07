<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.question.QuestionAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.question.eum"%><%@ page import="jc.guest.GuestHallAction,jc.guest.GuestUserInfo"%><%
response.setHeader("Cache-Control","no-cache");
//取得用户信息的方法
QuestionAction action = new QuestionAction(request);
GuestUserInfo guestUser = GuestHallAction.getGuestUserSe(request);
if (guestUser == null){
	   response.sendRedirect("/guest/nick.jsp");
	   return;
}
//判断是不是需要判断对错
List list = action.getTodayList(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="每日龙榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您目前的排名是:<%=action.getOrderTotal("todayValue")%><br/>
名次   姓名     级别     当日闯关数<br/>
<%for(int i=0;i<10;i++) { %>
<%if(action.page*10+i < list.size()) {%>
第<%=action.page*10+i+1%>名
<%if(GuestHallAction.getGuestUser(((eum)list.get(action.page*10+i)).getId()) != null)  {%>
<a href="/guest/info.jsp?uid=<%=((eum)list.get(action.page*10+i)).getId()%>">
<%=StringUtil.toWml(GuestHallAction.getGuestUser(((eum)list.get(action.page*10+i)).getId()).getUserName())%>
</a>
<%=action.getEumToday((eum)list.get(action.page*10+i),action.page*10+i+1)%>
<%}%><br/>
<%}%>
<%}%>
<%if((action.page > 0)&&(action.page <= list.size()/10)){%>
<a href="historyToday.jsp?topage=<%=action.getprepage()%>">上一页</a>
<%}%>
<%if((action.page < list.size()/10)&&(action.page < 9)){%>
<a href="historyToday.jsp?topage=<%=action.getnexpage()%>">下一页</a><br/>
<%}%>
<br/>
<a href="index.jsp">问答接龙</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>