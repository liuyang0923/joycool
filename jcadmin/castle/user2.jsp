<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static String[] valueNames = {"总人口","总文明度/天","城堡数"};
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	CastleUserBean user = CastleUtil.getCastleUser(id);
	
	int modify = action.getParameterIntS("i");
	int modValue = action.getParameterInt("value");
	if(modify!=-1){
		switch(modify){
		case 0:
			user.setPeople(modValue);
			break;
		case 1:
			user.setCivilSpeed(modValue);
			break;
		case 2:
			user.setCastleCount(modValue);
			SqlUtil.executeUpdate("update castle_user set castle_count="+modValue+" where uid="+id,5);
			break;
		}
		service.updateUserCivil(user);
	}
	
	List list = service.getCastleList(id);
	int[] values = new int[valueNames.length];;
	int[] values2 = {
		user.getPeople(),
		user.getCivilSpeed(),
		user.getCastleCount(),
	};
	
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	城主信息<br/><br/>
【<%=StringUtil.toWml(user.getName())%>】<br/>
<table class="tbg" cellpadding="2" cellspacing="1"><tr valign=top><td width="250">

总人口:<%=user.getPeople()%><br/>
<%if(user.getInfo2().length()!=0){%><%=user.getInfo2()%><br/><%}%>
<a href="../user/queryUserInfo.jsp?id=<%=user.getUid() %>">用户信息</a><br/>
==城堡列表(<%=user.getCastleCount()%>)==<br/>
<table class="tbg" cellpadding="2" cellspacing="1"><tr><td></td><td>名字</td><td>坐标</td><td>人口</td></tr>
<%for(int i = 0; i < list.size() ; i++) {
	CastleBean castleBean = (CastleBean)list.get(i);
	UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
	
	values[0] += userRes.getPeople();
	values[1] += userRes.getCivil();
	values[2] ++;		
	
%><%if(user.getCastleCount()!=1&&user.getMain()==castleBean.getId()){%>*<%}
%><tr><td><%=i+1 %></td><td><a href="castle.jsp?id=<%=castleBean.getId() %>"><%=StringUtil.toWml(castleBean.getCastleName()) %></a></td><td>(<%=castleBean.getX() %>|<%=castleBean.getY() %>)</td><td><%=userRes.getPeople() %></td></tr>
<%} %></table>
<br/>

</td><td width="300">

<table class="tbg" cellpadding="2" cellspacing="1">
<tr><td></td><td>当前值</td><td>理论值</td><td></td></tr>
<%for(int i=0;i<valueNames.length;i++){
%><tr><td><%=valueNames[i]%></td><td><%if(values[i]!=values2[i]){%><font color=red><%=values2[i]%></font><%}else{%><%=values2[i]%><%}%></td><td><%=values[i]%></td>
<td><a href="user2.jsp?id=<%=id%>&i=<%=i%>&value=<%=values[i]%>" <%--onclick="return confirm('<%=valueNames[i]%>:<%=values2[i]%>-><%=values[i]%>.确认修正数据?')"--%>>修正数据</a></td></tr>
<%}%>
</table>
</td></tr></table>

<br/>
<a href="user.jsp?id=<%=id%>">返回城主信息</a><br/>

<%@include file="bottom.jsp"%>
</html>