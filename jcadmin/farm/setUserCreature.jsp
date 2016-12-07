<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
int userId = action.getParameterInt("id");
int pos = action.getParameterInt("pos");
int bindPos = action.getParameterInt("bindPos");
FarmUserBean user=null;
if(userId>0){
user = FarmWorld.one.getFarmUserCache(userId);
if(user!=null){
	List questCreatureFinish = user.getQuestCreatureFinish();
	
	for(int j = 0;j<questCreatureFinish.size();j++){
		int[] f = (int[])questCreatureFinish.get(j);
		f[2] = action.getParameterInt("j"+j);
	}

}
response.sendRedirect("viewUser.jsp?id=" + userId);
return;
}
%>
