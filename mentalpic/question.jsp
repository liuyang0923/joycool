<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mentalpic.*"%>
<%! public static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   MentalPicAction action = new MentalPicAction(request);
   String tip ="";
   if (action.getLoginUser() == null){
	   action.sendRedirect("../user/login.jsp",response);
	   return;
   }
   List optionList = null;
   MentalPicOption option = null;
   int id = action.getParameterInt("id");
   int answerId = action.getParameterInt("a");
   MentalPicQuestion question = action.service.getQuestion(" id=" + id + " and del=0");
   if (question == null){
	   tip ="题目不存在或已被删除.";
   } else {
	   optionList = action.service.getOptionList(" que_id=" + question.getId());
	   MentalPicUser mentalUser = action.service.getMentalPicUser(" user_id=" + action.getLoginUser().getId() + " and que_id=" + question.getId());
   	   // 用户已答过这题了
	   if (mentalUser != null){
   		   if (!action.pastTime(mentalUser,"INTERVAL 7 DAY")){
   			   // 没过一周呢，直接显示答案
   			   action.sendRedirect("result.jsp?id=" + question.getId(),response);
   			   return;
   		   }
   	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=question==null?"错误":StringUtil.toWml(question.getTitle()) %>"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=StringUtil.toWml(question.getContent())%><br/><%
if(question.getFlag() == 1){
	// 看图选字
	%><img src="<%=action.picUrlPath + question.getContentPic() %>" alt="x" /><br/><%
	for (int i=0;i<optionList.size();i++){
		option = (MentalPicOption)optionList.get(i);
		%><%=i+1%>.<a href="result.jsp?id=<%=question.getId()%>&amp;a=<%=i+1%>"><%=option.getOption()%></a><br/><%
	}
} else {
	// 选项。图片3张换行
	for (int i=0;i<optionList.size();i++){
		option = (MentalPicOption)optionList.get(i);
		%><a href="result.jsp?id=<%=question.getId()%>&amp;a=<%=i+1%>"><img src="<%=action.getPicUrlPath() + option.getOption()%>" alt="x" /></a><%if ((i+1)%3==0){%><br/><%}%><%
	}%><br/><%
}%><a href="index.jsp">返回</a><br/><%
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>