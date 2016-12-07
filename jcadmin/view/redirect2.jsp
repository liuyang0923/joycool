<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%!
static void addRedirect(String url){
	if(url==null)
		return;
	int pos = url.indexOf('.');
	if(pos!=-1){
		addRedirect(url, url.substring(0,pos)+"X"+url.substring(pos));
	}
}
static void addRedirect(String url, String toUrl){
	if(url == null ||toUrl==null)
		return;
	HashMap newMap = (HashMap)WGameFilter.wap20Map.clone();
	newMap.put(url, toUrl);
	WGameFilter.wap20Map = newMap;
	SqlUtil.executeUpdate("insert into url_redirect2 set type=0,url='"+StringUtil.toSql(url)+"',to_url='"+StringUtil.toSql(toUrl)+"'", 5);
}
%><%
	CustomAction action = new CustomAction(request);
	int act = action.getParameterInt("a");
	if(act==1){
		addRedirect(action.getParameterNoEnter("url"));
		response.sendRedirect("redirect2.jsp");
		return;
	} else 	if(act==2){
		addRedirect(action.getParameterNoEnter("url"), action.getParameterNoEnter("url2"));
		response.sendRedirect("redirect2.jsp");
		return;
	} else if(act==3){
		SqlUtil.executeUpdate("delete from url_redirect2 where id=" + action.getParameterInt("del"), 5);
		WGameFilter.loadWap20Map();	// 删除了一个，重新载入
		response.sendRedirect("redirect2.jsp");
		return;
	} else if(act==4){
		WGameFilter.loadWap20Map();
	}
	List list = SqlUtil.getObjectsList("select id,url,to_url,type from url_redirect2 order by url", 5);
%>
<html>
<head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
特殊跳转<br/>

<table>
<tr><td>id</td><td>from</td><td>to</td><td>类型</td><td>模块</td><td></td></tr>
<%
for(int i=0;i<list.size();i++){
Object[] obj = (Object[])list.get(i);
String url = obj[1].toString();
	String module = "(未知)";
	net.joycool.wap.bean.ModuleBean moduleBean = PositionUtil.getModuleBean(url);
	if(moduleBean!=null)
		module=moduleBean.getPositionName();
%>
<tr><td width="30"><%=obj[0]%></td><td width="300"><%=obj[1]%></td><td width="300"><%=obj[2]%></td><td><%=obj[3]%></td><td><%=module%></td><td width=30 align=center><a href="redirect2.jsp?a=3&del=<%=obj[0]%>" onclick="return confirm('确定删除?')">删</a></td></tr>
<%}%>
</table>
<p>
<form action="redirect2.jsp?a=1" method="post">
<input type="text" name="url" value="">
<input type=submit value="快速添加">（自动加X.jsp）
</form>
</p>
<p>
<form action="redirect2.jsp?a=2" method="post">
from:<input type="text" name="url" value="">
to:<input type="text" name="url2" value="">
<input type=submit value="添加">
</form>
</p>
<p><a href="redirect2.jsp?a=4" onclick="return confirm('确认重新载入？')">重新载入</a></p>
<body>
</html>