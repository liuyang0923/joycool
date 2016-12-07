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
<h3>今日问答排名</h3>
<table cellpadding=2><tr><td>用户名</td><td></td><td>创关成功</td><td>创关次数</td><td>成功比例</td><td>总创关成功</td><td>ip</td><td>ua</td></tr>
<%
	UserBean user = null;
	List top = SqlUtil.getIntsList("select id,todayValue,totalValue,today2 from game_question_history order by today2 desc limit 30");
	for(int i=0;i<top.size();i++){
	int[] res = (int[])top.get(i);
	user=UserInfoUtil.getUser(res[0]);
	float rate = (float)res[1]*100/res[3];
%>
<tr>
<td><%=res[0]%></td>
<td><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=res[0]%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)<%}else{%>(未知)<%}%></td>
<td align=right><%=res[1]%></td>
<td align=right><%=res[3]%></td>
<td align=right><font color=<%if(rate>=60){%>red<%}else{%>black<%}%>><%=StringUtil.numberFormat(rate)%>%</font></td>
<td align=right><%=res[2]%></td>
<td><%=user.getIpAddress()%></td><td width="300"><%=user.getUserAgent()%></td>
</tr>
<%}%>
</table>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>