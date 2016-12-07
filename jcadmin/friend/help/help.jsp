<%@ page language="java" import="java.util.*,net.joycool.wap.bean.*,jc.answer.*,net.joycool.wap.util.*" pageEncoding="utf-8"%><%
	HelpAction action = new HelpAction(request);
	int pid = action.getParameterInt("pid");
	action.checkDel();
	List list = null;
	PagingBean paging = null;
	BeanProblem bp = null;
	if(pid > 0){
		bp = action.getProb(pid);
		if(bp != null){
			list = action.service.get222AnswerList(" del=0 and p_id=" + pid + " order by id desc");
		}
	}
	if(list != null && list.size() > 0)
		paging = new PagingBean(action, list.size(), 30, "p");
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%if(bp != null){
  %><title><%=StringUtil.toWml(bp.getPTitle())%></title><%
  }else{
  %><title>空帖子</title><%
  } 
  %>
  </head>
  <body><%
  if(bp != null && action.getParameterInt("p") >= 0){
   %><table border=1 width=100% align=center>
	<tr>
		<td align="center" width="6%">
			<font color=#1A4578>发帖人</font>
		</td>
		<td><a href="../../user/queryUserInfo.jsp?id=<%=bp.getUid()%>"><%=UserInfoUtil.getUser(bp.getUid()).getNickNameWml()%></a></td>
		<td align="center" width="6%">操作</td>
	</tr>
	<tr>
		<td align="center" width="15%">
			<font color=#1A4578>标题</font>
		</td>
		<td><%=StringUtil.toWml(bp.getPTitle())%></td>
		<td align="center" rowspan="3"><a href="help.jsp?pid=<%=pid%>">更新</a></td>
	</tr>
	<tr>
		<td align="center" width="20%">
			<font color=#1A4578>奖励</font>
		</td>
		<td><%=StringUtil.toWml(bp.getPrize())%></td>
		<td></td>
	</tr>
	<tr>
		<td align="center">
			<font color=#1A4578>帖子内容</font>
		</td>
		<td><%=StringUtil.toWml(bp.getPCont())%></td>
		<td></td>
	</tr>
  </table><%
  }
  	if(list != null && list.size() > 0){
  	%><table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align="center" width="5%">回复人</td>
		<td align="center">回复内容</td>
		<td align="center" width="5%">操作</td>
	</tr><%
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			BeanAnswer ba = (BeanAnswer)list.get(i);
			if(ba != null){
		  	%><tr>
		  	<td><%=UserInfoUtil.getUser(ba.getUid()).getNickNameWml()%></td>
		  	<td><%=StringUtil.toWml(ba.getACont())%></td>
		  	<td><a href="help.jsp?pid=<%=pid%>&delaid=<%=ba.getId()%>" onclick="return confirm('你确定要删除这条回复嘛？')">删除</a></td>
		  	</tr><%
			}
		}
  	%></table><%
	  	if(paging != null){
	  	%><%=paging.shuzifenye("help.jsp?jcfr=1&pid="+pid, true, "|", response)%><%
	  	}
  	}else{
  		%>回复为空!<br/><%
  	}
%><a href="helps.jsp">返回发帖页面</a><br/>
<a href="index.jsp">返回求助首页</a>
  </body>
</html>
