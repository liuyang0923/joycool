<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="java.util.*"%><%!

%><%
	CustomAction action = new CustomAction(request);
	
List userList = new ArrayList(RichWorld.userMap.values());
	
	if(request.getParameter("clearMoney")!=null){
	
		for(int i=0;i<userList.size();i++){
			RichUserBean ru = (RichUserBean)userList.get(i);
			if(ru.getWorldId()<0) {
				ru.setMoney(0);
				ru.setSaving(0);
				ru.bag.clear();
			}
		}	
	
		response.sendRedirect("rich2.jsp");
		return;
	}
	boolean all = (request.getParameter("all")!=null);
%>
<html>
	<head>
<script src="../js/sorttable.js"></script>
<style>
.sortable {border-collapse: collapse;}
.sortable td {border-width: 1px;padding: 2px;border-style: solid;border-color: #888888;}
</style>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>大富翁数据2</h3>
<table width="500px" class="sortable">
<tr><td>userId</td>
<td class='sorttable_default'>flag-worldId</td>
<td>status</td><td>role</td>
<td>money</td>
<td>saving</td>
<td>log</td>
</tr>
<%
for(int i=0;i<userList.size();i++){
RichUserBean ru = (RichUserBean)userList.get(i);
if(ru.getWorldId()<0 && (!all||ru.getMoney()+ru.getSaving()==0)) continue;
RichWorld w = null;
if(ru.getWorldId()>=0)
	w = RichAction.worlds[ru.getWorldId()];
%>
<tr>
<td><a href="../user/queryUserInfo.jsp?id=<%=ru.getUserId()%>"><%=ru.getUserId()%></a></td>
<td><%if(w!=null){%><%=w.getFlag()%><%}%>-<%=ru.getWorldId()%></td>
<td><%=ru.getStatus()%></td>
<td><%=ru.getRole()%></td>
<td><%=ru.getMoney()%></td>
<td><%=ru.getSaving()%></td>
<td><%=ru.log.size()%>-<a href="rich3.jsp?id=<%=ru.getUserId()%>">查看</a></td>
</tr>
<%}%>
</table><br/>
		<a href="?all=1">显示全部</a><br/>
		<a href="?clearMoney=1" onclick="return confirm('清除所有无效用户的钱吗？')">清除无效数据</a><br/><br/>
		<a href="index.jsp">返回上一级</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>