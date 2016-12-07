..<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.world.*" %><%@ page import="java.util.Hashtable" %><%@ page import="java.util.Iterator" %><%@ page import="java.util.Set" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="java.util.LinkedList" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.framework.BaseAction" %><%response.setHeader("Cache-Control", "no-cache");			
if (session.getAttribute("adminLogin") == null) {
		BaseAction.sendRedirect("/jcadmin/login.jsp", response);
		return;
	}
%>
<br>
<a href="chatInfo.jsp">返回</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>
<br/>
<br/>
<%
String key = request.getParameter("key");
String id = request.getParameter("id");
if(key != null && id!=null){
long chatId= StringUtil.toLong(id);
if(chatId==-1){
	BaseAction.sendRedirect("/jcadmin/tong/chat//chatList.jsp?key="+key, response);
	return;
}
LinkedList chatList = ChatWorld.get(key,ChatWorldBean.TONG_CHAT);
Iterator it = chatList.iterator();
	while(it.hasNext()){
		ChatWorldBean chatWorld = (ChatWorldBean)it.next();
		if(chatWorld.getId()!=chatId){
			continue;	
		}
		it.remove();	
		break;
	}
}
if(true){
BaseAction.sendRedirect("/jcadmin/tong/chat//chatList.jsp?key="+key, response);
return;
}
%>
