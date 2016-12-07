<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="java.util.*"%><%
String clear = request.getParameter("clear");
if(clear != null){
	if("knife".equals(clear)){
		KnifeFrk.questionList = null;
		KnifeFrk.knifeList = null;
	}	
	else if("zippo".equals(clear)){
		ZippoFrk.zippoListById = null;
		ZippoFrk.zippoListByType = null;
		ZippoFrk.zippoTypeList = null;
		ZippoFrk.zippoStarList = null;
	}	
	//response.sendRedirect("clearCache.jsp");
	BaseAction.sendRedirect("/jcadmin/wxsj/clearCache.jsp", response);
}
%>

缓存管理<br>
<br>
<a href="clearCache.jsp?clear=knife">清空“勇士爱军刀”缓存</a><br>
<a href="clearCache.jsp?clear=zippo">清空“zippo搭配游戏”缓存</a><br>