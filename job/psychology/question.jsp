<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.PsychologyAction"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PsychologyAction action=new PsychologyAction();
int GAME_POINT=0;//扣除的乐币数
String msg=null;
if(null!=request.getParameter("answerId")){
	UserBean user=action.getLoginUser(request);
	UserStatusBean userStatus=UserInfoUtil.getUserStatus(user.getId());
	RankAction.addPoint(user,1);//增加1点经验值
	if(userStatus.getGamePoint()>=GAME_POINT){
		UserInfoUtil.updateUserStatus("game_point=game_point-"+GAME_POINT,"user_id="+user.getId(),user.getId(),UserCashAction.GAME,"心理测试扣除乐币1500");
		String redirectUrl="/job/psychology/temp.jsp?answerId="+request.getParameter("answerId");
		//response.sendRedirect(redirectUrl);
		BaseAction.sendRedirect("/job/psychology/temp.jsp?answerId="+request.getParameter("answerId"), response);
		return;
	}else{
		msg="您的乐币不足!";
	}
}
%>
<%
PsychologyBean psychology=null;
PsychologyAnswerBean answer=null;
Vector answerList=null;
action.question(request,response);
psychology=(PsychologyBean)request.getAttribute("psychology");
answerList=(Vector)request.getAttribute("answerList");
String[] answerMarks=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心理测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(null!=msg){%><%=msg%><br/><%}%>
<%=StringUtil.toWml(psychology.getContent())%><br/>
<%
for(int i=0;i<answerList.size();i++){
	answer=(PsychologyAnswerBean)answerList.get(i);
%><%=answerMarks[i]%>、<a href="question.jsp?id=<%=answer.getPsychologyId()%>&amp;answerId=<%=answer.getId()%>"><%=StringUtil.toWml(answer.getAnswer())%></a><br/>
<%}%>
<a href="index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
