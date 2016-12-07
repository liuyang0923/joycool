<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30;%>
<%ExamAction action = new ExamAction(request);
  int noteId = action.getParameterInt("nid");
  int delAll = action.getParameterInt("delAll");
  int totalCount = 100000;
  PagingBean paging = null;
  int pageNow = 0;
  List noteList = null;
  ExamNote note = null; 
  if (delAll == 1){
	  	String[] deleteIds = request.getParameterValues("did");
		String ids = action.array2String(deleteIds, ",");
		if (!"".equals(ids)){
			SqlUtil.executeUpdate("update exam_note set del=1 where id in(" + ids + ")",5);
		}
  }
  paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
  pageNow = paging.getCurrentPageIndex();
  noteList = ExamAction.service.getNoteList(" 1 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE); 
%>
<html>
	<head>
		<title>备战考试->所有笔记</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp"><-返回首页</a><form action="allNote.jsp?delAll=1" method="post"><input type="submit" value="删除所选" onclick="return confirm('确定删除？')"/><br/>
		<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>
						<font color=#1A4578>ID</font>
					</td>
					<td align=center>
						<font color=#1A4578>用户</font>
					</td>
					<td align=center>
						<font color=#1A4578>书包ID</font>
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
				</tr>
				<%if(noteList != null && noteList.size() > 0){
					for (int i = 0; i < noteList.size(); i++) {
						note = (ExamNote) noteList.get(i);
						%><tr>
							<td><%=note.getId()%></td>
							<td><a href="/user/ViewUserInfo.do?userId=<%=note.getUserId()%>"><%=note.getUserId()%></a></td>
							<td><%=note.getBagId()%></td>
							<td><%=StringUtil.toWml(note.getContent()) %></td>
							<td><%=DateUtil.formatDate2(note.getCreateTime()) %></td>
							<td><%if(note.getDel() == 1){%><font color="red">已删</a><%}else{%>未删<%}%></td>
							<td><%=note.getFlag()%></td>
							<td><input type=checkbox name="did" value="<%=note.getId()%>"></td>
						</tr>
						<%
					}
				}%>
		</table>
		</form>
		<%=paging.shuzifenye("allNote.jsp", false, "|", response)%>
	</body>
</html>