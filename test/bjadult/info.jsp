<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");
%>
<%!
/**
 * 更新用户经验值
 * @param userId
 */
public void addUserPoint(UserBean loginUser,int point){
	RankAction.addPoint(loginUser,point);
	NoticeBean notice = new NoticeBean();
	notice.setUserId(loginUser.getId());
	notice.setTitle("感谢支持,"+point+"点经验值已经加上!");
	notice.setContent("");
	notice.setType(NoticeBean.GENERAL_NOTICE);
	notice.setHideUrl("");
	notice.setLink("/chat/hall.jsp");
	NoticeUtil.getNoticeService().addNotice(notice);
}




%>
<%
String questionName="question";

if(null==session.getAttribute(questionName+1)){
 //response.sendRedirect(("page1.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page1.jsp", response);
 return;
 }
int answer1=StringUtil.toInt((String)session.getAttribute(questionName+1));
 if(answer1!=1)
 {
 
if(null==session.getAttribute(questionName+2)){
 //response.sendRedirect(("page2.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page2.jsp", response);
 return;
 }
 int answer2=StringUtil.toInt((String)session.getAttribute(questionName+2));
	 if(answer2==5||answer2==7){
	 	for(int i=18;i<=31;i++){
			session.removeAttribute(questionName+i);
		}
		 if(null==session.getAttribute(questionName+3)){
		 //response.sendRedirect(("page3.jsp"));
		 BaseAction.sendRedirect("/test/bjadult/page3.jsp", response);
		 return;
		 }
		  if(null==session.getAttribute(questionName+11)){
		 //response.sendRedirect(("page4.jsp"));
		 BaseAction.sendRedirect("/test/bjadult/page4.jsp", response);
		 return;
		 }
		
	 }else{
	 	for(int i=3;i<=17;i++){
			session.removeAttribute(questionName+i);
		}
	 	 if(null==session.getAttribute(questionName+18)){
		 //response.sendRedirect(("page3.jsp"));
		 BaseAction.sendRedirect("/test/bjadult/page3.jsp", response);
		 return;
		 }
		  if(null==session.getAttribute(questionName+25)){
		 //response.sendRedirect(("page4.jsp"));
		 BaseAction.sendRedirect("/test/bjadult/page4.jsp", response);
		 return;
		 }
		 
	 }
   if(null==session.getAttribute(questionName+32)){
 //response.sendRedirect(("page5.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page5.jsp", response);
 return;
 }
 if(null==session.getAttribute(questionName+40)){
 //response.sendRedirect(("page6.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page6.jsp", response);
 return;
 }
  if(null==session.getAttribute(questionName+46)){
 //response.sendRedirect(("page7.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page7.jsp", response);
 return;
 }
 }

int point=500;
TestAction action=new TestAction(request);
if(action.getLoginUser().getCityno()!=55){
	//response.sendRedirect(("infoNonBj.jsp"));
	BaseAction.sendRedirect("/test/bjadult/infoNonBj.jsp", response);
	return;
}
if(answer1==1){
	point=50;
}else{
	point=500;
}




if(action.isTested()==false){
	action.saveRecord(request);
	addUserPoint(action.getLoginUser(request),point);
}
else{
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/bjadult/infoTested.jsp", response);
	return;
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="成人用品调查问卷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
谢谢参与！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>