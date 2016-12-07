<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static int COUNT_PRE_PAGE = 10; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<% response.setHeader("Cache-Control","no-cache");
   ExamAction action = new ExamAction(request);
   int subjectId = action.getParameterInt("s");
   int flag = action.getParameterInt("f");
   int searchId = action.getParameterInt("sid");
   String keyWord = action.getParameterNoEnter("kw");
   if (subjectId < 1 || subjectId > ExamAction.getSubjectTypeMap().size()){
	   subjectId = 1;
   }
   if (flag < 1 || flag > 2){
	   flag = 1;
   }
   if (searchId < 0 || searchId > 2){
	   searchId = 0;
   }
   String tip = "";
   int totalCount = 0;
   PagingBean paging = null;
   int pageNow = 0;
   List libList = null;
   String topLink = action.getSubjectTop("lib.jsp?f=" + flag,subjectId,true);
   if(searchId < 2){
	   // 查找全部
	   totalCount = SqlUtil.getIntResult("select count(id) from exam_lib where type=" + request.getAttribute("currentType") + " and del=0 and flag=" + flag,5);
	   paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	   pageNow = paging.getCurrentPageIndex();
	   libList = ExamAction.service.getLibList(" type=" + request.getAttribute("currentType") + " and del=0 and flag=" + flag + " order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
   } else {
	   // 关键字查找
	   if (!"".equals(keyWord) || keyWord.length() < 20){
		   totalCount = SqlUtil.getIntResult("select count(id) from exam_lib where type=" + request.getAttribute("currentType") + " and del=0 and flag=" + flag + " and title like '%" + StringUtil.toSql(keyWord) + "%'",5);
		   paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
		   pageNow = paging.getCurrentPageIndex();
		   libList = ExamAction.service.getLibList(" type=" + request.getAttribute("currentType") + " and del=0 and flag=" + flag + " and title like '%" + StringUtil.toSqlLike(keyWord) + "%' order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	   } else {
		   tip = "没有输入关键字,或关键字过长." + keyWord;
	   }
   }
%><wml><card title="备战考试"><p><%=BaseAction.getTop(request, response)%>
==<%if(flag==1){%>初中<%}else{%>高中<%}%>题库==<br/>
<%=topLink%>
<% if ("".equals(tip)){
%><input name="kw" value="" maxlength="20" />
<anchor>关键字搜索<go href="lib.jsp?s=<%=subjectId %>&amp;f=<%=flag%>&amp;sid=2" method="post">
<postfield name="kw" value="$kw" />
</go></anchor>|<a href="lib.jsp?s=<%=subjectId %>&amp;f=<%=flag%>&amp;sid=1">全部</a><br/>
<%
if (libList.size() > 0){
	ExamLib lib = null;
	for (int i = 0 ; i < libList.size() ; i++){
		lib = (ExamLib)libList.get(i);
		%><%=i + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + 1%>.<a href="seelib.jsp?s=<%=request.getAttribute("currentType")%>&amp;f=<%=flag %>&amp;lid=<%=lib.getId()%>"><%=StringUtil.toWml(lib.getTitle())%></a><br/><%
	}%><%=paging.shuzifenye("lib.jsp?s=" + subjectId + "&amp;f=" + flag + "&amp;sid=" + searchId + "&amp;kw=" + keyWord, true, "|", response)%><%
} else {
		%>没有找到相关资料.<br/><%	
}%><a href="index.jsp">返回首页</a><br/><%
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%><br/>
</p></card></wml>