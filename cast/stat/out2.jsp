<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%

	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	if(id <= 0){
		response.sendRedirect("index.jsp");
		return;
	}
	List list = SqlUtil.getObjectsList("select uid,name,info,area from outs where id="+id, 5);
	if(list.size()==0){
		response.sendRedirect("index.jsp");
		return;
	}
	Object[] obj = (Object[])list.get(0);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="名人堂"><p>
名人【<a href="/user/ViewUserInfo.do?userId=<%=obj[0]%>"><%=StringUtil.toWml(obj[1].toString())%></a>】<br/>
<%=StringUtil.toWml(obj[2].toString())%><br/>
<a href="out.jsp?i=<%=obj[3]%>">返回名人堂</a><br/>
<a href="/cast/index.jsp">返回城堡大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>