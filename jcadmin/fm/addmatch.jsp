<%@ page language="java"
	import="java.util.*,jc.family.game.*,net.joycool.wap.util.*,jc.family.game.boat.*"
	pageEncoding="utf-8"%>
<%
	GameAction action = new GameAction(request, response);
	String[] weekDay = { "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
	String[] weekGame = { "", "龙舟", "雪仗", "问答" };
	String[] state = { "生效", "不生效" };
	int id = action.getParameterInt("id");
	WeekMatchBean bean = null;
	if (id > 0) {
		bean = GameAction.service.getWeekMatchBean(" id=" + id);
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>添加周赛事</title>
		<link href="../farm/common.css" rel="stylesheet" type="text/css">
	</head>
	
	<body>
		<p style="color: red;"><%=1 == action.getParameterInt("a") ? "名称不能为空!" : ""%></p>
		<form action="weekgame.jsp?a=1" method="post">
			<%
				if (bean != null) {
					Calendar cal = Calendar.getInstance();
					int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
			%>
			<input type="hidden" name="id" value="<%=bean.getId()%>" />
			<table border="1">
				<tr>
					<td>
						日期
					</td>
					<td>
						<select name="weekday">
							<%
								for (int i = 0; i < weekDay.length; i++) {
							%><option value="<%=i%>" <%if(i==bean.getWeekDay()){%> selected="selected" <%}%>><%=weekDay[i]%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						时间
					</td>
					<td>
						<input type="text" id="time2" name="startTime" size="6" value="<%=DateUtil.formatTime(bean.getStarttime())%>" />
						到
						<input type="text" id="time3" name="endTime" size="6" value="<%=DateUtil.formatTime(bean.getEndtime())%>" />格式:(hh:mm)
					</td>
				</tr>
				<tr>
					<td>
						活动
					</td>
					<td>
						<select name="type">
							<%
								for (int i = 1; i < weekGame.length; i++) {
							%><option value="<%=i%>" <%if(i==bean.getType()){%> selected="selected" <%}%>><%=weekGame[i]%></option>
							<%
								}
							%>
						</select>
						分
					</td>
				</tr>
				<tr>
					<td>
						状态
					</td>
					<td>
						<select name="effect">
							<%
								for (int i = 0; i < state.length; i++) {
							%><option value="<%=i%>" <%if(i==bean.getEffect()){%> selected="selected" <%}%>><%=state[i]%></option>
							<%
								}
							%>
						</select>
						分
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" value="修改" />
					</td>
					<td>
						<a href="weekgame.jsp">取消</a>
					</td>
				</tr>
			</table>
			<%
				if (bean.getWeekDay() == weekday) {
			%>
			<a href="todaygame.jsp?wid=<%=bean.getId()%>">立刻开赛</a>
			<br />
			<%
				}
				} else {
			%>
			<table border="1">
				<tr>
					<td>
						日期
					</td>
					<td>
						<select name="weekday">
							<%
								for (int i = 0; i < weekDay.length; i++) {
							%><option value="<%=i%>"><%=weekDay[i]%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						时间
					</td>
					<td>
						<input type="text" id="time2" name="startTime" size="6" />
						到
						<input type="text" id="time3" name="endTime" size="6" />格式:(hh:mm)
					</td>
				</tr>
				<tr>
					<td>
						活动
					</td>
					<td>
						<select name="type">
							<%
								for (int i = 1; i < weekGame.length; i++) {
							%><option value="<%=i%>"><%=weekGame[i]%></option>
							<%
								}
							%>
						</select>
						分
					</td>
				</tr>
				<tr>
					<td>
						状态
					</td>
					<td>
						<select name="effect">
							<%
								for (int i = 0; i < state.length; i++) {
							%><option value="<%=i%>"><%=state[i]%></option>
							<%
								}
							%>
						</select>
						分
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" value="添加" />
					</td>
					<td>
						<a href="weekgame.jsp">取消</a>
					</td>
				</tr>
			</table>
			<%
				}
			%>
		</form>
		<a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
		

		
	</body>
	

	
</html>