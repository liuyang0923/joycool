<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.StringUtil,java.util.List,jc.family.*"%><%!
static int COUNT_PER_PAGE = 20;	// 一页10个好友
%><%response.setHeader("Cache-Control","no-cache");
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
FamilyHomeBean fmHome=action.getFmByID(id);
if(fmHome==null){
	%><script type="text/javascript">alert('家族不存在')</script>
	<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
	<%
	return;
}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/>
  	<table border="1">
  		<tr>
  			<td>帮会ID</td>
  			<td>名称</td>
  			<td>族长</td>
  			<td>等级</td>
  			<td>游戏经验</td>
  			<td>人数</td>
  			<td>家族基金</td>
  			<td>家族公告</td>
  		</tr>
  		<tr>
  			<td><%=fmHome.getId()%></td>
  			<td><a href="familyhome.jsp?id=<%=fmHome.getId()%>"><%=fmHome.getFm_nameWml()%></a></td>
  			<td><a href="../user/queryUserInfo.jsp?id=<%=fmHome.getLeader_id()%>"><%=fmHome.getLeaderNickNameWml()%></a></td>
  			<td><%=Constants.FM_LEVEL_NAME[fmHome.getFm_level()]%></td>
  			<td><%=fmHome.getGame_num()%></td>
  			<td><a href="familymember.jsp?id=<%=id%>"><%=fmHome.getFm_member_num()%></a></td>
  			<td><a href="funddetail.jsp?id=<%=id%>"><%=fmHome.getMoney()%></a></td>
  			<td><%=fmHome.getBulletin()%></td>
  		</tr>
  	</table><%
  	int p=action.getParameterInt("p");
  	PagingBean paging=new PagingBean(action, fmHome.getFm_member_num(),COUNT_PER_PAGE, "p");
  	List list=action.service.selectFmUserList(id,paging.getStartIndex(),paging.getCountPerPage(),"");
  	%>
  	<div>家族成员</div>
  	<table border="1">
  		<tr>
  			<td>编号</td>
  			<td>用户ID</td>
  			<td>用户名称</td>
  			<td>称号</td>
  			<td>功勋</td>
  			<td>权限</td>
  			<td>挑战精英</td>
  		</tr><%
  		for(int i=0;i<list.size();i++){
  			FamilyUserBean fmUser=(FamilyUserBean)list.get(i);
  			%>
  		<tr>
  			<td><%=i+1+COUNT_PER_PAGE*p%></td>
  			<td><a href="../user/queryUserInfo.jsp?id=<%=fmUser.getId()%>"><%=fmUser.getId()%></a></td>
  			<td><%=fmUser.getNickNameWml()%></td>
  			<td><%=StringUtil.toWml(fmUser.getFm_name())%></a></td>
  			<td><%=fmUser.getCon_fm()%></td>
  			<td><%=fmUser.getFm_flags()%></td>
  			<td><%=FamilyUserBean.isVsGame(fmUser.getFm_state())%></td>
  		</tr><%
  		}
  	%></table>
  	<%=paging.shuzifenye("familymember.jsp?id=" + id, true, "|", response)%>
  </body>
</html>