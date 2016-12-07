<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mentalpic.*"%>
<%! public static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   MentalPicAction action = new MentalPicAction(request);
   MentalPicQuestion question = null;
   int totalCount = 0;
   PagingBean paging = null;
   int pageNow = 0;
   List questionList = null;
   totalCount = SqlUtil.getIntResult("select count(id) from mental_pic_question where del=0", 4);
   paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
   pageNow = paging.getCurrentPageIndex();
   questionList = action.service.getQuestionList(" del=0 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="图片心理测试"><p>
<%=BaseAction.getTop(request, response)%>
你了解自己吗？想要更多的了解自己，快来通过图片测试一下吧！<br/>
请以第一直觉为准。<br/>
<% if (questionList != null && questionList.size() > 0){
   for (int i=0;i<questionList.size();i++){
	   question = (MentalPicQuestion)questionList.get(i);
	   %><%=i+paging.getStartIndex()+1 %>.<a href="question.jsp?id=<%=question.getId() %>"><%=StringUtil.toWml(question.getTitle())%></a><br/><%
   }%><%=paging.shuzifenye("index.jsp", false, "|", response)%><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>