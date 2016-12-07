<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10;
	public static String[] status = {"未审","通过审核","未通过审核","取消资格"};%>
<%
MatchAction action = new MatchAction(request);
MatchUser matchUser = null;
MatchArea matchArea = null;
MatchRank rank = null;
int recover = action.getParameterInt("r");
int cancel = action.getParameterInt("c");
if (cancel > 0){
	// 取消参赛
	SqlUtil.executeUpdate("update match_user set checked=3 where user_id=" + cancel,5);
	matchUser = MatchAction.getMatchUser(cancel);
	if (matchUser != null){
		matchArea = MatchAction.getArea(matchUser.getAreaId());	// 取得此用户所在的分区bean
		matchUser.setChecked(3);
		//改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
	}
	// 被取消的用户所在的分区人数-1
	SqlUtil.executeUpdate("update match_area set `count`=`count`-1 where id=(select area_id from match_user where user_id=" + cancel + ")",5);
	if (matchArea != null){
		matchArea.setCount(matchArea.getCount() - 1);
	}
}
if (recover > 0){
	SqlUtil.executeUpdate("update match_user set checked=0 where user_id=" + recover,5);
	matchUser = MatchAction.getMatchUser(recover);
	if (matchUser != null){
		matchArea = MatchAction.getArea(matchUser.getAreaId());
		matchUser.setChecked(0);
		//改缓存
		MatchAction.matchUserCache.put(new Integer(matchUser.getUserId()),matchUser);
	}
	// 被取消的用户所在的分区人数+1
	SqlUtil.executeUpdate("update match_area set `count`=`count`+1 where id=(select area_id from match_user where user_id=" + recover + ")",5);
	if(matchArea != null){
		matchArea.setCount(matchArea.getCount() + 1);
	}
}
MatchInfo matchInfo = MatchAction.getCurrentMatch();
int playerCount = (matchInfo != null ? matchInfo.getUserCount() : 0);
PagingBean paging = new PagingBean(action,playerCount,COUNT_PRE_PAGE,"p");
List list = MatchAction.service.getRankList(" 1 limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>参赛用户列表</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户</font>
				</td>
				<td align=center>
					<font color=#1A4578>宣言</font>
				</td>
				<td align=center>
					<font color=#1A4578>最新上传</font>
				</td>
				<td align=center>
					<font color=#1A4578>状态</font>
				</td>
				<td align=center>
					<font color=#1A4578>当前积分/总积分</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<% for (int i = 0 ; i < list.size() ; i++){
				rank = (MatchRank)list.get(i);
				if (rank != null){
					matchUser = MatchAction.getMatchUser(rank.getUserId());
					if (matchUser != null){
						%><tr>
								<td><%=rank.getId()%></td>
								<td><%=UserInfoUtil.getUser(rank.getUserId()).getNickNameWml()%>(<%=rank.getUserId()%>)</td>
								<td><%=matchUser.getEncounceWml()%></td>
								<td><img src="<%=action.getCurrentPhoto(matchUser,false)%>" alt="无法显示" /></td>
								<td><%=status[matchUser.getChecked()]%></td>
								<td><%=matchUser.getVoteCount()-matchUser.getConsume()%>/<%=matchUser.getVoteCount()%></td>
								<%if (matchUser.getChecked() == 3){
									%><td><a href="player.jsp?r=<%=matchUser.getUserId()%>" onclick="return confirm('真的要恢复她的资格吗？')">恢复资格</a></td><%
								} else {
									%><td><a href="player.jsp?c=<%=matchUser.getUserId()%>" onclick="return confirm('真的要取消她的资格吗？')">取消资格</a></td><%
								}%>
								
						  </tr><%
					}
				}
			}%>
		</table>
		<%=paging!=null?paging.shuzifenye("player.jsp",false,"|",response):""%>
	</body>
</html>