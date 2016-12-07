<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mentalpic.*"%>
<%! public static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   MentalPicAction action = new MentalPicAction(request);
   String tip = "";
   boolean isAnswered = false;
   if (action.getLoginUser() == null){
	   action.sendRedirect("../user/login.jsp",response);
	   return;
   }
   MentalPicUser mentalUser = null;
   MentalPicOption option = null;
   List optionList = null;
   int id = action.getParameterInt("id");
   int answerId = action.getParameterInt("a");
   MentalPicQuestion question = action.service.getQuestion(" id=" + id + " and del=0");
   if (question == null){
	   tip ="题目不存在或已被删除.";
   } else {
	   optionList = action.service.getOptionList(" que_id=" + question.getId());
	   mentalUser = action.service.getMentalPicUser(" user_id=" + action.getLoginUser().getId() + " and que_id=" + question.getId());
   	   // 用户已答过这题了
	   if (mentalUser != null){
   		   if (action.pastTime(mentalUser,"INTERVAL 7 DAY")){
   			   // 已过了7天，重新答题
   			   option = action.answerQuestion(question,optionList,answerId,mentalUser);
   		   } else {
   			   // 没过半年呢，直接显示答案
   			   option = (MentalPicOption)optionList.get(mentalUser.getSelected()-1);
   			   isAnswered = true;
   		   }
   	   } else {
   		   // 用户没答过这道题
   		   option = action.answerQuestion(question,optionList,answerId,null);	   
   	   }
   	   if (option == null){
   		   tip = (String)request.getAttribute("tip");
   	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="图片心理测试"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%if (isAnswered){%>(想再次测试这道题,需要等一周哦.)<br/>你已经做过这道题了.上次选择的是:<%}else{%>你选择的是:<%}if (question.getFlag() == 0){%><br/><img src="<%=action.getPicUrlPath() + option.getOption()%>" alt="x" /><%}else{%><%=option.getOption()%><%}%><br/>
<%=StringUtil.toWml(option.getAnalyse())%><br/><a href="index.jsp">返回</a><br/>
<%	
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>