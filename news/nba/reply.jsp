<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	int mid = action.getParameterInt("mid");
	int sign = action.GetSign();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发表评论"><p><%=BaseAction.getTop(request, response)%><%
if(mid > 0){
 %><input type="text" name="wqk<%=sign%>" maxlength="50"/>
<anchor>发言
<go href="back.jsp?mid=<%=mid %>" method="post">
<postfield name="cont" value="$wqk<%=sign%>"/>
</go>
</anchor><br/><a href="alive.jsp?mid=<%=mid%>">返回</a>&#160;<%}
%><a href="index.jsp">返回首页</a><br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>