<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static String status[] = {"尚未开赛","进行中","已结束"}; %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
List list = null;
MatchInfo matchInfo = null;
int del = action.getParameterInt("d");
if (del > 0){
	matchInfo = MatchAction.service.getInfo(" id=" + del);
	if (matchInfo != null && matchInfo.getFalg() == 0){
		SqlUtil.executeUpdate("delete from match_info where flag=0 and id=" + del,5);
	} else {
		tip = "要删除的赛事不存在.";
	}
}
int type = action.getParameterInt("t");
if (type == 0){
	list = MatchAction.service.getInfoList(" 1 order by id desc");
} else if (type == 1){
	// 添加赛事
	int submit = action.getParameterInt("s");
	if (submit == 1){
		matchInfo = new MatchInfo();
		String title = action.getParameterNoEnter("title");
		if (title == null || "".equals(title) || title.length() > 100){
			tip = "没有填写标题或标题太长.";
		} else {
			matchInfo.setTitle(title);
			String startDate = action.getParameterNoEnter("startDate");
			String endDate = action.getParameterNoEnter("endDate");
			String[] startTmp = startDate.split("-");
			String[] endTmp = endDate.split("-");
			if (startTmp.length == 3 && endTmp.length == 3){
				if (action.isDateExist(StringUtil.toInt(startTmp[0]),StringUtil.toInt(startTmp[1]),StringUtil.toInt(startTmp[2]))==false
						|| action.isDateExist(StringUtil.toInt(endTmp[0]),StringUtil.toInt(endTmp[1]),StringUtil.toInt(endTmp[2]))==false){
					tip = "时间输入错误.";
				} else {
					matchInfo.setStartTimeStr(startDate);
					matchInfo.setEndTimeStr(endDate);
					if (!action.createMatch(matchInfo)){
						tip = (String)request.getAttribute("tip");
					} else {
						tip = "添加成功";
					}
				}
			} else {
				tip = "时间输入错误.";
			}
		}
	}
} else if (type == 2){
	// 查看赛事
	int matchId = action.getParameterInt("iid");
	if (matchId <= 0){
		tip = "您要查看的赛事不存在.";
	} else {
		matchInfo = MatchAction.service.getInfo(" id=" + matchId);
		if (matchInfo == null){
			tip = "您要查看的赛事不存在.";
		}
	}
} else if (type == 3){
	// 编辑赛事
	int id = action.getParameterInt("id");
	matchInfo = MatchAction.service.getInfo(" id=" + id);
	if (matchInfo == null){
		tip = "要查看的赛事不存在.";
	} else {
		int submit = action.getParameterInt("s");
		if (submit == 1){
			String title = action.getParameterNoEnter("title");
			String startTime = "";
			if (title == null || "".equals(title) || title.length() > 100){
				tip = "没有填写标题或标题太长.";
			} else {
				if (matchInfo.getFalg() == 0){
					String startDate = action.getParameterNoEnter("startDate");
					String endDate = action.getParameterNoEnter("endDate");
					String[] startTmp = startDate.split("-");
					String[] endTmp = endDate.split("-");
					if (startTmp.length == 3 && endTmp.length == 3){
						if (action.isDateExist(StringUtil.toInt(startTmp[0]),StringUtil.toInt(startTmp[1]),StringUtil.toInt(startTmp[2]))==false
								|| action.isDateExist(StringUtil.toInt(endTmp[0]),StringUtil.toInt(endTmp[1]),StringUtil.toInt(endTmp[2]))==false){
							tip = "时间输入错误.";
						} else {
							SqlUtil.executeUpdate("update match_info set title='" + StringUtil.toSql(title) + "',start_time='" + startDate + "',end_time='" + endDate + "' where id=" + matchInfo.getId(),5);
							response.sendRedirect("info.jsp");
							return;
						}
					} else {
						tip = "时间输入错误.";
					}
				} else if (matchInfo.getFalg() == 1){
					// 比赛进行中,只改名子。
					SqlUtil.executeUpdate("update match_info set title='" + StringUtil.toSql(title) + "' where id=" + matchInfo.getId(),5);
					if (MatchAction.matchNow != null){
						MatchAction.matchNow.setTitle(title);
					}
					response.sendRedirect("info.jsp");
					return;
				}

			}
		}
	}
}
%>
<html>
	<head>
		<title>信息查询</title>
<script type="text/javascript">
function checkDate(year,month,day){
	var startDate=new Date(year.substring(2),month-1,day,0,0,0);
	if(startDate.getDate()!=day){
		alert ('日期错误.');
	}
}
</script>
<script language="JavaScript" src="/jcadmin/js/WebCalendar.js" ></script>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>
		<%=tip%><br/>
		<%if (type == 0){
			%><%if (list != null && list.size() > 0){
					for (int i = 0 ; i < list.size() ; i++){
						matchInfo = (MatchInfo)list.get(i);
						if (matchInfo != null){
							%><a href="info.jsp?t=2&iid=<%=matchInfo.getId()%>"><%=matchInfo.getTitleWml()%>(<%=status[matchInfo.getFalg()]%>)</a>
							<%if (matchInfo.getFalg() < 2){
								%>|<a href="info.jsp?id=<%=matchInfo.getId()%>&t=3">编辑</a>
								<%if (matchInfo.getFalg() == 0){
										%>|<a href="info.jsp?d=<%=matchInfo.getId()%>" onclick="return confirm('确实要删除本次比赛吗？')">删除</a><%
								  }
							}%><br/><%
						}
					}
				}%><br/><a href="info.jsp?t=1">新建活动</a><br/><%
		} else if (type == 1){
			%><form action="info.jsp?t=1&s=1" method="post">
				题目:<input type="text" name="title" /><br/>
				开始日期:<input type="text" size=14 name="startDate" value="" onclick="SelectDate(this,'yyyy-MM-dd');"/><br/>
				结束日期:<input type="text" size=14 name="endDate" value="" onclick="SelectDate(this,'yyyy-MM-dd');"/><br/>
			  	<input type="submit" value="确认"><input type="button" value="返回" onClick="document.location='info.jsp'"/>
			  </form><%
		} else if (type == 2){
			if (matchInfo != null){
				%>题目:<%=matchInfo.getTitleWml()%><br/>
				  开始:<%=DateUtil.formatSqlDatetime(matchInfo.getStartTime())%><br/>
				  结束:<%=DateUtil.formatSqlDatetime(matchInfo.getEndTime())%>(<%=status[matchInfo.getFalg()]%>)<br/>
				  当前参赛人数:<%=matchInfo.getUserCount()%><br/>
				  当前投票人数:<%=matchInfo.getFansCount()%><br/>
				  当前投票靓点:<%=matchInfo.getVoteCount()%><br/>
				  当前酷币靓点:<%=matchInfo.getPointCount()%><br/>
				  当前乐币靓点:<%=matchInfo.getGamePointCount()%><br/>
				<%
			}%><a href="info.jsp">返回上一页</a><br/><%
		} else if (type == 3){
			%>编辑比赛:<br/>原比赛信息如下:<br/>
			<%if (matchInfo != null){
			  	%>开始:<%=DateUtil.formatSqlDatetime(matchInfo.getStartTime())%><br/>
			  	  结束:<%=DateUtil.formatSqlDatetime(matchInfo.getEndTime())%><br/>------------------------<br/>
			  	  <form action="info.jsp?t=3&s=1&id=<%=matchInfo.getId()%>" method="post">
					题目:<input type="text" name="title" value="<%=matchInfo.getTitleWml()%>"/><br/>
					<%if (matchInfo.getFalg() == 0){
						%>开始日期:<input type="text" size=14 name="startDate" value="<%=DateUtil.formatSqlDatetime(matchInfo.getStartTime())%>" onclick="SelectDate(this,'yyyy-MM-dd');"/><br/>
						  结束日期:<input type="text" size=14 name="endDate" value="<%=DateUtil.formatSqlDatetime(matchInfo.getEndTime())%>" onclick="SelectDate(this,'yyyy-MM-dd');"/><br/><%	
					}%>
				  	<input type="submit" value="确认"><input type="button" value="返回" onClick="document.location='info.jsp'"/>
			  	<%
			  }
		}
		%>
	</body>
</html>