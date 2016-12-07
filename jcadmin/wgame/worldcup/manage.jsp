<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="jc.game.worldcup.*,jc.credit.UserInfo,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static int COUNT_PRE_PAGE = 10; %>
<%
WorldCupAction action = new WorldCupAction(request);
WcMatch wcMatch = null;
String tip = "";
int over = action.getParameterInt("o");
if (over > 0){
	wcMatch = WorldCupAction.service.getMatch(" id=" + over);
	if (wcMatch != null){
		SqlUtil.executeUpdate("update wc_match set flag=1 where id=" + over,5);
		// 结算比分
		action.statResult(wcMatch.getId());
	}
}
int show = action.getParameterInt("show");
if (show > 0){
	wcMatch = WorldCupAction.service.getMatch(" id=" + show);
	if (wcMatch != null){
		SqlUtil.executeUpdate("update wc_match set `show`=" + (wcMatch.getShow()==0?1:0) + " where id=" + show,5);
	}
}
int add = action.getParameterInt("add");
if (add == 1){
	String matchDate = action.getParameterNoEnter("matchDate");
	int hour = action.getParameterInt("hour");
	int minute = action.getParameterInt("minute");
	String team1 = action.getParameterNoEnter("team1");
	String team2 = action.getParameterNoEnter("team2");
	int win = action.getParameterInt("win");
	int tie = action.getParameterInt("tie");
	int lose = action.getParameterInt("lose");
	if (team1 == null || "".equals(team1) || team2 == null || "".equals(team2)){
		tip = "没有输入球队的名子？";
	} else	if (win <= 0 || lose <= 0){
		tip = "胜、负赔率不可<=0";
	} else {
		wcMatch = new WcMatch();
		StringBuilder sb = new StringBuilder();
		if ("".equals(matchDate)){
			tip = "没有输入开赛日期.";
		} else {
			sb.append(matchDate);
			sb.append(" ");
			if (hour < 10){
				sb.append("0");
			}
			sb.append(hour);
			sb.append("-");
			if (minute < 10){
				sb.append("0");
			}
			sb.append(minute);
			sb.append("-00");
			wcMatch.setMatchTimeStr(sb.toString());
			wcMatch.setTeam1(team1);
			wcMatch.setTeam2(team2);
			wcMatch.setWin(win);
			wcMatch.setTie(tie);
			wcMatch.setLose(lose);
			if (!action.addMatch(wcMatch)){
				tip = (String)request.getAttribute("tip");
			}
		}
	}
}
int totalCount = SqlUtil.getIntResult("select count(id) from wc_match",5);
PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
List list = WorldCupAction.service.getMatchList(" 1 order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>世界杯</title>
		<script language="JavaScript" src="/jcadmin/js/WebCalendar.js" ></script>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%=tip %><br/>
		[录入赛程]<br/>
		<form action="manage.jsp?add=1" method="post">
			开赛日期:<input type="text" size=14 name="matchDate" value="" onclick="SelectDate(this,'yyyy-MM-dd');"/><br/>
			开赛时间:<select name="hour">
					<%for (int i = 0 ; i <= 23 ; i++){
						%><option value="<%=i%>"><%=i%></option><%
					}%></select>时
					<select name="minute">
					<%for (int i = 0 ; i < 60 ; i++){
						%><option value="<%=i%>"><%=i%></option><%
					}%></select>分<br/>
			球队1:<input type="text" name="team1"><br/>
			球队2:<input type="text" name="team2" /><br/>
			<font color="red">如果想输入赔率1.3，请输入130。</font><br/>
			胜:<input type="text" name="win" /><br/>
			平:<input type="text" name="tie" /><br/>
			负:<input type="text" name="lose" /><br/>
			<input type="submit" value="添加" />
			<input type="button" value="返回" onClick="javascript:window.location.href='index.jsp'">
		</form>
		[已添加赛程]<br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>id</font>
				</td>
				<td align=center>
					<font color=#1A4578>开赛时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>球队1</font>
				</td>
				<td align=center>
					<font color=#1A4578>球队2</font>
				</td>
				<td align=center>
					<font color=#1A4578>胜</font>
				</td>
				<td align=center>
					<font color=#1A4578>平</font>
				</td>
				<td align=center>
					<font color=#1A4578>负</font>
				</td>
				<td align=center>
					<font color=#1A4578>队1进球</font>
				</td>
				<td align=center>
					<font color=#1A4578>队2进球</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<%if (list != null && list.size() > 0){
				for (int i = 0 ; i < list.size() ; i++){
					wcMatch = (WcMatch)list.get(i);
					if (wcMatch != null){
						%><tr>
							<td><%=wcMatch.getId()%></td>
							<td><%=DateUtil.formatSqlDatetime(wcMatch.getMatchTime())%></td>
							<td><%=wcMatch.getTeamWml1()%></td>
							<td><%=wcMatch.getTeamWml2()%></td>
							<td><%=wcMatch.getWinf()%></td>
							<td><%=wcMatch.getTief()%></td>
							<td><%=wcMatch.getLosef()%></td>
							<td><%=wcMatch.getScore1()%></td>
							<td><%=wcMatch.getScore2()%></td>
							<td><%if(wcMatch.getFlag()==1){%>比赛已关闭.<%}else{%><a href="manage.jsp?show=<%=wcMatch.getId()%>"><%if (wcMatch.getShow()==0){%>显示<%}else{%>关闭<%}%></a>|<a href="modify.jsp?mid=<%=wcMatch.getId()%>">修改</a>|<a href="score.jsp?mid=<%=wcMatch.getId()%>">比分</a>|<a href="manage.jsp?o=<%=wcMatch.getId()%>" onclick="return confirm('确定要结束吗？')">结束</a><%}%></td>
						</tr><%
					}
				}
			}%>
		</table>
		<%=paging==null?"":paging.shuzifenye("manage.jsp", false, "|", response)%>
	</body>
</html>