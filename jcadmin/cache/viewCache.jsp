<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.bean.money.*"%><%
int index = StringUtil.toInt(request.getParameter("i"));
List cacheList = CacheManage.getCacheList();
if(index<0){
BaseAction.sendRedirect("cacheAdmin2.jsp", response);
return;
}
String strKey = request.getParameter("key");
int rmikey = StringUtil.toInt(strKey);
Object rmKey = strKey;
if(rmikey != -1) {
	rmKey = new Integer(rmikey);
} else {
	Integer2 rmiikey = Integer2.parse(strKey);
	if(rmiikey != null)
		rmKey = rmiikey;
}

ICacheMap cache = (ICacheMap)cacheList.get(index);

Object obj = cache.sgt(rmKey);

%><html>
<head>
<title>缓存查看</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>

<%=StringUtil.outputObject(obj)%>
<br/>
<a href="viewGroupCache2.jsp?i=<%=index%>&key=<%=strKey%>">删除</a>
<p>
<a href="viewGroupCache2.jsp?i=<%=index%>">返回</a>
</p>
</body>
</html>