<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%

CustomAction action = new CustomAction(request);
int type = action.getParameterInt("type");
String cmd = null;

if(type == 1) {
	cmd="tail /usr/local/tomcat2/logs/catalina.out -n" + action.getParameterInt("c");
}else if(type==2){
	cmd="free";
}else if(type==3){
	cmd="df";
}else if(type == 4) {
	cmd="cat /usr/local/tomcat2/logs/localhost_log." + action.getParameterString("d") + ".txt | grep 'Exception' -A" + action.getParameterInt("l");
}

if(type == 99) {
	cmd = request.getParameter("cmd");
	System.out.println(cmd);
} else if(SqlUtil.isTest) {
	System.out.println(cmd);
	cmd="cmd /c dir \"c:/windows\"";
}

%><meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<html>
<head><head>
<body style="font-family:Courier New;font-size=14;word-break:break-all;background-color:black;color:#BBBBBB;margin-left:50px;">
<%if(cmd!=null){%><b><%=cmd%></b><br/><%}%>
<table border="0"><tr><td width="400">
<form action="linux.jsp?type=1" method=post>
<input type=text name="c" value="300" size="6"/><input type=submit value="查询catalina.out">,
<input type=button value="内存" onclick="window.location='linux.jsp?type=2'">,<input type=button value="硬盘" onclick="window.location='linux.jsp?type=3'">
</form></td><td width=300><form action="linux.jsp?type=4" method=post>
<select name="d"><%
Calendar cal = Calendar.getInstance();
for(int i=0;i<10;i++){String date = DateUtil.formatDate(cal.getTime());%><option value="<%=date%>"><%=date%></option><%
cal.add(Calendar.DATE, -1);
}%></select>
<select name="l"><%for(int i=3;i<10;i++){%><option value="<%=i%>" <%if(i==6){%>selected<%}%> ><%=i%></option><%}%></select>行
<input type=submit value="查询localhost">
</form></td><td><form action="linux.jsp?type=99" method=post><input type=text name="cmd"><input type=submit value="执行"></form></td></tr></table>
<%
if(cmd!=null)
try
{
 Process process = Runtime.getRuntime().exec(cmd); 

 InputStreamReader ir=new InputStreamReader(process.getInputStream());
 LineNumberReader input = new LineNumberReader (ir);

 String line;
 while ((line = input.readLine ()) != null) {
 if(line.indexOf("Exception")!=-1){
 	out.println("<font color=red>");
 	out.println(StringUtil.toWml(line));
 	out.println("</font>");
 }else{
 	out.println(StringUtil.toWml(line));
 }
	
	out.print("<br/>");
}
  
}catch (java.io.IOException e){
	e.printStackTrace();
} 

%>
</body></html>