<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!


%><%

CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("id");
List list;
if(userId==0)
	list = SqlUtil.getIntList("select id from team", 3);
else
	list = SqlUtil.getIntList("select team_id from team_user where user_id="+userId, 3);
PagingBean paging = new PagingBean(action, list.size(), 20, "p");
%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<table width="800">
<%

int endIndex = paging.getEndIndex();
for(int i = paging.getStartIndex();i < endIndex;i++){
TeamBean team = TeamAction.getTeam(((Integer)list.get(i)).intValue());
%><tr><td width=30><%=i+1%></td><td width="120"><a href="team.jsp?id=<%=team.getId()%>"><%=StringUtil.toWml(team.getName())%></a></td>
<td><%=team.getCount()%></td><td width="120"><%=DateUtil.formatDate2(team.getCreateTime())%></td>
<%
UserBean user = UserInfoUtil.getUser(team.getDuty0());
%><td><a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a></td><td><%=StringUtil.toWml(team.getInfo())%></td></tr>
<%}%>
</table>
<%=paging.shuzifenye("teams.jsp?id="+userId, true, "|", response)%>
<body>
</html>