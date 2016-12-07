<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.guest.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30; %>
<% 	GuestAction action = new GuestAction(request);
	String tip = "";
	Guest guest = null;
	int guestId = 0;
	int clear = action.getParameterInt("c");
	int modifyId = action.getParameterInt("mid");
	int model = action.getParameterInt("m");
	List list = null;
	if (model == 1){
		//显示刚改过名的用户
		list = action.getChangedUserList();
	} else {
		list = action.getChatUserList();
	}
	if (modifyId > 0){
		   guest = action.getGuestById(modifyId);
		   if (guest != null){
		   // 修改昵称
			   Guest guest1 = new Guest();
			   guest1.setId(guest.getId());
			   guest1.setNickName("游客" + guest.getId());
			   guest1.setUid(guest.getUid());
			   guest1.setAge(guest.getAge());
			   guest1.setGender(guest.getGender());
			   guest1.setCreateTime(guest.getCreateTime());
			   // 修改游客列表中的Bean
			   action.getGuestMap().put(new Integer(guest.getId()),guest1);
			   if (model == 1){
				   list.remove(new Integer(guest1.getId()));
			   }
		   }
	}
	if (clear == 1){
		if (modifyId > 0){
			action.delFromChatUserList(modifyId);
			response.sendRedirect("index.jsp");
			return;
		}
	} else if (clear == 2){
		action.clearChatUserList();
		response.sendRedirect("index.jsp");
		return;
	}
	PagingBean paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
%>
<html>
	<head>
		<title>游客聊天室</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%if ("".equals(tip)){
%>==目前共有<font color="red"><b><%= list.size() %></b></font></>人==<a href="index.jsp?m=1">刚修改昵称的用户</a>|<a href="index.jsp">全部用户</a>|<a href="index.jsp?m=<%=model %>">刷新</a><br/>
		<table border=1 width=100% align=center>
						<tr bgcolor=#C6EAF5>
							<td align=center>
								<font color=#1A4578>游客ID</font>
							</td>
							<td align=center>
								<font color=#1A4578>昵称</font>
							</td>
							<td align=center>
								<font color=#1A4578>UID</font>
							</td>
							<td align=center>
								<font color=#1A4578>创建时间</font>
							</td>
							<td align=center>
								<font color=#1A4578>修改</font>
							</td>
						</tr>
						<%for (int i =  paging.getStartIndexR(); i >  paging.getEndIndexR(); i--){
							guestId = StringUtil.toInt(list.get(i).toString());
							guest = action.getGuestById(guestId);
							%><tr>
								<td><%=guest.getId() %></td>
								<td><%=guest.getNickNameWml() %></td>
								<td><%=guest.getUid() %></td>
								<td><%=DateUtil.formatSqlDatetime(guest.getCreateTime()) %></td>
								<td><a href="index.jsp?m=<%=model%>&mid=<%=guest.getId()%>" onclick="return confirm('确定修改？')">修改</a><!-- |<a href="index.jsp?c=1&mid=<%=guest.getId() %>" onclick="return confirm('真的要踢吗？')">踢</a> --></td>
					 		</tr><%
						}%>
		</table><%=paging.shuzifenye("index.jsp?m=" + model, true, "|", response)%><br/>
<%
} else {
	%><font color="red"><%=tip%></font><br/><a href="index.jsp">返回</a><br/><%
}%>
	</body>
</html>