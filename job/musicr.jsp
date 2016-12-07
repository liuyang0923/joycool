<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.MusicAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
//判断页面刷新
if(session.getAttribute("music") == null){
	response.sendRedirect("/job/musicq.jsp");
	return;
}
//删除防止刷新参数
session.removeAttribute("music");
//读取连赢次数
int winCountM=0;
String count=(String)session.getAttribute("winCountM");
if(count!=null){
winCountM=Integer.parseInt(count);
}
String types=(String)request.getParameter("types");
if(types==null){
types="mid";
}
MusicAction action = new MusicAction(request);
action.result(request,response);
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean status=action.getUserStatus(loginUser.getId());
UserStatusBean status=UserInfoUtil.getUserStatus(loginUser.getId());
//取得用户选择答案参数
String success=(String)request.getAttribute("success");
String error=(String)request.getAttribute("error");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="听歌猜名结果" ontimer="<%=response.encodeURL("/job/musicq.jsp?types="+types)%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//判断答案,如果正确执行下面代码
if(success!=null){
   if(success.equals("success")){
   //设置连赢次数
   session.setAttribute("winCountM",String.valueOf(winCountM+1));
   %>
    您答对了!加100乐币!<br/>
    <a href="/job/musicq.jsp?types=<%=types%>">继续答题</a><br/>
   <%
   }
}
//判断答案,如果错误执行下面代码
if(error!=null){
   if(error.equals("error")){
   //删除连赢次数
   session.removeAttribute("winCountM");
   %>
    您答错了!不扣乐币<br/>
    <a href="/job/musicq.jsp?types=<%=types%>">继续答题</a><br/>
   <%
   }
}
%>
<br/>
你现有乐币<%=status.getGamePoint()%><br/>
<a href="/job/mindex.jsp">返回上一级</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>