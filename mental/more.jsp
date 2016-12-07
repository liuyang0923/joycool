<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mental.*"%>
<% response.setHeader("Cache-Control","no-cache");
   MentalAction action = new MentalAction(request);
   int mode = action.getParameterInt("m");
   MentalQuestion question = new MentalQuestion();
   MentalUser mentalUser = action.getCurrentUser();
   StringBuffer strBuffer = new StringBuffer();
   MentalProperty property = null;
   String tip = "";
   String userAnswer[] = {""};
   String level[] = {""};
   String analyse[] = {""};
   if (action.getLoginUser() == null){
	   tip = "请先登陆.";
   } else if (mentalUser == null || mentalUser.getQueNow() < 8){
	   tip = "请先去答题,再看结果.";
   } else {
	   if (mode < 0 || mode > 8){
		   mode = 0;
	   }
	   userAnswer = mentalUser.getAnswer().split("\\|");
	   question = action.getUserResult(mode);
	   if (mode != 0 && question == null){
			tip  = "题目不存在.";   
	   }
	   String tmp[] = mentalUser.getAnswer().split("\\|");
	   property = action.service.getProperty(" id=" + tmp[0]);
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友心理测试"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (mode == 0){
List questionList = action.service.getQuestionList(" que_id >=2 and que_id <=6");
	if(property != null){
		%>所属类型:<%=StringUtil.toWml(property.getDescribe())%><br/><%
		List mateList = action.service.getPropertyList(" id in(" + property.getMate() + ")");
		if (mateList != null && mateList.size() > 0){
			%>匹配类型:<%
			for (int i = 0 ; i < mateList.size() ; i++){
				property = (MentalProperty)mateList.get(i);
				strBuffer.append(property.getDescribe() + ",");
			}
		}%><%=strBuffer.substring(0,strBuffer.length()-1)%><br/>匹配指数:★★★★★<br/><%
	}
	if (questionList != null && userAnswer.length > 0){
		String tmp = "";
		%><a href="more.jsp?m=1">性格特点</a><br/>
		<% question = (MentalQuestion)questionList.get(0);
		   if (question != null){
			   level = question.getLevel().split("\\|");
		   }%>
		<a href="more.jsp?m=2">社交值:<%=StringUtil.toWml(level[StringUtil.toInt(userAnswer[1])-1]) %></a><br/>
		<% question = (MentalQuestion)questionList.get(1);
		   if (question != null){
			   level = question.getLevel().split("\\|");
		   }%>
		<a href="more.jsp?m=3">好友印象：<%=StringUtil.toWml(level[StringUtil.toInt(userAnswer[2])-1]) %></a><br/>
		<% question = (MentalQuestion)questionList.get(2);
		   if (question != null){
			   level = question.getLevel().split("\\|");
		   }%>
		<a href="more.jsp?m=4">情商值:<%=StringUtil.toWml(level[StringUtil.toInt(userAnswer[3])-1]) %></a><br/>
		<% question = (MentalQuestion)questionList.get(3);
		   if (question != null){
			   level = question.getLevel().split("\\|");
		   }%>
		<a href="more.jsp?m=5">魅力值:<%=StringUtil.toWml(level[StringUtil.toInt(userAnswer[4])-1]) %></a><br/>
		<% question = (MentalQuestion)questionList.get(4);
		   if (question != null){
			   level = question.getLevel().split("\\|");
		   }%>
		<a href="more.jsp?m=6">是否花心:<%=StringUtil.toWml(level[StringUtil.toInt(userAnswer[5])-1]) %></a><br/>
		<a href="more.jsp?m=7">工作解释</a><br/>
		<a href="more.jsp?m=8">运势总结</a><br/>
		<a href="question.jsp">重新测试</a><br/>
		重测需等到:<%=DateUtil.formatSqlDatetime(mentalUser.getCreateTime()+2592000000l)%><br/>
		<%
	}
} else {
%><%=StringUtil.toWml(question.getTitle())%>:<%if(!"".equals(question.getLevel())){
		level = question.getLevel().split("\\|");
		if (level.length > 0 && userAnswer.length > 0){
			%><%=StringUtil.toWml(level[StringUtil.toInt(userAnswer[question.getQueId()-1])-1]) %><%
		}
	}%><br/><%analyse = question.getAnalyse().split("\\|");
	if (analyse.length > 0){
		%><%=StringUtil.toWml(analyse[StringUtil.toInt(userAnswer[question.getQueId()-1])-1]) %><%
	}%><br/><a href="more.jsp">返回</a><br/><%
}
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>