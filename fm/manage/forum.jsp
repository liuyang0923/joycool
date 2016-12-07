<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fm=action.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(fm==null){
	%><wml><card title="系统提示"><p align="left"><%=BaseAction.getTop(request, response)%>
	家族已解散<br/>
	<a href="/fm/index.jsp">返回家族首页</a><br/><%
}else{
	int cmd=action.getParameterInt("cmd");
	%><wml><card title="论坛创建"><p align="left"><%=BaseAction.getTop(request, response)%><%
	if(cmd==0){
		%>如果您的家族人数到达10人,就能申请拥有自己的家族论坛了!<br/>
		<a href="forum.jsp?cmd=1">创建论坛</a><br/><%
	}else{
		if(!fmLoginUser.isflagLeader()){
			%>您不是族长,没有权限<br/><%
		}else if(fm.getFm_member_num()<10){
			%>您的家族人数不满10人,还不能创建论坛!<br/><%
		}else if(fm.getForumId()!=0){
			%>您的家族已经有论坛!<br/><%
		}else{
			int forum=action.creatForum(fmLoginUser.getFm_id(),fm.getFm_name(),fmLoginUser.getId());
			if(forum!=0){
				%>家族论坛创建成功!<br/>
				<a href="/jcforum/forum.jsp?forumId=<%=forum%>">家族论坛</a><br/><%
			}else{
				%>失败<br/><%
			}
		}
	}
	%><a href="management.jsp">返回家族管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>