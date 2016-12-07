<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.util.*,java.text.*,jc.family.game.*,java.util.*"%>
<%
GameAction action = new GameAction(request,response);
String[] weekDay = {"周日","周一","周二","周三","周四","周五","周六"};
String[] weekGame = {"","龙舟","雪仗","问答"};
String[] state = {"已生效","未生效"};
int del = action.getParameterInt("del");
if(del > 0){
	GameAction.service.upd("delete from fm_game_weekgame where id="+del);
	request.setAttribute("tip","删除成功!");
}
if(1==action.getParameterInt("a")){
	int id = action.getParameterInt("id");
	int type = action.getParameterInt("type");
	int weekday = action.getParameterInt("weekday");// 星期几
	int effect = action.getParameterInt("effect");// 是否生效
	String startTime = action.getParameterString("startTime");
	String endTime = action.getParameterString("endTime");
	startTime += ":00";
	endTime += ":00";
	Date startDate = DateFormat.getTimeInstance().parse(startTime);
	Date endDate = DateFormat.getTimeInstance().parse(endTime);
	WeekMatchBean wmb = new WeekMatchBean();
	wmb.setId(id);
	wmb.setType(type);
	wmb.setWeekDay(weekday);
	wmb.setStarttime(startDate);
	wmb.setEnd_time(endDate);
	wmb.setEffect(effect);
	if(id > 0){
		GameAction.service.alterWeekGame(wmb);
		request.setAttribute("tip","修改成功!");
	} else {
		GameAction.service.insertWeekGame(wmb);
		response.sendRedirect("weekgame.jsp?a=2");
		return;
	}
}
if(2 == action.getParameterInt("a")){
	request.setAttribute("tip","添加成功!");
}
List list = GameAction.service.getWeekMatchList("1 order by weekday,id");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加游戏管理</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
  	<a href="addmatch.jsp">添加赛事</a><br/>
  	<table border="1">
  		<tr>
  			<td align="center">日期</td>
  			<td align="center">时间</td>
  			<td align="center">游戏</td>
  			<td align="center">操作</td>
  			<td align="center">状态</td>
  		</tr>
  <%if(list != null && list.size() > 0){
  		for(int i=0;i<list.size();i++){
  			WeekMatchBean bean = (WeekMatchBean) list.get(i);
  			if(bean != null){
  	%>
  		<tr>
  			<td align="center"><%=weekDay[bean.getWeekDay()]%></td>
  			<td align="center"><%=DateUtil.formatTime(bean.getStarttime())%>至<%=DateUtil.formatTime(bean.getEndtime())%></td>
  			<td align="center"><%=weekGame[bean.getType()]%></td>
  			<td align="center"><a href="addmatch.jsp?id=<%=bean.getId()%>">修改</a><a href="weekgame.jsp?del=<%=bean.getId()%>">删除</a></td>
  			<td align="center"><%=state[bean.getEffect()]%></td>
  		</tr>
  	<%
  			}
  		}
	%>
	</table>
	<%
  }else{
  %>
  	暂无赛事!<br/>
  <%
  } %>
	<a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>