<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static IUserService userService = ServiceFactory.createUserService();
Integer zero = new Integer(0);
static String[] statusMap = {"封禁","解封"};
static HashMap groupMap = new HashMap();
static{
groupMap.put("chat","聊天");
groupMap.put("home","家园");
groupMap.put("tong","帮会");
groupMap.put("mail","信件");
groupMap.put("info","资料");
groupMap.put("game","游戏");
groupMap.put("team","圈子");
groupMap.put("forum","论坛");
}
%><%

CustomAction action = new CustomAction(request);
int id = action.getParameterInt("uid");
%>
<html>
<head>
</head>
<body>
forbid_log<br/>
<font color=red><%=id%>案底</font>
<a href="queryUserInfo.jsp?id=<%=id%>">查询该用户</a>
<table width="100%" border="2">
<tr>
<td width="10%"></td>
<td>时间</td>
<td>执行人</td>
<td>封禁组</td>
<td>描述</td>
<td>状态</td>
<td width="10%">时间</td>
</tr>
<%
//显示封禁列表
List forbidList = SqlUtil.getObjectsList("select create_time,`group`,bak,status,time,operator,id from forbid_log where user_id="+id+" order by create_time desc");
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	int i = 1;
	while(itr.hasNext()){
	Object[] objs = (Object[])itr.next();
Object obj1 = groupMap.get(objs[1].toString());
%>
<tr>
<td width="10%"><%=objs[6]%></td>
<td width="120"><%=objs[0].toString().substring(2,19)%></td>
<td><%
int oper = ((Long)objs[5]).intValue();
if(oper==0){%>管理员<%}else{%><a href="queryUserInfo.jsp?id=<%=oper%>"><%=UserInfoUtil.getUser(oper).getNickName()%>(<%=oper%>)</a><%}%></td>
<td><%=obj1==null?objs[1]:obj1%></td>
<td><%=objs[2]%></td>
<td><%=statusMap[((Integer)objs[3]).intValue()]%></td>
<td width="10%"><%=objs[4]%>分钟</td>
</tr>
<%
	    i ++;
	}
}
%>
</table>

<body>
</html>