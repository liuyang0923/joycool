<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%

	ThreadGroup system = null;
	ThreadGroup tg = Thread.currentThread().getThreadGroup();
	while (tg != null) {
		system = tg;
		tg = tg.getParent();
	}


	/*String user = (String)session.getAttribute("test");
	if(user == null) {
		double i = Math.random();
		user = "test" + i;
		session.setAttribute("test", user);
	}*/
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'thread.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  CPU:<%=Runtime.getRuntime().availableProcessors()%><br/>
  空闲内存:<%=Runtime.getRuntime().freeMemory() / 1024%>K<br/>
  总内存：
  <%=Runtime.getRuntime().totalMemory() / 1024%>K<br/>
  最大内存：
  <%=Runtime.getRuntime().maxMemory() / 1024%>K<br/>
  已占用的内存：
  <%=(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024%>K<br/>
  <table>
  	<tr><td>线程ID<td><td>后台线程</td><td>状态</td><td>线程名字</td><td>类名</td></tr>
  	<%
  	if (system != null) {
		Thread[] thds = new Thread[system.activeCount()];
		int nthds = system.enumerate(thds);
		for (int i = 0; i < nthds; i++) {
			if(!thds[i].isDaemon()) {
				//System.out.println(thds[i] + "   " + thds[i].isDaemon());
				//System.out.println(thds[i].getId());
				//System.out.println(thds[i].getName());
				//System.out.println(thds[i].getClass().getPackage());
				//System.out.println(thds[i].getClass().getName());
				//System.out.println(thds[i].toString());
				//System.out.println("*************************");
				//thds[i].interrupt();
			
		
	
  	%>
  	<tr><td><%= thds[i].getId()%><td><td><%= thds[i].isDaemon()?"是":"否" %></td><td><%=thds[i].isAlive()&&(!thds[i].isInterrupted()) ?"正常":"中断" %></td><td><%= (thds[i].getName().length() > 15)?thds[i].getName().substring(0, 14)+ "...":thds[i].getName() %></td><td><%= thds[i].getClass().getName()%></td></tr>
  	<%}}
  	}
  	%>
  	
  </table>
  </body>
</html>
