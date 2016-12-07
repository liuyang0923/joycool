<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%!
static void addLoginMap(String url,String entry){
	if(url == null||entry==null)
		return;
	HashMap newMap = (HashMap)WGameFilter.loginMap.clone();
	newMap.put(url, entry);
	WGameFilter.loginMap = newMap;
	SqlUtil.executeUpdate("insert into login_map set url='"+StringUtil.toSql(url)+"',entry='"+StringUtil.toSql(entry)+"'", 0);
}
%><%
	CustomAction action = new CustomAction(request);
	int act = action.getParameterInt("a");
	if(act==1){
		addLoginMap(action.getParameterNoEnter("url"), action.getParameterNoEnter("entry"));
		response.sendRedirect("loginmap.jsp");
		return;
	} else if(act==3){
		SqlUtil.executeUpdate("delete from login_map where id=" + action.getParameterInt("del"), 0);
		WGameFilter.loadLoginMap();	// 删除了一个，重新载入
		response.sendRedirect("loginmap.jsp");
		return;
	} else if(act==4){
		WGameFilter.loadLoginMap();
	}
	List list = SqlUtil.getObjectsList("select id,url,entry from login_map order by url", 0);
%>
<html>
<head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
登陆才能访问的模块<br/>

<table>
<tr><td>id</td><td>url</td><td>entry</td><td>模块</td><td></td></tr>
<%
for(int i=0;i<list.size();i++){
Object[] obj = (Object[])list.get(i);
String url = obj[2].toString();
	String module = "(未知)";
	net.joycool.wap.bean.ModuleBean moduleBean = PositionUtil.getModuleBean(url);
	if(moduleBean!=null)
		module=moduleBean.getPositionName();
%>
<tr><td width="30"><%=obj[0]%></td><td width="100"><%=obj[1]%></td><td width="300"><%=obj[2]%></td><td><%=module%></td><td width=30 align=center><a href="loginmap.jsp?a=3&del=<%=obj[0]%>" onclick="return confirm('确定删除?')">删</a></td></tr>
<%}%>
</table>
<p>
<form action="loginmap.jsp?a=1" method="post">
模块前缀:<input type="text" name="url" value="">
入口页面:<input type="text" name="entry" value="">
<input type=submit value="添加">
</form>
</p>
<p><a href="loginmap.jsp?a=4" onclick="return confirm('确认重新载入？')">重新载入</a></p>
<body>
</html>