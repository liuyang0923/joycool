<%@ page language="java" import="jc.news.nba.*,java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	action.updateMatch();
	List list = action.service.getMatchList(" static_value=2");
%><html>
  <head>
    <title>直播后台列表</title>
  </head>
  <body>		
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>双方队伍</font>
		</td>
		<td align=center>
			<font color=#1A4578>操作</font>
		</td>
	</tr><%
  	if(list != null && list.size() > 0){
  		for(int i = 0; i < list.size(); i++){
  			BeanMatch bm = (BeanMatch)list.get(i);
		  	%><tr>
		  	<td align=center><a href="alive.jsp?mid=<%=bm.getId()%>"><%=bm.getTeam1()%>VS<%=bm.getTeam2()%></a></td>
			<td align=center><a href="reply.jsp?mid=<%=bm.getId()%>">进入言论管理</a>|
			<a href="live.jsp?oId=<%=bm.getId()%>" onclick="return confirm('确认结束直播?')">结束直播</a></td>
			</tr><%	
  		}
  	}else{
  	%><tr>
  		<td>暂无直播哦!</td>
  		<td>暂无操作!</td>
  	</tr><%
  	}
   %>
</table><br/><a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
