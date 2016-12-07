<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
Vector ml = WGameDataAction.getRumorList();
if(request.getParameter("delete") != null){
	int deleteId = Integer.parseInt(request.getParameter("delete"));
	ml.remove(deleteId);
	//response.sendRedirect("rumorList.jsp");
	BaseAction.sendRedirect("/jcadmin/rumorList.jsp", response);
	return;
}
int count = ml.size();
int i;
String m = null;
%>
<%
for(i = 0; i < count; i ++){
	m = (String)ml.get(i);
%>
<%=(i + 1)%>.<%=m%>|<a href="rumorList.jsp?delete=<%=i%>">删除</a><br/>
<%
}
%>