<%@ page language="java" pageEncoding="utf-8" import="jc.family.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,jc.family.game.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
GameAction action = new GameAction(request,response);
List memberList = GameAction.service.getApplyList("select * from fm_game_apply where is_pay=1 order by m_id desc,fid");

%>
<html>
  <head>
    <title>正在游戏人员</title>
 <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <% 
  if (memberList != null && memberList.size() > 0) {
	PagingBean paging = new PagingBean(action,memberList.size(),30,"p");
  	%>
  	<table border="1">
  		<tr>
  			<td>赛事id</td>
  			<td>所属家族</td>
  			<td>名字</td>
  		</tr>
  	<% 
	  	for (int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	  		ApplyBean mbean = (ApplyBean) memberList.get(i);
	  		FamilyHomeBean fm = GameAction.getFmByID(mbean.getFid());
	  		UserBean ub = UserInfoUtil.getUser(mbean.getUid());
	  		%>
	  		<tr>
	  			<td><%=mbean.getMid()%></td>
	  			<td><%
	  			if (fm != null) {
	  				%><%=fm.getFm_nameWml()%><br/><%
	  			} else {
					%>无<br/><%	  			
	  			}
	  			%></td>
	  			<td><%
	  			if (ub !=  null) {
	  				%><%=ub.getNickNameWml()%><%
	  			} else {
					%>无<br/><%	  	
	  			}
	  			%></td>
	  		</tr>
	  		<%
	  	}
  	 %>
  	</table>
  	<%=paging.shuzifenye("members.jsp?jcfr=1",true,"|",response)%><%
  } else {
  	%>无<br/><%
  }
   %>
 <a href="/jcadmin/fm/index.jsp">返回游戏管理主页面</a>
  </body>
</html>
