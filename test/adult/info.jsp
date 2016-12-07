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
int point=400;
TestAction action=new TestAction(request);
if(null!=session.getAttribute(questionName+3)){
	point=100;
}else if(null!=session.getAttribute(questionName+4)){
	point=100;
}else{
	point=400;
}

if(action.isTested()==false){
	action.saveRecord(request);
	addUserPoint(action.getLoginUser(request),point);
}
else{
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/adult/infoTested.jsp", response);
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