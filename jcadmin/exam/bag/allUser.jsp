<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30;%>
<%ExamAction action = new ExamAction(request);
  int del = action.getParameterInt("d");
  int delAll = action.getParameterInt("delAll");
  int bagId = action.getParameterInt("bid");
  int totalCount = 100000;
  PagingBean paging = null;
  int pageNow = 0;
  List bagList = null;
  ExamBag bag = null; 
  if (delAll == 1){
	  	String[] deleteIds = request.getParameterValues("did");
		String ids = action.array2String(deleteIds, ",");
		if (!"".equals(ids)){
			SqlUtil.executeUpdate("update exam_bag set del=1 where id in(" + ids + ")",5);
		}
  }
  paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
  pageNow = paging.getCurrentPageIndex();
  bagList = ExamAction.service.getBagList(" 1 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);  
%>
<html>
	<head>
		<title>备战考试->所有用户</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp"><-返回首页</a><form action="allUser.jsp?delAll=1" method="post"><input type="submit" value="删除所选" onclick="return confirm('确定删除？')"/><br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户</font>
				</td>
				<td align=center>
					<font color=#1A4578>类型</font>
				</td>
				<td align=center>
					<font color=#1A4578>标题</font>
				</td>
				<td align=center>
					<font color=#1A4578>内容</font>
				</td>
				<td align=center>
					<font color=#1A4578>创建时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>删除情况</font>
				</td>
				<td align=center>
					<font color=#1A4578>FLAG</font>
				</td>
				<td align=center>
					<font color=#1A4578>删</font>
				</td>
				<td align=center>
					<font color=#1A4578>管理</font>
				</td>
			</tr>
			<%if(bagList != null && bagList.size() > 0){
				ExamType exType = null;
				for (int i = 0; i < bagList.size(); i++) {
					bag = (ExamBag) bagList.get(i);
					exType = action.getTypeFromMap(bag.getQueType());
					%><tr>
						<td><%=bag.getId()%></td>
						<td><a href="/user/ViewUserInfo.do?userId=<%=bag.getUserId()%>"><%=bag.getUserId()%></a></td>
						<td><%=exType.getTypeName()%></td>
						<td><%=StringUtil.toWml(bag.getTitle()) %></td>
						<td><%=StringUtil.toWml(StringUtil.limitString(bag.getContent(),40))%></td>
						<td><%=DateUtil.formatDate2(bag.getCreateTime()) %></td>
						<td><%if(bag.getDel() == 1){%><font color="red">已删</a><%}else{%>未删<%}%></td>
						<td><%=bag.getFlag()%></td>
						<td><input type=checkbox name="did" value="<%=bag.getId()%>"></td>
						<td><a href="userModify.jsp?bid=<%=bag.getId()%>&back=1">管理</a>|<a href="note.jsp?bid=<%=bag.getId()%>&type=<%=bag.getQueType()%>&uid=<%=bag.getUserId() %>&back=1"">留言</a></td>
					</tr>
					<%
				}
			}
			%>
		</table><%=paging.shuzifenye("allUser.jsp", false, "|", response)%>
	</form>
	</body>
</html>