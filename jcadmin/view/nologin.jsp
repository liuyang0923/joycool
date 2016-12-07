<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%!
static void addNoLogin(String url){
	if(url == null)
		return;
	HashSet newMap = (HashSet)WGameFilter.noLoginSet.clone();
	newMap.add(url);
	WGameFilter.noLoginSet = newMap;
	SqlUtil.executeUpdate("insert into no_login set url='"+StringUtil.toSql(url)+"'", 0);
}
%><%
	CustomAction action = new CustomAction(request);
	int act = action.getParameterInt("a");
	if(act==1){
		addNoLogin(action.getParameterNoEnter("url"));
		response.sendRedirect("nologin.jsp");
		return;
	} else if(act==3){
		SqlUtil.executeUpdate("delete from no_login where id=" + action.getParameterInt("del"), 0);
		WGameFilter.loadNoLoginSet();	// 删除了一个，重新载入
		response.sendRedirect("nologin.jsp");
		return;
	} else if(act==4){
		WGameFilter.loadNoLoginSet();
	}
	List list = SqlUtil.getObjectsList("select id,url from no_login order by url", 0);
%>
<html>
<head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
游客能访问的页面<br/>

<table>
<tr><td>id</td><td>url</td><td>模块</td><td></td></tr>
<%
for(int i=0;i<list.size();i++){
Object[] obj = (Object[])list.get(i);
String url = obj[1].toString();
	String module = "(未知)";
	net.joycool.wap.bean.ModuleBean moduleBean = PositionUtil.getModuleBean(url);
	if(moduleBean!=null)
		module=moduleBean.getPositionName();
%>
<tr><td width="30"><%=obj[0]%></td><td width="300"><%=obj[1]%></td><td><%=module%></td><td width=30 align=center><a href="nologin.jsp?a=3&del=<%=obj[0]%>" onclick="return confirm('确定删除?')">删</a></td></tr>
<%}%>
</table>
<p>
<form action="nologin.jsp?a=1" method="post">
url:<input type="text" name="url" value="">
<input type=submit value="添加">
</form>
</p>
<p><a href="nologin.jsp?a=4" onclick="return confirm('确认重新载入？')">重新载入</a></p>
<body>
</html>