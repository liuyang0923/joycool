<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.util.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*,net.joycool.wap.action.jcadmin.*"%><%@ page import="net.joycool.wap.action.tong.*"%><%@ page import="net.joycool.wap.bean.tong.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><html>
<head>
<title>帮会周工资</title>
</head>
<body><%
Long t = (Long)application.getAttribute("lastWeekTongSalary");
int minute;
if(t == null)
minute=0;
else
minute =(int)(System.currentTimeMillis() - t.longValue()) / (1000*60);
%>
<%=minute%>分钟
</body>
</html>