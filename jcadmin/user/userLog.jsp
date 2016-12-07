<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static IUserService userService = ServiceFactory.createUserService();
Integer zero = new Integer(0);
static String[] typeName = {"","改密码","改昵称","改银行密码","短信改密码","短信改银密","改头像","改个人资料"};

%><%
boolean admin1 = group.isFlag(1);
CustomAction action = new CustomAction(request);
int id = action.getParameterInt("uid");
%>
<html>
<head>
</head>
<body>
<font color=red><%=id%>修改历史</font>
<a href="queryUserInfo.jsp?id=<%=id%>">查询该用户</a>
<table width="600" border="2">
<tr>
<td width="20"></td>
<td>时间</td>
<td align=center>类型</td>
<td>结果</td>
</tr>
<%
//显示封禁列表
List logList = SqlUtil.getObjectsList("select create_time,bak,type from user_log where user_id="+id+" order by create_time desc");
if(logList != null){
	Iterator itr = logList.iterator();
	int i = 1;
	while(itr.hasNext()){
	Object[] objs = (Object[])itr.next();
	int type=((Integer)objs[2]).intValue();
%>
<tr>
<td width="20"><%=i%></td>
<td width="120"><%=objs[0].toString().substring(2,19)%></td>
<td align=center width=100><%=typeName[type]%></td>
<td><%if(admin1||type==2||type==7||type==6){%><%=StringUtil.toWml((String)objs[1])%><%}else{%>(隐藏)<%}%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>

<body>
</html>