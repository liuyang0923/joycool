<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static IUserService userService = ServiceFactory.createUserService();
Integer zero = new Integer(0);
static String[] typeName = {"","改密码","改昵称","改银行密码","短信改密码","短信改银密","改头像","改个人资料"};

%><%

CustomAction action = new CustomAction(request);
int id = action.getParameterInt("uid");
PagingBean paging = new PagingBean(action,10000,30,"p");
%>
<html>
<head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<font color=red><%=id%>用户资料审查</font>
<a href="queryUserInfo.jsp?id=<%=id%>">查询该用户</a>
<table width="600" border="2">
<tr>
<td></td>
<td>用户</td>
<td>时间</td>
<td align=center>类型</td>
<td>结果</td>
</tr>
<%
//显示封禁列表
List logList = SqlUtil.getObjectsList("select create_time,bak,type,user_id,id from user_log where type in(2,6,7) order by id desc limit " + paging.getStartIndex()+",30");
if(logList != null){
	Iterator itr = logList.iterator();
	int i = 1;
	while(itr.hasNext()){
	Object[] objs = (Object[])itr.next();
	int type=((Integer)objs[2]).intValue();
%>
<tr>
<td width="50"><%=objs[4]%></td>
<td width="80"><a href="../user/info.jsp?id=<%=objs[3]%>"><%=objs[3]%></a></td>
<td width="120"><%=objs[0].toString().substring(2,19)%></td>
<td align=center width=100><%=typeName[type]%></td>
<td><%if(type==6){%><img src="/rep/friend/attach/<%=objs[1]%>" alt="x"/><%}else{%><%=StringUtil.toWml((String)objs[1])%><%}%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<%=paging.shuzifenye("head.jsp",false,"|",response)%><br/>
<p align="center"><a href="../chatmanage.jsp">返回</a></p>
<body>
</html>