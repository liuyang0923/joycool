<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.cache.ICacheMap,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=1; 
	static String[] checked = {"未审","通过","未通过","取消资格"};
%>
<%
MatchAction action = new MatchAction(request);
MatchUser matchUser = null;
MatchFans matchFans = null;
String tip = "";
Integer tmp = null;
ICacheMap userMap = MatchAction.matchUserCache;	// 用户缓存
ICacheMap fansMap = MatchAction.matchFansCache;	// 粉丝缓存
int in = action.getParameterInt("in");
if (in > 0){
	long interval = action.getParameterLong("interval");
	// 修改间隔时间
	MatchAction.setSTART_RANK_INTERVAL(interval);
	tip = "间隔时间修改成功.";
}
int clearUid = action.getParameterInt("cu");
if (clearUid > 0){
	// 从user缓存中删除一位用户
	userMap.rm(new Integer(clearUid));
}
int fansUid = action.getParameterInt("fu");
if (fansUid > 0){
	// 从fans缓存中删除一位用户
	fansMap.rm(new Integer(fansUid));
}
int clear = action.getParameterInt("c");
if (clear == 1){
	// 清空user缓存
	userMap.clear();
	tip = "user缓存已清空";
} else if (clear == 2){
	// 清空fans缓存
	fansMap.clear();
	tip = "fans缓存已清空";
} else if (clear == 3){
	// 清空当前比赛
	//MatchAction.setMatchNow(null);
	MatchAction.matchNow = null;
	tip = "当前比赛已清空";
} else if (clear == 4){
	// 清空分区缓存
	MatchAction.areaMap.clear();
	tip = "分区缓存已清除";
}
int type = action.getParameterInt("t");
%>
<html>
	<head>
		<title>缓存查询</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a>|<a href="cache.jsp">刷新</a><br/>
		<%=tip%><br/>
		<% if (type == 0){
			// 查看缓存列表
			%><table border=1 width=100% align=center>
					<tr bgcolor=#C6EAF5>
						<td align=center><font color=#1A4578>缓存名</font></td>
						<td align=center><font color=#1A4578>数量</font></td>
						<td align=center><font color=#1A4578>操作</font></td>
					</tr>
					<tr>
						<td>MatchUser</td>
						<td><%=userMap.size()%></td>
						<td><a href="cache.jsp?t=1">查看</a>|<a href="cache.jsp?c=1" onclick="return confirm('真的要清空吗?')">清空</a></td>
					</tr>
					<tr>
						<td>MatchFans</td>
						<td><%=fansMap.size()%></td>
						<td><a href="cache.jsp?t=2">查看</a>|<a href="cache.jsp?c=2" onclick="return confirm('真的要清空吗?')">清空</a></td>
					</tr>
					<tr>
						<td>MatchArea</td>
						<td><%=MatchAction.areaMap.size()%></td>
						<td><a href="cache.jsp?c=4" onclick="return confirm('真的要清空吗?')">清空</a></td>
					</tr>
					<tr>
						<td>matchNow(当前比赛)</td>
						<td>...</td>
						<td><a href="cache.jsp?t=3">查看</a>|<a href="cache.jsp?c=3" onclick="return confirm('真的要清空吗?')">清空</a></td>
					</tr>
			   </table>
			   请输入比赛时间的毫秒数。1秒=1000毫秒。<br/>
			   比赛人数/粉丝人数如果在30人(含)以下，则时时更新，间隔变量不生效。<br/>
			   MatchAction.START_RANK_INTERVAL<br/>
		   	   <form action="cache.jsp?in=1" method="post">
					修改排名间隔时间:<input type="text" name="interval" value="<%=MatchAction.START_RANK_INTERVAL%>"/>
					<input type="submit" value="修改" /><br/>
			   </form>
			   常用时间列表:<br/>
			   15分钟=15*60*1000=900000<br/>
			   30分钟=30*60*1000=1800000<br/>
			   1小时=60*60*1000=3600000<br/>
			   2小时=120*60*1000=7200000<br/><%
		} else if (type == 1){
			// 查看user缓存详细信息
			%><a href="cache.jsp">回列表</a>|<a href="cache.jsp?t=1">刷新本页面</a><br/>
			<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center><font color=#1A4578>uid</font></td>
					<td align=center><font color=#1A4578>本期靓点</font></td>
					<td align=center><font color=#1A4578>宣言</font></td>
					<td align=center><font color=#1A4578>照片</font></td>
					<td align=center><font color=#1A4578>checked</font></td>
					<td align=center><font color=#1A4578>创建时间</font></td>
					<td align=center><font color=#1A4578>总靓点</font></td>
					<td align=center><font color=#1A4578>已消费靓点</font></td>
					<td align=center><font color=#1A4578>操作</font></td>
				</tr>
				<%List list = userMap.keyList();
				  if (list != null && list.size() > 0){
					  for (int i = 0 ; i < list.size() ; i++){
						 tmp = Integer.valueOf(list.get(i).toString());
						 matchUser = (MatchUser)userMap.get(tmp);
						 if (matchUser != null){
							 %><tr>
							 		<td><%=matchUser.getUserId()%></td>
							 		<td><%=matchUser.getVoteCount()%></td>
							 		<td><%=matchUser.getEncounceWml()%></td>
							 		<td><%=matchUser.getPhoto()%></td>
							 		<td><%=checked[matchUser.getChecked()]%></td>
							 		<td><%=DateUtil.formatSqlDatetime(matchUser.getCreateTime())%></td>
							 		<td><%=matchUser.getTotalVote()%></td>
							 		<td><%=matchUser.getTotalConsume()%></td>
							 		<td><a href="cache.jsp?cu=<%=matchUser.getUserId()%>&t=1" onclick="return confirm('确实要删除她吗?')">删除</a></td>
							    </tr><%
						 }
					  }
				  }%>
			</table>
			<%
		} else if (type == 2){
			// 查看fans缓存详细信息
			%><a href="cache.jsp">回列表</a>|<a href="cache.jsp?t=2">刷新本页面</a><br/>
			<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center><font color=#1A4578>uid</font></td>
					<td align=center><font color=#1A4578>钻石</font></td>
					<td align=center><font color=#1A4578>蓝宝石</font></td>
					<td align=center><font color=#1A4578>翡翠石</font></td>
					<td align=center><font color=#1A4578>金珍珠</font></td>
					<td align=center><font color=#1A4578>玫瑰花</font></td>
					<td align=center><font color=#1A4578>塑料花</font></td>
					<td align=center><font color=#1A4578>物品6</font></td>
					<td align=center><font color=#1A4578>物品7</font></td>
					<td align=center><font color=#1A4578>总价格</font></td>
					<td align=center><font color=#1A4578>操作</font></td>
				</tr>
				<%List list = fansMap.keyList();
				  if (list != null && list.size() > 0){
					  for (int i = 0 ; i < list.size() ; i++){
						 int good[] = new int[8];
						 tmp = Integer.valueOf(list.get(i).toString());
						 matchFans = (MatchFans)fansMap.get(tmp);
						 if (matchFans != null){
							 good = matchFans.getGood();
							 %><tr>
							 		<td><%=matchFans.getUserId()%></td>
									<%for (int j = 0 ; j < good.length ; j++){
										%><td><%=good[j]%></td><%	
									}%>
									<td><%=matchFans.getPrices()%></td>
									<td><a href="cache.jsp?fu=<%=matchFans.getUserId()%>&t=2" onclick="return confirm('确实要删除吗?')">删除</a></td>
							    </tr><%
						 }
					  }
				  }%>
			</table>
			<%
		} else if (type == 3){
			// 查看当前比赛
			%><a href="cache.jsp">回列表</a>|<a href="cache.jsp?t=3">刷新本页面</a><br/>
			<%if (MatchAction.matchNow != null){
				%>当前比赛名称:<%=MatchAction.matchNow.getTitleWml()%><br/>
				  开赛时间:<%=DateUtil.formatSqlDatetime(MatchAction.matchNow.getStartTime())%><br/>
				  毕赛时间:<%=DateUtil.formatSqlDatetime(MatchAction.matchNow.getEndTime())%><br/>
				  参赛总人数:<%=MatchAction.matchNow.getUserCount()%><br/>
				  投票总人数:<%=MatchAction.matchNow.getFansCount()%><br/><%
			  } else {
				%>空.<%
			  }
		}%>
	</body>
</html>