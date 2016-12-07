<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10; %>
<%
MatchAction action = new MatchAction(request);
UserInfo userInfo = null;
MatchUser matchUser = null;
MatchEnouHistory history = null;
List list = null;
PagingBean paging = null;
String tip = "";
int see = action.getParameterInt("see");
int del = action.getParameterInt("del");
int del2 = action.getParameterInt("del2");
// 删除
if (del > 0){
	SqlUtil.executeUpdate("delete from match_enou_history where id=" + del,5);
}
// 删除用户表中的最新宣言
if (del2 > 0){
	SqlUtil.executeUpdate("update match_user set enounce='' where user_id=" + del2,5);
	matchUser = MatchAction.getMatchUser(del2);
	if (matchUser != null){
		matchUser.setEncounce("");
		// 改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
	}
}
// 查看
if (see > 0){
	matchUser = MatchAction.getMatchUser(see);
	if (matchUser != null){
		paging = new PagingBean(action,20,COUNT_PRE_PAGE,"p");
		list = MatchAction.service.getEnouHistoryList(" user_id=" + matchUser.getUserId() + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	}
} else {
	MatchInfo matchInfo = MatchAction.getCurrentMatch();
	int playerCount = (matchInfo != null ? matchInfo.getUserCount() : 0);
	paging = new PagingBean(action,playerCount,COUNT_PRE_PAGE,"p");
	list = MatchAction.service.getMatchUserList(" 1 order by create_time desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}
%>
<html>
	<head>
		<title>审核参赛用户</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a>|<a href="enounce.jsp">查看全部</a><br/>
		<%if ("".equals(tip)){
			%><table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>
						<font color=#1A4578>用户</font>
					</td>
					<%if (see > 0){
						%><td align=center>
						  		<font color=#1A4578>宣言</font>
						  </td>
						<%
					  }%>
					<td align=center>
						<font color=#1A4578>宣言</font>
					</td>
					<td align=center>
						<font color=#1A4578>审核</font>
					</td>
				</tr>
				<%
				if (see > 0){
					for (int i = 0 ; i < list.size() ; i++){
						history = (MatchEnouHistory)list.get(i);
						if (history != null){
							%><tr>
									<td><%=UserInfoUtil.getUser(history.getUserId()).getNickNameWml()%></td>
									<td><%=DateUtil.formatSqlDatetime(history.getCreateTime())%></td>
									<td><%=history.getEnounceWml()%></td>
									<td><a href="enounce.jsp?del=<%=history.getId()%>" onclick="return confirm('真的要删除？')">删除</a></td>
							  </tr><%
						}
					}
				} else {
					for (int i = 0 ; i < list.size() ; i++){
						matchUser = (MatchUser)list.get(i);
						if (matchUser != null){
							%><tr>
									<td><%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getUserId()%>)</td>
									<td><%=matchUser.getEncounceWml()%></td>
									<td><a href="enounce.jsp?see=<%=matchUser.getUserId()%>">查看</a>|<a href="enounce.jsp?del2=<%=matchUser.getUserId()%>" onclick="return confirm('真的要删除？')">删除</a></td>
							  </tr><%
						}
					}
				}%>
			  </table>
			  <%=paging!=null?paging.shuzifenye("enounce.jsp?see=" + see,true,"|",response):""%>
			  <%
		} else {
			%><%=tip%><br/><%
		}
		%>
	</body>
</html>