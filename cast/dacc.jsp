<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	
	CastleAction action = new CastleAction(request);
	CastleUserBean user = action.getCastleUser();
	int protectTime = (int)((user.getProtectTime() - System.currentTimeMillis()) / 1000);
	boolean flag = action.account();
	List cacheList = cacheService.getCacheCommonList2(action.getUserBean().getId(),3);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="删除帐号"><p>
<%if(request.getAttribute("msg") != null){%><%=request.getAttribute("msg")%><br/><%}%>
帐号名:<%=StringUtil.toWml(user.getName())%><br/>
【删除帐号】<br/>
<%if(cacheList.size()>0){
Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
%>帐号等待删除中<a href="account.jsp?cdel=<%=cacheBean.getId()%>" >取消</a><br/>
剩余时间<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%
}
} else{

%>一旦删除帐号,拥有的金币和所有城堡都会消失,请谨慎使用删除功能!<br/>
请输入银行密码确认删除<br/>
<input name="pwd" value=""/><br/>
<anchor>确认删除帐号<go href="account.jsp?del=1" method="post">
<postfield name="pwd" value="$pwd"/>
</go></anchor><br/>
<%}%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>