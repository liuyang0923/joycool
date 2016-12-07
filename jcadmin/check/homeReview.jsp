<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*,net.joycool.wap.service.impl.HomeServiceImpl,net.joycool.wap.action.home.HomeAction,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.bean.home.*"%>
<%!public final int COUNT_PRE_PAGE = 20;
	public static HomeServiceImpl service = new HomeServiceImpl();%><%
	
			HomeAction action = new HomeAction(request);
			int totalCount = 0;
			PagingBean paging = null;
			int pageNow = 0;
			int deleteId = action.getParameterInt("did");
			if (deleteId > 0){
				// 删除
				action.getHomeService().updateHomeReview("review='(内容已删除)'"," id=" + deleteId);
			}
			totalCount = 100000;  //SqlUtil.getIntResult("select count(id) from jc_home_review", 0);
			paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
			pageNow = paging.getCurrentPageIndex();
			Vector homeReviewList = action.getHomeService().getHomeReviewList("review!='(内容已删除)' order by id desc limit " + pageNow * COUNT_PRE_PAGE  + ", " + COUNT_PRE_PAGE);
			HomeReviewBean homeReview=null;
			UserBean user = null;
			UserBean user2 = null;
%>
<html>
	<head>
		<title>删除家园留言</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
		<%
		%>删除家园留言(<font color="red"><%=totalCount%></font>):<br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center width="40">
					<font color=#1A4578>ID</font>
				</td>
				<td align=center width="80">
					<font color=#1A4578>用户</font>
				</td>
				<td align=center width="80">
					<font color=#1A4578>回复者</font>
				</td>
				<td align=center>
					<font color=#1A4578>内容</font>
				</td>
				<td align=center width="80">
					<font color=#1A4578>创建时间</font>
				</td>	
				<td align=center width="40">
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<% for (int i = 0 ; i < homeReviewList.size() ; i++){
					homeReview  = (HomeReviewBean)homeReviewList.get(i);
//					user = UserInfoUtil.getUser(homeReview.getUserId());
//					user2 = UserInfoUtil.getUser(homeReview.getReviewUserId());
					%><tr>
							<td><%=homeReview.getId()%></td>
							<td><a href="../user/queryUserInfo.jsp?id=<%=homeReview.getUserId() %>"><%=homeReview.getUserId()%></a></td>
							<td><a href="../user/queryUserInfo.jsp?id=<%=homeReview.getReviewUserId() %>"><%=homeReview.getReviewUserId()%></a></td>
							<td><%=StringUtil.toWml(homeReview.getReview())%></td>
							<td width="80"><%=DateUtil.formatSqlDatetime(homeReview.getCreateTime())%></td>
							<td><a href="homeReview.jsp?did=<%=homeReview.getId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确定删除？')">删除</a></td>
					 </tr><%
			   }
			%>
		</table>
		<%=paging.shuzifenye("homeReview.jsp", false, "|", response)%>
	</body>
</html>