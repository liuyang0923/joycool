<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.*"%><%@include file="../filter.jsp"%><%!
static int COUNT_PER_PAGE = 20;	// 一页10个好友
%><%
response.setHeader("Cache-Control","no-cache");
FamilyAction action=new FamilyAction(request,response);
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>

	<a href="ask/asklist.jsp">问答题库管理</a>&nbsp;<br/>
<%if(group.isFlag(1)){%>
  	<a href="weekgame.jsp">添加游戏管理</a>&nbsp;
	<a href="todaygame.jsp">游戏赛事表管理</a>&nbsp;
	<a href="historymatch.jsp">历史赛事</a>&nbsp;
	<a href="boat/boat.jsp">龙舟后台</a>&nbsp;
	<a href="yard/index.jsp">家族餐厅</a>&nbsp;
	<a href="vsconfig.jsp">对战信息</a>&nbsp;
	<a href="vs/termlist.jsp">争霸赛</a> <a href="medal.jsp">勋章</a><br>
  	===家族情况===<br>
  	<form method="get" action="index.jsp">家族名称:<input name="name"/>&nbsp;<input type="submit" value="查询"/></form>
  	<form method="get" action="familyhome.jsp">家族ID:<input name="id"/>&nbsp;<input type="submit" value="查询"/></form><%
  	int cmd=action.getParameterInt("cmd");
  	String name=action.getParameterNoEnter("name");
  	String query="";
  	if(cmd==1){
  		query="order by fm_level";
  	}
  	if(cmd==2){
  		query="order by game_num";
  	}
  	if(cmd==3){
  		query="order by fm_member_num";
  	}
  	if(cmd==4){
  		query="order by money";
  	}
  	if(cmd==5){
  		query="order by fm_level desc";
  	}
  	if(cmd==6){
  		query="order by game_num desc";
  	}
  	if(cmd==7){
  		query="order by fm_member_num desc";
  	}
  	if(cmd==8){
  		query="order by money desc";
  	}
  	if(name!=null){
  		query=" and fm_name like '"+StringUtil.toSqlLike(name)+"%'";
  	}
  	%>
  	<a href="index.jsp">全部家族</a>&nbsp;<a href="index.jsp?cmd=<%=cmd==1?5:1%>">等级</a>&nbsp;
  	<a href="index.jsp?cmd=<%=cmd==2?6:2%>">游戏经验</a>&nbsp;
  	<a href="index.jsp?cmd=<%=cmd==3?7:3%>">人数</a>&nbsp;
  	<a href="index.jsp?cmd=<%=cmd==4?8:4%>">家族基金</a><br/><%
  	int count=action.service.selectIntResult("select count(id) from fm_home");
  	PagingBean paging=new PagingBean(action, count, COUNT_PER_PAGE, "p");
  	int p=action.getParameterInt("p");
  	List list=null;
  	if(name!=null){
  		list=action.service.selectFamilyIdList("1 "+query);
  	}else{
  		list=action.service.selectFamilyIdList("1 "+query+" limit "+paging.getStartIndex()+","+ paging.getCountPerPage());
  	}
  	%>
  	<table border="1">
  		<tr>
  			<td>编号</td>
  			<td>帮会ID</td>
  			<td>名称</td>
  			<td>族长</td>
  			<td>等级</td>
  			<td>游戏经验</td>
  			<td>人数</td>
  			<td>家族基金</td>
  		</tr><%
  		for(int i=0;i<list.size();i++){
  		FamilyHomeBean fmHome=action.getFmByID(((Integer)list.get(i)).intValue());
  		if(fmHome!=null){%>
  		<tr>
  			<td><%=i+1+COUNT_PER_PAGE*p%></td>
  			<td><%=fmHome.getId()%></td>
  			<td><a href="familyhome.jsp?id=<%=fmHome.getId()%>"><%=fmHome.getFm_nameWml()%></a></td>
  			<td><a href="../user/queryUserInfo.jsp?id=<%=fmHome.getLeader_id()%>"><%=fmHome.getLeaderNickNameWml()%></a></td>
  			<td><%=jc.family.Constants.FM_LEVEL_NAME[fmHome.getFm_level()]%></td>
  			<td><%=fmHome.getGame_num()%></td>
  			<td><%=fmHome.getFm_member_num()%></td>
  			<td><%=fmHome.getMoney()%></td>
  		</tr>
  		<%}
  		}%>
  	</table>
  	<%=paging.shuzifenye("index.jsp?cmd=" + cmd, true, "|", response)%>
<%}%>
帮会转家族功能：<br/>
	<form method="get" action="tongforfm.jsp">帮会ID:<input name="tongid"/>家族ID:<input name="fmid"/>&nbsp;<input type="submit" value="提交"/></form>
  </body>
</html>