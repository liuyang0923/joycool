<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IJobService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation" %><%response.setHeader("Cache-Control","no-cache");%>
<%
IJobService jobService=ServiceFactory.createJobService();
if(null!=request.getParameter("delete")){
	int id=StringUtil.toInt(request.getParameter("delete"));
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	dbOp.startTransaction();
	dbOp.executeUpdate("delete from job_psychology where id="+id);
	dbOp.executeUpdate("delete from job_psychology_answer where psychology_id="+id);
	dbOp.commitTransaction();
	dbOp.release();
}
%>
<%
int PAGE_SIZE=20;
int pageIndex=0;
int itemCount=0;
int totalPageCount=0;
Vector questionList=null;
PsychologyBean psychology=null;
if(null!=request.getParameter("pageIndex")){
	pageIndex=StringUtil.toInt(request.getParameter("pageIndex"));
}
String strWhere=" 1=1 order by id ";
itemCount=jobService.getPsychologyCount(strWhere);
totalPageCount=itemCount/PAGE_SIZE;
if(itemCount%PAGE_SIZE>0)
	totalPageCount++;
pageIndex=Math.min(pageIndex,totalPageCount);
pageIndex=Math.max(0,pageIndex);
questionList=jobService.getPsychologyList(strWhere+" limit "+pageIndex*PAGE_SIZE+","+PAGE_SIZE);
%>
<html>
<head>
</head>
<body>
心理测试题目管理<br/><br/>
<a href="addQuestion.jsp">增加题目</a>
<table width="800" border="1">
<tr><td>编号</td><td width="600">标题</td><td>操作</td></tr>
<%
for(int i=0;i<questionList.size();i++){
	psychology=(PsychologyBean)questionList.get(i);
%>
<tr><td><%=i+1%></td><td><%=psychology.getTitle()%></td><td><a href="addQuestion.jsp">增加题目</a>&nbsp;&nbsp;
<a href="question.jsp?id=<%=psychology.getId()%>">修改</a>&nbsp;&nbsp;
<a href="index.jsp?delete=<%=psychology.getId()%>&pageIndex=<%=pageIndex%>">删除</a></td></tr>
<%}%>
</table>
<%
String prefixUrl="http://wap.joycool.net/jcadmin/job/psychology/index.jsp";
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%} %>
<font color="red">说明：点击删除会删除题目和题目相对应的答案</font><br/>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>