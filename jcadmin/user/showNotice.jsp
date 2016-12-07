<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.NoticeAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%>
<%!static int COUNT_PRE_PAGE = 20;
   static String[] status = {"未读","已读","","普通","系统","","","","","","保持未读"};
   static String[] mark = {"","游戏","家园回复","帮会通知","上线提示","信箱","聊天","多回合游戏","宠物系统","动作"};
%>
<%CustomAction action = new CustomAction(request);
PagingBean paging = null;
NoticeBean bean = null;
List list = null;
int pageNow = 0;
int userId = action.getParameterInt("uid");
if (userId > 0){
	paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
	pageNow = paging.getCurrentPageIndex();
	list = NoticeAction.noticeService.getNoticeList(" user_id=" + userId + " order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE );
}%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<form action="showNotice.jsp" method="get">
用户ID:<input type="text" name="uid">
<input type="submit" value="查询">
</form>
<table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>id</font>
		</td>
		<td align=center>
			<font color=#1A4578>user_id</font>
		</td>
		<td align=center>
			<font color=#1A4578>title</font>
		</td>
		<td align=center>
			<font color=#1A4578>content</font>
		</td>
		<td align=center>
			<font color=#1A4578>link</font>
		</td>
		<td align=center>
			<font color=#1A4578>tong_id</font>
		</td>
		<td align=center>
			<font color=#1A4578>status</font>
		</td>
		<td align=center>
			<font color=#1A4578>type</font>
		</td>
		<td align=center>
			<font color=#1A4578>create_time</font>
		</td>
		<td align=center>
			<font color=#1A4578>mark</font>
		</td>
	</tr>
	<% if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				bean = (NoticeBean)list.get(i);
				if (bean != null){
					%><tr>
						<td><%=bean.getId()%></td>
						<td><%=bean.getUserId()%></td>
						<td><%=StringUtil.toWml(bean.getTitle())%></td>
						<td><%=StringUtil.toWml(bean.getContent())%></td>
						<td><%=StringUtil.toWml(bean.getLink())%></td>
						<td><%=bean.getTongId()%></td>
						<td><%=status[bean.getStatus()]%></td>
						<td><%=bean.getType()%></td>
						<td><%=bean.getCreateDatetime()%></td>
						<td><%=bean.getMark()>=mark.length?"其他":mark[bean.getMark()]%></td>
					  </tr><%
				}
			}
	   }%>
</table>
<%=paging != null?paging.shuzifenye("showNotice.jsp?uid=" + userId, true, "|", response):""%>
<body>
</html>