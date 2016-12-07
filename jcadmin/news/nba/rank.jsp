<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	action.instRank();
	List list = action.service.getRank(" del=0 order by id desc");
	String[] loc = {"大西洋赛区:","东南赛区:","中部赛区:","西南赛区:","西北赛区:","太平洋赛区:","东部赛区:","西部赛区:"};
%><html>
  <head>
    <title>排行管理</title>
  </head>
  <body>		
  <table border=1 width=70% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>排名|球队|胜|负|胜率|胜场差</font>
		</td>
	</tr>
  	<%
  	if(list != null && list.size() > 0){
		for(int i=0;i<list.size();i++){
			BeanRank br = (BeanRank)list.get(i);
	  		%><tr>
		  		<td>
				<font color=#1A4578><%=loc[br.getLoc()]%></font>
				</td>
			</tr>
			<tr>
				<td align=center>
				<font color=#1A4578><%=StringUtil.toWmlIgnoreAnd(br.getCont())%></font>
				</td>
			</tr>
	  		<%
  		}
  	}else{
  	%><tr>
		<td align=center>
		<font color=#1A4578>暂无排名!</font>
		</td>
	</tr><%
  	}
  	%>
  </table>
	<a href="rankupd.jsp">录入新排名</a><br/>
  	<a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
