<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; 
	public static MoodService service =new MoodService();
%>
<html>
	<head>
		<title>心情管理</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%
			MoodAction action =new MoodAction(request);
			String f=action.getParameterString("f");
			int id=action.getParameterInt("id");
			
			MoodBean mb=null;
			ArrayList list=null;
			PagingBean paging=null;
			String pageUrl="";
			
			if ("findById".equals(f)){
				//查找一个心情
				mb=service.getMoodById(id);
				if(mb==null){
					action.sendRedirect("mood.jsp",response);
					return;
				}else{
					int totalCount=1;
					paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
					//int pageNow = paging.getCurrentPageIndex();
					list=new ArrayList();
					list.add(mb);
					pageUrl="mood.jsp?id=0";				
				}
			}else if ("findByUserId".equals(f)){
				//根据用户ID查找其所有心情
				int totalCount = SqlUtil.getIntResult("select count(*) from mood_list where user_id=" + id,5);
				paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
				int pageNow = paging.getCurrentPageIndex();
				list=service.getAllMoodById(id,pageNow,COUNT_PRE_PAGE);
				pageUrl="mood.jsp?f=findByUserId&id=" + id;
			}else{
				//所有心情
				int totalCount = SqlUtil.getIntResult("select count(*) from mood_list",5);
			    paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
				int pageNow = paging.getCurrentPageIndex();
				list=service.getAllMood(pageNow,COUNT_PRE_PAGE);
				pageUrl="list.jsp?id=0";
			}
		%>
		<table width="100%" style="border:none;">
			<tr>
				<td style="border:none;">		
					<form method="post" action="mood.jsp?f=findById">
						心情ID：<input type="text" name="id">
						<input type="submit" value="查找">
					</form>
				</td>
			</tr>
			<tr>
				<td width=80% style="border:none;">
					<form method="post" action="mood.jsp?f=findByUserId">
						用户ID：<input type="text" name="id">
						<input type="submit" value="查找">
					</form>
				</td>
				<td width=20% align=right style="border: none;">
					<form method="post" action="moodAllReply.jsp">
						<input type="submit" value="查看所有回复">
					</form>
				</td>
				<td width=20% align=right style="border:none;">
					<form method="post" action="moodLast.jsp">
						<input type="submit" value="查看最新心情">
					</form>				
				</td>
				<td width=20% align=right style="border:none;">
					<form method="post" action="mood.jsp?f=all">
						<input type="submit" value="查看所有心情">
					</form>				
				</td>
			</tr>
		</table>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center ><font color=#1A4578>ID</font></td>
				<td align=center><font color=#1A4578>用户</font></td>
				<td align=center><font color=#1A4578>心情</font></td>
				<td align=center><font color=#1A4578>创建时间</font></td>
				<td align=center><font color=#1A4578>浏览次数</font></td>
				<td align=center><font color=#1A4578>回复次数</font></td>
				<td align=center><font color=#1A4578>类型</font></td>
				<td align=center><font color=#1A4578>操作</font></td>
			</tr>
			<%
				for(int i=0;i<list.size();i++){
					mb=(MoodBean)list.get(i);
					UserBean user = UserInfoUtil.getUser(mb.getUserId());
					if (mb !=null){
						%><tr><td><%=mb.getId() %></td><%
						%><td><a href="../user/queryUserInfo.jsp?id=<%=mb.getUserId() %>"><%=user.getNickNameWml()%></a></td><%
						%><td><%=StringUtil.toWml(mb.getMood()) %></td><%
						%><td><%=DateUtil.formatSqlDatetime(mb.getCreateTime()) %></td><%
						%><td><%=mb.getViewCount() %></td><%
						if(mb.getReplyCount()!=0){
							%><td><font color=blue><%=mb.getReplyCount() %></font></td><%
						}else{
							%><td><font color=gray><%=mb.getReplyCount() %></font></td><%
						}
						%><td><img src="../../beacon/mo/img/<%=mb.getType()%>.gif"/>&nbsp;<%=mb.getType()%></td><%
						%><td><a href="moodOperate.jsp?f=mood&id=<%=mb.getId()%>&uid=<%=mb.getUserId() %>" onclick="return confirm('确定删除？')">删</a>&nbsp;&nbsp;
						<a href="moodReply.jsp?id=<%=mb.getId()%>">查回复</a></td></tr><%
					}
				}			  
			  	%></table>
				<%=paging.shuzifenye("mood.jsp?id=" + 0, true, "|", response)%>
	</body>
</html>