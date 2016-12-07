<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page
	import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static int COUNT_PRE_PAGE = 10; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<% response.setHeader("Cache-Control","no-cache");
   ExamAction action = new ExamAction(request);
   int libId = action.getParameterInt("lid");
   int in = action.getParameterInt("in");
   ExamLib lib = ExamAction.service.getLib(" id=" + libId + " and del=0");
   String tip = "";
   if (lib == null){
	   tip = "您要查找的资料不存在.";
   } else {
//	   if (in == 1){
//		   放入自己的书包
//		   ExamBag myBag = new ExamBag();
//		   myBag.setUserId(action.getLoginUser().getId());
//		   myBag.setTitle(lib.getTitle());
//		   myBag.setContent(lib.getContent());
//		   myBag.setQueType(lib.getType());
//		   myBag.setDel(0);
//		   myBag.setFlag(0);
//		   boolean result = action.writeToBag(myBag);
//		   if (result){
//			   tip = "放入成功.";
//		   } else {
//			   tip = (String)request.getAttribute("tip");
//		   }
//	   }
   }
%><wml><card title="备战考试"><p><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>==<%=StringUtil.toWml(lib.getTitle()) %>==<br/>
<%=StringUtil.toWml(lib.getContent())%><br/>
放入我的书包(开发中)<br/>
<%--<a href="seelib.jsp?lid=<%=lib.getId()%>&amp;in=1">放入我的书包</a><br/> --%>
<a href="lib.jsp?s=<%=lib.getType()%>&amp;f=<%=lib.getFlag()%>">返回上一页</a><br/>
<%	
} else {
	%><%=tip%><br/><%if (lib==null){%><a href="index.jsp">返回</a><%}else{%><a href="lib.jsp?s=<%=lib.getType()%>&amp;f=<%=lib.getFlag()%>">返回</a><%}%><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%><br/>
</p></card></wml>