<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/bottom.jsp");
	return;
}
if(action.getPetUser()==null){
	response.sendRedirect("index.jsp");
	return;
}
int task = StringUtil.toInt(request.getParameter("task"));
if(task==1||task==2){
	Tiny2Action action2 = new Tiny2Action(request, response);
	if(action2.checkGame(8)) return;
	if(action2.getGame() == null){
		action2.startGame(games[0], 8);
		return;
	}
}
action.matchact();
String result = (String)request.getAttribute("result");
String url = (String)request.getAttribute("url");
if(url==null)
	url = "/pet/runindex.jsp";
String tip = (String)request.getAttribute("tip");
url = (url);

if("failure".equals(result)) {%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="趣味比赛" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您的宠物不满足以下比赛条件(3秒后返回)<br/>
饥饿大于50<br/>
清洁大于50<br/>
等级大于5<br/>
健康大于50<br/>
<a href="<%=url%>">返回比赛列表</a><br/>
<%@include file="bottom.jsp"%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>

<%}else{
response.sendRedirect(url);
}%>