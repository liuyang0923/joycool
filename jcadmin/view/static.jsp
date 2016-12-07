<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="java.lang.reflect.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static String[][] func = {
	{"net.joycool.wap.cache.GameQuestionCacheUtil","SetHashMapNull","重置问答接龙数据"},
	{"net.joycool.wap.util.LoadResource","","资源相关"},
	{"net.joycool.wap.spec.farm.FarmWorld","","桃花源相关"},
	{"net.joycool.wap.spec.castle.ResNeed","","城堡相关"},
};
static String[][] var = {
	{"net.joycool.wap.action.jcforum.ForumxAction","newsTypeMap","清空新闻类别缓存"},
};
%><%
String tip=null;
String c1=request.getParameter("class");
String name=request.getParameter("name");
Class c = null;
if(c1==null) c1=""; else {
try{
c = Class.forName(c1);
}catch(Exception e){
tip="<font color=red>Class Not Found</font>";
}
}
if(name==null) name="";
String act = request.getParameter("a");

if(c!=null){
	if("1".equals(act)) {
		try{
		    Field f1 = c.getDeclaredField(name);
		    if(request.getParameter("op").equals("查看")){
		    	Object obj = f1.get(null);
		    	tip = name + "<br/>"+StringUtil.outputObject(obj);
		    } else if(request.getParameter("op").equals("设置为空")){
			    if(f1.isAccessible())
					f1.set(null, null);
				else {
					f1.setAccessible(true);
					f1.set(null, null);
					f1.setAccessible(false);
				}
				tip="<font color=green><b>执行成功</b></font>";
			} else if(request.getParameter("op").equals("设置值")){
				String vs = request.getParameter("value");
				Object value = null;
				String type = f1.getType().getName();
				if(type.equals("java.lang.Integer")||type.equals("int")){
					value=Integer.valueOf(vs);
				}else if(type.equals("java.lang.Long")||type.equals("long")){
					value=Long.valueOf(vs);
				}else if(type.equals("java.lang.Double")||type.equals("double")){
					value=Long.valueOf(vs);
				} else if(type.equals("java.lang.String")){
					value=vs;
				}
			    if(f1.isAccessible())
					f1.set(null, value);
				else {
					f1.setAccessible(true);
					f1.set(null, value);
					f1.setAccessible(false);
				}
				tip="<font color=green><b>执行成功</b></font>";
			}
		} catch(Exception e){
			tip="<font color=red>执行失败 - "+e.getLocalizedMessage()+"</font>";
		}
		
	} else if("2".equals(act)) {
		try{
			Method m1 = c.getMethod(name, null);
			Object obj = m1.invoke(null, null);
			tip="<font color=green><b>执行成功</b></font>";
			if(obj != null)
				tip += "<br/>"+StringUtil.outputObject(obj);
		} catch(Exception e){
			tip="<font color=red>执行失败 - "+e.getLocalizedMessage()+"</font>";
		}
	}
}
%>
<html>
<head>

</head>
<body>
<%if(tip!=null){%><%=tip%><br/><%}%>
<form action="static.jsp?a=1" method="post">
class:<input type=text name="class" value="<%=c1%>"/>
name:<input type=text name="name" value="<%=name%>"/>
<input type=submit name="op" value="查看"/>
<input type=submit name="op" value="设置为空"/>
<input type=text name="value" value=""/>
<input type=submit name="op" value="设置值"/>
</form>
<%if(c!=null){
Field[] fs = c.getFields();
for(int i=0;i<fs.length;i++){
if(!Modifier.isStatic(fs[i].getModifiers())) continue;		// 无参数
%>
<a href="static.jsp?class=<%=c1%>&name=<%=fs[i].getName()%>"><%=fs[i].getName()%></a><br/>
<%}}%><br/>

<form action="static.jsp?a=2" method="post">
class:<input type=text name="class" value="<%=c1%>"/>
name:<input type=text name="name" value="<%=name%>"/>
<input type=submit value="调用"/>
</form>
<%if(c!=null){
Method[] ms = c.getMethods();
for(int i=0;i<ms.length;i++){
if(ms[i].getParameterTypes().length!=0||!Modifier.isStatic(ms[i].getModifiers())) continue;		// 无参数
%>
<a href="static.jsp?class=<%=c1%>&name=<%=ms[i].getName()%>"><%=ms[i].getName()%></a><br/>
<%}}%><br/>
设置空<br/>
<%for(int i=0;i<var.length;i++){%>
<a href="static.jsp?class=<%=var[i][0]%>&name=<%=var[i][1]%>"><%=var[i][2]%>></a><br/>
<%}%>
函数<br/>
<%for(int i=0;i<func.length;i++){%>
<a href="static.jsp?class=<%=func[i][0]%>&name=<%=func[i][1]%>"><%=func[i][2]%></a><br/>
<%}%>
<body>
</html>