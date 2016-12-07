<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	
	CastleAction action = new CastleAction(request);
	CastleUserBean user = action.getCastleUser();
	if(user == null) {
		response.sendRedirect("s.jsp");
		return;
	}
	int protectTime = (int)((user.getProtectTime() - System.currentTimeMillis()) / 1000);
	boolean flag = action.account();
	List cacheList = cacheService.getCacheCommonList2(action.getUserBean().getId(),3);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="帐号管理"><p>
<%if(request.getAttribute("msg") != null){%><%=request.getAttribute("msg")%><br/><%}%>
帐号名:<%=StringUtil.toWml(user.getName())%><br/>
<%if(cacheList.size()>0){
Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
%>警告!本帐号将在<%=cacheBean.getTimeLeft()%>后删除,一旦帐号被删除,拥有的金币和城堡将会全部消失.<a href="account.jsp?cdel=<%=cacheBean.getId()%>" >取消删除</a><br/><%}
}%>
<%if(user.isLocked()){%>你的帐号已经被冻结。<a href="ad.jsp">解除帐号冻结>></a><br/><%}%>
个人资料:<%=user.getInfo2()%><br/><a href="uInfo.jsp">修改个人资料</a><br/>
拥有<%=user.getGold()%>个金币<br/>
总文明度:<%=user.getCivil(System.currentTimeMillis())%><br/>
文明度增长:<%=user.getCivilSpeed()%>/天<br/>
【<a href="dacc.jsp">删除帐号!!!</a>】<br/>
<%if(cacheList.size()>0){
Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
%>帐号等待删除中<a href="account.jsp?cdel=<%=cacheBean.getId()%>" >取消</a><br/>
剩余时间<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%
}
}%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>