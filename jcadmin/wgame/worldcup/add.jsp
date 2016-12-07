<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="jc.game.worldcup.*,jc.credit.UserInfo,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%
WorldCupAction action = new WorldCupAction(request);
WcInfo wcInfo = WorldCupAction.getWcInfo();
if (wcInfo == null){
	wcInfo = new WcInfo();
	wcInfo.setLimitTime(0);
	wcInfo.setPrizeId(0);
	wcInfo.setHelpId(0);
	wcInfo.setSubjectId(0);
}
String tip = "";
int add = action.getParameterInt("a");
if (add == 1){
	int limitTime = action.getParameterInt("limitTime");
	int prizeId = action.getParameterInt("prizeId");
	int helpId = action.getParameterInt("helpId");
	int subjectId = action.getParameterInt("subjectId");
	if (limitTime < 0){
		tip = "开赛禁止下注时间不可小于0.";
	} else if (prizeId <= 0){
		tip = "奖项跳转ID不可小等于0.";
	} else if (helpId <= 0){
		tip = "说明跳转ID不可小等于0.";
	} else {
		if (subjectId < 0)
			subjectId = 0;
		if (limitTime > 100)
			limitTime = 100;			
		wcInfo.setLimitTime(limitTime);
		wcInfo.setPrizeId(prizeId);
		wcInfo.setHelpId(helpId);
		wcInfo.setSubjectId(subjectId);
		if (WorldCupAction.service.getInfo(" id=1") ==  null){
			// 第一次运行，没有赛事
			WorldCupAction.service.addInfo(wcInfo);
		} else {
			// 更新比赛信息
			SqlUtil.executeUpdate("update wc_info set limit_time=" + wcInfo.getLimitTime() + ",prize_id=" + wcInfo.getPrizeId() + ",help_id=" + wcInfo.getHelpId() + ",subject_id=" + wcInfo.getSubjectId() + " where id=1",5);
		}
		tip = "赛事信息更新完毕.";
	}
}
int clear = action.getParameterInt("c");
if (clear == 1){
	// 清除排名表，把所有用户的彩币重新置回1W
	SqlUtil.executeUpdate("truncate table wc_rank",5);
	SqlUtil.executeUpdate("update wc_user set `point`=10000",5);
	tip = "排名表被清空,彩币已被重置.";
}
%>
<html>
	<head>
		<title>竞猜管理</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%=tip%><br/>
		==竞猜管理==<br/>
		<form action="add.jsp?a=1" method="post">
			开赛<input type="text" name="limitTime" value="<%=wcInfo.getLimitTime()%>" />分钟后禁止投注.<br/>
			奖项跳转ID:<input type="text" name="prizeId" value="<%=wcInfo.getPrizeId()%>"/><br/>
			说明跳转ID:<input type="text" name="helpId" value="<%=wcInfo.getHelpId()%>"/><br/>
			返回竞猜首页:<input type="text" name="subjectId" value="<%=wcInfo.getSubjectId()%>"/>(若没有请填0)<br/>
			<input type="submit" value="确定">
			<input type="button" value="返回" onClick="javascript:window.location.href='index.jsp'">
		</form>
		======清除排名======<br/>
		<font color="red">清空排名表，所有用户的彩币置为初始值:10000</font><br/>
		<a href="add.jsp?c=1" onClick="return confirm('真的要清除所有排名?')">清除排名</a><br/>
	</body>
</html>