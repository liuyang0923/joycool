<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] levelNames={"?","超级管理员","高级管理员","(废弃)","普通管理员","编辑"};
%><%
if(!group.isFlag(0)) return;
CustomAction action = new CustomAction(request, response);

int adminId = action.getParameterInt("aid");
AdminUserBean adminUser2 = AdminAction.getAdminUser("id=" + adminId);
if(adminUser2==null) adminId=0;

List quotaList = SqlUtil.getIntsList("select id,money/100000000 from admin_quota");
Map quotaMap = new HashMap(quotaList.size());
for(int i=0;i<quotaList.size();i++){
	int[] ints = (int[])quotaList.get(i);
	quotaMap.put(new Integer(ints[0]),new Integer(ints[1]));
}


long money2 = action.getParameterIntS("money");
if(!action.isMethodGet() && adminId>0 && money2>=0){
	Integer oldMoney = (Integer)quotaMap.get(adminUser2.id);

	LogUtil.logAdmin(adminUser.getName()+"把管理员"+adminUser2.getName()+"的配额从 "+oldMoney + "亿改为 "+money2+"亿");
	if(oldMoney==null)
		SqlUtil.executeUpdate("insert into admin_quota set money=" + (money2*100000000)+",id="+adminId+",name='"+StringUtil.toSql(adminUser2.getName())+"'");
	else
		SqlUtil.executeUpdate("update admin_quota set money=" + (money2*100000000)+" where id="+adminId);
	response.sendRedirect("quota.jsp");
	return;
}

String username=request.getParameter("username");
String password=request.getParameter("password");
int level = action.getParameterIntS("level");
int moduserid = action.getParameterIntS("moduser");

if(!action.isMethodGet() &&moduserid<=0&& username!=null && username.length()>0 && password!=null&&level>0){
	LogUtil.logAdmin(adminUser.getName()+"添加了管理员"+username);
	SqlUtil.executeUpdate("insert into admin set name='"+StringUtil.toSql(username)+"',password='"+StringUtil.toSql(password)+"',group_id="+level+",create_datetime=now()");
	response.sendRedirect("quota.jsp");
	return;
}

AdminUserBean moduser=AdminAction.getAdminUser("id=" + moduserid);;
if(!action.isMethodGet() && moduser!=null){
	if(password.length()==0)
		password=moduser.getPassword();
	SqlUtil.executeUpdate("update admin set name='"+StringUtil.toSql(username)+"',password='"+StringUtil.toSql(password)+"',group_id="+level+" where id=" + moduser.id);
	response.sendRedirect("quota.jsp");
	return;
}
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%
	List adminList = AdminAction.getAdminUserList("disabled=0 order by id");
%>
<table width="340">
<tr>
	<td align="center">
		账户名
	</td>
	<td align="center">
		级别
	</td>
	<td align="center">
		乐币配额
	</td>
	<td align="center">
		道具配额
	</td>
</tr>
	<%for (int i = 0; i < adminList.size(); i++) {
		AdminUserBean au = (AdminUserBean)adminList.get(i);
		Integer money = (Integer)quotaMap.get(au.id);
%>
	<tr>
		<td align="center">
			<a href="quota.jsp?moduser=<%=au.id%>"><%=au.getName()%></a>
		</td>
		<td align="center">
			<%=levelNames[au.getGroupId()]%>
		</td>
		<td align=right>
			<%if(money!=null){%><%=money%><%}else{%>0<%}%> 亿 <a href="quota.jsp?aid=<%=au.id%>">修改</a>
		</td>
		<td align="center">
			<a href="quota2.jsp?aid=<%=au.getId()%>">查看详细</a>
		</td>
	</tr>
	<%}%>
</table><br/>
<%if(adminId>0){%>
<form action="quota.jsp?aid=<%=adminId%>" method="post">
【<%=adminUser2.getName()%>】<br/>
修改配额：<input type=text name="money" value="">亿
<input type=submit value="确认修改"><br/>
</form>
<br/>
<%}%>
<%if(moduser!=null){%>
<form action="quota.jsp" method="post">
【修改管理员】<br/>
<input type=hidden name="moduser" value="<%=moduserid%>">
用户名：<input type=text name="username" value="<%=StringUtil.toWml(moduser.getName())%>"><br/>
重置密码：<input type=text name="password" value="">(不想修改就留空)<br/>
级别：<%for(int i=1;i<levelNames.length;i++){%>
<input type=radio name="level" value="<%=i%>"<%if(moduser.groupId==i){%> checked="checked"<%}%>><%=levelNames[i]%>
<%}%>
<br/>
<input type=submit value="确认修改"><br/>
</form>
<br/>
<%}else{%>
<form action="quota.jsp" method="post" onsubmit="return confirm('确认添加？')">
【添加新管理员】<br/>
用户名：<input type=text name="username" value=""><br/>
初始密码：<input type=text name="password" value=""><br/>
级别：<%for(int i=1;i<levelNames.length;i++){%>
<input type=radio name="level" value="<%=i%>" checked="checked"><%=levelNames[i]%>
<%}%>
<br/>
<input type=submit value="确认添加"><br/>
</form>
<br/>
<%}%>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
