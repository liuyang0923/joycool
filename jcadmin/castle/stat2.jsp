<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
%><%
	CustomAction action = new CustomAction(request);
	CastleUtil.stat2People();
	int cur = action.getParameterInt("p");
	
	int start = cur * 10;
	int limit = 11;
	
	List list = service.getTongArray(start, limit);
	int count = list.size() > 10 ? 10 : list.size();
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
<table class="tbg" cellpadding="2" cellspacing="1" width="300"><tr class="rbg"><td></td><td>联盟</td><td>总人口</td><td>成员</td></tr>
<%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
%>
<tr><td><%=obj[0]%></td><td><a href="tong.jsp?id=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[3])%></a></td><td><%=obj[2]%></td><td><%=obj[4] %>
</td></tr><%}%></table>
<%if(list.size() > 10) {%><a href="stat2.jsp?p=<%=cur+1%>">下一页</a><%}else{%>下一页<%}%>
<%if(cur > 0) {%><a href="stat2.jsp?p=<%=cur-1%>">上一页</a><%}else{%>上一页<%}%><br/>
跳转到:<input name="p" maxLength="3" format="*N"/><anchor>GO<go href="stat2.jsp"><postfield name="p" value="$p"/></go></anchor><br/>
<%@include file="bottom.jsp"%>
</html>