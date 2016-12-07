<%@ page language="java" import="java.util.*,jc.news.nba.*,net.joycool.wap.bean.*,net.joycool.wap.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%
	NbaAction action = new NbaAction(request);
	action.updReply();
	List list = action.service.getReplyList("  del=0 order by id desc");
	PagingBean paging = null;
%><html>
  <head>
    <title>全部言论管理</title>
  </head>
  <body>
  全部言论管理:<br/>		
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align="center" width="5%">
			<font color=#1A4578>UserId</font>
		</td>
		<td align="center" width="10%">
			<font color=#1A4578>UserName</font>
		</td>
		<td align=center>
			<font color=#1A4578>内容</font>
		</td>
		<td align="center" width="15%">
			<font color=#1A4578>发表时间</font>
		</td>
		<td align="center" width="10%">
			<font color=#1A4578>操作</font>
		</td>
	</tr><%
  	if(list != null && list.size() > 0){
		paging = new PagingBean(action, list.size(), 30, "p");
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			BeanReply br = (BeanReply)list.get(i);
	  		%><tr>
	  		<td align=center><%=br.getUid()%></td><%
	  		if(br.getUid() == 0){
	  		%><td align=center>直播间管理员:</td><%
	  		}else{
	  		%><td align=center><%=UserInfoUtil.getUser(br.getUid()).getNickNameWml()%></td><%
	  		}													
  			%><td align=left><%=StringUtil.toWml(br.getCont())%></td>
  			<td align=center><%=DateUtil.formatDate2(br.getCreateTime())%></td>
  			<td align=center><a href="replyall.jsp?del=<%=br.getId()%>&mid=<%=br.getMatchId()%>" onclick="return confirm('确定删除？')">删除</a></td>
			</tr><%
  		}
  	}
  	%>	
  	</table><%
  	if(paging != null){
  	%><%=paging.shuzifenye("replyall.jsp?jcfr=1", true, "|", response)%><%
  	}
  	%><a href="admin.jsp">返回NBA专题</a>
  </body>
</html>
