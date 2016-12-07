<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};%><%
CustomAction action = new CustomAction(request, response);


int start = action.getParameterInt("start");
int end = action.getParameterInt("end");
if(end-start>100000)
	end = start + 100000;
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>银行排名</h3>
<table cellpadding=2><tr><td>用户名</td><td>存款</td><td></td></tr>
<%
	UserBean user = null;
	List top = SqlUtil.getIntsList("select user_id,money/100000000 from jc_bank_store_money where money>=50000000000 order by money desc");
	for(int i=0;i<top.size();i++){
	int[] res = (int[])top.get(i);
	user=UserInfoUtil.getUser(res[0]);
%>
<tr><td><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=res[0]%>"><%=user.getNickNameWml()%></a><%}else{%>(未知)<%}%></td><td align=right>
<%=res[1]%>
</td><td>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)</td></tr>
<%}%>
</table>
<h3>股市排名</h3>
<table cellpadding=2><tr><td>用户名</td><td>存款</td></tr>
<%
	top = SqlUtil.getIntsList("select user_id,(money+money_f)/100000000 from stock_account where money+money_f>=50000000000 order by money+money_f desc");
	for(int i=0;i<top.size();i++){
	int[] res = (int[])top.get(i);
	user=UserInfoUtil.getUser(res[0]);
%>
<tr><td><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=res[0]%>"><%=user.getNickNameWml()%></a><%}else{%>(未知)<%}%></td><td align=right>
<%=res[1]%>
</td></tr>
<%}%>
</table><br/>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>